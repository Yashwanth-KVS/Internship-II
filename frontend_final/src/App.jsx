import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import "./App.css";
import Login from "./components/Login";
import Registration from "./components/Registration";
import Layout from "./layout/Layout";
import Home from "./pages/Home";
import ManagerDashboard from "./components/ManagerDashboard";
import About from "./pages/About";
import CandidateDashboard from "./components/CandidateDashboard";
import JobDetail from "./components/JobDetail";
import { UserProvider, UserContext } from "./utils/UserContext";
import PrivateRoute from "./utils/PrivateRoute";
import Profile from "./components/Profile";
import CandidateApplications from "./components/CandidateApplications";
import AdminDashboard from "./components/AdminDashboard";
import MainDashBoard from "./components/MainDashBoard";

function App() {
  return (
    <UserProvider>
      <Router>
        <Layout>
          <div className="App">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route 
                path="/home" 
                element={<UserContext.Consumer>
                  {({ user }) => 
                    user ? <Navigate to={user.userType === "Candidate" ? "/candidate-dashboard" : "/manager-dashboard"} /> : <Home />
                  }
                </UserContext.Consumer>} 
              />
              <Route path="/Dashboard" element={<MainDashBoard />} />
              <Route path="/about" element={<About />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Registration />} />
              <Route
                path="/manager-dashboard"
                element={
                  <PrivateRoute allowedUserType="Manager">
                    <ManagerDashboard />
                  </PrivateRoute>
                }
              />
              <Route
                path="/manager-profile"
                element={
                  <PrivateRoute allowedUserType="Manager">
                    <Profile />
                  </PrivateRoute>
                }
              />
              <Route
                path="/candidate-dashboard"
                element={
                  <PrivateRoute allowedUserType="Candidate">
                    <CandidateDashboard />
                  </PrivateRoute>
                }
              />
              <Route
                path="/candidate-applications"
                element={
                  <PrivateRoute allowedUserType="Candidate">
                    <CandidateApplications />
                  </PrivateRoute>
                }
              />
              <Route
                path="/candidate-profile"
                element={
                  <PrivateRoute allowedUserType="Candidate">
                    <Profile />
                  </PrivateRoute>
                }
              />
              <Route path="/manager-job/:jobId" element={<JobDetail />} />
              <Route
                path="/admin-dashboard"
                element={
                  <PrivateRoute allowedUserType="Admin">
                    <AdminDashboard />
                  </PrivateRoute>
                }
              />
              {/* Catch-all route */}
              <Route path="*" element={<UserContext.Consumer>
                {({ user }) => {
                  if (user) {
                    // User is logged in, stay on the same page or redirect
                    return <Navigate to={user.userType === "Candidate" ? "/candidate-dashboard" : "/manager-dashboard"} />;
                  }
                  // User is not logged in, redirect to home
                  return <Navigate to="/home" />;
                }}
              </UserContext.Consumer>} />
            </Routes>
          </div>
        </Layout>
      </Router>
    </UserProvider>
  );
}

export default App;
