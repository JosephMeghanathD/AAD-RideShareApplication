import axios from 'axios';
import React, { useState } from 'react';
import Spinner from '../../BasicElements/Spinner';
import ErrorToast from '../../BasicElements/ErrorToast';

const Signup = () => {
  const [userName, setUserId] = useState('');
  const [name, setName] = useState('');
  const [emailId, setEmailId] = useState('');
  const [role, setRole] = useState('Rider');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [loading, setLoading] = useState(false);


  const handleSignup = (e) => {
    e.preventDefault();
    setLoading(true);

    const signUpReq = async () => {
      setErrorMessage(null);
      try {
        const response = await axios.post('https://auth-service-1002278726079.us-central1.run.app/api/rs/public/signUp', {
          userName,
          password,
          emailId,
          name,
          role
        });
        console.log(response);
        window.location.href = "./login?username=" + userName;
      } catch (error) {
        console.error('Error fetching data:', error);
        setErrorMessage(error.response.data.body.detail);
        setLoading(false);
      }
    };

    signUpReq();
  };
  return (
    <div className="auth-container">
      <form onSubmit={handleSignup}>
        <input
          type="text"
          placeholder="User ID"
          value={userName}
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
            pattern='[+]'
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
        {loading ? <Spinner /> : <button type="submit">Signup</button>}
        {errorMessage && <ErrorToast message={errorMessage} />}
      </form>
    </div>
  );
};

export default Signup;
