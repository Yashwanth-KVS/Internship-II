import React from "react";
import Navbar from "./Navbar"; // Assuming Navbar is in the same folder

function Layout({ children }) {
  return (
    <div className="layout-container">
      <Navbar />
      <main className="main-content">
        {children}
      </main>
    </div>
  );
}

export default Layout;
