import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Table from 'react-bootstrap/Table';
import "../css/Main.css";
import Carousel from 'react-bootstrap/Carousel';
import image from "../img/backGround.jpg";

function Grid() {
  return (
    <div style={{
      backgroundImage: `url(${image})`,
      height: 92.3 + 'vh',
      backgroundSize: 'cover'
    }} className="d-flex justify-content-center align-items-center">

      <h1 className='text-center'>Computer ・ Software Engineering
        <br />
        41th MOS
        <h5>Make Our Software. Since 1981.</h5>
      </h1>

    </div>


  );
}

export default Grid;
