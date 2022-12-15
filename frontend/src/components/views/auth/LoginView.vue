<template>
  <div class="login-container">
    <div class="card">
      <img src="@/assets/images/logo.svg" alt="tarbus-logo" class="logo" />
      <Form @submit="handleLogin" :validation-schema="schema">
        <div class="form-group">
          <Field name="email" type="text" class="form-control" placeholder="Email" />
          <ErrorMessage name="email" class="error-feedback" />
        </div>
        <div class="form-group">
          <Field name="password" type="password" class="form-control" placeholder="Hasło" />
          <ErrorMessage name="password" class="error-feedback" />
        </div>

        <div class="form-group">
          <button class="btn btn-primary btn-block login-btn" :disabled="loading">
            <span v-show="loading" class="spinner-border spinner-border-sm" />
            <span>Zaloguj się</span>
          </button>
        </div>

        <div class="form-group">
          <div v-if="message" class="alert alert-danger" role="alert">
            {{ message }}
          </div>
        </div>
      </Form>
      <p class="terms">
        Rejestrując / Logując się wyrażasz zgodę na nasze
        <a>Warunki Korzystania</a> i
        <a>Politykę Prywatności</a>
      </p>
    </div>
  </div>
</template>

<script>
import { Form, Field, ErrorMessage } from 'vee-validate';
import * as yup from 'yup';

export default {
  name: 'LoginView',
  components: {
    Form,
    Field,
    ErrorMessage,
  },
  data() {
    const schema = yup.object().shape({
      email: yup.string().required('Username is required!'),
      password: yup.string().required('Password is required!'),
    });
    return {
      loading: false,
      message: '',
      schema,
    };
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    },
  },
  created() {
    if (this.loggedIn) {
      this.$router.push('/');
    }
  },
  methods: {
    handleLogin(user) {
      this.loading = true;
      this.$store.dispatch('auth/login', user).then(
        () => {
          this.$router.push('/');
        },
        (error) => {
          this.loading = false;
          // prettier-ignore
          this.message = (error.response && error.response.data && error.response.data.message)
          || error.message
          || error.toString();
        },
      );
    },
  },
};
</script>

<style lang="scss" scoped>
@import '../../../assets/styles/variables.scss';

.login-container {
  width: 100vw;
  height: 100vh;
  position: relative;

  display: flex;
  align-items: center;

  &::before {
    content: '';
    position: absolute;
    width: 100%;
    height: 100%;
    background: url('@/assets/images/login-bg.png') center/cover;
    filter: brightness(55%);
  }
}

.card {
  position: relative;
  background-color: $color-white;
  width: 45rem;
  height: 45rem;
  padding: 6.5rem 8.5rem;
  border-radius: 0.75rem;
  box-shadow: 0 2px 2px rgba(0, 0, 0, 0.3);
  margin-left: 15%;

  display: flex;
  flex-direction: column;
}

.logo {
  width: 20rem;
  margin-bottom: 0.5rem;
}

.form-control {
  width: 100%;
  border: none;
  border-bottom: 1px solid darken($color-gray, 10%);
  margin-top: 2.5rem;
  padding: 0.5rem 0.25rem;
  outline: none;

  &::placeholder {
    color: $color-gray-medium;
    font-weight: 300;
    font-size: 1.2rem;
  }
}

.login-btn {
  margin-top: 2.5rem;
  width: 100%;
  background-color: $color-primary;
  color: $color-white;
  text-transform: uppercase;
  border: none;
  border-radius: 0.3rem;
  padding: 0.75rem 3rem;
  font-size: 1.2rem;
  transition: all 0.3s;

  &:hover {
    cursor: pointer;
    background-color: darken($color-primary, 5%);
  }
}

.terms {
  margin-top: 2rem;
  font-size: 1rem;
  font-weight: 300;
  text-align: center;
  color: $color-gray-medium;

  a {
    text-decoration: underline;
    font-weight: normal;
    cursor: pointer;
  }
}

.error-feedback {
  color: red;
}
</style>
