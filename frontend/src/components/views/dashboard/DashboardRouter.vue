<template>
  <the-menu />
  <router-view />
  <div class="team-test" />
</template>

<script>
import TheMenu from '../../layouts/TheMenu.vue';

export default {
  name: 'DashboardRouter',
  components: {
    TheMenu,
  },
};
</script>
