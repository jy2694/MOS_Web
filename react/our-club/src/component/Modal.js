import React, { useState } from 'react';
import { Button, Modal, Form, Stack } from 'react-bootstrap';
// import "../css/Write.css";

function PageModal() {
    const [show, setShow] = useState(false);
    return (
        <>

            <Modal
                size='lg'
                show={show}
                onHide={() => setShow(false)}
                dialogClassName="modal-90w"
                aria-labelledby="example-custom-modal-styling-title">

                <Modal.Header className='modalHeader' >
                    &nbsp;&nbsp;&nbsp;&nbsp; 제목

                    <div className='inputBox'>
                        <Form.Control
                            type="name"
                            placeholder="제목을 입력하세요."
                        />
                    </div>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group controlId="exampleForm.ControlTextarea1">
                        <Form.Control className='textArea' as='textarea' rows={3} />
                    </Form.Group>
                    {/* 오류 나는 곳 */}
                    <div className="filebox bs3-primary">
                        <input className="upload-name" value="파일선택" disabled="disabled" />

                        <label for="ex_filename">업로드</label>
                        <input type="file" id="ex_filename" className="upload-hidden" />
                    </div>
                    
                </Modal.Body>

            </Modal>
        </>
    )
}

export default PageModal;