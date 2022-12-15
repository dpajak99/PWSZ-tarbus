<template>
  <div class="tab" :class="[size, active ? 'active' : '']">
    <p class="tab__label">
      <slot name="label"></slot>
    </p>
  </div>
</template>

<script>
export default {
  name: 'VListItem',
  props: {
    size: {
      type: String,
      required: false,
      default: 'small',
    },
    active: {
      type: Boolean,
      required: false,
      default: false,
    },
  },
};
</script>

<style lang="scss" scoped>
@import '../../assets/styles/variables.scss';

.tab {
  width: 100%;
  display: flex;
  align-items: center;
  color: $color-gray-dark;

  &:hover {
    cursor: pointer;
  }

  &__label {
    flex: 1;
    font-size: 1.2rem;
  }
}

.small {
  padding: 0.7rem 2rem;
}

.large {
  padding: 1.5rem 3rem;
  border-radius: 0;

  p {
    text-transform: none;
  }
}

.active {
  background-color: $color-primary-light;
}
</style>
