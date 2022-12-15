<template>
  <div class="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  components: {},
};
</script>

<style lang="scss">
@import '@/assets/styles/variables.scss';

* {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
  font-family: 'Poppins', sans-serif;
  text-decoration: none;
}

html {
  font-size: 62.5%;
}

.app {
  min-height: 100vh;
  max-height: 100vh;
  display: grid;
  grid-template-areas:
    'menu menu menu'
    'sidebar main team';
  grid-template-columns: 25.5rem 1fr 5.5rem;
  grid-template-rows: max-content 1fr;
}

main {
  grid-area: main;
  height: 100%;
  overflow-y: scroll;

  &::-webkit-scrollbar {
    width: 5px;
  }

  &::-webkit-scrollbar-track {
    background-color: rgba(0, 0, 0, 0);
    border-radius: 10px;
  }

  &::-webkit-scrollbar-track:hover {
    background-color: rgba(0, 0, 0, 0.1);
    border-radius: 10px;
  }

  &::-webkit-scrollbar-thumb {
    border-radius: 10px;
    background-color: rgb(188, 192, 196);
  }
}

h6 {
  color: #4C5264;
  font-weight: normal;
  font-size: 13px;
}

h5 {
  color: #4C5264;
  font-weight: normal;
  font-size: 15px;
}

// ###################
// Grid styling
.window {
  width: 100%;
  height: 100%;
}

.grid-error-message {
  &__message {
    color: red;
  }
}

// ##################################################
// ##################################################
// LIBRARIES COMPONENTS STYLING
// ##################################################

// COLLAPSE TAB
.tab-collapse {
  border: none;
}

.tab-collapse-item {
  color: $color-gray-dark;
}
</style>
