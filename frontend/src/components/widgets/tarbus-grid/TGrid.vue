<template>
  <div class="t-grid">
    <div
      v-if="selectAllVisible"
      class="main-checkbox"
    >
      <t-checkbox
        class="main-checkbox__checkbox"
        :checked="isAllRowsSelected"
        @click="selectAllRows"
      />
      <svg
        class="main-checkbox__leading"
        width="18"
        height="10"
        viewBox="0 0 13 5"
        fill="none"
        xmlns="http://www.w3.org/2000/svg">
        <path d="M0.333984 0L4.33398 4L8.33398 0H0.333984Z" fill="#767676" />
      </svg>
    </div>
    <div
      v-for="(rowContainer, index) in data"
      :key="`rowContainer_${data.length}_${index}`"
    >
      <t-row-container
        :id="`${index}`"
        :rowIndex="index"
        :rowData="rowContainer"
        :onRowUpdate="onRowUpdate"
        :selected="isAllRowsSelected"
        :onSelectRow="onSelectRow"
        :onUnselectRow="onUnselectRow"
      />
    </div>
  </div>
</template>

<script>
import TCheckbox from './TCheckbox.vue';
import TRowContainer from './TRowContainer.vue';

export default {
  name: 'TGrid',
  components: {
    TRowContainer,
    TCheckbox,
  },
  props: {
    data: {
      type: Array,
      required: true,
    },
    selectAllVisible: {
      type: Boolean,
      required: false,
      default: true,
    },
    onSelectedRowsUpdated: {
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
      isAllRowsSelected: false,
      selectedRows: [],
    };
  },
  methods: {
    selectAllRows() {
      this.isAllRowsSelected = !this.isAllRowsSelected;
    },
    onSelectRow(row) {
      if (this.selectedRows.filter((e) => e.id === row.id).length > 0) return;
      this.selectedRows.push(row);
      this.onSelectedRowsUpdated(this.selectedRows);
    },
    onUnselectRow(row) {
      this.selectedRows = this.selectedRows.filter((e) => e.id !== row.id);
      this.onSelectedRowsUpdated(this.selectedRows);
    },
  },
};
</script>

<style lang="scss">
@import '../../../assets/styles/variables.scss';

.main-checkbox {
  margin-left: 30px;

  &__checkbox {
    &.checkbox {
      border: solid 2px $color-black-light;
    }
  }

  &__leading {
    margin-left: 3px;
  }
}

.t-grid {
  width: 99%;
  padding-right: 1%;
}
</style>
