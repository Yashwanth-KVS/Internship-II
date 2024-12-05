import React, { useContext, useEffect, useState } from "react";
import AddJobForm from "./AddJobForm";
import { getJobsByManagerId } from "../utils/apis";
import { Container, Row, Col, Card, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../utils/UserContext";

function ManagerDashboard() {
  const [showForm, setShowForm] = useState(false);
  const [jobs, setJobs] = useState([]);
  const [error, setError] = useState("");
  const { user } = useContext(UserContext);
  const navigate = useNavigate();

  const fetchJobs = async () => {
    if (user.userType === "Manager") {
      try {
        const res = await getJobsByManagerId(user.manager.id);
        const data = await res.json();
        setJobs(data);
      } catch (err) {
        setError("Failed to load job listings. Please try again.");
      }
    }
  };

  useEffect(() => {
    fetchJobs();
  }, [showForm]);

  const handleAddJobClick = () => {
    setShowForm(true);
  };

  if (error) {
    return (
      <Container className="mt-5">
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
      </Container>
    );
  }

  return (
    <div className="manager-dashboard-background">
      <div className="container mt-5">
        <Row>
          <Col md={6} className="mb-4">
            {/* Add Job Form Card */}
            <Card className="shadow-sm">
              <Card.Header className="bg-primary text-white">
                <h5 className="mb-0">Add Job Listing</h5>
              </Card.Header>
              <Card.Body>
                <AddJobForm setShowForm={setShowForm} />
              </Card.Body>
            </Card>
          </Col>
         
          <Col md={6} className="mb-4">
            {/* Job Listings Card */}
            <Card className="shadow-sm">
              <Card.Header className="bg-primary text-white">
                <h5 className="mb-0">Job Listings</h5>
              </Card.Header>
              
              <Card.Body>
                <div className="list-group">
                  {jobs.length === 0 ? (
                    <div className="list-group-item">
                      No jobs available at the moment.
                    </div>
                  ) : (
                    jobs.map((job, index) => (
                      <div className="list-group-item" key={index}>
                        <h6 className="mb-1">{job.listingTitle}</h6>
                        <p className="mb-1">{job.jobDescription}</p>
                        <Button
                          className="btn btn-primary"
                          onClick={() => navigate(`/manager-job/${job.id}`)}
                        >
                          View Details
                        </Button>
                        
                      </div>
                    ))
                  )}
                </div>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </div>
    </div>
  );
  
}

export default ManagerDashboard;