import React, { createContext, useState, useContext, useEffect } from 'react';
import { loginUser } from '../services/authService';
import { jwtDecode } from 'jwt-decode';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    if (token) {
      try {
        const decoded = jwtDecode(token);
        // In a real app, you should also verify the token expiration
        if (decoded.exp * 1000 > Date.now()) {
          setUser({ username: decoded.sub });
        } else {
          localStorage.removeItem('authToken');
        }
      } catch (error) {
        console.error("Invalid token:", error);
        localStorage.removeItem('authToken');
      }
    }
    setLoading(false);
  }, []);

  const login = async (username, password) => {
    const response = await loginUser({ username, password });
    const token = response.token; // Assuming the token is in response.token
    localStorage.setItem('authToken', token);
    const decoded = jwtDecode(token);
    setUser({ username: decoded.sub });
  };

  const logout = () => {
    localStorage.removeItem('authToken');
    setUser(null);
    // It's good practice to redirect to login page after logout,
    // which is handled by the PrivateRoute.
  };

  const value = { user, login, logout, isAuthenticated: !!user };

  return (
    <AuthContext.Provider value={value}>
      {!loading && children}
    </Auth.Provider>
  );
};

export const useAuth = () => {
  return useContext(AuthContext);
};