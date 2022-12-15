<template>
  <the-preview-list>
    <template #list>
      <div class="list">
        <div class="list__header">
          <h5>Twoje wersje</h5>
          <v-tab
            size="small-clear"
            @click="isDialogVisible = true"
          >
            <template #icon>
              <plus-circle-outline />
            </template>
            <template #label>
              Dodaj
            </template>
          </v-tab>
        </div>
        <div
          v-for="version in versionsList"
          :key="version.id"
          @click="selectedVersion = version"
        >
          <v-list-item
            :active="selectedVersion.id === version.id"
            class="list__item"
          >
            <template #label>
              <div
                v-if="version.status !== null
                  && version.status !== 'created'
                  && version.status !== 'creating'"
                class="list__item__status"
                :class="getColorClassByStatus(version)"
              >
                <div class="list__item__status__icon">
                  <Circle />
                </div>
                <div class="list__item__status__label">
                  {{ getTextsByStatus(version) }}
                </div>
              </div>
              <div
                v-if="version.status === 'creating'"
                class="list__item__status"
              >
                <div class="list__item__status__icon">
                  <spinner-circular />
                </div>
                <div class="list__item__status__label">
                  Tworzenie
                </div>
              </div>
              <p><b>{{ version.companyName }}</b></p>
              <p>{{ version.date ? version.date : "-" }}</p>
              <p>{{ version.shortName ? version.shortName : "-" }}</p>
            </template>
          </v-list-item>
        </div>
      </div>
    </template>
    <template #preview>
      <div class="preview">
        <div class="preview__left">
          <h5>Wybrana wersja rozkładu jazdy</h5>
          <ul>
            <li>
              <v-tab
                size="small-clear"
                @click="deleteVersion"
              >
                <template #icon>
                  <trash-can />
                </template>
                <template #label>
                  Usuń
                </template>
              </v-tab>
            </li>
            <li>
              <h6>Rozkład obowiązuje od</h6>
              <t-input
                :initialValue="selectedVersion.dateStart"
                :editable="true"
                :on-component-value-updated="
                  (value) => saveVersion(() => selectedVersion.dateStart = value)"
              />
            </li>
            <li>
              <h6>Rozkład obowiązuje do</h6>
              <t-input
                :initialValue="selectedVersion.date"
                :editable="true"
                :on-component-value-updated="
                  (value) => saveVersion(() => selectedVersion.date = value)"
              />
            </li>
            <li>
              <h6>Skrócona nazwa rozkładu jazdy</h6>
              <t-input
                :initialValue="selectedVersion.shortName"
                :editable="true"
                :on-component-value-updated="
                  (value) => saveVersion(() => selectedVersion.shortName = value)"
              />
            </li>
            <li>
              <h6>Kod śledzenia wersji</h6>
              <t-input
                :initialValue="selectedVersion.subscribeCode"
              />
            </li>
            <li>
              <h6>Status</h6>
              <t-input
                :initialValue="selectedVersion.status"
              />
            </li>
          </ul>
          <br />
          <hr />
          <br />
          <h5>Ustawienia publikacji</h5>
          <ul>
            <li>
              <h6>Data publikacji</h6>
              <t-input
                :initialValue="selectedVersion.status"
              />
            </li>
            <li>
              <h6>Godzina publikacji</h6>
              <t-input
                :initialValue="selectedVersion.status"
              />
            </li>
            <li>
              <v-button>
                <template #label>
                  Zapisz ustawienia publikacji
                </template>
              </v-button>
            </li>
          </ul>
        </div>
        <div class="preview__right">
          <v-button
            v-if="selectedVersion.status === 'active'"
            style="width: 250px; float: right"
          >
            <template #label>
              Publikuj zmiany w aplikacji
            </template>
          </v-button>
        </div>
      </div>
    </template>
  </the-preview-list>
  <el-dialog
    title="Dodawanie nowej wersji rozkładu"
    v-model="isDialogVisible"
    width="45%">
    <div class="dialog">
      <div class="dialog__left">
        <ul>
          <li>
            <h6>Rozkład obowiązuje od</h6>
            <t-input
              :editable="true"
              :on-component-value-updated="(value) => { createScheduleDateStart = value }"
            />
          </li>
          <li>
            <h6>Rozkład obowiązuje do</h6>
            <t-input
              :editable="true"
              :on-component-value-updated="(value) => { createScheduleDateEnd = value }"
            />
          </li>
          <li>
            <h6>Skrócona nazwa rozkładu jazdy</h6>
            <t-input
              :editable="true"
              :on-component-value-updated="(value) => createScheduleShortName = value"
            />
          </li>
          <li>
            <h6>Firma</h6>
            <t-single-select>
              <template #selected>
                <span>
                  {{ createScheduleSelectedCompanyParent.name }}
                </span>
              </template>
              <template #items>
                <div
                  v-for="company in companiesList"
                  :key="company.id"
                  @click="() => { createScheduleSelectedCompanyParent = company }"
                >
                  {{company.name}}
                </div>
              </template>
            </t-single-select>
          </li>
        </ul>
      </div>
      <div class="dialog__right">
        <ul>
          <li>
            <h6>Skopiuj dane z rozkładu</h6>
            <t-single-select>
              <template #selected>
                <span
                  v-if="createScheduleSelectedVersionParent == null"
                >
                  Brak rodzica
                </span>
                <span
                  v-else
                >
                  {{ createScheduleSelectedVersionParent.companyName }}
                  {{ createScheduleSelectedVersionParent.date }}
                  {{ createScheduleSelectedVersionParent.shortName }}
                </span>
              </template>
              <template #items>
                <div
                  v-for="version in versionsList"
                  :key="version.id"
                  @click="() => { createScheduleSelectedVersionParent = version }"
                >
                  <div v-if="version.status !== 'creating'">
                    <div
                      v-if="version.status !== null && version.status !== 'created'"
                      class="list__item__status"
                      :class="getColorClassByStatus(version)"
                    >
                      <Circle />
                      {{ getTextsByStatus(version) }}
                    </div>
                    <p><b>{{ version.companyName }}</b></p>
                    <p>{{ version.date ? version.date : "-" }}</p>
                    <p>{{ version.shortName ? version.shortName : "-" }}</p>
                  </div>
                </div>
              </template>
            </t-single-select>
          </li>
        </ul>
        <p>Skopiuj tabele:</p>
        <el-checkbox-group
          style="z-index: 0;"
          v-model="createScheduleCheckedOptions">
          <el-checkbox label="bus_lines">
            Linie autobusowe
          </el-checkbox>
          <el-checkbox label="routes">
            Cele
          </el-checkbox>
          <el-checkbox label="route_connections">
            Trasy linii
          </el-checkbox>
          <el-checkbox label="destinations">
            Nazwy odjazdów
          </el-checkbox>
          <el-checkbox label="tracks">
            Kursy
          </el-checkbox>
          <el-checkbox label="departures">
            Odjazdy
          </el-checkbox>
        </el-checkbox-group>
      </div>
    </div>
    <v-button
      @click="createNewVersion"
    >
      <template #label>
        Stwórz nową wersję
      </template>
    </v-button>
  </el-dialog>
  <el-dialog
    title="Informacja"
    v-model="isSendingData"
    width="30%">
    Kopiowanie wersji to czasochłonna operacja. Powiadomimy Cię jak skończymy.
    Wróć tutaj za około 10 minut
    <template #footer>
      <v-button
        @click="reload"
      >
        <template #label>
          OK
        </template>
      </v-button>
    </template>
  </el-dialog>
