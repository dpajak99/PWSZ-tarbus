<template>
  <div
    class="select-container"
    tabindex="0"
  >
    <div
      class="selected-item"
      @click="isDropdownVisible = !isDropdownVisible"
    >
      <div class="selected-item__label">
        <slot name="selected" />
      </div>
      <div class="selected-item__icon">
        <chevron-up
          v-if="isDropdownVisible"
        />
        <chevron-down
          v-else
        />
      </div>
    </div>
    <the-scroll-container
      class="dropdown"
      :style="`
      visibility: ${isDropdownVisible ? 'visible' : 'hidden'};
      max-height: ${isDropdownVisible ? '200px' : '0'}
      `"
      @click="isDropdownVisible = false"
    >
      <template #content>
        <slot name="items" />
      </template>
    </the-scroll-container>
  </div>
</template>

<script>
import {
  ChevronDown,
  ChevronUp,
} from 'mdue';

import TheScrollContainer from '../../layouts/TheScrollContainer.vue';

export default {
  name: 'TSingleSelect',
  components: {
    TheScrollContainer,
    ChevronDown,
    ChevronUp,
  },
  data() {
    return {
      isDropdownVisible: false,
    };
  },
};
</script>

<style lang="scss">
@import '../../../assets/styles/_variables.scss';

.select-container {
  width: 100%;
  height: $size-grid-component;
  cursor: pointer;
  display: block;
  background-color: $color-grid-background;
}

.selected-item {
  width: 100%;
  padding: 3px 18px;
  display: flex;
  align-items: center;

  &__label {
    flex: 6;
  }

  &__icon {
    text-align: right;
    font-size: 18px;
    flex: 1;
  }
}

.dropdown {
  padding: 10px;
  user-select: none;
  flex: 1;
  height: 200px;
  position: relative;
  background-color: white;
  cursor: pointer;
  display: block;
  z-index: 1000;
  overflow: hidden;
  box-shadow: rgba(60, 64, 67, 0.2) 0 6px 6px 2px;
  transition: max-height 0.25s ease-out;
  border-radius: 4px;
  overflow-y: auto;
}
</style>
