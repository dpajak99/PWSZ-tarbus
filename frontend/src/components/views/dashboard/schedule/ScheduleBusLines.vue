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
          <refresh/>
        </template>
      </v-tab>
      <v-tab
        @click="addNewLine"
      >
        <template #label>
          <p>Dodaj nową linię</p>
        </template>
        <template #icon>
          <plus-circle-outline/>
        </template>
      </v-tab>
      <v-tab
        @click="removeSelected"
      >
        <template #label>
          <p>Usuń</p>
        </template>
        <template #icon>
          <trash-can/>
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
import BusLinesDataService from '../../../../services/schedule/busLines.service';
import VTab from '../../../UI/VTab.vue';
import VGridLayout from '../../../UI/VGridLayout.vue';

export default {
  name: 'ScheduleBusLines',
  data() {
    return {
      busLinesArray: [],
      gridData: [],
      selectedRows: [],
      isLoading: true,
      errorMessage: null,
      emptyItem: {
        id: null,
        name: null,
        comments: null,
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
    async fetchBusLinesFromApi() {
      const currentVersion = JSON.parse(localStorage.getItem('current_schedule_version'));
      await BusLinesDataService.getByVersion(currentVersion.id)
        .then((response) => {
          this.busLinesArray = response.data;
          console.log(response.data);
        })
        .catch((err) => {
          this.errorMessage = err;
        });
    },
    parseBusLinesToGridData() {
      const data = [];
      this.busLinesArray.forEach((line, index) => {
        data.push(this.getGridRowFromLine(line, index + 1));
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
      const size = this.selectedRows.length;
      const linesToDelete = this.selectedRows.map((item) => item.values);
      this.selectedRows = [];
      BusLinesDataService.deleteAll(linesToDelete).then((response) => {
        console.log(response);
        this.refreshData();
        this.$notify({
          message: `Pomyślnie skasowano ${size} linie`,
          type: 'success',
          position: 'bottom-right',
        });
      }).catch((err) => {
        this.errorMessage = `onRowDelete${err.toString()}`;
      });
    },
    onRowDelete(row) {
      console.log(row);
      BusLinesDataService.delete(row.values.id).then((response) => {
        console.log(response);
        this.refreshData();
        this.$notify({
          message: 'Pomyślnie skasowano linię',
          type: 'success',
          position: 'bottom-right',
        });
      }).catch((err) => {
        this.errorMessage = `onRowDelete${err.toString()}`;
      });
    },
    async onRowUpdate(row) {
      const updatedRow = row;
      updatedRow.values.version = {
        id: JSON.parse(localStorage.getItem('current_schedule_version')).id,
      };
      const result = await BusLinesDataService.update([updatedRow.values]).catch((err) => {
        this.errorMessage = err;
      });
      this.$notify({
        message: 'Pomyślnie zaktualizowano linię',
        type: 'success',
        position: 'bottom-right',
      });
      return result.data[0];
    },
    getGridRowFromLine(line, index) {
      const data = [];
      data.push({
        type: 'text',
        flex: 1,
        editable: false,
        label: 'L.P.',
        hidden: false,
        key: 'lp',
        value: `${index}`,
      });
      data.push({
        type: 'text',
        hidden: true,
        key: 'id',
        value: line.id,
      });
      data.push({
        type: 'text',
        flex: 3,
        editable: true,
        label: 'Nazwa linii',
        key: 'name',
        value: line.name,
      });
      data.push({
        type: 'text',
        flex: 7,
        editable: true,
        label: 'Uwagi',
        hidden: false,
        key: 'comments',
        value: line.comments,
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
      this.busLinesArray = [];
      this.gridData = [];
      this.isLoading = true;
      this.fetchBusLinesFromApi().then(() => {
        this.isLoading = false;
        this.parseBusLinesToGridData();
      });
    },
    addNewLine() {
      this.busLinesArray.push(this.emptyItem);
      this.parseBusLinesToGridData();
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
