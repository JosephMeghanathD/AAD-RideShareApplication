import axios from 'axios';
import React, { useState } from 'react';
import Spinner from '../../BasicElements/Spinner';
import ErrorToast from '../../BasicElements/ErrorToast';

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
      setErrorMessage(null);
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
        setLoading(false);
      }
    };
    loginReq();
  };

  return (
    <div className="auth-container">
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
        {errorMessage && <ErrorToast message={errorMessage} />}
      </form>
    </div>
  );
};

export default Login;
