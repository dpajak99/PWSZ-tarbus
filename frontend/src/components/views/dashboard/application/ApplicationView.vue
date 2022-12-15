<template>
  <the-sidebar tabsFor="application" />
  <main>
    <router-view />
  </main>
</template>

<script>
import TheSidebar from '../../../layouts/TheSidebar.vue';

export default {
  name: 'ApplicationView',
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
