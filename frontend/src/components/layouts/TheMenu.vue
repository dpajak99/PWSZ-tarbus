<template>
  <div class="menu">
    <img src="@/assets/images/hamburger.svg" alt="hamburger" />
    <img src="@/assets/images/logo.svg" alt="logo" />
    <div class="menu__tabs">
      <router-link v-for="tab in tabs" :to="tab.link" :key="tab.id">
        <v-tab :active="activeTab === tab.link">
          <template #label>
            <p>{{ tab.label }}</p>
          </template>
          <template #icon>
            <component :is="tab.icon" />
          </template>
        </v-tab>
      </router-link>
      <el-dropdown trigger="click" class="version-dropdown">
        <span class="version-dropdown-link">
          Edytowana wersja: {{ selectedVersion['companyName'] }} - {{ selectedVersion['date'] }}
          <i class="el-icon-arrow-down el-icon--right" />
        </span>
        <template #dropdown>
          <el-dropdown-menu class="version-dropdown__content">
            <el-dropdown-item
              v-for="version in versionsList"
              :key="version.id"
              class="version-dropdown__item"
              @click="() => selectNewVersion(version)"
            >
              {{version['companyName']}} - {{version['date']}}
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
    <div class="menu__more">
      <moon-waxing-crescent />
      <img src="@/assets/images/apps.svg" alt="apps" />
      <bell />
      <the-profile-dropdown />
    </div>
  </div>
</template>

<script>
// prettier-ignore
import {
  Bell, MoonWaxingCrescent,
} from 'mdue';

import { ElDropdown } from 'element-plus';

import VTab from '../UI/VTab.vue';
import tabs from '../data/tabs';
import ScheduleVersionService from '../../services/schedule/schedule-version.service';
import TheProfileDropdown from './TheProfileDropdown.vue';

export default {
  name: 'TheMenu',
  components: {
    TheProfileDropdown,
    VTab,
    Bell,
    ElDropdown,
    MoonWaxingCrescent,
  },
  data() {
    return {
      /* eslint-disable */
      activeTab: this.$route.path.slice(
        this.$route.path.indexOf('/'),
        this.$route.path.lastIndexOf('/')
      ),
      tabs: tabs,
      versionsList: [],
      selectedVersion: {},
    };
  },
  methods: {
    selectNewVersion(selectedVersion) {
      this.selectedVersion = selectedVersion;
      localStorage.setItem('current_schedule_version', JSON.stringify(selectedVersion));
      window.location.reload();
    },
    async fetchScheduleVersions() {
      await ScheduleVersionService.getAllUserVersions().then((response) => {
        this.versionsList = response.data;
        const currentVersion = JSON.parse(localStorage.getItem('current_schedule_version'));
        if(  currentVersion == null || !this.versionsList.some( e => e.id === currentVersion.id)) {
          this.selectNewVersion(this.versionsList[0])
        } else {
          this.selectedVersion = currentVersion;
        }
      });
    }
  },
  watch: {
    $route(newRoute) {
      this.activeTab = newRoute.path.slice(
        this.$route.path.indexOf('/'),
        this.$route.path.lastIndexOf('/')
      );
    },
  },
  mounted() {
    this.fetchScheduleVersions();
  }
};
</script>

<style lang="scss" scoped>
@import '../../assets/styles/variables.scss';

.menu {
  display: flex;
  gap: 2.5rem;
  align-items: center;
  padding: 1.5rem 2.5rem;
  font-size: 1.8rem;
  border-bottom: 1px solid $color-gray;
  color: $color-gray-dark;

  grid-area: menu;

  &__tabs {
    margin: 0 3.5rem;
    display: flex;
    align-items: center;
    gap: 1rem;
    width: 100%;
  }

  &__more {
    display: flex;
    align-items: center;
    gap: 2rem;
    margin-left: auto;
  }
}
</style>

<style lang="scss">
@import '../../assets/styles/variables.scss';

.version-dropdown-link {
  cursor: pointer;
  font-size: 1.3rem;

  display: flex;
  justify-content: space-between;
}

.version-dropdown {
  background-color: $color-gray-light;
  padding: 1rem;
  justify-self: flex-end;
  width: 400px;

  border-radius: 0.5rem;

  &__item {
    width: 400px;
    cursor: pointer;
  }
}

.version-dropdown-menu {
  border: 2px solid red;
}
</style>
