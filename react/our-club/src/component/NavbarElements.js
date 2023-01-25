import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import SignIn from './Sign_in';
import React from "react";

function Nb() {
    return (
        <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
            <Container>
                <Navbar.Brand href="/" >
                    <h2>MOS</h2>
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="me-auto">
                        <Nav.Link href="Notice">공지사항</Nav.Link>
                        <Nav.Link href="">갤러리</Nav.Link>
                        <Nav.Link href="Seminar">세미나</Nav.Link>
                        <Nav.Link href="ExecutionDetails">집행 내역</Nav.Link>
                        <Nav.Link href="TestGenealogy"> 족보</Nav.Link>
                    </Nav>
                    <Nav>
                        <SignIn />
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    )
}
export default Nb;
