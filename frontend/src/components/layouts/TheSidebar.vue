<template>
  <div class="sidebar">
    <div class="sidebar__tabs">
      <router-link v-for="tab in tabs" :to="tab.link" :key="tab.id">
        <v-tab size="large" :active="activeTab === tab.link">
          <template #label>
            <p>{{ tab.label }}</p>
          </template>
          <template #icon>
            <component :is="tab.icon" />
          </template>
        </v-tab>
      </router-link>
      <el-collapse class="tab-collapse">
        <el-collapse-item
          :name="tab.id"
          class="tab-collapse-item"
          v-for="tab in collapsedTabs"
          :key="tab.id"
        >
          <template #title>
            <component :is="tab.groupIcon" />
            {{ tab.groupTitle }}
          </template>
          <router-link v-for="item in tab.items" :key="item.id" :to="item.link">
            <v-tab size="large" :active="activeTab === item.link">
              <template #label>
                <p>{{ item.label }}</p>
              </template>
              <template #icon>
                <component :is="item.icon" />
              </template>
            </v-tab>
          </router-link>
        </el-collapse-item>
      </el-collapse>
    </div>
    <div class="sidebar__settings">
      <v-tab size="large">
        <template #label>
          <p>Ustawienia</p>
        </template>
        <template #icon>
          <cog />
        </template>
      </v-tab>
      <v-tab size="large">
        <template #label>
          <p>Ustawienia</p>
        </template>
        <template #icon>
          <cloud-circle />
        </template>
      </v-tab>
    </div>
  </div>
</template>

<script>
// prettier-ignore
import {
  Cog, CloudCircle,
} from 'mdue';

import VTab from '../UI/VTab.vue';

import tabs from '../data/tabs';

export default {
  name: 'TheSidebar',
  props: {
    tabsFor: {
      type: String,
      required: true,
    },
  },
  components: {
    VTab,
    Cog,
    CloudCircle,
  },
  computed: {
    tabs() {
      /* eslint arrow-parens: ["error", "as-needed"] */
      return tabs
        .filter(el => el.label === this.tabsFor && el.children)
        .flatMap(el => el.children)
        .filter(el => el.label);
    },
    collapsedTabs() {
      /* eslint arrow-parens: ["error", "as-needed"] */
      return tabs
        .filter(el => el.label === this.tabsFor && el.children)
        .flatMap(el => el.children)
        .filter(el => !el.label)
        .flat();
    },
  },
  data() {
    return {
      activeTab: this.$route.path,
    };
  },
  watch: {
    $route(newRoute) {
      this.activeTab = newRoute.path;
    },
  },
};
</script>

<style lang="scss" scoped>
@import '../../assets/styles/variables.scss';

.sidebar {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  border-right: 1px solid $color-gray;

  grid-area: sidebar;

  &__tabs {
    display: flex;
    flex-direction: column;
  }

  &__settings {
    border-top: 1px solid $color-gray;
    padding: 1rem 0;
    color: $color-grayish-blue;
  }
}
</style>
