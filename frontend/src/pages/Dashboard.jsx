import React, { useState, useEffect, useCallback } from 'react';
import { useAuth } from '../contexts/AuthContext';
import * as subService from '../services/subscriptionService';
import CategorySection from '../components/CategorySection';
import SubscriptionForm from '../components/SubscriptionForm';
import SubscriptionList from '../components/SubscriptionList';
import NotificationList from '../components/NotificationList';

const Dashboard = () => {
  const { user, logout } = useAuth();
  const [categories, setCategories] = useState([]);
  const [subscriptions, setSubscriptions] = useState([]);
  const [notifications, setNotifications] = useState([]);
  const [activeCategoryId, setActiveCategoryId] = useState('all');

  const fetchAllData = useCallback(async () => {
    try {
      const [catRes, subRes, notifRes] = await Promise.all([
        subService.getCategories(),
        subService.getSubscriptions(),
        subService.getNotifications(activeCategoryId),
      ]);
      setCategories(catRes.data);
      setSubscriptions(subRes.data);
      setNotifications(notifRes.data);
    } catch (error) {
      console.error("Failed to fetch dashboard data:", error);
    }
  }, [activeCategoryId]);

  useEffect(() => {
    fetchAllData();
  }, [fetchAllData]);

  const handleRefresh = () => {
    // Refetch notifications and subscriptions after an update
    subService.getNotifications(activeCategoryId).then(res => setNotifications(res.data));
    subService.getSubscriptions().then(res => setSubscriptions(res.data));
  };

  const filteredSubscriptions = activeCategoryId === 'all'
    ? subscriptions
    : subscriptions.filter(s => s.categoryId === activeCategoryId);

  return (
    <div className="container mx-auto p-4 md:p-8 max-w-7xl">
      <header className="flex justify-between items-center mb-10">
        <div>
          <h1 className="text-3xl md:text-4xl font-bold text-gray-900">✨ 키워드 알림 AI 서비스</h1>
          <p className="mt-1 text-lg text-gray-600">
            <span className="font-bold">{user?.username}</span>님, 환영합니다.
          </p>
        </div>
        <button onClick={logout} className="btn bg-gray-200 hover:bg-gray-300 text-gray-800 font-bold py-2 px-4 rounded">
          로그아웃
        </button>
      </header>

      <div className="md:grid md:grid-cols-4 md:gap-8">
        <aside className="md:col-span-1 mb-8 md:mb-0">
          <CategorySection
            activeCategoryId={activeCategoryId}
            setActiveCategoryId={setActiveCategoryId}
          />
        </aside>

        <main className="md:col-span-3 space-y-12">
          <SubscriptionForm categories={categories} onSubscriptionAdded={handleRefresh} />
          <SubscriptionList subscriptions={filteredSubscriptions} onRefresh={handleRefresh} />
          <NotificationList notifications={notifications} />
        </main>
      </div>
    </div>
  );
};

export default Dashboard;