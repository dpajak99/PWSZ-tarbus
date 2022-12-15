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
      @click="isAddDialogVisible = true"
    >
      <template #label>
        <p>Dodaj nowe oznaczenie</p>
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
    <TGrid
      :data="gridData"
      :onSelectedRowsUpdated="onSelectedRowsUpdated"
      :on-row-update="onRowUpdate" />
  </div>
  <el-dialog
    title="Stwórz nazwę dla kursu"
    v-model="isAddDialogVisible"
    width="30%">
    <h6>Dodaj dla linii</h6>
    <el-dropdown trigger="click" class="version-dropdown">
      <span class="version-dropdown-link">
        {{ addTrackNameSelectedLine != null ? addTrackNameSelectedLine.title : '---' }}
        <i class="el-icon-arrow-down el-icon--right" />
      </span>
      <template #dropdown>
        <el-dropdown-menu class="version-dropdown__content">
          <el-dropdown-item
            v-for="line in gridData"
            :key="line.title"
            class="version-dropdown__item"
            @click="addTrackNameSelectedLine = line"
          >
            {{ line.title }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
    <h6 v-if="addTrackNameSelectedLine != null">Dodaj dla trasy</h6>
    <el-dropdown v-if="addTrackNameSelectedLine != null" trigger="click" class="version-dropdown">
      <span class="version-dropdown-link">
        {{ addTrackNameSelectedTrack != null ? addTrackNameSelectedTrack.title : '---' }}
        <i class="el-icon-arrow-down el-icon--right" />
      </span>
      <template #dropdown>
        <el-dropdown-menu class="version-dropdown__content">
          <el-dropdown-item
            v-for="track in addTrackNameSelectedLine.children"
            :key="track.title"
            class="version-dropdown__item"
            @click="addTrackNameSelectedTrack = track"
          >
            {{ track.title }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
    <h6>Symbol</h6>
    <t-input
      :editable="true"
      :on-component-value-updated="updateSymbol"
    />
    <h6>Nazwa</h6>
    <t-input
      :editable="true"
      :on-component-value-updated="updateDestination"
    />
    <h6>Opis</h6>
    <t-input
      :editable="true"
      :on-component-value-updated="updateDescription"
    />
    <br /><br />
    <v-button
      @click="addTrackName"
    >
      <template #label>
        Dodaj
      </template>
    </v-button>
  </el-dialog>
</template>
<script>
import { Refresh, PlusCircleOutline, TrashCan } from 'mdue';
import VTab from '../../../UI/VTab.vue';
import TGrid from '../../../widgets/tarbus-grid/TGrid.vue';
import DestinationsDataService from '../../../../services/schedule/destinations.service';
import VButton from '../../../UI/VButton.vue';
import TInput from '../../../widgets/tarbus-grid/TInput.vue';

export default {
  name: 'ScheduleTracksNames',
  data() {
    return {
      destinationsArray: [],
      gridData: [],
      selectedRows: [],
      isLoading: true,
      errorMessage: null,
      isAddDialogVisible: false,
      addTrackNameSelectedLine: null,
      addTrackNameSelectedTrack: null,
      addTrackNameSelectedSymbol: '',
      addTrackNameSelectedDestination: '',
      addTrackNameSelectedDescription: '',
    };
  },
  components: {
    TInput,
    VButton,
    TGrid,
    Refresh,
    TrashCan,
    PlusCircleOutline,
    VTab,
  },
  methods: {
    onRowUpdate(row) {
      console.log(row);
      return row;
    },
    updateSymbol(value) {
      this.addTrackNameSelectedSymbol = value;
    },
    updateDestination(value) {
      this.addTrackNameSelectedDestination = value;
    },
    updateDescription(value) {
      this.addTrackNameSelectedDescription = value;
    },
    addTrackName() {
      console.log(this.addTrackNameSelectedTrack.additionalProps.routeId);
      const destination = {
        routeId: this.addTrackNameSelectedTrack.additionalProps.routeId,
        symbols: this.addTrackNameSelectedSymbol,
        boardName: this.addTrackNameSelectedDestination,
        scheduleName: this.addTrackNameSelectedDescription,
      };
      console.log(destination);
      DestinationsDataService.add([destination]).then(() => {
        this.isAddDialogVisible = false;
        this.refresh();
      });
    },
    async fetchDestinationsFromApi() {
      const currentVersion = JSON.parse(localStorage.getItem('current_schedule_version'));
      await DestinationsDataService.getByVersion(currentVersion.id).then((response) => {
        this.destinationsArray = response.data;
      }).catch((err) => {
        this.errorMessage = err;
      });
    },
    parseDestinationsToGridData() {
      const newGridData = [];
      this.destinationsArray.forEach((line) => {
        const children = [];
        line.routes.forEach((route) => {
          const data = [];
          route.destinations.forEach((destination) => {
            data.push(this.getGridRowFromDestination(destination));
          });
          console.log(route);
          children.push({
            title: route.routeName,
            additionalProps: {
              routeId: route.routeId,
            },
            data,
          });
        });
        const rowGroup = {
          title: line.line,
          children,
        };
        newGridData.push(rowGroup);

        this.gridData = newGridData;
      });
    },
    onSelectedRowsUpdated(rows) {
      this.selectedRows = rows;
    },
    onRowDelete(row) {
      console.log('delete', row);
    },
    openEditRoute(row) {
      alert(row.id);
    },
    getGridRowFromDestination(destination) {
      const data = [];
      data.push({
        type: 'text',
        flex: 1,
        editable: true,
        label: 'Symbol',
        hidden: false,
        key: 'symbols',
        value: destination.symbols,
      });
      data.push({
        type: 'text',
        flex: 2,
        editable: true,
        label: 'Nazwa ekranowa',
        hidden: false,
        key: 'boardName',
        value: destination.boardName,
      });
      data.push({
        type: 'text',
        flex: 3,
        editable: true,
        label: 'Nazwa tabliczkowa',
        hidden: false,
        key: 'scheduleName',
        value: destination.scheduleName,
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
      return data;
    },
    refresh() {
      this.isLoading = true;
      this.fetchDestinationsFromApi().then(() => {
        this.isLoading = false;
        this.parseDestinationsToGridData();
      });
    },
  },
  mounted() {
    this.refresh();
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
