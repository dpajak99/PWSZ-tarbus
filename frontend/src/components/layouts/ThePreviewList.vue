<template>
  <div class="table">
    <div class="table__list">
      <the-scroll-container>
        <template #content>
          <slot name="list" />
        </template>
      </the-scroll-container>
    </div>
    <div class="table__preview">
      <the-scroll-container>
        <template #content>
          <slot name="preview" />
        </template>
      </the-scroll-container>
    </div>
  </div>
</template>

<script>
import TheScrollContainer from './TheScrollContainer.vue';

export default {
  name: 'ThePreviewList',
  components: {
    TheScrollContainer,
  },
};
</script>

<style lang="scss" scoped>
@import '../../assets/styles/variables.scss';
.table {
  display: flex;
  width: 100%;
  height: 100%;
  &__list {
    flex: 1;
    border-right: 1px solid #eee;
  }
  &__preview {
    flex: 5;
  }
}
</style>
