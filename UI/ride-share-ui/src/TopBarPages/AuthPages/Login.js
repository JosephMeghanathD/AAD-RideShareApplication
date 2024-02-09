import axios from 'axios';
import React, { useState } from 'react';

const Login = () => {
  const searchParams = new URLSearchParams(window.location.search);
  const userNameParam = searchParams.get('username');
  const [username, setUsername] = useState(userNameParam);
  const [password, setPassword] = useState('');

  const [errorMessage, setErrorMessage] = useState('');

  const handleLogin = (e) => {
    e.preventDefault();
    const loginReq = async () => {
      try {
        const response = await axios.post('http://localhost:8080/api/rs/public/login', {
          username,
          password
        });
        console.log(response);
        localStorage.setItem("jwtToken", response.data);
        window.location.href = "./home";
      } catch (error) {
        console.error('Error fetching data:', error);
        setErrorMessage(error.response.data.body.detail);
      }
    };

    loginReq();
  };

  return (
    <div className="auth-container">
      {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
      <form onSubmit={handleLogin}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit">Login</button>
      </form>
    </div>
  );
};

export default Login;
