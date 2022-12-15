import http from '../../http-common';

class BusLinesDataService {
  // eslint-disable-next-line class-methods-use-this
  update(data) {
    return http.post('/api/schedule/lines', data);
  }

  // eslint-disable-next-line class-methods-use-this
  getByVersion(versionId) {
    return http.get(`/api/schedule/lines?versionId=${versionId}`);
  }

  // eslint-disable-next-line class-methods-use-this
  delete(busLineId) {
    return http.delete(`/api/schedule/lines/delete/${busLineId}`);
  }

  // eslint-disable-next-line class-methods-use-this
  deleteAll(busLines) {
    return http.post('/api/schedule/lines/delete', busLines);
  }
}

export default new BusLinesDataService();
