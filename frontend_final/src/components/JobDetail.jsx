import React, { useContext, useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getJobById, getApplicationsByJobId, updateJobById, deleteJobById } from "../utils/apis"; 
import { Container, Row, Col, Card, Button, Form, Alert, ListGroup } from "react-bootstrap";
import { UserContext } from "../utils/UserContext";

function JobDetail() {
  const { jobId } = useParams(); 
  const [job, setJob] = useState(null);
  const [applications, setApplications] = useState([]);
  const [error, setError] = useState("");
  const [editMode, setEditMode] = useState(false); 
  const [updatedJob, setUpdatedJob] = useState({});
  const { user } = useContext(UserContext);
  const navigate = useNavigate();

  const formatDate = (date) => {
    if (!date) return ""; // Return empty string if date is undefined or null
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, "0"); // Month is 0-indexed
    const day = String(d.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  const approveupdateApplicationStatus = async (applicationId) => {
    try {
        // Define the URL based on the action
        const url = `http://localhost:8080/applications/${applicationId}/approve`;
        
        // Send PUT request to update status
        const res = await fetch(url, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            }
        });

        // Check if the response is not ok
        if (!res.ok) {
            throw new Error("Failed to approve application");
        }

        // Optional: Update local state or provide feedback
        console.log(`Application approved successfully`);
    } catch (error) {
        console.error(`Failed to approve application`, error);
    }
};

