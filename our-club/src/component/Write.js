import axios from 'axios';
import React, { useEffect, useState } from 'react';
import { Button, Modal, Form, Stack } from 'react-bootstrap';
import "../css/Write.css"

function Write(props) {
  const [write, setWrite] = useState(false);

  //게시글을 서버에 전송할 때 필요한 값은 제목, 내용, 첨부파일들
  const [inputs, setInputs] = useState({
    title: '',
    context: '',
    files: []
  });
  const { title, context, files } = inputs;

  //폼 내부에 input 태그의 값이 변경되면 실행될 함수
  const onChange = e => {
    //폼 컨트롤의 값이 변경되면 해당 컨트롤의 value와 name 값을 가져온다.
    const { value, name } = e.target;
    //첨부된 파일이 변경될 경우는 처리가 다르므로 나누어서 처리
    if (name == "files") {
      //첨부파일 변경 시 File 배열에 담는다. ( 그대로 사용 시 String으로 처리됨. )
      const uploadFiles = Array.prototype.slice.call(e.target.files);
      const fileList = [];
      uploadFiles.forEach((uploadFile) => {
        fileList.push(uploadFile);
      });
      //State에 저장
      setInputs({
        ...inputs,
        [name]: fileList
      });
    } else {
      //제목과 내용은 바로 State에 저장
      setInputs({
        ...inputs,
        [name]: value
      });
    }
  }

  //제출을 눌렀을 때 실행되는 함수
  const postArticle = () => {
    //String과 파일을 동시에 담을 수 있는 객체인 FormData 객체 사용
    const formData = new FormData();

    //files라는 키 값으로 첨부 파일들을 담음.
    inputs['files'].forEach((file) => {
      formData.append('files', file);
    });
    //제목과 내용도 아래의 키값으로 내용을 담음.
    formData.append('title', inputs['title']);
    formData.append('context', inputs['context']);
    //FormData를 백엔드에 넘김.
    //이때 파일을 포함하고 있으므로 Content-Type 헤더는 아래와 같이 조정
    axios.post('/' + props.category, formData, {
      headers: {
          "Content-Type": "multipart/form-data",
      }
  })
      .then(function (response) {
        //정상 처리된 경우 새로고침 하여 모달을 닫고 리스트를 다시 구성함.
        window.location.reload();
      })
      .catch(function (error) {
        //비정상적인 경우 경고
        alert("게시글을 게시할 수 없습니다.")
      })
  };
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
            name="title"
            type="name"
            placeholder="제목을 입력하세요."
            onChange={onChange}
          />

        </Modal.Header>
        <Modal.Body>
          <Form.Group controlId="exampleForm.ControlTextarea1">
            <Form.Control name="context" className='textArea' as='textarea' rows={3} onChange={onChange} />
          </Form.Group>
          {/* 오류 나는 곳 */}
          <div className="filebox bs3-primary">
            <input className="upload-name" value="파일선택" disabled="disabled" />
            <label for="ex_filename">업로드</label>
            <input name="files" multiple type="file" id="ex_filename" className="upload-hidden" onChange={onChange} />
          </div>
          <div className="d-flex justify-content-end">
            <Button variant="dark" type="submit" onClick={postArticle}>제출하기</Button>
            <Button variant="dark" type="button" className="outButton" value="button" onClick={() => setWrite(false)}>나가기</Button>
          </div>
        </Modal.Body>
      </Modal>
    </>
  );
}
export default Write;