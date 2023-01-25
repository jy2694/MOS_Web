import React, { useState } from 'react';
import { Button, Modal, Form, Stack } from 'react-bootstrap';
import "../css/Write.css"

function Write(props) {
  const [write, setWrite] = useState(false);
  return (
    <>
      <div >
        <Button variant="dark" onClick={() => setWrite(true)} >
          글쓰기
        </Button>
      </div>
      <Modal
        size='lg'
        show={write}
        onHide={() => setWrite(false)}
        dialogClassName="modal-90w"
        aria-labelledby="example-custom-modal-styling-title">

        <Modal.Header className="d-flex justify-content-center" >
           제목
          
            <Form.Control

              type="name"
              placeholder="제목을 입력하세요."
            />
          
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
          <div className="d-flex justify-content-end">
            <Button variant="dark" type="submit">제출하기</Button>
            <Button variant="dark" type="button" className="outButton" value="button" >나가기</Button>
          </div>
        </Modal.Body>
      </Modal>


    </>
  );
}
export default Write;