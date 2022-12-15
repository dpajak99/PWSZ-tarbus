import http from '../../http-common';

class UserService {
  // eslint-disable-next-line class-methods-use-this
  getUsers() {
    return http.get('/api/admin/users?page=0');
  }

  // eslint-disable-next-line class-methods-use-this
  update(data) {
    return http.post('/api/user', data);
  }
}

export default new UserService();
