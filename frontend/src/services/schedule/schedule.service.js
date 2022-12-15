import http from '../../http-common';

class GeneratorService {
  // eslint-disable-next-line class-methods-use-this
  generateTimetables(data) {
    console.log(data);
    return http.post('/api/generator/generateSchedule', data);
  }

  // eslint-disable-next-line class-methods-use-this
  generateDatabaseInfo() {
    return http.post('/api/schedule-config/generateDatabaseInfo');
  }
}

export default new GeneratorService();
