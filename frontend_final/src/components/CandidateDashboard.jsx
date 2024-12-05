import { useEffect, useState } from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";
import { useNavigate } from "react-router-dom";

function CandidateDashboard() {
  const [profile, setProfile] = useState(null);
  const [jobs, setJobs] = useState([]);
  const [error, setError] = useState("");
  const [applicationStatus, setApplicationStatus] = useState("");
  const [selectedJob, setSelectedJob] = useState(null);
  const [searchQuery, setSearchQuery] = useState(""); // Added searchQuery state
  const navigate = useNavigate();

  useEffect(() => {
    const candidateId = localStorage.getItem("candidateId");
    console.log(candidateId);

    if (!candidateId) {
      setError("No candidate ID found. Please log in again.");
      return;
    }

    const fetchProfile = async () => {
      try {
        const res = await axios.get(
          `http://localhost:8080/candidates/${candidateId}`
        );
        setProfile(res.data);
        console.log(res.data);
      } catch (error) {
        setError("Failed to load profile data. Please try again.");
      }
    };

    const fetchJobs = async () => {
      try {
        const res = await axios.get(`http://localhost:8080/jobs`);
        setJobs(res.data);
      } catch (error) {
        setError("Failed to load job listings. Please try again.");
      }
    };

    fetchProfile();
    fetchJobs();
  }, []);

  const handleApplyClick = async (jobId) => {
    const candidateId = localStorage.getItem("candidateId");
    if (!candidateId) {
      setError("No candidate ID found. Please log in again.");
      return;
    }

    try {
      await axios.post("http://localhost:8080/applications", {
        userId: profile.userId,
        userName: profile.fullName,
        jobId: jobId,
        coverLetter: "", // You can customize the cover letter as needed
        customResume: profile.resume,
        applicationStatus: "Pending",
      });

      setApplicationStatus("Application submitted successfully.");
      setError("");

      // Clear the status message after 3 seconds
      setTimeout(() => {
        setApplicationStatus("");
      }, 3000);
    } catch (error) {
      if (error.response && error.response.status === 409) {
        setError("You have already applied for this job.");
      } else {
        setError("Failed to submit application. Please try again.");
      }

      setApplicationStatus("");
      setTimeout(() => setError(""), 1000);
    }
  };

  const handleViewDetails = (job) => {
    setSelectedJob(job);
  };

  const handleCloseModal = () => {
    setSelectedJob(null);
  };

  // Filter jobs based on the search query
  const filteredJobs = jobs.filter((job) =>
    job.listingTitle.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const renderLoading = () => (
    <div className="text-center">
      <div className="spinner-border text-primary" role="status">
        <span className="visually-hidden">Loading...</span>
      </div>
      <p className="mt-3">Loading...</p>
    </div>
  );

  const renderJobListings = () => (
    <div className="card shadow-lg border-0 rounded-4 bg-dark">
      <div className="card-header bg-primary text-white border-0 rounded-top mt-4">
      <h5 className="mb-0">Job Listings</h5>
        {/* Search Bar */}
      </div>
      
      <div className="mb-3">
        <br></br>
          <input
            type="text"
            className="form-control"
            placeholder="Search job listings..."
            aria-label="Search job listings"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </div>
      <div className="card-body">
        {(error || applicationStatus) && (
          <div className="alert-container mb-3">
            {error && (
              <div className="alert alert-danger rounded-3" role="alert">
                {error}
              </div>
            )}
            {applicationStatus && (
              <div className="alert alert-success rounded-3" role="alert">
                {applicationStatus}
              </div>
            )}
          </div>
        )}

        <div className="list-group">
          {filteredJobs.length === 0 ? (
            <div className="list-group-item">
              No jobs available at the moment.
            </div>
          ) : (
            filteredJobs.map((job) => (
              <div
                className="list-group-item d-flex justify-content-between align-items-center border-0 rounded-3 shadow-sm mb-4"
                key={job.id}
              >
                <div className="col me-5">
                  <h6 className="mb-1">{job.listingTitle}</h6>
                  <p className="mb-1 text-muted">{job.jobDescription}</p>
                </div>
                <div className="ms-auto">
                  <button
                    className="btn btn-primary me-2"
                    onClick={() => handleApplyClick(job.id)}
                  >
                    Apply
                  </button>
                  <button
                    className="btn btn-outline-primary"
                    onClick={() => handleViewDetails(job)}
                  >
                    <i className="bi bi-eye"></i>
                  </button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>

      {/* Job Details Modal */}
      {selectedJob && (
        <div
          className="modal fade show d-block"
          style={{ backgroundColor: "rgba(0, 0, 0, 0.3)" }}
          tabIndex="-1"
          role="dialog"
        >
          <div className="modal-dialog modal-dialog-centered" role="document">
            <div className="modal-content rounded-4">
              <div className="modal-header border-bottom-0">
                <h5 className="modal-title">Job Details</h5>
                <button
                  type="button"
                  className="btn-close"
                  onClick={handleCloseModal}
                  aria-label="Close"
                ></button>
              </div>
              <div className="modal-body">
                <h3 className="mb-4 text-center">{selectedJob.jobTitle}</h3>
                <p>
                  <strong>Department:</strong> {selectedJob.department}
                </p>
                <p>
                  <strong>Description:</strong> {selectedJob.jobDescription}
                </p>
                <p>
                  <strong>Additional Information:</strong>{" "}
                  {selectedJob.additionalInformation}
                </p>
                <p>
                  <strong>Posted:</strong>{" "}
                  {new Date(selectedJob.dateListed).toLocaleDateString()}
                </p>
                <p>
                  <strong>Status:</strong> {selectedJob.listingStatus}
                </p>
              </div>
              <div className="modal-footer border-top-0">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={handleCloseModal}
                >
                  Close
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );

  return (
    <div className="dashboard-background">
      <div className="container mt-4">
        <h1 className="text-center mb-4">
          {profile ? profile.fullName : "Candidate"}'s Dashboard
        </h1>
        <div className="d-flex justify-content-center">
          <div className="col-md-8">
            {!profile ? renderLoading() : renderJobListings()}
          </div>
        </div>
      </div>
    </div>
  );
}

export default CandidateDashboard;
