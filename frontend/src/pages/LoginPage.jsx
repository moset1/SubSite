import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { signupUser } from '../services/authService';

const LoginPage = () => {
  const [isLogin, setIsLogin] = useState(true);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="max-w-md w-full bg-white p-8 rounded-xl shadow-lg">
        {isLogin ? <LoginForm /> : <SignupForm />}
        <p className="text-center text-sm mt-4">
          {isLogin ? "계정이 없으신가요? " : "이미 계정이 있으신가요? "}
          <button onClick={() => setIsLogin(!isLogin)} className="font-medium text-blue-600 hover:text-blue-500">
            {isLogin ? "회원가입" : "로그인"}
          </button>
        </p>
      </div>
    </div>
  );
};

const LoginForm = () => {
  const { login } = useAuth();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await login(username, password);
    } catch (err) {
      setError('로그인에 실패했습니다. 사용자 이름 또는 비밀번호를 확인해주세요.');
      console.error(err);
    }
  };

  return (
    <div>
      <h2 className="text-2xl font-bold text-center mb-6">로그인</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        {error && <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">{error}</div>}
        <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} placeholder="사용자 이름" required className="block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500" />
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="비밀번호" required className="block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500" />
        <button type="submit" className="btn w-full py-2 px-4 text-white bg-blue-600 hover:bg-blue-700 rounded-md">로그인</button>
      </form>
    </div>
  );
};

const SignupForm = () => {
  const { login } = useAuth();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await signupUser({ username, password });
      // After successful signup, log the user in
      await login(username, password);
    } catch (err) {
      setError('회원가입에 실패했습니다. 다른 사용자 이름을 시도해주세요.');
      console.error(err);
    }
  };

  return (
    <div>
      <h2 className="text-2xl font-bold text-center mb-6">회원가입</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        {error && <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">{error}</div>}
        <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} placeholder="사용자 이름" required className="block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500" />
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="비밀번호" required className="block w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500" />
        <button type="submit" className="btn w-full py-2 px-4 text-white bg-blue-600 hover:bg-blue-700 rounded-md">회원가입</button>
      </form>
    </div>
  );
};

export default LoginPage;