import React, { useState } from 'react';
import { checkUpdates } from '../services/subscriptionService';

const SubscriptionList = ({ subscriptions, onRefresh }) => {
  const [loading, setLoading] = useState(false);

  const handleCheckUpdates = async () => {
    setLoading(true);
    try {
      await checkUpdates();
      // Give some time for backend to process, then refresh notifications
      setTimeout(() => {
        onRefresh();
      }, 3000); // 3 seconds delay
    } catch (error) {
      console.error("Failed to check updates:", error);
      alert("업데이트 확인에 실패했습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <section>
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-2xl font-bold">내 구독 목록</h2>
        <button onClick={handleCheckUpdates} disabled={loading} className="btn py-2 px-4 rounded-md shadow-sm text-sm font-medium text-white bg-green-600 hover:bg-green-700 flex items-center">
          {loading && <div className="spinner"></div>}
          <span>업데이트 확인</span>
        </button>
      </div>
      <div className="bg-white p-4 rounded-xl shadow-lg space-y-3 min-h-[100px]">
        {subscriptions.length === 0 ? (
          <p className="text-center text-gray-500 py-4">표시할 구독이 없습니다.</p>
        ) : (
          subscriptions.map(sub => (
            <div key={sub.id} className="p-3 border rounded-lg flex justify-between items-center bg-gray-50">
              <div className="flex-grow overflow-hidden mr-2">
                <p className="font-semibold truncate">{sub.url}</p>
                <p className="text-sm text-gray-600 truncate">키워드: {sub.keywords.join(', ')}</p>
              </div>
              <div className="flex-shrink-0 space-x-2">
                <button data-id={sub.id} className="edit-btn text-blue-500 hover:text-blue-700 font-semibold text-sm">수정</button>
                <button data-id={sub.id} className="delete-btn text-red-500 hover:text-red-700 font-semibold text-sm">삭제</button>
              </div>
            </div>
          ))
        )}
      </div>
    </section>
  );
};

export default SubscriptionList;