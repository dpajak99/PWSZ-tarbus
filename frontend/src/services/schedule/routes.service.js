import http from '../../http-common';

class RoutesDataService {
  // eslint-disable-next-line class-methods-use-this
  getRouteDetails(routeId) {
    return http.get(`api/schedule/routes/${routeId}`);
  }

  // eslint-disable-next-line class-methods-use-this
  getByVersion(versionId) {
    return http.get(`api/schedule/routes?versionId=${versionId}`);
  }
}

export default new RoutesDataService();
