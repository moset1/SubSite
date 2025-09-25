import apiClient from './api';

// --- Category APIs ---
export const getCategories = () => apiClient.get('/subscription-service/api/categories');
export const addCategory = (name) => apiClient.post('/subscription-service/api/categories', { name });
export const deleteCategory = (id) => apiClient.delete(`/subscription-service/api/categories/${id}`);

// --- Subscription APIs ---
export const getSubscriptions = () => apiClient.get('/subscription-service/api/subscriptions');
export const addSubscription = (data) => apiClient.post('/subscription-service/api/subscriptions', data);
export const updateSubscription = (id, data) => apiClient.put(`/subscription-service/api/subscriptions/${id}`, data);
export const deleteSubscription = (id) => apiClient.delete(`/subscription-service/api/subscriptions/${id}`);
export const checkUpdates = () => apiClient.post('/subscription-service/api/subscriptions/check-updates');


// --- Notification APIs ---
export const getNotifications = (categoryId) => {
  const params = categoryId && categoryId !== 'all' ? { categoryId } : {};
  return apiClient.get('/subscription-service/api/notifications', { params });
};