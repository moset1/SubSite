import React, { useState } from 'react';
import { addSubscription } from '../services/subscriptionService';

const SubscriptionForm = ({ categories, onSubscriptionAdded }) => {
  const [categoryId, setCategoryId] = useState('');
  const [url, setUrl] = useState('');
  const [keywords, setKeywords] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!categoryId || !url || !keywords) {
      alert("카테고리, URL, 키워드를 모두 입력해주세요.");
      return;
    }
    try {
      const keywordsArray = keywords.split(',').map(k => k.trim()).filter(Boolean);
      await addSubscription({ categoryId, url, keywords: keywordsArray });
      setCategoryId('');
      setUrl('');
      setKeywords('');
      onSubscriptionAdded(); // Notify parent to refresh list
    } catch (error) {
      console.error("Failed to add subscription:", error);
      alert("구독 추가에 실패했습니다.");
    }
  };

  return (
    <section id="add-subscription-section">
      <div className="bg-white p-6 rounded-xl shadow-lg">
        <h2 className="text-2xl font-bold mb-4">새 구독 추가</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="subscription-category" className="block text-sm font-medium text-gray-700">카테고리 선택</label>
            <select
              id="subscription-category"
              value={categoryId}
              onChange={(e) => setCategoryId(e.target.value)}
              required
              className="mt-1 block w-full px-3 py-2 bg-white border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500"
            >
              <option value="" disabled>카테고리를 선택하세요</option>
              {categories.map(cat => (
                <option key={cat.id} value={cat.id}>{cat.name}</option>
              ))}
            </select>
          </div>
          <div>
            <label htmlFor="site-url" className="block text-sm font-medium text-gray-700">블로그 RSS 피드 URL</label>
            <input type="url" id="site-url" value={url} onChange={(e) => setUrl(e.target.value)} placeholder="https://d2.naver.com/d2.atom" required className="mt-1 block w-full px-3 py-2 bg-white border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500" />
          </div>
          <div>
            <label htmlFor="keywords" className="block text-sm font-medium text-gray-700">키워드 (쉼표로 구분)</label>
            <input type="text" id="keywords" value={keywords} onChange={(e) => setKeywords(e.target.value)} placeholder="AI, MSA" required className="block w-full px-3 py-2 bg-white border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-blue-500 focus:border-blue-500" />
          </div>
          <button type="submit" className="btn w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
            구독 추가
          </button>
        </form>
      </div>
    </section>
  );
};

export default SubscriptionForm;