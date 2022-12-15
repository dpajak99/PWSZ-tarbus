<template>
  <div class="grid-tabs">
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
      @click="generateSelectedTables"
    >
      <template #label>
        <p>Generuj tabliczki</p>
      </template>
      <template #icon>
        <printer />
      </template>
    </v-tab>
    <v-tab>
      <template #label>
        <p>Usuń</p>
      </template>
      <template #icon>
        <trash-can />
      </template>
    </v-tab>
  </div>

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
      :on-row-update="onRowUpdated" />
  </div>
  <el-dialog
    title="Generowanie tabliczki przystankowej"
    v-model="isDialogVisible"
    width="30%">
    <el-progress :percentage="50" :indeterminate="true" :duration="1" />
    <template #footer />
  </el-dialog>
</template>
<script>
import { Refresh, Printer, TrashCan } from 'mdue';
import VTab from '../../../UI/VTab.vue';
import TGrid from '../../../widgets/tarbus-grid/TGrid.vue';
import RoutesDataService from '../../../../services/schedule/routes.service';
import GeneratorService from '../../../../services/schedule/schedule.service';

export default {
  name: 'ScheduleSchedule',
  data() {
    return {
      routesArray: [],
      gridData: [],
      selectedRows: [],
      isLoading: true,
      errorMessage: null,
      isDialogVisible: false,
    };
  },
  components: {
    TGrid,
    Refresh,
    TrashCan,
    Printer,
    VTab,
  },
  methods: {
    generateSelectedTables() {
      const routes = this.selectedRows.map((e) => e.values.routeId);
      this.generateTables(routes, true);
    },
    onPrintScheduleClicked(row) {
      const routes = [row.values.routeId];
      this.generateTables(routes, false);
    },
    async generateTables(routes, wantsZip) {
      const params = {
        routes,
        wantsZip,
        companyId: JSON.parse(localStorage.getItem('current_schedule_version')).companyId,
      };
      await GeneratorService.generateTimetables(params).then((response) => {
        const filePath = response.data;
        const a = document.createElement('A');
        a.href = filePath;
        a.download = filePath;
        a.target = '_blank';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        console.log(response);
      });
    },
    onRowUpdated() {},
    async fetchRoutesFromApi() {
      const currentVersion = JSON.parse(localStorage.getItem('current_schedule_version'));
      await RoutesDataService.getByVersion(currentVersion.id).then((response) => {
        this.routesArray = response.data;
      }).catch((err) => {
        this.errorMessage = err;
      });
    },
    parseRoutesToGridData() {
      const newGridData = [];
      this.routesArray.forEach((line) => {
        const data = [];
        line.routes.forEach((route) => (data.push(this.getGridRowFromRoute(route))));
        const rowGroup = {
          title: line.line,
          data,
        };
        newGridData.push(rowGroup);
      });
      this.gridData = newGridData;
    },
    onSelectedRowsUpdated(rows) {
      this.selectedRows = rows;
    },
    onRowDelete(row) {
      console.log('delete', row);
    },
    onOpenEditScheduleClicked(row) {
      this.$router.push({
        path: '/sheet/schedule',
        query: {
          id: row.values.routeId,
        },
      });
    },
    getGridRowFromRoute(route) {
      const data = [];
      data.push({
        type: 'text',
        editable: true,
        hidden: true,
        key: 'routeId',
        value: route.routeId,
      });
      data.push({
        type: 'text',
        flex: 1,
        editable: false,
        label: 'Kierunek',
        hidden: false,
        key: 'routeDetails',
        value: route.routeDetails,
      });
      data.push({
        type: 'text',
        flex: 2,
        editable: false,
        label: 'Opis trasy',
        hidden: false,
        key: 'routeDesc',
        value: route.routeDesc,
      });
      data.push({
        type: 'action',
        flex: 1,
        label: 'Narzędzia',
        actions: [
          {
            icon: 'trash',
            onClick: this.onRowDelete,
          },
          {
            icon: 'print',
            onClick: this.onPrintScheduleClicked,
          },
        ],
      });
      data.push({
        type: 'button',
        flex: 1,
        value: 'Edytuj rozkład jazdy',
        onClick: this.onOpenEditScheduleClicked,
      });
      return data;
    },
    refreshData() {
      this.fetchRoutesFromApi().then(() => {
        this.isLoading = false;
        this.parseRoutesToGridData();
      });
    },
  },
  mounted() {
    this.refreshData();
  },
};
</script>
<style lang="scss" scoped>
@import '../../../../assets/styles/variables.scss';

.grid-tabs {
  padding: 10px 9px;
  display: flex;
  z-index: 1;
  background: $color-white;
  align-items: center;
  width: 100%;
}
</style>
