import Table from 'react-bootstrap/esm/Table';
import Write from './Write';
import "../css/Seminar.css";
import Container from 'react-bootstrap/esm/Container';
import Paginations from './Pagination';
import React, { useState } from 'react';
import { Button ,Modal, Form } from 'react-bootstrap';

function Article(props) {
    const [show, setShow] = useState(false);
    const [content, setContent] = useState({
        titleV : '',
        contentV: ''
    });

    const showArticle = (title, content) => {
        setContent({
            titleV: title,
            contentV: content
        });
        setShow(true);
    };
    return (
        <>
            <Container>
                <div className='snt'>
                    <Table striped bordered hover size="sm" className=''>
                        <thead>

                            <tr>
                                <th>글 번호</th>
                                <th colSpan={2}>게시글</th>
                                <th>작성자</th>
                            </tr>
                        </thead>
                        <tbody className='postList'>
                            <tr>
                                <td>1</td>
                                <td colSpan={2}
                                    onClick={() => showArticle("타이틀 테스트", "내용 테스트")}>
                                    {props.category}
                                </td>
                                <td>배준형</td>
                            </tr>

                        </tbody>
                    </Table>
                </div>
                <div className="d-flex justify-content-end">
                    <Write />
                </div>
                <div className="d-flex justify-content-center">
                    <Paginations />
                </div>
            </Container>
            <Modal
        size='lg'
        show={show}
        onHide={() => setShow(false)}
        dialogClassName="modal-90w"
        aria-labelledby="example-custom-modal-styling-title">

        <Modal.Header className="d-flex justify-content-center" >
           제목
          
            <Form.Control
              type="name"
              placeholder="제목을 입력하세요."
              value={content['titleV']}
              disabled
            />
          
        </Modal.Header>
        <Modal.Body>
          <Form.Group controlId="exampleForm.ControlTextarea1">
            <Form.Control
                className='textArea' 
                as='textarea' 
                rows={3}
                value={content['contentV']}
                disabled />
          </Form.Group>
          {/* 오류 나는 곳 */}
          <div className="filebox bs3-primary">
            <span>asdfgh</span>
          </div>
          <div className="d-flex justify-content-end">
            <Button variant="dark" type="button" className="outButton" value="button" >나가기</Button>
          </div>
        </Modal.Body>
      </Modal>

        </>

    )
}

export default Article;