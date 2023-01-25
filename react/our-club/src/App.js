import './App.css';
import Grid from './screens/Main';
import Nb from './component/NavbarElements';
import Smn from './screens/Seminar';
import React from 'react';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Copyright from './component/Copyright';
import TestGenealogy from './screens/TestGenealogy';
import ExecutionDetails from './screens/ExecutionDetails';
import Notice from './screens/Notice';
function App() {
  return (
    <>
      <Nb />
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
