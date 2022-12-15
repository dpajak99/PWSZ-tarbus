import http from '../../http-common';

class DeparturesService {
  // eslint-disable-next-line class-methods-use-this
  getRouteDepartures(routeId) {
    return http.get(`api/schedule/departures/route?routeId=${routeId}`);
  }

  // eslint-disable-next-line class-methods-use-this
  update(data) {
    return http.post('/api/schedule/departures', data);
  }

  // eslint-disable-next-line class-methods-use-this
  remove(data) {
    return http.post('/api/schedule/departures/delete', data);
  }
}

export default new DeparturesService();
