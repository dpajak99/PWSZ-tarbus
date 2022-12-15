<template>
  <div
    :class="checked ? 'checkbox checkbox__checked' : 'checkbox'"
  >
    <div
      v-if="checked"
      class="checkbox__done"
    >
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
        <path d="M0 0h24v24H0V0z" fill="none" />
        <path d="M9 16.2L4.8 12l-1.4 1.4L9 19 21 7l-1.4-1.4L9 16.2z" />
      </svg>
    </div>
    <div
      v-else
      class="checkbox__disactive"
    >
      <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" />
    </div>
  </div>
</template>

<script>

export default {
  name: 't-checkbox',
  components: {},
  props: {
    checked: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {};
  },
  methods: {},
};

</script>
<style lang="scss">
@import '../../../assets/styles/variables.scss';

.checkbox {
  background-color: white;
  border: solid 1px $color-gray;
  border-radius: 2px;
  cursor: pointer;
  display: inline-block;
  height: 16px;
  line-height: inherit;
  padding: 0;
  user-select: none;
  width: 16px;

  &__checked {
    background-color: $color-primary;
    border: solid 1px $color-primary;
    padding: 0;

    :active {
      background-color: $color-primary-active;
    }
  }

  svg {
    padding: 0;
    margin: 0;
    width: 100%;
    height: 100%;
    fill: #fff;
  }

  &__done {
    width: 100%;
    height: 100%;
  }

  &__disactive {
    width: 100%;
    height: 100%;
  }
}
</style>
