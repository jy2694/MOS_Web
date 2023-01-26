import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';
import SignIn from './Sign_in';
import React, {useState} from "react";
import axios from 'axios';

function Nb(props) {
    const [show, setShow] = useState(false);
    const handleClose = (lgn) => {
        setShow(false);
        if (lgn) {
            axios.post('/logout')
            .then(function (response){
                window.location.reload();
            })
            .catch(function(error){
                alert("로그인이 필요합니다.");
            })
        }
    }
    const handleShow = () => setShow(true);

    return (
        <>
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
                            {props.name && <Nav.Link href="TestGenealogy"> 족보</Nav.Link>}
                        </Nav>
                        <Nav>
                            {!props.name && <SignIn />}
                            {props.name && <Navbar.Text>
                                {props.name} 님, 안녕하세요. <Button variant="dark" onClick={handleShow}>로그아웃</Button>
                            </Navbar.Text>}
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
            <Modal show={show} onHide={() => handleClose(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>로그아웃</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <label>로그아웃 하시겠습니까?</label>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => handleClose(false)}>
                        닫기
                    </Button>
                    <Button variant="primary" onClick={() => {handleClose(true)}}>
                        로그아웃
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}
export default Nb;
