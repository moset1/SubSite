import apiClient from './api';

export const loginUser = async (credentials) => {
  // Assuming the UserService login endpoint is at /user-service/login
  const response = await apiClient.post('/user-service/login', credentials);
  return response.data;
};

export const signupUser = async (userDetails) => {
  // Assuming the UserService signup endpoint is at /user-service/signup
  const response = await apiClient.post('/user-service/signup', userDetails);
  return response.data;
};