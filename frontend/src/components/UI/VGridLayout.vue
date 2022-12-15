<template>
  <div class="grid-layout">
    <div class="grid-layout__action-bar">
      <slot name="action-bar" />
    </div>
    <div class="grid-layout__grid">
      <slot name="grid" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'VGridLayout',
  props: {

  },
};
</script>

<style lang="scss" scoped>
@import '../../assets/styles/variables.scss';

.grid-layout {
  width: 100%;
  height: 100%;

  &__action-bar {
    padding: 10px 9px;
    display: flex;
    z-index: 1;
    background: $color-white;
    align-items: center;
    width: 100%;
  }

  &__grid {

  }
}
</style>
