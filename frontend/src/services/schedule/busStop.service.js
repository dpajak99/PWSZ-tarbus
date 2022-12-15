import http from '../../http-common';

class BusStopDataService {
  // eslint-disable-next-line class-methods-use-this
  getAll(name) {
    if (name != null) {
      return http.get(`/api/schedule/stops?name=${name}`);
    }
    return http.get('/api/schedule/stops/');
  }

  // eslint-disable-next-line class-methods-use-this
  update(data) {
    return http.post('/api/schedule/stops/', data);
  }
}

export default new BusStopDataService();
