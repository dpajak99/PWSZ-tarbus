<template>
  <the-sidebar tabsFor="administration" />
  <main>
    <router-view />
  </main>
</template>

<script>
import TheSidebar from '../../../layouts/TheSidebar.vue';

export default {
  name: 'AdministrationView',
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
