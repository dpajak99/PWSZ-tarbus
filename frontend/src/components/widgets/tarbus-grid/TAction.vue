<template>
  <div class="actions-container">
    <div
      v-for="(action, index) in component.actions"
      :key="`action_${index}`"
      class="actions-container__action"
    >
      <button
        @click="() => action.onClick(currentRow)"
      >
        <TrashCan
          v-if="action.icon === 'trash'"
        />
        <Printer
          v-if="action.icon === 'print'"
        />
      </button>
    </div>
  </div>
</template>

<script>
import { TrashCan, Printer } from 'mdue';

export default {
  name: 't-action',
  components: {
    TrashCan,
    Printer,
  },
  props: {
    component: Object,
    currentRow: Object,
  },
  data() {
    return {};
  },
  methods: {},
};
</script>

<style lang="scss">
@import '../../../assets/styles/variables.scss';

.actions-container {
  width: 100%;
  height: $size-grid-component;
  text-align: center;
  background-color: #F7F8F9;

  &__action {
    position: relative;
    float: right;
    width: 30px;
    cursor: pointer;
    padding: 3px 10px;
    text-align: center;
    font-size: 16px;

    button {
      background: none;
      border: none;
      cursor: pointer;
    }
  }
}
</style>
