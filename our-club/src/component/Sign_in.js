import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import axios from 'axios';

function SignIn() {
    const [show, setShow] = useState(false);
    const [inputs, setInputs] = useState({
        userId: '',
        userPw: ''
    })

    const { userId, userPassword } = inputs;

    const onChange = e => {
        const { value, name } = e.target;
        setInputs({
            ...inputs,
            [name]: value
        });
    };

    const handleClose = (lgn) => {
        setShow(false);
        if (lgn) {
            axios.post('/signin', inputs)
            .then(function (response){
                window.location.reload();
            })
            .catch(function(error){
                alert("회원 정보가 일치하지 않습니다.");
            })
        }
    }
    const handleShow = () => setShow(true);

    return (
        <>
            <Button variant="dark" onClick={handleShow}>
                로그인
            </Button>

            <Modal show={show} onHide={() => handleClose(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>로그인</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3" controlId="exampleForm.ControlInput1">
                            <Form.Label>아이디</Form.Label>
                            <Form.Control
                                name="userId"
                                type="name"
                                placeholder=""
                                onChange={onChange}
                                value={userId}
                                autoFocus
                            />
                        </Form.Group>
                        <Form.Group
                            className="mb-3"
                            controlId="exampleForm.ControlTextarea1"
                        >
                            <Form.Label>비밀번호</Form.Label>

                            <Form.Control rows={1}
                                type="password"
                                name="userPw"
                                onChange={onChange}
                                value={userPassword}/>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => handleClose(false)}>
                        닫기
                    </Button>
                    <Button variant="primary" onClick={() => {
                        handleClose(true)
                    }}>
                        로그인
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}



export default SignIn;