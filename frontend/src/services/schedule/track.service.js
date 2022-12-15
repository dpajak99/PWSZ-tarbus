import http from '../../http-common';

class TrackDataService {
  // eslint-disable-next-line class-methods-use-this
  update(data) {
    return http.post('/api/schedule/tracks', data);
  }

  // eslint-disable-next-line class-methods-use-this
  remove(data) {
    return http.post('/api/schedule/tracks/delete', data);
  }
}

export default new TrackDataService();
