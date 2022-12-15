export default function authHeader() {
  const user = JSON.parse(localStorage.getItem('user'));

  if (user && user.token) {
    const tokenModel = JSON.parse(user.token);
    return { Authorization: `Bearer ${tokenModel.type}` };
  }
  return {};
}
