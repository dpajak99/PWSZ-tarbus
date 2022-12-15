<template>
  <div class="t-row">
    <t-checkbox
      class="t-row__checkbox"
      :key="`checkbox_${rowId}`"
      :checked="isRowSelected"
      @click="() => selectRow(null)"
    />
    <div
      v-for="(component, index) in cRowData"
      :key="`${id}-${index}`"
      :style="`flex: ${component.flex}`"
      class="t-row__component"
    >
      <div
        class="t-row__component__title"
      >
        <span v-if="rowIndex === 0">{{ component.label }}</span>
      </div>
      <t-input
        v-if="!component.hidden && component.type === 'text'"
        :colIndex="index"
        :initial-value="component.value != null ? component.value.toString() : '---'"
        :onComponentValueUpdated="onComponentValueUpdated"
        :editable="component.editable"
      />
      <t-action
        v-if="!component.hidden && component.type === 'action'"
        :component="component"
        :current-row="getRowValues()"
      />
      <button
        v-if="!component.hidden && component.type === 'button'"
        class="component-button"
        @click="() => onButtonComponentAction(component)"
      >
        {{ component.value }}
      </button>
    </div>
  </div>
</template>

<script>
import TCheckbox from './TCheckbox.vue';
import TInput from './TInput.vue';
import TAction from './TAction.vue';

export default {
  name: 't-row',
  components: {
    TAction,
    TInput,
    TCheckbox,
  },
  props: {
    id: {
      type: String,
      required: true,
    },
    rowIndex: {
      type: Number,
      required: true,
    },
    rowData: {
      type: Array,
      required: true,
    },
    selected: {
      type: Boolean,
      required: true,
    },
    onUnselectRow: {
      type: Function,
      required: true,
    },
    onSelectRow: {
      type: Function,
      required: true,
    },
    onRowUpdate: {
      type: Function,
      required: true,
    },
  },
  data() {
    return {
      isRowSelected: this.selected,
      cRowData: this.rowData,
      rowId: 0,
    };
  },
  methods: {
    selectRow(val) {
      if (val !== null) {
        this.isRowSelected = val;
      } else {
        this.isRowSelected = !this.isRowSelected;
      }
      const row = this.getRowValues();
      if (this.isRowSelected) {
        this.onSelectRow(row);
      } else {
        this.onUnselectRow(row);
      }
    },
    async onComponentValueUpdated(value, rowIndex) {
      this.cRowData[rowIndex].value = value;
      const newRowData = await this.onRowUpdate(this.getRowValues());
      this.cRowData.forEach((item, index) => {
        if (item.key === 'id') {
          this.cRowData[index].value = newRowData.id;
          this.rowId = newRowData.id;
        }
      });
      console.log('Res');
      console.log(this.cRowData);
      console.log(newRowData);
      this.cRowData[rowIndex].value = newRowData[this.cRowData.key];
    },
    getRowValues() {
      const newRowData = {};
      let model = {};
      this.cRowData.forEach((c) => {
        if (c.type === 'text') {
          newRowData[c.key] = c.value;
        } else if (c.type === 'model') {
          model = c.model;
        }
      });
      return {
        id: this.id,
        model,
        values: newRowData,
      };
    },
    onButtonComponentAction(component) {
      const value = this.getRowValues();
      component.onClick(value);
    },
  },
  watch: {
    selected(val) {
      this.selectRow(val);
    },
    rowData(val) {
      this.cRowData = val;
    },
  },
  mounted() {
    this.cRowData.forEach((item) => {
      if (item.key === 'id') {
        this.itemId = item.value;
      }
    });
  },
};
</script>

<style lang="scss">
@import '../../../assets/styles/variables.scss';

.t-row {
  padding-left: 30px;
  position: relative;
  width: 100%;
  display: flex;

  &__component {
    padding: 0 5px;

    &__title {
      height: 14px;
      color: $color-gray-medium;
    }
  }

  &__checkbox {
    margin-top: 24px;
  }
}

.component-button {
  width: 100%;
  height: $size-grid-component;
  font-weight: bold;
  font-size: 12px;
  cursor: pointer;
  border: none;
  color: $color-white;
  background-color: $color-primary;
}
</style>
