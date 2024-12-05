import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';


function Home() {

  useEffect(() => {
    // Fade-in effect when the component mounts
    const elements = document.querySelectorAll('.fade-in');
    elements.forEach((el, idx) => {
      setTimeout(() => {
        el.classList.add('visible');
      }, idx * 200); // Delay the fade-in for each element
    });
  }, []);

  return (
    <div className="page-background">
      {/* Hero Section */}
      <div className="container-fluid hero-section d-flex align-items-center justify-content-center text-center">
        <div className="hero-content fade-in">
          <h1 className="display-4 fw-bold text-white">Recruitment Portal</h1>
          <p className="lead text-white-50">
            Simplify your recruitment process with job postings, candidate applications, and seamless hiring.
          </p>
          <div className="mt-4">
            <Link to="/login" className="btn btn-outline-light btn-lg hover-scale me-3">Login</Link>
            <Link to="/register" className="btn btn-outline-light btn-lg hover-scale">Register</Link>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Home;
