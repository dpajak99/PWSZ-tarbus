<template>
  <sheet
    ref="sheetHeader"
    :title="sheetTitle"
    :tools="sheetTools"
    :menuItems="menuItems"
    :onSave="onSave"
    :isSendingComplete="isSendingComplete"
    :currentDialogMessage="currentDialogMessage"
    :progressBarValue="progressBarValue"
    :onRefresh="setUpSheetData"
  >
    <slot>
      <sheet-component
        ref="sheetData"
        :rows="rows"
        :columns="columns"
      >
        <template #dropdownOptions>
          <li @click="() => onColumnDeleted(1)">
            <trash-can class="menu-dialog__options__icon" />
            Usuń kolumnę
          </li>
          <li @click="() => insertNewColumn(0)">
            <layers-plus class="menu-dialog__options__icon" />
            Wstaw kolumnę przed
          </li>
          <li @click="() => insertNewColumn(1)">
            <layers-minus class="menu-dialog__options__icon" />
            Wstaw kolumnę po
          </li>
        </template>
      </sheet-component>
    </slot>
  </sheet>
</template>

<script>

import {
  Undo, Redo, LayersPlus, LayersMinus, TrashCan,
} from 'mdue';
import Sheet from '../../widgets/tarbus-sheet/Sheet.vue';
import RoutesDataService from '../../../services/schedule/routes.service';
import TrackDataService from '../../../services/schedule/track.service';
import DeparturesService from '../../../services/schedule/departure.service';
import SheetComponent from '../../widgets/tarbus-sheet/SheetComponent.vue';
import SheetMultipleTitle from '../../widgets/SheetMultipleTitle';
import MathUtilities from '../../../utilities/MathUtilities';

