<template>
  <div class="wrapper">
    <div class="box">
      <p class="box__title">twoje szablony</p>
      <div class="box__content">
        <v-timetable-card :timetables="timetablesData.templatesOwned" />
      </div>
    </div>
    <div class="box">
      <p class="box__title">darmowe szablony</p>
      <div class="box__content">
        <v-timetable-card :timetables="timetablesData.templatesFree" />
      </div>
    </div>
    <div class="box">
      <p class="box__title">dostępne szablony</p>
      <div class="box__content">
        <v-timetable-card :timetables="timetablesData.templatesAvaliable" />
      </div>
    </div>
  </div>
</template>

<script>
import VTimetableCard from '../../../UI/VTimetableCard.vue';

// Testing data
const timetablesData = {
  templatesOwned: [
    {
      id: 1,
      title: 'timetable 1',
      image: 'https://idoc.pub/img/crop/300x300/34wmw05v6zl7.jpg',
      logo:
        'data:image/png;base64,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',
      status: ['OWNED', 'ACTIVE'],
    },
    {
      id: 2,
      title: 'timetable 2',
      image: 'https://idoc.pub/img/crop/300x300/34wmw05v6zl7.jpg',
      logo:
        'data:image/png;base64,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',
      status: ['OWNED', 'ACTIVE'],
    },
  ],
  templatesFree: [
    {
      id: 1,
      title: 'timetable free 1',
      image:
        'https://martinsalbury.com.au/wp-content/uploads/2020/11/20548-Martins-Printed-timetable-Nov20-FINAL-01.jpg',
      logo: 'https://icon-library.com/images/netflix-logo-icon/netflix-logo-icon-3.jpg',
      status: ['OWNED', 'ACTIVE'],
    },
  ],
  templatesAvaliable: [
    {
      id: 1,
      title: 'timetable 2',
      image: 'https://idoc.pub/img/crop/300x300/34wmw05v6zl7.jpg',
      logo: 'https://www.creativefreedom.co.uk/wp-content/uploads/2017/06/Twitter-featured.png',
      status: ['OWNED', 'ACTIVE'],
    },
  ],
};

export default {
  data() {
    return {
      timetablesData,
    };
  },
  components: {
    VTimetableCard,
  },
};
</script>

<style lang="scss" scoped>
@import '../../../../assets/styles/variables.scss';

.wrapper {
  padding: 2rem;

  display: flex;
  flex-direction: column;
  gap: 4rem;
}

.box {
  &__title {
    text-transform: uppercase;
    font-weight: 500;
    color: $color-gray-medium;
    grid-row: 1/2;
    margin-bottom: 1rem;
  }

  &__content {
    display: flex;
    gap: 2rem;
    align-items: flex-start;
    flex-wrap: wrap;
  }
}
</style>
