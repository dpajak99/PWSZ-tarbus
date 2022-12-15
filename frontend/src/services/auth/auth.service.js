import http from '../../http-common';

class AuthService {
  static login(user) {
    console.log(user);
    return http
      .post('api/auth/signin', {
        email: user.email,
        password: user.password,
      })
      .then((response) => {
        console.log(response);
        if (response.data.token) {
          const userModel = response.data;
          userModel.token = JSON.stringify(userModel.token);
          localStorage.setItem('user', JSON.stringify(userModel));
        }

        return response.data;
      });
  }

  static logout() {
    localStorage.removeItem('user');
  }

  static register(user) {
    return http.post('api/auth/signup', {
      username: user.username,
      email: user.email,
      password: user.password,
    });
  }
}

export default AuthService;
