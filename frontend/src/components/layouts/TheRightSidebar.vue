<template>
  <div class="sidebar">
    <div class="account account-active">
      <account-multiple />
    </div>
    <div class="account">
      <account-multiple />
    </div>
    <div class="account">
      <account-multiple />
    </div>
    <plus />
  </div>
</template>

<script>
// prettier-ignore
import {
  AccountMultiple, Plus,
} from 'mdue';

export default {
  name: 'TheRightSidebar',
  components: {
    AccountMultiple,
    Plus,
  },
};
</script>

<style lang="scss" scoped>
@import '../../assets/styles/variables.scss';

.sidebar {
  grid-area: team;
  border-left: 1px solid $color-gray;
  z-index: 100;
  padding: 1.5rem 0;

  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
  overflow-x: hidden;

  svg {
    width: 2rem;
    height: 2rem;
    color: $color-gray-dark;
  }
}

.account {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 4rem;
  height: 4rem;
  padding: 0.5rem;
  border-radius: 50%;
}

.account-active {
  background: $color-primary-light;
  position: relative;

  &::after {
    position: absolute;
    content: '';
    background: $color-primary;
    height: 120%;
    width: 1rem;
    left: 107%;
    border-radius: 5rem;
  }
}
</style>
