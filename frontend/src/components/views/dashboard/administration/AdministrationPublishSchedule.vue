<template>
  <div style="width: 400px">
    <v-button
      @click="onGenerateDatabaseInfoButtonClicked"
    >
      <template #label>
        Generuj database-info.json...
      </template>
    </v-button>
  </div>
</template>
<script>
import VButton from '../../../UI/VButton.vue';
import ScheduleService from '../../../../services/schedule/schedule.service';

export default {
  components: { VButton },
  methods: {
    onGenerateDatabaseInfoButtonClicked() {
      ScheduleService.generateDatabaseInfo().then(
        (response) => {
          console.log(response);
          this.$notify({
            message: 'Pomyślnie stworzono plik',
            type: 'success',
            position: 'bottom-right',
          });
        },
        (error) => {
          console.log(error);
          this.$notify({
            message: 'Błąd',
            type: 'error',
            position: 'bottom-right',
          });
        },
      );
    },
  },
};
</script>
