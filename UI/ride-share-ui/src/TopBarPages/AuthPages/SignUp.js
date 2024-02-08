import axios from 'axios';
import React, { useState } from 'react';

const Signup = () => {
  const [userId, setUserId] = useState('');
  const [name, setName] = useState('');
  const [emailId, setEmailId] = useState('');
  const [role, setRole] = useState('Rider');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const handleSignup = (e) => {
    const signUpReq = async () => {
      try {
        const response = await axios.post('http://localhost:8080/api/rs/public/signUp', {
          userId,
          password,
          emailId,
          name,
          role
        });
        console.log(response)
      } catch (error) {
        console.error('Error fetching data:', error);
        setErrorMessage(error.response.data.body.detail);
      }
    };

    signUpReq();
  };
  return (
    <div className="auth-container">
      {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
      <form onSubmit={handleSignup}>
        <input
          type="text"
          placeholder="User ID"
          value={userId}
          onChange={(e) => setUserId(e.target.value)}
        />
        <input
          type="text"
          placeholder="Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <input
          type="email"
          placeholder="Email"
          value={emailId}
          onChange={(e) => setEmailId(e.target.value)}
        />
        <div class="radio-block no-border">
          <input id="Rider"
            type="radio"
            name="contrasts"
            value="Rider"
            checked={role === 'Rider'}
            onChange={() => setRole('Rider')}
            defaultChecked
          />
          <label for="Rider" >Rider</label>
          <input id="Driver"
            type="radio"
            name="contrasts"
            value="Driver"
            checked={role === 'Driver'}
            onChange={() => setRole('Driver')}
          />
          <label for="Driver" class="off-label">Driver</label>
          <span class="selected" aria-hidden="true"></span>
        </div>
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit">Signup</button>
      </form>
    </div>
  );
};

export default Signup;
