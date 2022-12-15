<template>
  <div
    class="timetable"
    v-for="timetable in timetables"
    :key="timetable.id"
    @click="
      dialog.visible = true;
      dialog.selectedTimetable = timetable.id;
      dialog.selectedTimetableImg = timetable.image;
    "
  >
    <img :src="timetable.image" alt="timetable" class="timetable__img" />
    <div class="timetable__about">
      <img :src="timetable.logo" alt="logo" class="timetable__logo" />
      <p class="timetable__title">{{ timetable.title }}</p>
      <ul class="timetable__status">
        <li v-for="status in timetable.status" :key="status">
          {{ status }}
        </li>
      </ul>
    </div>
  </div>

  <el-dialog title="Wybrana tabliczka" v-model="dialog.visible" width="30%">
    <img :src="dialog.selectedTimetableImg" alt="timetable" class="timetable__img" />
    <template #footer>
      <span class="dialog-footer">
        <button @click="onTemplateSelected" class="btn">Wybierz ten szablon</button>
        <button class="btn">Poproś o poprawki</button>
      </span>
    </template>
  </el-dialog>
</template>

<script>
import { ElDialog } from 'element-plus';

export default {
  data() {
    return {
      dialog: {
        selectedTimetable: null,
        selectedTimetableImg: null,
        visible: false,
      },
    };
  },
  props: {
    timetables: {
      type: Object,
      required: true,
    },
  },
  components: {
    ElDialog,
  },
  methods: {
    onTemplateSelected() {
      this.dialog.visible = false;
      // this.dialog.selectedTimetable - id wybranej tabliczki
    },
  },
};
</script>

<style lang="scss" scoped>
@import '../../assets/styles/variables.scss';

.timetable {
  border: 1px solid $color-gray;
  border-radius: 0.5rem;
  width: 20rem;
  padding: 1rem;
  box-shadow: rgba(149, 157, 165, 0.2) 0 3px 0.5rem;
  cursor: pointer;
  transition: all 0.3s;

  &:hover {
    transform: scale(1.05);
  }

  &__img {
    width: 100%;
  }

  &__logo {
    width: 2rem;
    height: 2rem;
    grid-row: 1/3;
    align-self: center;
  }

  &__title {
    font-size: 1.2rem;
    font-weight: 500;
  }

  &__about {
    border-top: 1px solid $color-gray;
    margin-top: 0.5rem;
    padding-top: 0.5rem;
    display: grid;
    grid-column-gap: 0.75rem;
    grid-template-columns: max-content 1fr;
    grid-template-rows: repeat(2, max-content);

    ul {
      list-style: none;
      display: flex;
      gap: 0.5rem;
    }
  }

  &__status {
    color: $color-success;
    font-weight: 700;
  }
}

.dialog-footer {
  display: flex;
  gap: 1rem;
}

.btn {
  padding: 1rem 1.5rem;
  font-weight: bold;
  font-size: 1.2rem;
  cursor: pointer;
  border: none;
  border-radius: 5px;
  color: $color-white;
  background-color: $color-primary;
  transition: all 0.3s;

  &:hover {
    background: darken($color-primary, 5%);
  }
}
</style>
