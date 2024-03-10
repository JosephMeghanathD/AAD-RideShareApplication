import React, { useState } from 'react';
import axios from 'axios';
import './CreateRide.css'; // Import CSS for styling

const CreateRideForm = () => {
  const [rideData, setRideData] = useState({
    startingFromLocation: '',
    destination: '',
    numberOfPeople: '',
    fare: '',
    timeOfRide: '',
  });

  const handleChange = (e) => {
    var { name, value } = e.target;
    setRideData({
      ...rideData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('Ride data:', rideData);
    try {
      rideData.timeOfRide = new Date(rideData.timeOfRide).getTime()
      const response = await axios.post('http://localhost:8080/api/rs/ride/post', rideData, {
        headers: {
            'Authorization': localStorage.getItem("jwtToken") || "XXX",
            'Content-Type': 'application/json',
        },
      });
      console.log('Ride created successfully:', response.data);
      window.location.href = "./user";
    } catch (error) {
      console.error('Error creating ride:', error);
    }
  };

  return (
    <div className="container">
      <h2>Create a Ride</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="startingFromLocation">Starting From Location:</label>
          <input type="text" id="startingFromLocation" name="startingFromLocation" value={rideData.startingFromLocation} onChange={handleChange} placeholder="Enter starting location" required />
        </div>
        <div className="form-group">
          <label htmlFor="destination">Destination:</label>
          <input type="text" id="destination" name="destination" value={rideData.destination} onChange={handleChange} placeholder="Enter destination" required />
        </div>
        <div className="form-group">
          <label htmlFor="numberOfPeople">Number of People:</label>
          <input type="number" id="numberOfPeople" name="numberOfPeople" value={rideData.numberOfPeople} onChange={handleChange} placeholder="Enter number of people" required />
        </div>
        <div className="form-group">
          <label htmlFor="fare">Fare:</label>
          <input type="number" id="fare" name="fare" value={rideData.fare} onChange={handleChange} placeholder="Enter fare" required />
        </div>
        <div className="form-group">
          <label htmlFor="timeOfRide">Time of Ride:</label>
          <input type="datetime-local" id="timeOfRide" name="timeOfRide" value={rideData.timeOfRide} onChange={handleChange} placeholder="Enter time of ride" required />
        </div>
        <button type="submit">Create Ride</button>
      </form>
    </div>
  );
};

export default CreateRideForm;
