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
    <v-tab>
      <template #label>
        <p>Dodaj nową trasę</p>
      </template>
      <template #icon>
        <plus-circle-outline />
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
    <t-grid
      :data="gridData"
      :onSelectedRowsUpdated="onSelectedRowsUpdated"
    />
  </div>
</template>
<script>
import { Refresh, PlusCircleOutline, TrashCan } from 'mdue';
import VTab from '../../../UI/VTab.vue';
import TGrid from '../../../widgets/tarbus-grid/TGrid.vue';
import RoutesDataService from '../../../../services/schedule/routes.service';

export default {
  name: 'ScheduleRoutes',
  data() {
    return {
      routesArray: [],
      gridData: [],
      selectedRows: [],
      isLoading: true,
      errorMessage: null,
    };
  },
  components: {
    TGrid,
    Refresh,
    TrashCan,
    PlusCircleOutline,
    VTab,
  },
  methods: {
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
    openEditRoute(row) {
      console.log(row.values.routeId);
      this.$router.push({
        path: '/sheet/track',
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
        editable: true,
        label: 'Kierunek - aplikacja',
        hidden: false,
        key: 'routeName',
        value: route.routeName,
      });
      data.push({
        type: 'text',
        flex: 1,
        editable: true,
        label: 'Kierunek - tabliczka przystankowa',
        hidden: false,
        key: 'routeDetails',
        value: route.routeDetails,
      });
      data.push({
        type: 'text',
        flex: 3,
        editable: true,
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
        ],
      });
      data.push({
        type: 'button',
        flex: 1,
        value: 'Edytuj trasę',
        onClick: this.openEditRoute,
      });
      return data;
    },
  },
  mounted() {
    this.fetchRoutesFromApi().then(() => {
      this.isLoading = false;
      this.parseRoutesToGridData();
    });
  },
};
</script>
<style lang="scss" scoped>
@import '../../../../assets/styles/variables.scss';

.window {
  width: 100%;
  height: 100%;
}

.grid-tabs {
  padding: 10px 9px;
  display: flex;
  z-index: 1;
  background: $color-white;
  align-items: center;
  width: 100%;
}
</style>
