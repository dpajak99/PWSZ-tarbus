<template>
  <div class="t-row-container">
    <div
      v-if="localRowData.title"
      class="t-row-container__header"
    >
      <t-checkbox
        :checked="isGroupSelected"
        @click="selectGroup"
      />
      <div class="t-row-container__header__title">{{ localRowData.title }}</div>
    </div>
    <div
      v-if="containsChildren()"
    >
      <div
        v-for="(rowContainer, index) in localRowData.children"
        :key="`rowContainer_${id}-${index}`"
      >
        <t-row-container
          :id="`${id}-${index}`"
          :rowIndex="index"
          :rowData="rowContainer"
          :selected="isGroupSelected"
          :onSelectRow="onSelectRow"
          :onUnselectRow="onUnselectRow"
        />
      </div>
    </div>
    <div
      v-else-if="containsRows()"
      class="t-row-container__rows"
    >
      <div
        v-for="(singleRow, index) in localRowData.data"
        :key="`singleRow_${localRowData.length}-${index}`"
      >
        <t-row
          :id="`${id}-${index}`"
          :rowIndex="index"
          :rowData="singleRow"
          :selected="isGroupSelected"
          :onSelectRow="onSelectRow"
          :onRowUpdate="onRowUpdate"
          :onUnselectRow="onUnselectRow"
        />
      </div>
    </div>
    <div
      v-else
      class="text_found_nothing"
    >
      ---- Brak rekordów
    </div>
  </div>
</template>

<script>
import TCheckbox from './TCheckbox.vue';
import TRow from './TRow.vue';

export default {
  name: 't-row-container',
  components: {
    TRow,
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
      type: Object,
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
      isGroupSelected: this.selected,
      localRowData: this.rowData,
    };
  },
  methods: {
    selectGroup() {
      this.isGroupSelected = !this.isGroupSelected;
    },
    containsChildren() {
      return this.localRowData.children != null && this.localRowData.children !== '' && this.localRowData.children.length !== 0;
    },
    containsRows() {
      return this.localRowData.data != null && this.localRowData.data !== '' && this.localRowData.data.length !== 0;
    },
  },
  watch: {
    selected(val) {
      this.isGroupSelected = val;
    },
    rowData(val) {
      this.localRowData = val;
      console.log(this.localRowData.data.length);
    },
  },
};
</script>

<style lang="scss">
.text_found_nothing {
  width: 100%;
  padding-left: 30px;
  color: #999;
  font-size: 16px;
}

.t-row-container {
  padding-left: 30px;

  &__header {
    &__title {
      display: inline-block;
      padding: 15px 15px 5px 15px;
      font-size: 18px;
    }
  }
}
</style>