const denyupdateApplicationStatus = async (applicationId, action) => {
  try {
      // Define the URL based on the action (approve or deny)
      const url = `http://localhost:8080/applications/${applicationId}/deny`;

      // Send PUT request to update status
      const res = await fetch(url, {
          method: "PUT",
          headers: {
              "Content-Type": "application/json"
          }
      });

      // Check if the response is not ok
      if (!res.ok) {
          throw new Error(`Failed to deny application`);
      }

      // Optional: Update local state or provide feedback
      console.log(`Application rejected successfully`);
  } catch (error) {
      console.error(`Failed to reject application`, error);
  }
};


  // Fetch job details
  const fetchJobDetails = async () => {
    try {
      const res = await getJobById(jobId);
      if (!res.ok) {
        throw new Error("Failed to fetch job details");
      }
      const data = await res.json();
      setJob(data);
      setUpdatedJob(data); 
    } catch (err) {
      setError(err.message);
    }
  };

  // Fetch applications if manager
  const fetchApplications = async () => {
    if (user.userType === "Manager") {
      try {
        const res = await getApplicationsByJobId(jobId);
        if (!res.ok) {
          throw new Error("Failed to fetch applications");
        }
        const data = await res.json();
        setApplications(data);
      } catch (err) {
        setError(err.message);
      }
    }
  };

  useEffect(() => {
    fetchJobDetails();
    fetchApplications();
  }, [jobId]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUpdatedJob({ ...updatedJob, [name]: value });
  };

  const handleSave = async () => {
    try {
      const res = await updateJobById(jobId, updatedJob);
      if (!res.ok) {
        throw new Error("Failed to update job");
      }
      const updatedData = await res.json();
      setJob(updatedData); 
      setEditMode(false);
    } catch (err) {
      setError(err.message);
    }
  };

  // Handle job deletion
  const handleDelete = async () => {
    const confirmDelete = window.confirm("Are you sure you want to delete this job?");
    if (confirmDelete) {
      try {
        const res = await deleteJobById(jobId);
        if (!res.ok) {
          throw new Error("Failed to delete job");
        }
        alert("Job deleted successfully");
        navigate("/manager-dashboard"); // Redirect to manager dashboard after deletion
      } catch (err) {
        setError(err.message);
      }
    }
  };

  if (error) {
    return <Alert variant="danger">{error}</Alert>;
  }

  if (!job) {
    return <div>Loading job details...</div>;
  }

  return (
    <div
      style={{
        background: "linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)), url('https://a.storyblok.com/f/213807/1600x800/526cc101e9/finance-tech-midsize.jpg')",
        backgroundSize: "cover",
        backgroundPosition: "center",
        minHeight: "100vh",
        color: "white",
        padding: "20px"
      }}
    >
      <Container className="mt-5">
        <Row>
          {/* Left Column: Edit Job or Job Details */}
          <Col md={6} className="mb-4">
            <Card className="shadow-sm">
              <Card.Header className="bg-primary text-white">
                {editMode ? "Edit Job Listing" : "Job Details"}
              </Card.Header>
              <Card.Body>
                {editMode ? (
                  // Editable job form
                  <Form>
                    <Form.Group className="mb-3">
                      <Form.Label>Listing Title</Form.Label>
                      <Form.Control
                        type="text"
                        name="listingTitle"
                        value={updatedJob.listingTitle}
                        onChange={handleInputChange}
                        className="bg-light"
                      />
                    </Form.Group>
                    <Form.Group className="mb-3">
                      <Form.Label>Job Title</Form.Label>
                      <Form.Control
                        type="text"
                        name="jobTitle"
                        value={updatedJob.jobTitle}
                        onChange={handleInputChange}
                        className="bg-light"
                      />
                    </Form.Group>
                    <Form.Group className="mb-3">
                      <Form.Label>Department</Form.Label>
                      <Form.Control
                        type="text"
                        name="department"
                        value={updatedJob.department}
                        onChange={handleInputChange}
                        className="bg-light"
                      />
                    </Form.Group>
                    <Form.Group className="mb-3">
                      <Form.Label>Description</Form.Label>
                      <Form.Control
                        as="textarea"
                        rows={3}
                        name="jobDescription"
                        value={updatedJob.jobDescription}
                        onChange={handleInputChange}
                        className="bg-light"
                      />
                    </Form.Group>
                    <Form.Group className="mb-3">
                      <Form.Label>Additional Information</Form.Label>
                      <Form.Control
                        as="textarea"
                        rows={2}
                        name="additionalInformation"
                        value={updatedJob.additionalInformation}
                        onChange={handleInputChange}
                        className="bg-light"
                      />
                    </Form.Group>
                    <Button variant="success" onClick={handleSave} className="w-100 my-2">
                      Save Changes
                    </Button>
                    <Button variant="secondary" onClick={() => setEditMode(false)} className="w-100">
                      Cancel
                    </Button>
                  </Form>
                ) : (
                  // Job details display
                  <>
                    <h6>{job.listingTitle}</h6>
                    <p><strong>Job Title:</strong> {job.jobTitle}</p>
                    <p><strong>Description:</strong> {job.jobDescription}</p>
                    <p><strong>Department:</strong> {job.department}</p>
                    <p>
                    <strong>Date Listed:</strong>
                    <input
                      type="date"
                      value={job.dateListed ? formatDate(job.dateListed) : ""}
                      onChange={(e) => handleDateChange("dateListed", e.target.value)}
                      min={new Date().toISOString().split("T")[0]} // Prevent past dates
                    />
                  </p>
                  <p>
                    <strong>Date Closed:</strong>
                    <input
                      type="date"
                      value={job.dateListed ? formatDate(job.dateListed) : ""}
                      onChange={(e) => handleDateChange("dateClosed", e.target.value)}
                      min={new Date().toISOString().split("T")[0]} // Prevent past dates
                    />
                  </p>

                    <Button variant="primary" onClick={() => setEditMode(true)} className="w-100">
                      Edit Job
                    </Button>
                    <Button variant="danger" onClick={handleDelete} className="w-100 mt-2">
                      Delete Job
                    </Button>
                  </>
                )}
              </Card.Body>
            </Card>
          </Col>

          {/* Right Column: Applications List (For Managers) */}
          {user.userType === "Manager" && (
            <Col md={6} className="mb-4">
              <Card className="shadow-sm">
                <Card.Header className="bg-success text-white">
                  Job Applications
                </Card.Header>
                <Card.Body>
                  {applications.length > 0 ? (
                    <ListGroup>
                      {applications.map((application) => (
                        <ListGroup.Item key={application.id} className="mb-2">
                          <p><strong>User ID:</strong> {application.userId}</p>
                          <p><strong>Candidate Name:</strong> {application.userName}</p>
                          <p><strong>Date Applied:</strong> {new Date(application.dateApplied).toLocaleString()}</p>
                          <p><strong>Resume Link:</strong> <a href={application.customResume} target="_blank">Click here to view Resume</a></p>
                          <p><strong>Status:</strong> {application.applicationStatus}</p>
                          {application.applicationStatus === "Pending" && (
                          <div>
                    <button
                      className="btn btn-outline-success btn-sm me-2"
                      onClick={() => approveupdateApplicationStatus(application.id)}
                      style={{ borderRadius: "20px" }}
                    >
                      Approve
                    </button>
                    <button
                      className="btn btn-outline-danger btn-sm me-2"
                      onClick={() => denyupdateApplicationStatus(application.id, "Rejected")}
                      style={{ borderRadius: "20px" }}
                    >
                      Deny
                    </button>
                    </div>
                          )}
                        </ListGroup.Item>
                      ))}
                    </ListGroup>
                  ) : (
                    <p>No applications received for this job yet.</p>
                  )}
                </Card.Body>
              </Card>
            </Col>
          )}
        </Row>
      </Container>
    </div>
  );
}

export default JobDetail;
