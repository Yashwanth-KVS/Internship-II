import React, { useContext, useState } from "react";
import { createJob } from "../utils/apis";
import { Container, Form, Button, Row, Col } from "react-bootstrap";
import { UserContext } from "../utils/UserContext";

const getNow = () => {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, "0");
  const day = String(now.getDate()).padStart(2, "0");
  const hours = String(now.getHours()).padStart(2, "0");
  const minutes = String(now.getMinutes()).padStart(2, "0");
  return `${year}-${month}-${day}T${hours}:${minutes}`; // Format for datetime-local
};

const blankForm = {
  department: "",
  listingTitle: "",
  dateListed: getNow(),
  dateClosed: getNow(),
  jobTitle: "",
  jobDescription: "",
  additionalInformation: "",
  listingStatus: "",
}

function AddJobForm({ setShowForm }) {
  const [formData, setFormData] = useState(blankForm);
  const { user } = useContext(UserContext);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await createJob({
        ...formData,
        managerId: user.manager.id,
      });
      setFormData(blankForm)
      setShowForm(val => !val)
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <Container className="my-2">
      <Form onSubmit={handleSubmit} className="p-4 rounded">
        <Row className="mb-3">
          <Col md={4}>
            <Form.Group>
              <Form.Label>Department</Form.Label>
              <Form.Control
                type="text"
                name="department"
                value={formData.department}
                onChange={handleChange}
                required
              />
            </Form.Group>
          </Col>
          <Col md={4}>
            <Form.Group>
              <Form.Label>Listing Title</Form.Label>
              <Form.Control
                type="text"
                name="listingTitle"
                value={formData.listingTitle}
                onChange={handleChange}
                required
              />
            </Form.Group>
          </Col>
          <Col md={4}>
            <Form.Group>
              <Form.Label>Date Listed</Form.Label>
              <Form.Control
                type="datetime-local"
                name="dateListed"
                value={formData.dateListed}
                onChange={handleChange}
                min={`${new Date().toISOString().slice(0, 10)}T00:00`}
                required
              />
            </Form.Group>
          </Col>
        </Row>
        <Row className="mb-3">
        <Col md={4}>
          <Form.Group>
            <Form.Label>Date Closed</Form.Label>
            <Form.Control
              type="datetime-local"
              name="dateClosed"
              value={formData.dateClosed}
              onChange={handleChange}
              min={`${new Date().toISOString().slice(0, 10)}T00:00`}
              required
            />
          </Form.Group>
        </Col>
          <Col md={4}>
            <Form.Group>
              <Form.Label>Job Title</Form.Label>
              <Form.Control
                type="text"
                name="jobTitle"
                value={formData.jobTitle}
                onChange={handleChange}
                required
              />
            </Form.Group>
          </Col>
          <Col md={4}>
            <Form.Group>
              <Form.Label>Listing Status</Form.Label>
              <Form.Select
                name="listingStatus"
                value={formData.listingStatus}
                onChange={handleChange}
                required
              >
                <option value="">Select Status</option>
                <option value="active">Active</option>
                <option value="closed">Closed</option>
                <option value="pending">Pending</option>
              </Form.Select>
            </Form.Group>
          </Col>
        </Row>
        <Row className="mb-3">
          <Col md={12}>
            <Form.Group>
              <Form.Label>Job Description</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                name="jobDescription"
                value={formData.jobDescription}
                onChange={handleChange}
                required
              />
            </Form.Group>
          </Col>
        </Row>
        <Row className="mb-3">
          <Col md={12}>
            <Form.Group>
              <Form.Label>Additional Information</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                name="additionalInformation"
                value={formData.additionalInformation}
                onChange={handleChange}
              />
            </Form.Group>
          </Col>
        </Row>
        <Button type="submit" className="btn me-2">
          Add Job Listing
        </Button>
        <Button variant="secondary" onClick={() => setShowForm(false)}>
          Cancel
        </Button>
      </Form>
    </Container>
  );
}

export default AddJobForm;
