import http from '../../http-common';

class BusStopConnectionsDataService {
  // eslint-disable-next-line class-methods-use-this
  getConnection(from, to) {
    return http.get(`/api/schedule/stopConnections?from=${from}&to=${to}`);
  }

  // eslint-disable-next-line class-methods-use-this
  getAllConnections() {
    return http.get('/api/schedule/stopConnections');
  }

  // eslint-disable-next-line class-methods-use-this
  findEmptyConnections() {
    return http.get('/api/services/buildEmptyConnections');
  }

  // eslint-disable-next-line class-methods-use-this
  getEmptyConnections() {
    return http.get('/api/schedule/stopConnections/empty');
  }

  // eslint-disable-next-line class-methods-use-this
  update(data) {
    return http.put('/api/schedule/stopConnections', data);
  }

  // eslint-disable-next-line class-methods-use-this
  delete(data) {
    return http.post('/api/schedule/stopConnections/delete', data);
  }
}

export default new BusStopConnectionsDataService();
