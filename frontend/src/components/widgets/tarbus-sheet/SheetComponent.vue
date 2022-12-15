<template>
  <v-grid
    theme="compact"
    class="sheet-component"
    :range="true"
    :export="true"
    :stretch="true"
    :source="usingRows"
    :columns="usingColumns"
    :useClipboard="true"
    :resize="true"
    :autoSizeColumn="true"
    @beforeeditstart="e => beforeEdit(e)"
    @afteredit="e => afterEdit(e)"
    @click.right="openClickPopup"
    @click.left="onLeftMouseClick"
    @keyup="keyupHandler"
  />

  <div
    class="menu-dialog"
    :style="`
      top: ${dialogPositionY + 5}px;
      left: ${dialogPositionX + 5}px;
      max-height: ${dialogVisible ? '200px' : '0px'};
      box-shadow: ${dialogVisible ? 'rgba(60, 64, 67, 0.2) 0 2px 6px 2px;' : 'none'};
      transition: ${dialogVisible ? 'max-height 0.05s ease-in' : 'max-height 0.05s ease-in'};
    `"
  >
    <ul class="menu-dialog__options">
      <li @click="execCopy">
        <content-copy class="menu-dialog__options__icon" />
        Kopiuj
      </li>
      <!--      <li @click="execCut">-->
      <!--        <content-cut class="menu-dialog__options__icon"></content-cut>-->
      <!--        Wytnij-->
      <!--      </li>-->
      <!--      <li @click="execPaste">-->
      <!--        <content-paste class="menu-dialog__options__icon"></content-paste>-->
      <!--        Wklej-->
      <!--      </li>-->
      <hr />
      <li
        v-if="isClassicOptionsEnabled"
        @click="() => deleteRow()"
      >
        <trash-can class="menu-dialog__options__icon" />
        Usuń wiersz
      </li>
      <li
        v-if="isClassicOptionsEnabled"
        @click="() => insertNewRow(0)"
      >
        <layers-plus class="menu-dialog__options__icon" />
        Wstaw wiersz powyżej
      </li>
      <li
        v-if="isClassicOptionsEnabled"
        @click="() => insertNewRow(1)"
      >
        <layers-minus class="menu-dialog__options__icon" />
        Wstaw wiersz poniżej
      </li>
    </ul>
    <ul class="menu-dialog__options">
      <slot name="dropdownOptions" />
    </ul>
  </div>
</template>

<script>
import VGrid from '@revolist/vue3-datagrid';
import {
  ContentCopy, TrashCan, LayersPlus, LayersMinus,
} from 'mdue';

export default {
  name: 'SheetComponent',
  components: {
    VGrid,
    ContentCopy,
    TrashCan,
    LayersPlus,
    LayersMinus,
  },
  props: {
    rows: Array,
    columns: Array,
    onRowDeleted: Function,
    isClassicOptionsEnabled: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      usingRows: this.rows,
      usingColumns: this.columns,
      undoHistory: [],
      redoHistory: [],
      editedDataGrid: [],
      editedValue: {},
      dialogPositionX: 50,
      dialogPositionY: 50,
      dialogVisible: false,
    };
  },
  methods: {
    getRows() {
      return this.usingRows;
    },
    getColumns() {
      return this.usingColumns;
    },
    async deleteRow() {
      const grid = document.querySelector('revo-grid');
      const focused = await grid.getFocused();
      const tmpRows = [...this.usingRows];
      this.onRowDeleted(focused.model);
      tmpRows.splice(focused.cell.y, 1);
      if ('lp' in focused.model) {
        tmpRows.forEach((row, index) => {
          tmpRows[index].lp = index + 1;
        });
      }
      this.usingRows = tmpRows;
      this.dialogVisible = false;
    },
    async insertNewRow(rowsToAdd) {
      const grid = document.querySelector('revo-grid');
      const focused = await grid.getFocused();
      const clearRow = {};
      Object.keys(focused.model).forEach((key) => { clearRow[key] = ''; });
      const tmpRows = [...this.usingRows];
      tmpRows.splice(focused.cell.y + rowsToAdd, 0, clearRow);
      if ('lp' in clearRow) {
        tmpRows.forEach((row, index) => {
          tmpRows[index].lp = index + 1;
        });
      }
      this.usingRows = tmpRows;
      this.dialogVisible = false;
    },
    execCopy(e) {
      e.preventDefault();
      e.stopPropagation();
      document.execCommand('copy');
      this.dialogVisible = false;
    },
    onLeftMouseClick(e) {
      if (this.dialogVisible) {
        e.preventDefault();
        this.dialogVisible = false;
      }
    },
    openClickPopup(e) {
      e.preventDefault();
      e.stopPropagation();
      this.dialogPositionX = e.pageX;
      this.dialogPositionY = e.pageY;
      this.dialogVisible = true;
      return false;
    },
    beforeEdit(e) {
      this.editedDataGrid = this.usingRows;
      this.editedValue = e.detail.model[e.detail.prop];
    },
    afterEdit(e) {
      if (e.detail.val != null && this.editedValue !== e.detail.val) {
        this.addToUndo(this.editedDataGrid);
        this.redoHistory = [];
      }
    },
    keyupHandler(event) {
      event.stopPropagation();
      event.preventDefault();
      if (event.ctrlKey) {
        if (event.shiftKey && event.code === 'KeyZ') {
          this.redoHandler();
        } else if (event.code === 'KeyZ') {
          this.undoHandler();
        }
      }
    },
    addToUndo(value) {
      if (this.undoHistory.length > 10) {
        this.undoHistory.pop();
      }
      this.undoHistory.unshift(JSON.stringify(value));
    },
    addToRedo() {
      if (this.redoHistory.length > 10) {
        this.redoHistory.pop();
      }
      this.redoHistory.unshift(JSON.stringify(this.usingRows));
    },
    undoHandler() {
      if (this.undoHistory.length !== 0) {
        this.addToRedo();
        this.usingRows = JSON.parse(this.undoHistory[0]);
        this.undoHistory.shift();
      }
    },
    redoHandler() {
      if (this.redoHistory.length !== 0) {
        this.addToUndo(this.usingRows);
        this.usingRows = JSON.parse(this.redoHistory[0]);
        this.redoHistory.shift();
      }
    },
  },
  watch: {
    rows(newVal) {
      this.usingRows = newVal;
    },
    columns(newVal) {
      this.usingColumns = newVal;
    },
  },
};
</script>

<style lang="scss" scoped>

.sheet-component {
  width: 100vw;
  display: flex;
}

.menu-dialog {
  width: 200px;
  height: 300px;
  background-color: white;
  z-index: 1000;
  position: fixed;
  overflow: hidden;
  border-radius: 4px;
  max-height: calc(100vh - 70px);
  overflow-y: auto;

  &__options {
    font-size: 14px;
    letter-spacing: 0.2px;
    line-height: 20px;
    position: relative;
    cursor: pointer;
    list-style: none;
    margin: 0;
    white-space: nowrap;

    li {
      gap: 0.5rem;
      padding: 6px 8px 6px 15px;
      display: flex;
      align-items: center;
    }

    li:hover {
      background-color: #eeeeee;
    }

    svg {
      font-size: 1.5rem;
    }
  }
}

</style>
