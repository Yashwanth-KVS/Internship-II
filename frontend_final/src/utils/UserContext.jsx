import React, { createContext, useState } from 'react';
import { parseUser, removeUser } from './user';

export const UserContext = createContext();

export const UserProvider = ({ children }) => {
  const [user, setUser] = useState(parseUser()); // Store user state

  const login = (userData) => {
    setUser(userData);
  };

  const logout = () => {
    setUser(null); // Reset user state on logout
    removeUser();
  };

  return (
    <UserContext.Provider value={{ user, login, logout }}>
      {children}
    </UserContext.Provider>
  );
};
