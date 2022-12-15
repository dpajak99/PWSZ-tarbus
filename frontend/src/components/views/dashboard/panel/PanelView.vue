<template>
  <the-sidebar tabsFor="panel" />
  <main>
    <router-view />
  </main>
</template>

<script>
import TheSidebar from '../../../layouts/TheSidebar.vue';

export default {
  name: 'PanelView',
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
