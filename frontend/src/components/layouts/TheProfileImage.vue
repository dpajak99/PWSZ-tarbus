<template>
  <img
    :src="`${storageUrl}static/user/${userId}/profile.png`"
    @error="onImageLoadFailure($event)"
    alt="user_logo"
  />
</template>

<script>
import EmptyProfileImage from '../../assets/images/profile.png';

export default {
  name: 'TheProfileImage',
  props: {
    userId: Number,
  },
  data() {
    return {
      storageUrl: process.env.VUE_APP_STORAGE_URL,
    };
  },
  methods: {
    onImageLoadFailure(event) {
      // eslint-disable-next-line no-param-reassign
      event.target.src = EmptyProfileImage;
    },
  },
};
</script>

<style scoped>

</style>
