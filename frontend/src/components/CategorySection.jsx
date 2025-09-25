import React, { useState, useEffect } from 'react';
import { getCategories, addCategory } from '../services/subscriptionService';

const CategorySection = ({ activeCategoryId, setActiveCategoryId }) => {
  const [categories, setCategories] = useState([]);
  const [newCategory, setNewCategory] = useState('');

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    try {
      const response = await getCategories();
      setCategories(response.data);
    } catch (error) {
      console.error("Failed to fetch categories:", error);
    }
  };

  const handleAddCategory = async () => {
    if (!newCategory.trim()) return;
    try {
      await addCategory(newCategory);
      setNewCategory('');
      fetchCategories(); // Refresh list
    } catch (error) {
      console.error("Failed to add category:", error);
    }
  };

  return (
    <div className="bg-white p-4 rounded-xl shadow-lg sticky top-8">
      <h2 className="text-xl font-bold mb-4">카테고리</h2>
      <div className="flex items-center space-x-2 mb-4">
        <input
          type="text"
          value={newCategory}
          onChange={(e) => setNewCategory(e.target.value)}
          placeholder="새 카테고리"
          className="block w-full px-3 py-2 bg-white border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 text-sm"
        />
        <button onClick={handleAddCategory} className="btn bg-blue-600 text-white px-3 py-2 rounded-md whitespace-nowrap text-sm">
          추가
        </button>
      </div>
      <nav className="flex flex-col space-y-1">
        <button
          onClick={() => setActiveCategoryId('all')}
          className={`category-btn btn w-full text-left p-2 rounded-md hover:bg-gray-100 font-semibold ${activeCategoryId === 'all' ? 'active' : ''}`}
        >
          🗂️ 전체
        </button>
        {categories.map((cat) => (
          <button
            key={cat.id}
            onClick={() => setActiveCategoryId(cat.id)}
            className={`category-btn btn w-full text-left p-2 rounded-md hover:bg-gray-100 ${activeCategoryId === cat.id ? 'active' : ''}`}
          >
            {cat.name}
          </button>
        ))}
      </nav>
    </div>
  );
};

export default CategorySection;