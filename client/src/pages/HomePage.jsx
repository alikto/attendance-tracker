import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { login } from "../actions/authActions";
import { useNavigate } from "react-router-dom";

const HomePage = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogout = () => {

      dispatch({ type: "LOGOUT" });
      navigate("/");
  };
  return (
    <div>
      <h1>Hello!</h1>
      <button onClick={handleLogout}>
        <span>Çıkış Yap</span>
      </button>
    </div>
  );
};

export default HomePage;
