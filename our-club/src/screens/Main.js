import "../css/Main.css";
import image from "../img/backGround.jpg";

function Grid() {
  return (
    <div style={{
      backgroundImage: `url(${image})`,
      height: 92.3 + 'vh',
      backgroundSize: 'cover'
    }} className="d-flex justify-content-center align-items-center">

      <div className='text-center bg-white bg-opacity-25 p-5'>
        <h1>Computer ・ Software Engineering<br />42th MOS</h1>
        <h5>Make Our Software. Since 1981.</h5>
      </div>
    </div>


  );
}

export default Grid;
