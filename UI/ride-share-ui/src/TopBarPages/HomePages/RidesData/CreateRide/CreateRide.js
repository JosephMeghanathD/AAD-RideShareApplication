import React, { useState, useEffect } from 'react';
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
  const [rideId, setRideId] = useState(null);

  useEffect(() => {
    const queryParams = new URLSearchParams(window.location.search);
    const id = queryParams.get('rideId');
    if (id && id.trim() !== '') {
      setRideId(id);
      // Fetch ride data by ID
      fetchRideData(id);
    }
  }, []);

  const fetchRideData = async (id) => {
    try {
      const response = await axios.get(`https://ride-service-1002278726079.us-central1.run.app/api/rs/ride/${id}`, {
        headers: {
          'Authorization': localStorage.getItem("jwtToken") || "XXX",
          'Content-Type': 'application/json',
        },
      });
      const ride = response.data; // Assuming response.data is the ride object
      setRideData({
        startingFromLocation: ride.startingFromLocation,
        destination: ride.destination,
        numberOfPeople: ride.numberOfPeople,
        fare: ride.fare,
        timeOfRide: new Date(ride.timeOfRide).toISOString().slice(0, 16), // Format datetime-local value
      });
    } catch (error) {
      console.error('Error fetching ride data:', error);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setRideData({
      ...rideData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('Ride data:', rideData);
    try {
      const rideDataWithTimestamp = {
        ...rideData,
        timeOfRide: new Date(rideData.timeOfRide).getTime(), // Convert timeOfRide to timestamp
      };
      if (rideId) {
        // If rideId exists, it's an edit operation
        const response = await axios.put(`https://ride-service-1002278726079.us-central1.run.app/api/rs/ride/${rideId}`, rideDataWithTimestamp, {
          headers: {
            'Authorization': localStorage.getItem("jwtToken") || "XXX",
            'Content-Type': 'application/json',
          },
        });
        console.log('Ride updated successfully:', response.data);
      } else {
        // Otherwise, it's a create operation
        const response = await axios.post('https://ride-service-1002278726079.us-central1.run.app/api/rs/ride/post', rideDataWithTimestamp, {
          headers: {
            'Authorization': localStorage.getItem("jwtToken") || "XXX",
            'Content-Type': 'application/json',
          },
        });
        console.log('Ride created successfully:', response.data);
      }
      window.location.href = "./user";
    } catch (error) {
      if (error.response.status === 401) {
        localStorage.removeItem("jwtToken");
      }
      console.error('Error creating/updating ride:', error);
    }
  };

  return (
    <div className="container">
      <h2>{rideId ? 'Edit Ride' : 'Create a Ride'}</h2>
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
        <button type="submit">{rideId ? 'Edit Ride' : 'Create Ride'}</button>
      </form>
    </div>
  );
};

export default CreateRideForm;
