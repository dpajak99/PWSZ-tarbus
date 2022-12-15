import http from '../../http-common';

class DestinationsService {
  // eslint-disable-next-line class-methods-use-this
  add(data) {
    return http.post('/api/schedule/destinations', data);
  }

  // eslint-disable-next-line class-methods-use-this
  getDescriptions(id) {
    return http.get(`/api/schedule/destinations?routeId=${id}`);
  }

  // eslint-disable-next-line class-methods-use-this
  getByVersion(id) {
    return http.get(`/api/schedule/destinations/byVersion?versionId=${id}`);
  }
}

export default new DestinationsService();
