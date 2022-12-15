<template>
  <the-sidebar tabsFor="schedule"> </the-sidebar>
  <main>
    <router-view></router-view>
  </main>
</template>

<script>
import TheSidebar from '../../../layouts/TheSidebar.vue';

export default {
  name: 'ScheduleView',
  components: {
    TheSidebar,
  },
};
</script>

<style lang="scss" scoped>
main {
  grid-area: main;
}
</style>
