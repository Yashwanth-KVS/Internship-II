import React, { useContext } from "react";
import { Link, useNavigate } from "react-router-dom"; // Import useNavigate
import { UserContext } from "../utils/UserContext"; // Import UserContext

function Navbar({ title = "RP" }) {
  const { user, logout } = useContext(UserContext); // Get user and logout function
  const navigate = useNavigate(); // Initialize useNavigate

  const handleLogout = () => {
    logout(); // Call logout function
    navigate("/login"); // Redirect to login page after logout
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light p-2">
      <div className="container-fluid">
        <Link className="navbar-brand d-flex align-items-center" to="/home">
          {title}
          <i className="fa-solid fa-feather ms-2"></i> {/* Feather icon */}
        </Link>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav ms-auto">
            {user ? (
              user.userType === "Admin" ? (
                <>
                  <li className="nav-item mx-4">
                    <Link className="nav-link" to="/admin-dashboard">
                      Admin Dashboard
                    </Link>
                  </li>
                  <li className="nav-item mx-2">
                    <button
                      className="nav-link btn btn-link"
                      onClick={handleLogout}
                    >
                      Logout
                    </button>
                  </li>
                </>
              ) : // Display links based on user type
              user.userType === "Candidate" ? (
                <>
                  <li className="nav-item mx-4">
                    <Link className="nav-link" to="/candidate-dashboard">
                      Dashboard
                    </Link>
                  </li>
                  <li className="nav-item mx-4">
                    <Link className="nav-link" to="/candidate-applications">
                      Applications
                    </Link>
                  </li>
                  <li className="nav-item mx-4">
                    <Link className="nav-link" to="/candidate-profile">
                      Profile
                    </Link>
                  </li>
                  <li className="nav-item mx-2">
                    <button
                      className="nav-link btn btn-link"
                      onClick={handleLogout}
                    >
                      Logout
                    </button>
                  </li>
                </>
              ) : user.userType === "Manager" ? (
                <>
                  <li className="nav-item mx-4">
                    <Link className="nav-link" to="/manager-dashboard">
                      Dashboard
                    </Link>
                  </li>
                  <li className="nav-item mx-4">
                    <Link className="nav-link" to="/manager-profile">
                      Profile
                    </Link>
                  </li>
                  <li className="nav-item mx-2">
                    <button
                      className="nav-link btn btn-link"
                      onClick={handleLogout}
                    >
                      Logout
                    </button>
                  </li>
                </>
              ) : null
            ) : (
              <>
                <li className="nav-item">
                  <Link className="nav-link mx-3" to="/Dashboard">
                    Dashboard
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link mx-4" to="/about">
                    About
                  </Link>
                </li>
                
                <li className="nav-item mx-2">
                  <Link className="nav-link" to="/login">
                    Login
                  </Link>
                </li>
              </>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