</template>
<script>

import {
  Circle,
  TrashCan,
  PlusCircleOutline,
} from 'mdue';

import ThePreviewList from '../../../layouts/ThePreviewList.vue';
import ScheduleVersionService from '../../../../services/schedule/schedule-version.service';
import CompanyService from '../../../../services/auth/company.service';
import VListItem from '../../../UI/VListItem.vue';
import VTab from '../../../UI/VTab.vue';
import TInput from '../../../widgets/tarbus-grid/TInput.vue';
import VButton from '../../../UI/VButton.vue';
import TSingleSelect from '../../../widgets/tarbus-grid/TSingleSelect.vue';
import SpinnerCircular from '../../../widgets/spinners/spinner-circular.vue';

export default {
  name: 'ScheduleVersion',
  components: {
    SpinnerCircular,
    TSingleSelect,
    VButton,
    TInput,
    VTab,
    VListItem,
    ThePreviewList,
    Circle,
    TrashCan,
    PlusCircleOutline,
  },
  data() {
    return {
      isSendingData: false,
      versionsList: [],
      companiesList: [],
      isDialogVisible: false,
      selectedVersion: {},
      createScheduleSelectedVersionParent: null,
      createScheduleSelectedCompanyParent: {},
      createScheduleDateEnd: '',
      createScheduleDateStart: '',
      createScheduleShortName: '',
      createScheduleCheckedOptions: [],
    };
  },
  methods: {
    deleteVersion() {
      ScheduleVersionService.delete(this.selectedVersion.id).then(() => {
        this.$notify({
          message: 'Pomyślnie skasowano wersję',
          type: 'success',
          position: 'bottom-right',
        });
        this.reload();
      });
    },
    createNewVersion() {
      this.isDialogVisible = false;
      this.$notify({
        message: 'Pomyślnie stworzono wersję',
        type: 'success',
        position: 'bottom-right',
      });
      this.isSendingData = true;
      const newVersion = {
        date: this.createScheduleDateEnd,
        dateStart: this.createScheduleDateStart,
        shortName: this.createScheduleShortName,
        companyId: this.createScheduleSelectedCompanyParent.id,
        parent: this.createScheduleSelectedVersionParent.id,
        tables: this.createScheduleCheckedOptions,
      };
      ScheduleVersionService.create(newVersion).then(() => {
        this.isSendingData = false;
      });
    },
    reload() {
      window.location.reload();
    },
    saveVersion(saveMethod) {
      saveMethod();
      const version = {
        id: this.selectedVersion.id,
        date: this.selectedVersion.date,
        dateStart: this.selectedVersion.dateStart,
        company: {
          id: this.selectedVersion.companyId,
        },
        shortName: this.selectedVersion.shortName,
        subscribeCode: this.selectedVersion.subscribeCode,
        status: this.selectedVersion.status,
      };
      ScheduleVersionService.update(version).then(() => {
        this.$notify({
          message: 'Pomyślnie zaktualizowano wersję',
          type: 'success',
          position: 'bottom-right',
        });
      });
    },
    getColorClassByStatus(version) {
      switch (version.status) {
        case 'active':
          return 'color-success';
        case 'waiting':
          return 'color-waiting';
        case 'delete':
          return 'color-to-delete';
        default:
          return '';
      }
    },
    getTextsByStatus(version) {
      switch (version.status) {
        case 'active':
          return 'Aktywny';
        case 'waiting':
          return 'Zaplanowany';
        case 'delete':
          return 'Do skasowania';
        default:
          return '';
      }
    },
    async fetchScheduleVersions() {
      await ScheduleVersionService.getAllUserVersions().then((response) => {
        this.versionsList = response.data;
        // eslint-disable-next-line prefer-destructuring
        this.selectedVersion = response.data[0];
      });
    },
    async fetchUserCompanies() {
      await CompanyService.getUserCompanies().then((response) => {
        console.log(response.data);
        this.companiesList = response.data;
        // eslint-disable-next-line prefer-destructuring
        this.createScheduleSelectedCompanyParent = response.data[0];
      });
    },
  },
  mounted() {
    this.fetchScheduleVersions();
    this.fetchUserCompanies();
  },
};

</script>
<style lang="scss" scoped>
@import '../../../../assets/styles/variables.scss';

.dialog {
  display: flex;
  &__left {
    padding: 20px;
    flex: 1;
  }
  &__right {
    padding: 20px;
    flex: 1;
  }
}

ul {
  list-style-type: none;

  li {
    padding: 10px 0;
  }
}

.list {
  width: 100%;
  height: 100%;

  &__header {
    padding: 15px;
  }

  &__item {
    width: 100%;
    display: flex;
    align-items: center;
    border-bottom: 1px solid $color-gray-light;
    padding: 5px 15px;

    &__status {
      font-weight: bold;
      display: flex;
      align-items: center;
      gap: 5px;
      font-size: 8px;
      text-transform: uppercase;
      &__icon {

      }
      &__label {

      }
    }
  }
}

.preview {
  width: 100%;
  padding: 20px;
  display: flex;

  &__left {
    flex: 1;
  }

  &__right {
    flex: 1;
  }
}

.color-success {
  color: $color-success;
}

.color-waiting {
  color: $color-warning;
}

.color-to-delete {
  color: $color-error;
}
</style>
