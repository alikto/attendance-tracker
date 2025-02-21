import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import HomePage from "./pages/HomePage";

const App = () => {
    return (
            <Router>
                <Routes>
                    <Route path="/" element={<LoginPage />} />
                    <Route path="/home-page" element={<HomePage />} />
                </Routes>
            </Router>
    );
};

export default App;
