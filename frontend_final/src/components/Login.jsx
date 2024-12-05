import { useState, useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import { UserContext } from "../utils/UserContext";
import { setUser } from "../utils/user";
import { GoogleOAuthProvider, GoogleLogin } from "@react-oauth/google"; // Use the updated package

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [email, setEmail] = useState(""); // For forgot password
  const [showForgotPassword, setShowForgotPassword] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [fadeIn, setFadeIn] = useState(false);
  const navigate = useNavigate();
  const { login } = useContext(UserContext);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    try {
      const response = await axios.post("http://localhost:8080/login", {
        username,
        password,
      });

      if (response.status === 200) {
        const user = response.data;
        setUser(user);
        setSuccess("Login successful! Redirecting...");
        login(user);

        if (user.userType === "Candidate" && user.candidate && user.candidate.id) {
          localStorage.setItem("candidateId", user.candidate.id);
          console.log("candidateId set successfully:", user.candidate.id); // Debugging
        }

        switch (user.userType) {
          case "Candidate":
            navigate("/candidate-dashboard");
            break;
          case "Manager":
            navigate("/manager-dashboard");
            break;
          case "Admin":
            navigate("/admin-dashboard");
            break;
          default:
            navigate("/");
            break;
        }
      }
    } catch (err) {
      setError("Login failed. Please check your username and password.");

      setTimeout(() => {
        setError("");
      }, 1000);
    }
  };

   // Handle forgot password request
  const handleForgotPassword = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");
    console.log(email);
    try {
      const response = await axios.post("http://localhost:8080/forgot-password", { email });
      setSuccess("Password reset link has been sent to your email.");
    } catch (error) {
      setError("Error: " + (error.response?.data?.message || "Something went wrong."));
    }
  };
  // Toggle between login and forgot password forms
  const toggleForgotPasswordForm = () => {
    setShowForgotPassword(!showForgotPassword);
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  useEffect(() => {
    setFadeIn(true);
  }, []);

  // Google login handler
  const handleGoogleLoginSuccess = (credentialResponse) => {
    console.log("Google login success:", credentialResponse);
    // Use the credentialResponse to authenticate with your backend
    axios
      .post("http://localhost:8080/google-login", {
        token: credentialResponse.credential,
      })
      .then((res) => {
        const user = res.data;
        console.log("Redirecting...",user);
        setUser(user);
        login(user);
        if (user.userType === "Candidate" && user.candidate && user.candidate.id) {
          localStorage.setItem("candidateId", user.candidate.id);
          console.log("candidateId set successfully:", user.candidate.id); // Debugging
        }
        console.log("candidateId set successfully:", user.id);
        localStorage.setItem("candidateId", user.candidate.id);
        navigate("/candidate-dashboard");
      })
      .catch((error) => {
        console.error("Google login error:", error);
        setError("Google login failed.");
      });
  };

  return (
    <div className="page-background">
      <div className="container d-flex justify-content-center align-items-center">
        <div
          className={`card-login bg-dark text-light p-4 fade-in ${
            fadeIn ? "visible" : ""
          }`}
        >
           <h2 className="text-center mb-4">{showForgotPassword ? "Forgot Password" : "Login"}</h2>
          
          {showForgotPassword ? (
            // Forgot Password Form
            <form onSubmit={handleForgotPassword}>
              <fieldset>
                <legend className="visually-hidden">Forgot Password Form</legend>
                {error && <div className="alert alert-danger">{error}</div>}
                {success && <div className="alert alert-success">{success}</div>}

                <div className="mb-3">
                  <label htmlFor="email" className="form-label text-start">
                    Email Address
                  </label>
                  <input
                    type="email"
                    className="form-control"
                    id="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    placeholder="Enter your email"
                  />
                </div>
                <div className="mb-3 text-center">
                  <button type="submit" className="btn btn-primary">Send Reset Link</button>
                </div>
                <div className="text-center">
                  <button
                    type="button"
                    className="btn btn-link text-light"
                    onClick={toggleForgotPasswordForm}
                  >
                    Back to Login
                  </button>
                </div>
              </fieldset>
            </form>
          ) : (
            // Login Form
            <form onSubmit={handleSubmit}>
              <fieldset>
                <legend className="visually-hidden">Login Form</legend>
                {error && <div className="alert alert-danger">{error}</div>}
                {success && <div className="alert alert-success">{success}</div>}

                <div className="mb-3">
                  <label htmlFor="username" className="form-label text-start">
                    Username
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id="username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                    placeholder="Enter username"
                  />
                </div>
                <div className="mb-3">
                  <label htmlFor="password" className="form-label text-start">
                    Password
                  </label>
                  <div className="input-group">
                    <input
                      type={showPassword ? "text" : "password"}
                      className="form-control"
                      id="password"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      required
                      placeholder="Enter password"
                      autoComplete="off"
                    />
                    <button
                      type="button"
                      className="btn btn-outline-secondary"
                      onClick={togglePasswordVisibility}
                      style={{ cursor: "pointer", width: "50px" }}
                    >
                      {showPassword ? (
                        <i className="fas fa-eye-slash"></i>
                      ) : (
                        <i className="fas fa-eye"></i>
                      )}
                    </button>
                  </div>
                </div>
                <div className="mb-3 text-center">
                  <button type="submit" className="btn btn-primary">Login</button>
                </div>
                <div className="text-center">
                  <p>
                    Don't have an account? <a href="/register">Register here</a>
                  </p>
                  <button
                    type="button"
                    className="btn btn-link text-light"
                    onClick={toggleForgotPasswordForm}
                  >
                    Forgot Password?
                  </button>
                </div>
              </fieldset>
            </form>
          )}
          
          
          <div className="text-center mt-3">
            <GoogleOAuthProvider clientId="1045431858909-5dmen895jtbjsmrqa09ebkggd5tpglro.apps.googleusercontent.com">
              <GoogleLogin
                onSuccess={handleGoogleLoginSuccess}
                onError={() => {
                  console.log("Google login failed.");
                }}
              />
            </GoogleOAuthProvider>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
