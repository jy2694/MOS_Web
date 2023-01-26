import Pagination from 'react-bootstrap/Pagination';
function Paginations() {

  return (
    
    <Pagination >
      <Pagination.Prev href="/"/>
      <Pagination.Item href="#">{1}</Pagination.Item>
      <Pagination.Item href="#">{2}</Pagination.Item>
      <Pagination.Item href="#">{3}</Pagination.Item>
      <Pagination.Item href="#">{4}</Pagination.Item>
      <Pagination.Item href="#">{5}</Pagination.Item>
      <Pagination.Item href="#">{6}</Pagination.Item>
      <Pagination.Item href="#">{7}</Pagination.Item>
      <Pagination.Item href="#">{8}</Pagination.Item>
      <Pagination.Next href="/"/>
    </Pagination>
  );
}

export default Paginations;