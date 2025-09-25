import axios from 'axios';

const apiClient = axios.create({
  // The base URL will be proxied by Vite's dev server to the backend.
  // In production, this would be the actual API Gateway URL.
  baseURL: '/api',
});

// Interceptor to add the auth token to every request
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default apiClient;