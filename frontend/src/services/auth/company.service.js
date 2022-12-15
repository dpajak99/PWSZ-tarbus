import http from '../../http-common';

class CompanyService {
  // eslint-disable-next-line class-methods-use-this
  getUserCompanies() {
    return http.get('/api/auth/company/user');
  }
}

export default new CompanyService();
