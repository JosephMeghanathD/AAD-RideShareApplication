import axios from 'axios';
import React, { useState } from 'react';
import Spinner from '../../BasicElements/Spinner';

const Login = () => {
  const searchParams = new URLSearchParams(window.location.search);
  const userNameParam = searchParams.get('username');
  const [username, setUsername] = useState(userNameParam);
  const [password, setPassword] = useState('');

  const [errorMessage, setErrorMessage] = useState('');
  const [loading, setLoading] = useState(false);


  const handleLogin = (e) => {
    e.preventDefault();
    setLoading(true);

    const loginReq = async () => {
      try {
        setTimeout(() => { window.location.href = "./home"; }, 5000);
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
        setLoading(false);
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
        {loading ? <Spinner /> : <button type="submit">Login</button>}
      </form>
    </div>
  );
};

export default Login;
