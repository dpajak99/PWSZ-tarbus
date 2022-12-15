import AuthService from '../services/auth/auth.service';

const user = JSON.parse(localStorage.getItem('user'));
const initialState = user
  ? { status: { loggedIn: true }, user }
  : { status: { loggedIn: false }, user: null };

// eslint-disable-next-line import/prefer-default-export
export const auth = {
  namespaced: true,
  state: initialState,
  actions: {
    login({ commit }, _user) {
      return AuthService.login(_user).then(
        (__user) => {
          commit('loginSuccess', __user);
          return Promise.resolve(__user);
        },
        (error) => {
          commit('loginFailure');
          return Promise.reject(error);
        },
      );
    },
    logout({ commit }) {
      AuthService.logout();
      commit('logout');
    },
    register({ commit }, _user) {
      return AuthService.register(_user).then(
        (response) => {
          commit('registerSuccess');
          return Promise.resolve(response.data);
        },
        (error) => {
          commit('registerFailure');
          return Promise.reject(error);
        },
      );
    },
  },
  mutations: {
    loginSuccess(state, _user) {
      state.status.loggedIn = true;
      state.user = _user;
    },
    loginFailure(state) {
      state.status.loggedIn = false;
      state.user = null;
    },
    logout(state) {
      state.status.loggedIn = false;
      state.user = null;
    },
    registerSuccess(state) {
      state.status.loggedIn = false;
    },
    registerFailure(state) {
      state.status.loggedIn = false;
    },
  },
};
