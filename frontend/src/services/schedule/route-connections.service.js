import http from '../../http-common';

class RouteConnectionDataService {
  // eslint-disable-next-line class-methods-use-this
  getRouteConnection(routeId) {
    return http.get(`/api/schedule/routeConnections?routeId=${routeId}`);
  }

  // eslint-disable-next-line class-methods-use-this
  update(data) {
    return http.post('/api/schedule/routeConnections', data);
  }

  // eslint-disable-next-line class-methods-use-this
  delete(data) {
    return http.post('/api/schedule/routeConnections/delete', data);
  }
}

export default new RouteConnectionDataService();
