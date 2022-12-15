<template>
  <div
    class="input-container"
    tabindex="0"
  >
    <input
      ref="input"
      type="text"
      class="input-container__input"
      :class="getInputClass()"
      :placeholder="getPlaceholder()"
      :value="isActive ? value : ''"
      :readonly="!isActive"
      @dblclick="setActive"
      @blur="onBlur"
      @keyup.enter="() => setDisabled(true)"
      @keyup.esc="() => setDisabled(false)"
    >
    <div
      v-if="editable && !isActive"
      class="input-container__action"
    >
      <button
        @click="setActive"
      >
        <pencil />
      </button>
    </div>
  </div>
</template>

<script>
import { Pencil } from 'mdue';

export default {
  name: 't-input',
  components: {
    Pencil,
  },
  props: {
    initialValue: String,
    colIndex: Number,
    editable: Boolean,
    onComponentValueUpdated: {
      type: Function,
    },
  },
  data() {
    return {
      isActive: false,
      value: this.initialValue,
    };
  },
  methods: {
    getInputClass() {
      if (this.isActive) {
        return 'bg-white';
      } if (!this.editable) {
        return 'cursor-disabled';
      }
      return '';
    },
    getPlaceholder() {
      if (!this.isActive && this.value != null && this.value !== '') {
        return this.value;
      }
      if (!this.isActive && (this.value == null || this.value === '')) {
        return '---';
      }
      return '';
    },
    setDisabled(status) {
      if (this.isActive) {
        if (status && this.$refs.input.value !== '') {
          this.updateValue(this.$refs.input.value);
        }
        this.isActive = false;
      }
    },
    setActive() {
      if (this.editable) {
        this.isActive = true;
        this.$refs.input.focus();
      }
    },
    onBlur() {
      this.setDisabled(true);
    },
    updateValue(value) {
      this.value = value;
      this.onComponentValueUpdated(value, this.colIndex);
    },
  },
  watch: {
    initialValue(value) {
      this.value = value;
    },
  },
};
</script>

<style lang="scss">
@import '../../../assets/styles/variables.scss';
.bg-white {
  background-color: white !important;
}

.cursor-disabled {
  cursor: default;
}

.input-container {
  width: 100%;
  height: $size-grid-component;
  cursor: pointer;
  background-color: $color-grid-background;
  display: flex;

  &__action {
    width: 50px;
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

  &__input {
    width: 100%;
    background: none;
    font-size: 12px;
    padding: 5px 10px;
    border: none;

    &::placeholder {
      text-align: center;
      color: $color-black-light;
    }
  }
}
</style>
