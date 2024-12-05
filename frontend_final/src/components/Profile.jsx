import React, { useEffect, useState, useContext } from "react";
import axios from "axios";
import { UserContext } from "../utils/UserContext";
import {
  Container,
  Card,
  ListGroup,
  Button,
  Modal,
  Form,
} from "react-bootstrap";
import { useNavigate } from "react-router-dom";

function Profile() {
  const { user } = useContext(UserContext);
  const [profileData, setProfileData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    address: "",
    phone: "",
    resume: "",
    department: "",
  });
  const navigate = useNavigate();

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        let response;
        if (user.userType === "Candidate") {
          const candidateId = localStorage.getItem("candidateId");
          response = await axios.get(
            `http://localhost:8080/candidates/${candidateId}`
          );
        } else if (user.userType === "Manager") {
          const managerId = user.manager.id;
          response = await axios.get(
            `http://localhost:8080/managers/${managerId}`
          );
        }

        setProfileData(response.data);
        setFormData(response.data);
      } catch (err) {
        setError("Failed to load profile data.");
      } finally {
        setLoading(false);
      }
    };

    if (user) {
      fetchProfile();
    }
  }, [user]);

  const handleShowModal = () => setShowModal(true);
  const handleCloseModal = () => setShowModal(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (user.userType === 'Candidate') {
        await axios.put(
          `http://localhost:8080/candidates/${profileData.id}`,
          formData
        );
      } else {
        await axios.put(
          `http://localhost:8080/managers/${profileData.id}`,
          formData
        );
      }
      setProfileData(formData);
      handleCloseModal();
    } catch (err) {
      setError("Failed to update profile data.");
    }
  };

  if (loading) return <p>Loading profile...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div className="profile-background">
      <Container className="mt-5">
        <Card className="shadow-sm rounded profile-card p-2">
          <Card.Header className="bg-primary text-white">
            <h5 className="mb-0">Profile Details</h5>
          </Card.Header>
          <Card.Body>
            <ListGroup variant="flush">
              <ListGroup.Item>
                <strong>Full Name:</strong> {profileData?.fullName || "N/A"}
              </ListGroup.Item>
              <ListGroup.Item>
                <strong>Email:</strong> {profileData?.email || "N/A"}
              </ListGroup.Item>
              {user.userType === 'Candidate' && (
                <ListGroup.Item>
                  <strong>Address:</strong> {profileData?.address || "N/A"}
                </ListGroup.Item>
              )}
              <ListGroup.Item>
                <strong>Phone:</strong> {profileData?.phone || "N/A"}
              </ListGroup.Item>
              <ListGroup.Item>
                {user.userType === 'Candidate' ? (
                  <>
                    <strong>Resume:</strong>{" "}
                    {profileData?.resume ? (
                      <a
                        href={profileData.resume}
                        target="_blank"
                        rel="noopener noreferrer"
                      >
                        View Resume
                      </a>
                    ) : (
                      "N/A"
                    )}
                  </>
                ) : (
                  <>
                    <strong>Department:</strong> {profileData?.department || "N/A"}
                  </>
                )}
              </ListGroup.Item>
            </ListGroup>
            <div className="text-center mt-4">
              <Button variant="primary" onClick={handleShowModal}>
                Edit Profile
              </Button>
            </div>
          </Card.Body>
        </Card>

        {/* Modal for Editing Profile */}
        <Modal show={showModal} onHide={handleCloseModal} centered>
          <Modal.Header closeButton>
            <Modal.Title>Edit Profile</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form onSubmit={handleSubmit}>
              <Form.Group controlId="formFullName">
                <Form.Label>Full Name</Form.Label>
                <Form.Control
                  type="text"
                  name="fullName"
                  value={formData.fullName}
                  onChange={handleInputChange}
                  required
                />
              </Form.Group>
              <Form.Group controlId="formEmail" className="mt-3">
                <Form.Label>Email</Form.Label>
                <Form.Control
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleInputChange}
                  required
                />
              </Form.Group>
              {user.userType === 'Candidate' && (
                <Form.Group controlId="formAddress" className="mt-3">
                  <Form.Label>Address</Form.Label>
                  <Form.Control
                    type="text"
                    name="address"
                    value={formData.address}
                    onChange={handleInputChange}
                    required
                  />
                </Form.Group>
              )}
              <Form.Group controlId="formPhone" className="mt-3">
                <Form.Label>Phone</Form.Label>
                <Form.Control
                  type="text"
                  name="phone"
                  value={formData.phone}
                  onChange={handleInputChange}
                  required
                />
              </Form.Group>
              {user.userType === 'Candidate' ? (
                <Form.Group controlId="formResume" className="mt-3">
                  <Form.Label>Resume URL</Form.Label>
                  <Form.Control
                    type="text"
                    name="resume"
                    value={formData.resume}
                    onChange={handleInputChange}
                    required
                  />
                </Form.Group>
              ) : (
                <Form.Group controlId="formDepartment" className="mt-3">
                  <Form.Label>Department</Form.Label>
                  <Form.Control
                    type="text"
                    name="department"
                    value={formData.department}
                    onChange={handleInputChange}
                    required
                  />
                </Form.Group>
              )}
              <div className="text-center mt-4">
                <Button variant="primary" type="submit" className="mx-2">
                  Save Changes
                </Button>
                <Button
                  variant="secondary"
                  onClick={handleCloseModal}
                  className="mx-2"
                >
                  Cancel
                </Button>
              </div>
            </Form>
          </Modal.Body>
        </Modal>
      </Container>
    </div>
  );
}

export default Profile;
