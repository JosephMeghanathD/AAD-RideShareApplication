import React, { useState, useEffect } from "react";
import axios from "axios";

const CreateRideForm = () => {
  const [rideData, setRideData] = useState({
    startingFromLocation: "",
    destination: "",
    numberOfPeople: "",
    fare: "",
    timeOfRide: "",
  });
  const [rideId, setRideId] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const queryParams = new URLSearchParams(window.location.search);
    const id = queryParams.get("rideId");
    if (id && id.trim() !== "") {
      setRideId(id);
      fetchRideData(id);
    }
  }, []);

  const fetchRideData = async (id) => {
    try {
      const response = await axios.get(
        `https://ride-service-1002278726079.us-central1.run.app/api/rs/ride/${id}`,
        {
          headers: {
            Authorization: localStorage.getItem("jwtToken") || "XXX",
          },
        }
      );
      const ride = response.data;
      setRideData({
        startingFromLocation: ride.startingFromLocation,
        destination: ride.destination,
        numberOfPeople: ride.numberOfPeople,
        fare: ride.fare,
        timeOfRide: new Date(ride.timeOfRide).toISOString().slice(0, 16),
      });
    } catch (error) {
      console.error("Error fetching ride data:", error);
      setError("Failed to load ride data. Please try again.");
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setRideData({ ...rideData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    setError(null);

    try {
      const rideDataWithTimestamp = {
        ...rideData,
        timeOfRide: new Date(rideData.timeOfRide).getTime(),
      };

      const requestConfig = {
        headers: {
          Authorization: localStorage.getItem("jwtToken") || "XXX",
          "Content-Type": "application/json",
        },
      };

      if (rideId) {
        await axios.put(
          `https://ride-service-1002278726079.us-central1.run.app/api/rs/ride/${rideId}`,
          rideDataWithTimestamp,
          requestConfig
        );
      } else {
        await axios.post(
          "https://ride-service-1002278726079.us-central1.run.app/api/rs/ride/post",
          rideDataWithTimestamp,
          requestConfig
        );
      }
      window.location.href = "./user";
    } catch (error) {
      if (error.response?.status === 401) {
        localStorage.removeItem("jwtToken");
      }
      setError("An error occurred. Please check your details and try again.");
      console.error("Error creating/updating ride:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const FormInput = ({
    id,
    name,
    label,
    type,
    value,
    onChange,
    placeholder,
    required = true,
  }) => (
    <div className="mb-6">
      <label
        htmlFor={id}
        className="block mb-2 text-sm font-medium text-gray-700 dark:text-gray-300"
      >
        {label}
      </label>
      <input
        type={type}
        id={id}
        name={name}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        required={required}
        className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 dark:color-scheme-dark"
      />
    </div>
  );

  return (
    <div className="max-w-2xl mx-auto bg-white dark:bg-gray-800 p-8 rounded-2xl shadow-xl">
      <h2 className="text-3xl font-bold text-gray-900 dark:text-white mb-8 text-center">
        {rideId ? "Edit Your Ride" : "Create a New Ride"}
      </h2>
      <form onSubmit={handleSubmit}>
        <FormInput
          id="startingFromLocation"
          name="startingFromLocation"
          label="Starting From"
          type="text"
          value={rideData.startingFromLocation}
          onChange={handleChange}
          placeholder="e.g., Downtown Central Station"
        />
        <FormInput
          id="destination"
          name="destination"
          label="Destination"
          type="text"
          value={rideData.destination}
          onChange={handleChange}
          placeholder="e.g., Northwood Mall"
        />
        <div className="grid md:grid-cols-2 md:gap-6">
          <FormInput
            id="numberOfPeople"
            name="numberOfPeople"
            label="Available Seats"
            type="number"
            value={rideData.numberOfPeople}
            onChange={handleChange}
            placeholder="e.g., 2"
          />
          <FormInput
            id="fare"
            name="fare"
            label="Fare ($)"
            type="number"
            value={rideData.fare}
            onChange={handleChange}
            placeholder="e.g., 15"
          />
        </div>
        <FormInput
          id="timeOfRide"
          name="timeOfRide"
          label="Date and Time of Ride"
          type="datetime-local"
          value={rideData.timeOfRide}
          onChange={handleChange}
          placeholder="Select date and time"
        />

        <button
          type="submit"
          disabled={isSubmitting}
          className="w-full mt-4 text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-3 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {isSubmitting
            ? "Submitting..."
            : rideId
            ? "Update Ride"
            : "Create Ride"}
        </button>
        {error && (
          <p className="mt-4 text-sm text-center text-red-500">{error}</p>
        )}
      </form>
    </div>
  );
};

export default CreateRideForm;
