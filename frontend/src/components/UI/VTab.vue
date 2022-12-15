<template>
  <div class="tab" :class="[size, active ? 'active' : '']">
    <div class="tab__icon">
      <slot name="icon" />
    </div>
    <p class="tab__label">
      <slot name="label" />
    </p>
  </div>
</template>

<script>
export default {
  name: 'VTab',
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
  border-radius: 5px;
  gap: 0.5rem;
  display: flex;
  align-items: center;
  color: $color-gray-dark;

  &:hover {
    cursor: pointer;
  }

  &__label {
    font-size: 1.2rem;
    text-transform: uppercase;
  }

  &__icon {
    display: flex;
    font-size: 1.8rem;
  }
}

.small-clear {
  padding: 0;
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
