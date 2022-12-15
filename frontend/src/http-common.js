import axios from 'axios';
import authHeader from './services/auth/auth.header';

export default axios.create({
  baseURL: process.env.VUE_APP_BACKEND_URL,
  headers: {
    'Content-type': 'application/json',
    ...authHeader(),
  },
});
