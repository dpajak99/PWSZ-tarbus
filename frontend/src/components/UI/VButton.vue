<template>
  <div class="button">
    <slot name="label" />
  </div>
</template>

<script>
export default {
  name: 'VButton',
};
</script>

<style lang="scss" scoped>
@import '../../assets/styles/variables.scss';

.button {
  padding: 10px 15px;
  background-color: $color-primary;
  color: $color-white;
  font-weight: bold;
  text-align: center;
  font-size: 13px;
  cursor: pointer;
}
</style>
