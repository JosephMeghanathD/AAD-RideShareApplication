import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './UserProfile.css'; // Import the CSS file
import RideList from '../HomePages/RidesData/RideList';

const UserProfile = () => {
  const [userData, setUserData] = useState(null);

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/rs/get/${localStorage.getItem("currentUser")}`, {
          headers: {
            'Authorization': localStorage.getItem("jwtToken") || "XXX"
          },
        });
        setUserData(response.data);
      } catch (error) {
        if (error.response.status === 401) {
          localStorage.removeItem("jwtToken");
        }
        console.error('Error fetching user data:', error);
      }
    };

    fetchUserData();
  }, []);

  const handlePostRideClick = () => {
    // Redirect to CreateRideForm page
    // Replace with your own routing logic
    window.location.href = '/create-ride';
  };

  const handleLogout = (e) => {
    e.preventDefault();
    localStorage.removeItem('jwtToken');
    window.location.href = "/login";
  };

  return (
    <div>
      <div className="container">
      <div className="header">
        <button className="logout-button" onClick={handleLogout}>Logout</button>
      </div>
      <div className="user-profile">
        <h2>User Profile</h2>
        {userData ? (
          <div>
            <p><strong>User ID:</strong> {userData.userId}</p>
            <p><strong>Name:</strong> {userData.name}</p>
            <p><strong>Email:</strong> {userData.emailId}</p>
            <p><strong>Role:</strong> {userData.role}</p>
            <p><strong>Last Seen:</strong> {userData.lastSeen}</p>
          </div>
        ) : (
          <p>Loading user data...</p>
        )}
        <div className="button-container">
          <button onClick={handlePostRideClick}>Post a Ride</button>
        </div>
      </div>
    </div>
    <RideList forUser={'User'}/>
    </div>
  );
};

export default UserProfile;
