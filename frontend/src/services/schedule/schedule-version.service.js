import http from '../../http-common';

class ScheduleVersionService {
  // eslint-disable-next-line class-methods-use-this
  getAllUserVersions() {
    return http.get('/api/schedule/versions');
  }

  // eslint-disable-next-line class-methods-use-this
  create(data) {
    return http.post('/api/schedule/versions/createNew', data);
  }

  // eslint-disable-next-line class-methods-use-this
  update(data) {
    return http.post('/api/schedule/versions/', data);
  }

  // eslint-disable-next-line class-methods-use-this
  delete(id) {
    return http.delete(`/api/schedule/versions/${id}`);
  }
}

export default new ScheduleVersionService();
