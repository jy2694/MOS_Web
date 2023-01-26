import Table from 'react-bootstrap/esm/Table';
import Write from './Write';
import "../css/Seminar.css";
import Container from 'react-bootstrap/esm/Container';
import Pagination from 'react-bootstrap/Pagination';
import React, { useState, useEffect } from 'react';
import { Button, Modal, Form } from 'react-bootstrap';
import axios from 'axios';


function Article(props) {
    //Variable
    const [maxPage, setMaxPage] = useState(8);
    const [pages, setPages] = useState([1, 0]);
    const [page, setPage] = useState(1);
    const [show, setShow] = useState(false);
    const [articles, setArticles] = useState([]);
    const [permission, setPermission] = useState(2);
    const [content, setContent] = useState({
        IdV: 0,
        titleV: '',
        contentV: '',
        filesV: [],
        permission: false
    });

    //Function
    //모달 열어서 게시글 보여주기
    const showArticle = (article) => {
        axios.get('/permission?category='+props.category+'&id='+article.id)
        .then((res) => {
            setContent({
                IdV: article.id,
                titleV: article.title,
                contentV: article.context,
                filesV: article.files,
                permission: res.data
            });
            setShow(true);
        })
    };

    //백 엔드에서 가져온 게시글들 15개 씩 잘라서 페이지 구성
    const printArticleList = () => {
        const newArr = [];
        for (let i = (page - 1) * 15; i < ((page) * 15 > articles.length ? articles.length : (page) * 15); i++) {
            newArr.push(<tr key={i}>
                <td>{articles[i].id}</td>
                <td onClick={() => showArticle(articles[i])}>
                    {articles[i].title}
                </td>
                <td>{articles[i].createBy}</td>
            </tr>);
        }
        return newArr;
    }

    //페이지 이동을 위한 페이지네이션 구성
    const printPagination = () => {
        const newArr = [];
        for (let i = pages[0]; i <= pages[1]; i++) {
            newArr.push(<Pagination.Item key={i} onClick={() => setPage(i)}>{i}</Pagination.Item>);
        }
        return newArr;
    };

    //모달에 게시글과 함께 첨부파일 구성
    const printAttachmentFiles = () => {
        const newArr = [];
        for (let index = 0; index < content.filesV.length; index++) {
            const element = content.filesV[index];
            newArr.push(<><a key={index} href={element.originPath} download={element.fileName}>{element.fileName}</a><br /></>)
        }
        return newArr;
    }

    const deleteArticle = () => {
        axios.delete('/'+props.category+'?id='+content.IdV)
        .then(() => {
            window.location.reload();
        })
        .catch(() => {
            alert('권한이 없습니다.');
        })
    }


    useEffect(() => {
        //해당 카테고리 게시글 요청
        axios.get('/' + props.category)
            .then(function (response) {
                const articles = [];
                //현재 페이지에서 보여지는 최소, 최대 페이지 계산
                let printMinPage = page - 4;
                let printMaxPage = page + 4;
                if (printMinPage <= 0) printMinPage = 1;
                if (printMaxPage > Math.ceil(response.data.length / 15)) printMaxPage = Math.ceil(response.data.length / 15);
                setPages([printMinPage, printMaxPage]);

                //각 게시글의 첨부파일 요청
                for (let index = 0; index < response.data.length; index++) {
                    let data = response.data[index];
                    data['files'] = [];
                    axios.get("/attached-files?category=" + props.category + "&id=" + data.id)
                        .then(function (attachResponse) {
                            //각 첨부파일을 받아 리액트에서 사용할 수 있도록 변환
                            attachResponse.data.map((value, idx) => {
                                axios({
                                    method: 'get',
                                    url: "/files/" + value.originPath,
                                    responseType: 'blob'
                                }).then((res) => {
                                    //변환된 링크와 파일 이름을 저장
                                    const url = window.URL.createObjectURL(new Blob([res.data]));
                                    data['files'].push({
                                        fileName: value.filePath,
                                        originPath: url
                                    });
                                })
                            });
                        })
                    //첨부파일까지 모두 담은 데이터 객체를 배열에 담음.
                    articles.push(data);
                }
                //State에 저장함
                setArticles(articles);
                setMaxPage(Math.ceil(response.data.length / 15));
            })
            .catch((error) => {
                //오류 발생 시, 권한 부족임. 경고 후 뒤로 가기를 자동으로 수행함.
                alert("로그인이 필요합니다.");
                window.history.back();
            })
        //현재 로그인된 계정의 권한 확인.
        axios.get('/permission')
            .then((response) => {
                //백엔드로부터 권한을 받아 저장
                setPermission(response.data);
            })
    }, []);

    return (
        <>
            <Container>
                <div className='snt'>
                    <Table striped bordered hover size="sm" className=''>
                        <thead>
                            <tr>
                                <th className="w-25 h5">글 번호</th>
                                <th className="w-50 h5">게시글</th>
                                <th className="w-25 h5">작성자</th>
                            </tr>
                        </thead>
                        <tbody className='postList'>
                            {printArticleList()}
                        </tbody>
                    </Table>
                </div>
                <div className="d-flex justify-content-end">
                    {/* 카테고리와 권한을 보아 글 작성이 가능한 권한이면 표시 아니면 표시하지 않음. */}
                    {(props.category == "notice" || props.category == "assignment" || props.category == "usage") && permission == 0 && <Write category={props.category} />}
                    {(props.category == "gallery" || props.category == "pasttest") && permission <= 1 && <Write category={props.category} />}
                </div>
                <div className="d-flex justify-content-center">
                    <Pagination >
                        <Pagination.Prev onClick={() => setPage((page - 1 < 1 ? maxPage : page - 1))} />
                        {printPagination()}
                        <Pagination.Next onClick={() => setPage((page + 1 > maxPage ? 1 : page + 1))} />
                    </Pagination>
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
                        {printAttachmentFiles()}
                    </div>
                    <div className="d-flex justify-content-end mt-3">
                        {content.permission && <Button variant='dark' type="button" value="button" onClick={() => deleteArticle()}>삭제</Button>}
                        <Button variant="dark" type="button" className="outButton" value="button" onClick={() => setShow(false)}>나가기</Button>
                    </div>
                </Modal.Body>
            </Modal>

        </>

    )
}

export default Article;