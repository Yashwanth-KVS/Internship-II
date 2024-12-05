import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

function Registration() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [type, setType] = useState("");
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [address, setAddress] = useState("");
  const [phone, setPhone] = useState("");
  const [resume, setResume] = useState("");
  const [department, setDepartment] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [fadeIn, setFadeIn] = useState(false); // State for fade-in effect
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setError("");

    try {
      const userResponse = await axios.post("http://localhost:8080/users", {
        username,
        password,
        type,
      });

      if (userResponse.status === 201) {
        const userId = userResponse.data.id;

        if (type === "Candidate") {
          const candidateResponse = await axios.post(
            "http://localhost:8080/candidates",
            {
              userId,
              fullName,
              email,
              address,
              phone,
              resume,
            }
          );

          if (candidateResponse.status === 201) {
            setMessage(
              "Registration and profile creation successful! Redirecting to login..."
            );
            setTimeout(() => {
              navigate("/login");
            }, 2000);
          }
        } else if (type === "Manager") {
          const managerResponse = await axios.post(
            "http://localhost:8080/managers",
            {
              userId,
              fullName,
              email,
              department,
              phone,
            }
          );

          if (managerResponse.status === 201) {
            setMessage(
              "Manager registration successful! Redirecting to login..."
            );
            setTimeout(() => {
              navigate("/login");
            }, 2000);
          }
        }
      }
    } catch (error) {
      const errorMessage =
        error.response?.data?.message ||
        JSON.stringify(error.response?.data) ||
        "Registration failed. Please try again.";

      setError(
        typeof errorMessage === "string"
          ? errorMessage
          : errorMessage.toString()
      );
      // Clear the error message after 1 second
      setTimeout(() => {
        setError("");
      }, 1000);
    }
  };

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  useEffect(() => {
    setFadeIn(true); // Trigger fade-in effect on mount
  }, []);

  return (
    <div className="page-background">
      <div className="container d-flex justify-content-center align-items-center">
        <div
          className={`card-registration bg-dark text-light p-4 fade-in ${
            fadeIn ? "visible" : ""
          }`}
        >
          <h2 className="text-center mb-4">Register</h2>
          <form onSubmit={handleSubmit}>
            <fieldset>
              <legend className="visually-hidden">Registration Form</legend>
              {message && <div className="alert alert-success">{message}</div>}
              {error && <div className="alert alert-danger">{error}</div>}
              <div className="row mb-3">
                <label
                  htmlFor="username"
                  className="col-sm-3 col-form-label text-end"
                >
                  Username
                </label>
                <div className="col-sm-9">
                  <input
                    type="text"
                    className="form-control"
                    id="username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                    placeholder="Enter your email"
                  />
                </div>
              </div>
              <div className="row mb-3">
                <label
                  htmlFor="password"
                  className="col-sm-3 col-form-label text-end"
                >
                  Password
                </label>
                <div className="col-sm-9">
                  <div className="input-group">
                    <input
                      type={showPassword ? "text" : "password"}
                      className="form-control"
                      id="password"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      required
                      placeholder="Enter your password"
                      autoComplete="off"
                    />
                    <button
                      type="button"
                      className="btn btn-outline-secondary"
                      onClick={togglePasswordVisibility}
                      style={{ cursor: "pointer", width: "50px" }} // Fixed width
                    >
                      {showPassword ? (
                        <i className="fas fa-eye-slash"></i>
                      ) : (
                        <i className="fas fa-eye"></i>
                      )}
                    </button>
                  </div>
                </div>
              </div>
              <div className="row mb-3">
                <label
                  htmlFor="type"
                  className="col-sm-3 col-form-label text-end"
                >
                  Account Type
                </label>
                <div className="col-sm-9">
                  <select
                    className="form-select"
                    id="type"
                    value={type}
                    onChange={(e) => setType(e.target.value)}
                    required
                  >
                    <option value="" disabled>
                      Select account type
                    </option>
                    <option value="Candidate">Candidate</option>
                    <option value="Manager">Manager</option>
                  </select>
                </div>
              </div>

              {type === "Candidate" && (
                <>
                  <div className="row mb-3">
                    <label
                      htmlFor="fullName"
                      className="col-sm-3 col-form-label text-end"
                    >
                      Full Name
                    </label>
                    <div className="col-sm-9">
                      <input
                        type="text"
                        className="form-control"
                        id="fullName"
                        value={fullName}
                        onChange={(e) => setFullName(e.target.value)}
                        required
                        placeholder="Enter your full name"
                      />
                    </div>
                  </div>
                  <div className="row mb-3">
                    <label
                      htmlFor="email"
                      className="col-sm-3 col-form-label text-end"
                    >
                      Email
                    </label>
                    <div className="col-sm-9">
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
                  </div>
                  <div className="row mb-3">
                    <label
                      htmlFor="address"
                      className="col-sm-3 col-form-label text-end"
                    >
                      Address
                    </label>
                    <div className="col-sm-9">
                      <input
                        type="text"
                        className="form-control"
                        id="address"
                        value={address}
                        onChange={(e) => setAddress(e.target.value)}
                        required
                        placeholder="Enter your address"
                      />
                    </div>
                  </div>
                  <div className="row mb-3">
                    <label
                      htmlFor="phone"
                      className="col-sm-3 col-form-label text-end"
                    >
                      Phone
                    </label>
                    <div className="col-sm-9">
                      <input
                        type="text"
                        className="form-control"
                        id="phone"
                        value={phone}
                        onChange={(e) => setPhone(e.target.value)}
                        required
                        placeholder="Enter your phone number"
                      />
                    </div>
                  </div>
                  <div className="row mb-3">
                    <label
                      htmlFor="resume"
                      className="col-sm-3 col-form-label text-end"
                    >
                      Resume
                    </label>
                    <div className="col-sm-9">
                      <input
                        type="text"
                        className="form-control"
                        id="resume"
                        value={resume}
                        onChange={(e) => setResume(e.target.value)}
                        required
                        placeholder="Enter your resume link"
                      />
                    </div>
                  </div>
                </>
              )}
              {type === "Manager" && (
                <>
                  <div className="row mb-3">
                    <label
                      htmlFor="fullName"
                      className="col-sm-3 col-form-label text-end"
                    >
                      Full Name
                    </label>
                    <div className="col-sm-9">
                      <input
                        type="text"
                        className="form-control"
                        id="fullName"
                        value={fullName}
                        onChange={(e) => setFullName(e.target.value)}
                        required
                        placeholder="Enter your full name"
                      />
                    </div>
                  </div>
                  <div className="row mb-3">
                    <label
                      htmlFor="email"
                      className="col-sm-3 col-form-label text-end"
                    >
                      Email
                    </label>
                    <div className="col-sm-9">
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
                  </div>
                  <div className="row mb-3">
                    <label
                      htmlFor="department"
                      className="col-sm-3 col-form-label text-end"
                    >
                      Department
                    </label>
                    <div className="col-sm-9">
                      <input
                        type="text"
                        className="form-control"
                        id="department"
                        value={department}
                        onChange={(e) => setDepartment(e.target.value)}
                        required
                        placeholder="Enter your department"
                      />
                    </div>
                  </div>
                  <div className="row mb-3">
                    <label
                      htmlFor="phone"
                      className="col-sm-3 col-form-label text-end"
                    >
                      Phone
                    </label>
                    <div className="col-sm-9">
                      <input
                        type="text"
                        className="form-control"
                        id="phone"
                        value={phone}
                        onChange={(e) => setPhone(e.target.value)}
                        required
                        placeholder="Enter your phone number"
                      />
                    </div>
                  </div>
                </>
              )}
              <div className="col-sm-12 text-center">
                <button type="submit" className="btn btn-primary">
                  Register
                </button>
              </div>
              <div className="text-center mt-3">
                <p>
                  Already have an account?{""}
                  <a href="/login" className="btn btn-link">
                    Log in here
                  </a>
                </p>
              </div>
            </fieldset>
          </form>
        </div>
      </div>
    </div>
  );
}

export default Registration;
