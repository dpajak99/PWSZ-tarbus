<template>
  <v-grid-layout>
    <template #action-bar>
      <v-tab
        @click="refreshData"
      >
        <template #label>
          <p>Odśwież</p>
        </template>
        <template #icon>
          <refresh />
        </template>
      </v-tab>
      <v-tab
        @click="addNewStop"
      >
        <template #label>
          <p>Dodaj nowy przystanek</p>
        </template>
        <template #icon>
          <plus-circle-outline />
        </template>
      </v-tab>
      <v-tab
        @click="removeSelected"
      >
        <template #label>
          <p>Usuń</p>
        </template>
        <template #icon>
          <trash-can />
        </template>
      </v-tab>
    </template>
    <template #grid>
      <div
        v-if="errorMessage !== null"
        class="window grid-error-message"
      >
        <div class="grid-error-message__message">
          {{ errorMessage }}
        </div>
      </div>
      <div
        v-else
        class="window"
        v-loading="isLoading"
      >
        <TGrid
          :data="gridData"
          :onSelectedRowsUpdated="onSelectedRowsUpdated"
          :onRowUpdate="onRowUpdate"
        />
      </div>
    </template>
  </v-grid-layout>
</template>

<script>
import { PlusCircleOutline, Refresh, TrashCan } from 'mdue';
import TGrid from '../../../widgets/tarbus-grid/TGrid.vue';
import BusStopsDataService from '../../../../services/schedule/busStop.service';
import VTab from '../../../UI/VTab.vue';
import VGridLayout from '../../../UI/VGridLayout.vue';

export default {
  name: 'ScheduleBusStops',
  data() {
    return {
      busStopsArray: [],
      gridData: [],
      selectedRows: [],
      isLoading: true,
      errorMessage: null,
      emptyItem: {
        destinations: null,
        id: null,
        lat: null,
        lng: null,
        name: null,
        routeList: null,
        searchName: null,
      },
    };
  },
  components: {
    TGrid,
    Refresh,
    PlusCircleOutline,
    TrashCan,
    VTab,
    VGridLayout,
  },
  methods: {
    async fetchBusStopsFromApi() {
      await BusStopsDataService.getAll()
        .then((response) => {
          this.busStopsArray = response.data;
          console.log(response.data);
        })
        .catch((err) => {
          this.errorMessage = err;
        });
    },
    parseBusStopsToGridData() {
      const data = [];
      this.busStopsArray.forEach((line, index) => {
        data.push(this.getGridRowFromStop(line, index + 1));
      });
      this.gridData = [{
        data,
      }];
    },
    onSelectedRowsUpdated(rows) {
      console.log(rows);
      this.selectedRows = rows;
    },
    removeSelected() {
      // const size = this.selectedRows.length;
      // const linesToDelete = this.selectedRows.map((item) => item.values);
      // this.selectedRows = [];
      // BusLinesDataService.deleteAll(linesToDelete).then((response) => {
      //   console.log(response);
      //   this.refreshData();
      //   this.$notify({
      //     message: `Pomyślnie skasowano ${size} linie`,
      //     type: 'success',
      //     position: 'bottom-right',
      //   });
      // }).catch((err) => {
      //   this.errorMessage = `onRowDelete${err.toString()}`;
      // });
    },
    onRowDelete(/* row */) {
      // console.log(row);
      // BusLinesDataService.delete(row.values.id).then((response) => {
      //   console.log(response);
      //   this.refreshData();
      //   this.$notify({
      //     message: 'Pomyślnie skasowano linię',
      //     type: 'success',
      //     position: 'bottom-right',
      //   });
      // }).catch((err) => {
      //   this.errorMessage = `onRowDelete${err.toString()}`;
      // });
    },
    async onRowUpdate(row) {
      const updatedRow = row;
      let { name } = updatedRow.values;
      if (name == null) {
        name = '';
      }
      name = name.toLowerCase().replaceAll(' ', '').replaceAll('-', '');
      updatedRow.values.searchName = name;
      const result = await BusStopsDataService.update([updatedRow.values]).catch((err) => {
        this.errorMessage = err;
      });
      this.$notify({
        message: 'Pomyślnie zaktualizowano linię',
        type: 'success',
        position: 'bottom-right',
      });
      return result.data[0];
    },
    getGridRowFromStop(stop /* , index */) {
      const data = [];
      data.push({
        type: 'text',
        flex: 1,
        hidden: false,
        editable: true,
        label: 'ID',
        key: 'id',
        value: stop.id,
      });
      data.push({
        type: 'text',
        flex: 3,
        editable: true,
        label: 'Nazwa przystanku',
        key: 'name',
        value: stop.name,
      });
      data.push({
        type: 'text',
        flex: 2,
        editable: true,
        label: 'Miejscowość',
        key: 'city',
        value: '---',
      });
      data.push({
        type: 'text',
        flex: 2,
        editable: true,
        label: 'Droga',
        key: 'road_type',
        value: '---',
      });
      data.push({
        type: 'text',
        flex: 2,
        editable: true,
        label: 'Lat',
        hidden: false,
        key: 'lat',
        value: stop.lat,
      });
      data.push({
        type: 'text',
        flex: 2,
        editable: true,
        label: 'Lng',
        hidden: false,
        key: 'lng',
        value: stop.lng,
      });
      data.push({
        type: 'action',
        flex: 3,
        label: 'Narzędzia',
        actions: [
          {
            icon: 'trash',
            onClick: this.onRowDelete,
          },
        ],
      });
      return data;
    },
    refreshData() {
      this.busStopsArray = [];
      this.gridData = [];
      this.isLoading = true;
      this.fetchBusStopsFromApi().then(() => {
        this.isLoading = false;
        this.parseBusStopsToGridData();
      });
    },
    addNewStop() {
      const newLines = [...this.busStopsArray];
      newLines.unshift(this.emptyItem);
      console.log(newLines[0]);
      this.busStopsArray = newLines;
      this.parseBusStopsToGridData();
    },
  },
  mounted() {
    this.refreshData();
  },
};
</script>
<style lang="scss" scoped>
@import '../../../../assets/styles/variables.scss';
</style>