export default {
  name: 'ScheduleSheetPage',
  components: {
    SheetComponent,
    Sheet,
    LayersPlus,
    LayersMinus,
    TrashCan,
  },
  data() {
    return {
      isSendingComplete: false,
      currentDialogMessage: '',
      progressBarValue: 0,
      routeDetails: {},
      removedTracks: [],
      sheetTitle: [],
      columns: [],
      rows: [],
      menuItems: [
        {
          title: 'Plik',
          actions: [
            { label: 'Zapisz', onClick: this.onSave },
          ],
        },
      ],
      sheetTools: [
        { icon: Undo, onClick: 'function()' },
        { icon: Redo, onClick: 'function()' },
      ],
    };
  },
  methods: {
    async onColumnDeleted() {
      const finalColumns = [...this.$refs.sheetData.getColumns()];
      const grid = document.querySelector('revo-grid');
      const focused = await grid.getFocused();
      this.removedTracks.push({ id: finalColumns[focused.cell.x + 2].trackId });
      const mainCell = focused.cell.x % 2 === 0 ? focused.cell.x / 2 : (focused.cell.x - 1) / 2;
      finalColumns.splice(mainCell + 2, 1);
      this.columns = finalColumns;
      this.$refs.sheetData.dialogVisible = false;
    },
    async onSave() {
      this.isSendingComplete = false;
      this.$refs.sheetHeader.openDialog();
      const finalRows = this.$refs.sheetData.getRows();
      const finalColumns = this.$refs.sheetData.getColumns();
      const editedDepartures = [];
      const editedTracks = [];
      const removedDepartures = [];
      const routeId = this.$route.query.id;

      function parseTrack(row, trackId, index) {
        const days = row[`d_${trackId}`];
        const companyDays = row[`s_${trackId}`];
        editedTracks.push({
          id: trackId,
          lp: index - 1,
          dayString: companyDays,
          dayTypes: days,
          route: {
            id: routeId,
          },
        });
      }

      function parseDeparture(row, isLast, trackId) {
        const departureId = row[`id_${trackId}`];
        const departureTime = row[`d_${trackId}`];
        const departureSymbol = row[`s_${trackId}`];

        if (departureTime != null && departureTime.includes(':')) {
          editedDepartures.push({
            id: departureId,
            symbols: departureSymbol,
            time: departureTime,
            isLastDeparture: isLast,
            timeInMin: MathUtilities.mmHHtoMin(departureTime),
            track: {
              id: trackId,
            },
            busStop: {
              id: row.stopId,
            },
          });
        } else if (departureTime != null && departureId != null) {
          removedDepartures.push({
            id: departureId,
          });
        }
      }

      // ############
      for (let i = 0; i < finalRows.length; i += 1) {
        const row = finalRows[i];
        for (let j = 2; j < finalColumns.length; j += 1) {
          const trackId = finalColumns[j].children[0].prop.split('_')[1];
          if (i === 0) {
            parseTrack(row, trackId, j);
          } else {
            const isLast = this.isLastDeparture(finalRows, i, trackId);
            parseDeparture(row, isLast, trackId);
          }
        }
      }
      await this.sendParsedData(editedTracks, removedDepartures, editedDepartures);
    },
    async sendParsedData(editedTracks, removedDepartures, editedDepartures) {
      this.currentDialogMessage = 'Aktualizowanie tras';
      try {
        await TrackDataService.update(editedTracks)
          .then(() => {
            this.progressBarValue += 10;
            this.currentDialogMessage = 'Kasowanie obiektów 1/2';
          });
        await TrackDataService.remove(this.removedTracks)
          .then(() => {
            this.progressBarValue += 10;
            this.currentDialogMessage = 'Kasowanie obiektów 2/2';
          });
        await DeparturesService.remove(removedDepartures)
          .then(() => {
            this.progressBarValue += 10;
            this.currentDialogMessage = 'Aktualizowanie odjazdów';
          });
        await DeparturesService.update(editedDepartures).then(() => {
          this.progressBarValue = 100;
          this.isSendingComplete = true;
          this.currentDialogMessage = 'Zakończono';
        });
      } catch (err) {
        this.currentDialogMessage = `Wystąpił błąd przy aktualizowaniu danych: ${err}`;
      }
    },
    isLastDeparture(finalRows, rowIndex, trackId) {
      let maxRowIndex = 0;
      for (let i = 0; i < finalRows.length; i += 1) {
        const row = finalRows[i];
        const cellData = row[`d_${trackId}`];
        const isEmptyField = this.validateField(cellData);
        if (isEmptyField) {
          maxRowIndex = i;
        }
      }
      return rowIndex === maxRowIndex;
    },
    validateField(fieldData) {
      return fieldData.includes(':');
    },
    setUpSheet() {
      this.sheetTitle = [
        {
          label: 'Rozkład jazdy',
          value: this.routeDetails.busLine.name,
        },
        {
          label: 'Kierunek',
          value: this.routeDetails.name,
        },
        {
          label: 'Wersja',
          value: this.routeDetails.busLine.version.date,
        },
      ];
    },
    async setUpSheetData() {
      this.progressBarValue = 0;
      this.currentDialogMessage = '';
      this.isSendingComplete = false;
      this.removedRows = [];

      let rows = [];
      const tracksColumnsData = [];
      const sheetData = await this.fetchSheetData();
      const busStops = [];
      const firstRowData = {
        stopId: '---',
        stopName: '---',
      };

      function setUpDayTypes() {
        sheetData.tracks.forEach((track) => {
          firstRowData[`d_${track.trackId}`] = track.trackDayTypes;
          firstRowData[`s_${track.trackId}`] = track.trackDayString;
        });
      }

      function setupSheetGrid() {
        setUpDayTypes();
        busStops.push(firstRowData);
        sheetData.departures.forEach((item) => {
          const rowData = {
            stopId: item.stopId,
            stopName: item.stopName,
          };
          item.departures.forEach((departureObject, index) => {
            rowData[`id_${sheetData.tracks[index].trackId}`] = departureObject.id;
            rowData[`d_${sheetData.tracks[index].trackId}`] = departureObject.d;
            // console.log(departureObject);
            if (departureObject.isLast) {
              rowData[`s_${sheetData.tracks[index].trackId}`] = 'LAST';
            } else {
              rowData[`s_${sheetData.tracks[index].trackId}`] = departureObject.s;
            }
          });
          busStops.push(rowData);
        });
        rows = busStops;
      }

      sheetData.tracks.forEach((track) => {
        const clearHeader = this.getColumnHeaders(`Trasa ${track.trackLp}`, track.trackId);
        tracksColumnsData.push(clearHeader);
      });
      setupSheetGrid();

      this.rows = rows;
      this.columns = [
        {
          name: 'ID',
          prop: 'stopId',
          pin: 'colPinStart',
          readonly: true,
          size: 100,
        },
        {
          name: 'Nazwa przystaku',
          prop: 'stopName',
          pin: 'colPinStart',
          readonly: true,
          size: 350,
        },
        ...tracksColumnsData,
      ];
      console.log(this.columns);
    },
    async insertNewColumn(rowsToAdd) {
      const grid = document.querySelector('revo-grid');
      const focused = await grid.getFocused();
      if (focused.cell.y === 0) {
        return;
      }
      const columnId = MathUtilities.uuid();
      const clearHeader = this.getColumnHeaders(`Nowa ${this.$refs.sheetData.getColumns().length - 2}`, columnId);

      const tmpRows = [...this.$refs.sheetData.getRows()];
      tmpRows.forEach((row, index) => {
        tmpRows[index][`id_${columnId}`] = null;
        tmpRows[index][`d_${columnId}`] = '-';
        tmpRows[index][`s_${columnId}`] = '-';
      });

      const tmpColumns = [...this.$refs.sheetData.getColumns()];
      const mainCell = focused.cell.x % 2 === 0 ? focused.cell.x / 2 : (focused.cell.x - 1) / 2;
      tmpColumns.splice(mainCell + rowsToAdd + 2, 0, clearHeader);
      this.rows = tmpRows;
      this.columns = tmpColumns;
      this.$refs.sheetData.dialogVisible = false;
    },
    getColumnHeaders(title, columnId) {
      return {
        name: title,
        trackId: columnId,
        children: [
          {
            prop: `d_${columnId}`,
            columnTemplate: (createElement) => SheetMultipleTitle.createHeaderTemplate(createElement, 'Dzień', 'Godziny'),
          },
          {
            prop: `s_${columnId}`,
            columnTemplate: (createElement) => SheetMultipleTitle.createHeaderTemplate(createElement, 'Dzień n.', 'Symbole'),
          },
        ],
      };
    },
    async fetchSheetData() {
      let departureList = [];
      await DeparturesService.getRouteDepartures(this.$route.query.id).then((response) => {
        departureList = response.data;
        console.log(departureList);
      });
      return departureList;
    },
    async fetchRouteDetails() {
      await RoutesDataService.getRouteDetails(this.$route.query.id).then((response) => {
        this.routeDetails = response.data;
        console.log(response.data);
        this.setUpSheet();
      }).catch((err) => {
        this.errorMessage = err;
      });
    },
  },
  mounted() {
    this.fetchRouteDetails().then(() => {
      this.isLoading = false;
    });
    this.setUpSheetData();
  },
};

</script>

<style lang="scss" scoped>

.menu-dialog {
  &__options {
    font-size: 14px;
    letter-spacing: 0.2px;
    line-height: 20px;
    position: relative;
    cursor: pointer;
    list-style: none;
    margin: 0;
    white-space: nowrap;

    li {
      gap: 0.5rem;
      padding: 6px 8px 6px 15px;
      display: flex;
      align-items: center;
    }

    li:hover {
      background-color: #eeeeee;
    }

    svg {
      font-size: 1.5rem;
    }
  }
}
</style>
