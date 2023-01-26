import './App.css';
import Grid from './screens/Main';
import Nb from './component/NavbarElements';
import Smn from './screens/Seminar';
import React, { useEffect, useState } from 'react';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Copyright from './component/Copyright';
import TestGenealogy from './screens/TestGenealogy';
import ExecutionDetails from './screens/ExecutionDetails';
import Notice from './screens/Notice';
import axios from 'axios';

function App() {

  const [userData, setUserData] = useState({
    studentId: '',
    studentName: '',
    studentCollege: '',
    studentMajor: ''
  })

  useEffect(() => {
    axios.get('/userdata')
      .then(function (response) {
        setUserData({
          studentId: response.data[0],
          studentName: response.data[2],
          studentCollege: response.data[3],
          studentMajor: response.data[4]
        });
      })
      .catch((error) => { })
  }, []);

  return (
    <>
      <Nb name={userData['studentName']} />
      <BrowserRouter>
        <Routes>
          <Route path="notice" element={<Notice />} />
          <Route path="/" element={<Grid />} />
          <Route path="/seminar" element={<Smn />} />
          <Route path="/testgenealogy" element={<TestGenealogy />} />
          <Route path="/executiondetails" element={<ExecutionDetails />} />
        </Routes>
      </BrowserRouter>
      <Copyright />

    </>
  );
}
export default App;
