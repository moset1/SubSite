import React from 'react';

const NotificationList = ({ notifications }) => {
  return (
    <section>
      <h2 className="text-2xl font-bold mb-4">새 알림</h2>
      <div className="bg-white p-4 rounded-xl shadow-lg space-y-3 min-h-[150px]">
        {notifications.length === 0 ? (
          <p className="text-center text-gray-500 py-4">새 알림이 없습니다.</p>
        ) : (
          notifications.map(notif => (
            <div key={notif.id} className="p-3 border rounded-lg bg-white">
              <p className="text-sm text-gray-500">
                [<span className="font-medium text-green-700">{notif.keyword}</span>] 키워드 발견!
              </p>
              <a href={notif.link} target="_blank" rel="noopener noreferrer" className="font-semibold text-blue-600 hover:underline">
                {notif.title}
              </a>
              <p className="text-xs text-gray-400 mb-2">
                {new Date(notif.discoveredAt).toLocaleString()}
              </p>
              {/* AI Summary button can be added later */}
            </div>
          ))
        )}
      </div>
    </section>
  );
};

export default NotificationList;