import { useEffect, useState } from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import { useNavigate } from "react-router-dom";
import { Modal, Button, Form } from "react-bootstrap";

function CandidateApplications() {
  const [applications, setApplications] = useState([]);
  const [jobs, setJobs] = useState({});
  const [error, setError] = useState("");
  const [showEditModal, setShowEditModal] = useState(false);
  const [showDetailModal, setShowDetailModal] = useState(false);
  const [currentApplication, setCurrentApplication] = useState(null);
  const [currentDetails, setCurrentDetails] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const candidateId = localStorage.getItem("candidateId");

    if (!candidateId) {
      setError("No candidate ID found. Please log in again.");
      return;
    }

    const fetchApplications = async () => {
      try {
        const res = await axios.get(
          `http://localhost:8080/applications/candidate/${candidateId}`
        );
        setApplications(res.data);

        const jobIds = [...new Set(res.data.map((app) => app.jobId))];
        const jobResponses = await Promise.all(
          jobIds.map((jobId) =>
            axios.get(`http://localhost:8080/jobs/${jobId}`)
          )
        );
        const jobDetails = jobResponses.reduce((acc, curr) => {
          acc[curr.data.id] = curr.data;
          return acc;
        }, {});
        setJobs(jobDetails);
      } catch (error) {
        setError("Failed to load applications. Please try again.");
      }
    };

    fetchApplications();
  }, []);

  const getStatusColor = (status) => {
    switch (status) {
      case "Pending":
        return "#FFC107"; // Yellow
      case "Accepted":
        return "#28A745"; // Green
      case "Rejected":
        return "#DC3545"; // Red
      default:
        return "#6C757D"; // Gray
    }
  };

  const handleDeleteClick = async (applicationId) => {
    try {
      await axios.delete(`http://localhost:8080/applications/${applicationId}`);
      setApplications(applications.filter((app) => app.id !== applicationId));
      setError("");
    } catch (error) {
      setError("Failed to delete application. Please try again.");
    }
  };

  const handleEditClick = (application) => {
    setCurrentApplication(application);
    setShowEditModal(true);
  };

  const handleModalClose = () => {
    setShowEditModal(false);
    setCurrentApplication(null);
    setShowDetailModal(false);
    setCurrentDetails(null);
  };

  const handleSaveChanges = async () => {
    try {
      await axios.put(
        `http://localhost:8080/applications/${currentApplication.id}`,
        currentApplication
      );
      setApplications((prevApps) =>
        prevApps.map((app) =>
          app.id === currentApplication.id ? currentApplication : app
        )
      );
      handleModalClose();
    } catch (error) {
      setError("Failed to update application. Please try again.");
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setCurrentApplication((prev) => ({ ...prev, [name]: value }));
  };

  const handleDetailsClick = (application) => {
    setCurrentDetails(application);
    setShowDetailModal(true);
  };

  return (
    <div className="applications-background">
      <div className="container mt-5" style={{ padding: 0 }}>
        <h1 className="mb-4 text-center">My Applications</h1>
        {error && (
          <div className="alert alert-danger" role="alert">
            {error}
          </div>
        )}
        {applications.length === 0 ? (
          <p className="text-center">No applications found.</p>
        ) : (
          <div className="list-group">
            {applications.map((application) => {
              const job = jobs[application.jobId];
              return (
                <div
                  className="list-group-item d-flex justify-content-between align-items-start mb-3 p-3 border rounded"
                  key={application.id}
                  style={{ boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)" }}
                >
                  <div className="me-3 flex-grow-1">
                    <h5 className="mb-1">
                      <strong>
                        <span
                          style={{ cursor: "pointer", color: "#007BFF" }}
                          onClick={() => handleDetailsClick(application)}
                        >
                          {job ? job.jobTitle : "Loading..."}
                        </span>
                      </strong>
                    </h5>
                    <p className="mb-1">
                      <strong>Date Applied:</strong>{" "}
                      {new Date(application.dateApplied).toLocaleDateString()}
                    </p>
                  </div>
                  <div className="d-flex align-items-center me-3">
                    <span
                      className="badge"
                      style={{
                        backgroundColor: getStatusColor(application.applicationStatus),
                        color: "#fff",
                        padding: "0.5rem 1rem",
                        borderRadius: "20px",
                        fontSize: "0.875rem",
                      }}
                    >
                      {application.applicationStatus}
                    </span>
                  </div>
                  {application.applicationStatus === "Pending" && (
                  <div>
                    <button
                      className="btn btn-outline-primary btn-sm me-2"
                      onClick={() => handleEditClick(application)}
                      style={{ borderRadius: "20px" }}
                    >
                      Edit
                    </button>
                    <button
                      className="btn btn-outline-danger btn-sm"
                      onClick={() => handleDeleteClick(application.id)}
                      style={{ borderRadius: "20px" }}
                    >
                      Delete
                    </button>
                  </div>
                  )}
                </div>
              );
            })}
          </div>
        )}
{/* Edit Modal */}
<Modal show={showEditModal} onHide={handleModalClose} centered>
  <Modal.Header closeButton>
    <Modal.Title>Edit Application</Modal.Title>
  </Modal.Header>
  <Modal.Body>
    {currentApplication && (
      <Form>
        <Form.Group className="mb-3">
          <Form.Label>Cover Letter</Form.Label>
          <Form.Control
            as="textarea"
            rows={3}
            name="coverLetter"
            value={currentApplication.coverLetter}
            onChange={handleInputChange}
          />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Resume Link</Form.Label>
          <Form.Control
            type="text"
            name="customResume"
            value={currentApplication.customResume}
            onChange={handleInputChange}
            placeholder="Enter your resume link"
          />
        </Form.Group>
      </Form>
    )}
  </Modal.Body>
  <Modal.Footer>
    <Button variant="secondary" onClick={handleModalClose}>
      Cancel
    </Button>
    <Button variant="primary" onClick={handleSaveChanges}>
      Save Changes
    </Button>
  </Modal.Footer>
</Modal>

{/* Details Modal */}
<Modal show={showDetailModal} onHide={handleModalClose} centered>
  <Modal.Header closeButton>
    <Modal.Title>Application Details</Modal.Title>
  </Modal.Header>
  <Modal.Body>
    {currentDetails && (
      <>
        <h3 className="mb-4 text-center">
          {jobs[currentDetails.jobId]?.jobTitle}
        </h3>
        <p>
          <strong>Date Applied:</strong>{" "}
          {new Date(currentDetails.dateApplied).toLocaleDateString()}
        </p>
        <p>
          <strong>Cover Letter:</strong> {currentDetails.coverLetter}
        </p>
        <p>
          <strong>Resume Link:</strong>{" "}
          <a
            href={currentDetails.customResume}
            target="_blank"
            rel="noopener noreferrer"
          >
            View Resume
          </a>
        </p>
      </>
    )}
  </Modal.Body>
  <Modal.Footer>
    <Button variant="secondary" onClick={handleModalClose}>
      Close
    </Button>
  </Modal.Footer>
</Modal>

      </div>
    </div>
  );
  
}

export default CandidateApplications;
