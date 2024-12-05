import React, { useEffect } from "react";
import { Card, Row, Col, Container } from "react-bootstrap";

function About() {
  const features = [
    {
      title: "Hiring Manager Dashboard",
      description:
        "A comprehensive dashboard for hiring managers to post jobs, review applications, and manage candidate status.",
    },
    {
      title: "Candidate Dashboard",
      description:
        "A dedicated space for candidates to track their applications, view job postings, and receive updates.",
    },
    {
      title: "Easy to Apply",
      description:
        "Streamlined application process enabling candidates to apply quickly and efficiently, enhancing the recruitment experience.",
    },
    {
      title: "Real-time Status Updates",
      description:
        "Stay updated with real-time status updates for job postings, applications, and candidate progression.",
    },
  ];

  useEffect(() => {
    // Fade-in effect when the component mounts
    const elements = document.querySelectorAll('.fade-in');
    setTimeout(() => {
      elements.forEach(el => {
        el.classList.add('visible');
      });
    }, 200); // Delay before starting the fade-in
  }, []);

  return (
    <div className="page-background">
      <Container>
        <h1 className="about-title fade-in">About Our Application</h1>
        <p className="about-description fade-in">
          Our platform provides a modern solution for managing job listings and
          candidate applications, tailored for both hiring managers and job
          seekers.
        </p>
        <Row>
          {features.map((feature, index) => (
            <Col key={index} sm={12} md={6} className="mb-4">
              <Card className="feature-card fade-in">
                <Card.Body>
                  <Card.Title className="feature-title">
                    {feature.title}
                  </Card.Title>
                  <Card.Text>{feature.description}</Card.Text>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      </Container>
    </div>
  );
}

export default About;
