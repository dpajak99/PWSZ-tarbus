<template>
  <div class="wrapper">
    <div>
      <p class="headline">404</p>
      <p>Ups! Strona o podanym adresie nie istnieje</p>
      <router-link to="/dashboard">Strona główna</router-link>
    </div>
  </div>
</template>

<style lang="scss" scoped>
@import '../../../assets/styles/variables.scss';

.wrapper {
  border: 2px solid gold;
  height: 100vh;
  width: 100vw;

  display: flex;
  align-items: center;
  justify-content: center;
}

div {
  border-radius: 1rem;
  padding: 5rem;
  box-shadow: rgba(99, 99, 99, 0.2) 0 2px 8px 0;
  text-align: center;

  .headline {
    font-size: 12rem;
    line-height: 1;
  }

  p:not(.headline) {
    font-weight: bold;
    font-size: 1.2rem;
  }

  a {
    display: inline-block;
    margin-top: 2rem;
    background-color: $color-primary;
    padding: 1rem 2rem;
    border-radius: 0.5rem;
    color: $color-primary-light;
  }
}
</style>
