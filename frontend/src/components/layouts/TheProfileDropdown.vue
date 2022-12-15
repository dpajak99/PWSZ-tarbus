<template>
  <el-dropdown trigger="click" class="profile-dropdown">
    <span>
      <the-profile-image
        :userId="currentUser.id"
        class="profile-image-small"
      />
    </span>
    <template #dropdown>
      <div class="profile-dropdown__content">
        <div>
          <the-profile-image
            :userId="currentUser.id"
            class="profile-dropdown__content__profile-image-big"
          />
        </div>
        <h4>
          {{currentUser.firstName}}
          {{currentUser.lastName}}
        </h4>
        <p>{{currentUser.email}}</p>

        <button
          class="profile-button"
          @click="logout"
        >
          Zarządzaj kontem
        </button>
        <br /><br />
        <h6>Twoje firmy</h6>
        <div>
          <span
            v-for="company in companiesList"
            :key="company.id"
          >
            <img
              :src="`${storageUrl}static/company/${company.id}/logo.png`"
              class="company-logo-xs"
              alt="company_logo"
            />
          </span>
        </div>
        <button
          class="profile-button"
          @click="logout"
        >
          Wyloguj
        </button>
        <hr />
        <div class="privacy-policy-box">
          <a><p>Polityka prywatności</p></a>
          <a><p>Warunki korzystania z usługi</p></a>
        </div>
      </div>
    </template>
  </el-dropdown>
</template>

<script>
import CompanyService from '../../services/auth/company.service';
import TheProfileImage from './TheProfileImage.vue';

export default {
  name: 'TheProfileDropdown',
  components: { TheProfileImage },
  data() {
    return {
      storageUrl: process.env.VUE_APP_STORAGE_URL,
      companiesList: [],
      currentUser: JSON.parse(localStorage.getItem('user')),
    };
  },
  methods: {
    logout() {
      this.$store.dispatch('auth/logout');
      this.$router.push('/auth/login');
    },
    async fetchUserCompanies() {
      await CompanyService.getUserCompanies().then((response) => {
        console.log(response.data);
        this.companiesList = response.data;
        // eslint-disable-next-line prefer-destructuring
        this.createScheduleSelectedCompanyParent = response.data[0];
      });
    },
  },
  mounted() {
    this.fetchUserCompanies();
  },
};
</script>

<style lang="scss">
@import '../../assets/styles/variables.scss';

.company-logo-xs {
  height: 25px;
  margin: 10px 3px;
  filter: grayscale(100%);
  cursor: pointer;
  &:hover {
    filter: grayscale(0%);
  }
}

h4 {
  font-size: 16px;
  font-weight: 400;
  padding: 8px 0 0 0;
}

.privacy-policy-box {
  font-size: 12px;
  color: rgb(95, 99, 104);
  p {
    margin: 8px 0;
  }
}

hr {
  color: #cdcdcd;
  margin: 15px 0;
}

.profile-button {
  background-color: rgb(255, 255, 255);
  border: solid 1px rgb(218, 220, 224);
  border-radius: 100px;
  color: rgb(60, 64, 67);
  cursor: pointer;
  display: inline-block;
  letter-spacing: 0.25px;
  line-height: 16px;
  margin-top: 16px;
  max-width: 254px;
  padding: 8px 16px;
  text-align: center;
  text-decoration-color: rgb(60, 64, 67);
  text-decoration-line: none;
  text-decoration-style: solid;
  text-overflow: ellipsis;
  text-shadow: rgba(0, 0, 0, 0) 0 0 0, rgba(0, 0, 0, 0.68) 0 0 0;
  user-select: text;
  white-space: nowrap;
}

.profile-image-small {
  height: 3em;
  border-radius: 50%;
  &:hover {
    border: solid 1px #aaaaaa;
  }
  &:active {
    border: solid 3px #aaaaaa;
  }
}
.profile-dropdown {
  cursor: pointer;
  &__content {
    width: 250px;
    padding: 20px;
    text-align: center;
    min-height: 300px;
    background-color: $color-white;

    &__profile-image-big {
      border-radius: 50%;
      height: 7em;
    }
  }
}
</style>
