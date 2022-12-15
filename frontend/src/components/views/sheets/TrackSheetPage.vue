<template>
  <sheet
    ref="sheetHeader"
    :title="sheetTitle"
    :tools="sheetTools"
    :menuItems="menuItems"
    :onSave="onSave"
    :isSendingComplete="isSendingComplete"
    :progressBarValue="progressBarValue"
    :currentDialogMessage="currentDialogMessage"
    :onRefresh="setUpSheetData"
  >
    <slot>
      <sheet-component
        ref="sheetData"
        :rows="rows"
        :columns="columns"
        :isClassicOptionsEnabled="true"
        :onRowDeleted="onRowDeleted"
      >
      </sheet-component>
    </slot>
  </sheet>
</template>

<script>

import { Undo, Redo } from 'mdue';
import Sheet from '../../widgets/tarbus-sheet/Sheet.vue';
import RoutesDataService from '../../../services/schedule/routes.service';
import RouteConnectionDataService from '../../../services/schedule/route-connections.service';
import SheetComponent from '../../widgets/tarbus-sheet/SheetComponent.vue';

export default {
  name: 'TrackSheetPage',
  components: {
    SheetComponent,
    Sheet,
  },
  data() {
    return {
      isSendingComplete: false,
      currentDialogMessage: '',
      progressBarValue: 0,
      routeDetails: {},
      removedRows: [],
      sheetTitle: [],
      columns: [{ prop: 'name' }, { prop: 'details' }],
      rows: [{
        name: '1',
        details: 'Item 1',
      }],
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
    onRowDeleted(row) {
      if (row.id != null && row.id !== '') {
        this.removedRows.push(row);
      }
    },
    async onSave() {
      this.isSendingComplete = false;
      const finalRows = this.$refs.sheetData.getRows();
      this.$refs.sheetHeader.openDialog();
      const editedConnections = [];
      const deletedConnections = [];
      finalRows.forEach((row) => {
        editedConnections.push({
          id: row.id,
          isOptional: row.isOptional,
          lp: row.lp,
          route: {
            id: this.routeDetails.id,
          },
          busStop: {
            id: row.busStopId,
          },
        });
      });

      this.removedRows.forEach((row) => {
        deletedConnections.push({
          id: row.id,
        });
      });

      console.log(deletedConnections);
      console.log(editedConnections);

      await RouteConnectionDataService.update(editedConnections)
        .then(() => {
          this.progressBarValue += 60;
          this.currentDialogMessage = 'Kasowanie odjazdów';
        })
        .catch((err) => {
          this.currentDialogMessage = `Wystąpił błąd przy aktualizowaniu danych: ${err}`;
        });

      await RouteConnectionDataService.delete(deletedConnections)
        .then(() => {
          this.progressBarValue += 40;
          this.currentDialogMessage = 'Ukończono';
          this.isSendingComplete = true;
        })
        .catch((err) => {
          this.currentDialogMessage = `Wystąpił błąd przy kasowaniu odjazdów: ${err}`;
        });
    },
    setUpSheet() {
      this.sheetTitle = [
        {
          label: 'Trasa',
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

      const rows = await this.fetchSheetData();
      const columns = [
        {
          name: 'LP',
          prop: 'lp',
          readonly: true,
          size: 150,
        },
        {
          name: 'Opcjonalny',
          prop: 'isOptional',
          size: 150,
        },
        {
          name: 'ID przystanku',
          prop: 'busStopId',
          size: 150,
        },
        {
          name: 'Nazwa przystanku',
          prop: 'busStopName',
          readonly: true,
          size: 500,
        },
        {
          name: 'Lat',
          prop: 'busStopLat',
          readonly: true,
          size: 450,
        },
        {
          name: 'Lng',
          prop: 'busStopLng',
          readonly: true,
        },
      ];
      this.rows = rows;
      this.columns = columns;
    },
    async fetchSheetData() {
      let routeList = [];
      await RouteConnectionDataService.getRouteConnection(this.$route.query.id)
        .then((response) => {
          routeList = response.data;
        });
      return routeList;
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
    console.log('Params: ', this.$route.params);
    this.fetchRouteDetails().then(() => {
      this.isLoading = false;
    });
    this.setUpSheetData();
  },
};

</script>

<style scoped>

</style>
