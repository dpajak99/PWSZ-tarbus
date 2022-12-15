<template>
  <div class="sheet">
    <!-- ################### -->
    <!-- Header -->
    <header class="header">
      <div class="header__logo">
        <img src="@/assets/images/logo-without-name.svg" alt="logo" />
      </div>
      <div class="header__title">
        <ul>
          <li v-for="(item, index) in title" :key="index">
            {{ `${item.label}: ${item.value}` }}
          </li>
        </ul>
      </div>
      <div class="header__items">
        <el-dropdown trigger="click" v-for="(item, index) in menuItems" :key="index">
          <span class="version-dropdown-link">
            {{ item.title }}
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item
                v-for="(action, index) in item.actions"
                :key="index"
                @click="action.onClick"
              >
                {{ action.label }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
      <div class="header__prefix">
        <button
          class="header__prefix__button-save"
          @click="onSave"
        >Zapisz</button>

        <el-dropdown trigger="click" class="profile-dropdown">
          <span>
            <img src="@/assets/images/logo-michalus.svg" alt="logo-michalus" />
          </span>
          <template #dropdown>
            <div class="profile-dropdown__content">
              <button @click="logout">
                Wyloguj
              </button>
            </div>
          </template>
        </el-dropdown>
      </div>
    </header>
    <!-- ################### -->
    <!-- SubHeader -->
    <div class="subheader">
      <div class="subheader__tools">
        <a v-for="(tool, index) in tools" :key="index" @click="tool.onClick">
          <template v-if="tool.icon">
            <component :is="tool.icon" />
          </template>
        </a>
      </div>
      <div class="subheader__labeled-tools">
        <div v-for="(tool, index) in labeledTools" :key="index" @click="tool.onClick">
          <template v-if="tool.icon">
            <component :is="tool.icon" />
            {{ tool.label }}
          </template>
        </div>
      </div>
    </div>

    <!-- ################### -->
    <!-- Sheet / grid -->
    <div class="template">
      <slot />
    </div>
  </div>
  <el-dialog
    title="Zapisywanie formularza"
    v-model="isDialogVisible"
    width="30%"
    :before-close="handleDialogClose">

    <el-progress :percentage="progressBarValue" color="blue" />
    {{ currentDialogMessage }}

    <template #footer>
      <span class="dialog-footer">
        <el-button
          v-if="isSendingComplete"
          type="primary"
          @click="successCloseDialog">
          Zamknij
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { ElDropdown } from 'element-plus';

export default {
  name: 'Sheet',
  components: {
    ElDropdown,
  },
  props: {
    title: Object,
    menuItems: Object,
    tools: Object,
    labeledTools: Object,
    onSave: Function,
    isSendingComplete: Boolean,
    progressBarValue: Number,
    currentDialogMessage: Text,
    onRefresh: Function,
  },
  data() {
    return {
      isDialogVisible: false,
    };
  },
  methods: {
    openDialog() {
      this.isDialogVisible = true;
    },
    handleDialogClose(done) {
      if (this.isSendingComplete) {
        this.isDialogVisible = false;
        return;
      }
      this.$confirm('Czy na pewno chcesz zamknąć? Dane mogą zostać utracone, lub zapisane w połowie')
        .then(() => {
          done();
          this.isDialogVisible = false;
        })
        .catch(() => {});
    },
    successCloseDialog() {
      this.isDialogVisible = false;
      this.onRefresh();
    },
  },
};
</script>

<style lang="scss" scoped>
@import '../../../assets/styles/variables.scss';

.sheet {
  height: 100vh;
  width: 100vw;
  display: grid;
  overflow: hidden;
  grid-template-rows: max-content max-content 1fr;
}

.template {
  width: 100vw;
}

.header {
  display: grid;
  grid-template-columns: max-content 1fr max-content;
  grid-template-rows: repeat(2, max-content);
  grid-column-gap: 2rem;
  grid-row-gap: 0.3rem;
  position: relative;
  padding: 1.5rem;

  &::after {
    content: '';
    position: absolute;
    height: 0.1rem;
    width: 120%;
    bottom: 0;
    left: -10%;
    background-color: $color-gray;
  }

  &__logo {
    grid-row: 1/3;
    align-self: center;
  }

  &__items {
    grid-row: 2/3;
  }

  &__prefix {
    grid-row: 1/-1;
    display: flex;
    align-self: center;
    padding: 0 1.5rem;
    align-items: center;
    gap: 4rem;

    &__button-save {
      padding: 5px 20px;
      background-color: $color-primary;
      color: $color-white;
      cursor: pointer;
      border: none;
    }
  }

  &__title {
    ul {
      list-style: none;
      align-items: center;
      display: flex;
      font-size: 2rem;
      font-weight: 500;
      color: $font-color-dark;
    }

    li {
      padding-right: 1rem;
      margin-right: 1rem;
      &:not(:last-child) {
        position: relative;

        &::before {
          content: '';
          position: absolute;
          background-color: $font-color-dark;
          width: 0.2rem;
          height: 2rem;
          left: 100%;
          top: 50%;
          transform: translateX(-50%) translateY(-50%);
        }
      }
    }
  }
}

.subheader {
  padding: 0 1.5rem;
  display: flex;
  border-bottom: 1px solid #bbbbbb;
  align-items: center;
  gap: 4rem;

  &__tools {
    display: flex;
    padding: 0;
    color: #000;

    a {
      font-size: 2rem;
      display: flex;
      padding: 0.5rem 0.5rem;
      cursor: pointer;

      &:nth-child(even) {
        border-right: 2px solid $color-gray;
        margin-right: 0.5rem;
        padding-right: 1rem;
      }
    }
  }

  &__labeled-tools {
    display: flex;
    cursor: pointer;
    font-size: 1.2rem;
    font-weight: 500;
    color: $color-gray-dark;
    gap: 1.5rem;
  }
}

.version-dropdown-link {
  color: $font-color-dark-light;
  font-size: 1.3rem;
  cursor: pointer;

  margin-right: 1.5rem;
}
</style>
