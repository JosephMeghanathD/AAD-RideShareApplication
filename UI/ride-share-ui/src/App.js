import React, { useState, useEffect } from "react";
import "./App.css";
import NavBar from "./TopBarPages/NavBar";
import "./styles.css";
import Home from "./TopBarPages/HomePages/Home";
import AboutUs from "./TopBarPages/AboutUs";
import ContactUs from "./TopBarPages/ContactUs";
import AuthExtraction from "./TopBarPages/AuthPages/AuthExraction";
import Chat from "./TopBarPages/Chat/Chat";
import UserProfile from "./TopBarPages/Profile/UserProfile";
import CreateRideForm from "./TopBarPages/HomePages/RidesData/CreateRide/CreateRide";

import { Routes, Route } from "react-router-dom";

// Loading screen component styled with Tailwind CSS for a dark theme
const LoadingScreen = () => (
  <div className="flex flex-col items-center justify-center h-screen w-full bg-gray-900">
    {/* A more modern spinner */}
    <div className="w-12 h-12 rounded-full animate-spin
                    border-4 border-solid border-blue-500 border-t-transparent"></div>
    <p className="mt-4 text-lg text-gray-300">Checking services...</p>
  </div>
);

// Error screen component styled with Tailwind CSS for a dark theme
const ErrorScreen = () => (
  <div className="flex items-center justify-center h-screen w-full bg-gray-900">
    <div className="p-8 text-center bg-gray-800 border border-red-500 rounded-lg shadow-xl">
       <p className="text-xl font-bold text-red-400">Services Unavailable</p>
       <p className="mt-2 text-gray-300">Could not connect to one or more backend services. Please try again later.</p>
    </div>
  </div>
);


function App() {
  const [isLoading, setIsLoading] = useState(true);
  const [isError, setIsError] = useState(false);

  useEffect(() => {
    // Function to perform health checks on all backend services
    const checkServiceHealth = async () => {
      const serviceUrls = [
        'https://chat-service-1002278726079.us-central1.run.app/api/rs/health',
        'https://ride-service-1002278726079.us-central1.run.app/api/rs/health',
        'https://auth-service-1002278726079.us-central1.run.app/api/rs/health'
      ];

      try {
        // Perform all fetch requests concurrently
        const responses = await Promise.all(
          serviceUrls.map(url => fetch(url))
        );

        // Check if all responses have a 2xx status code (e.g., 200 OK)
        const allServicesOk = responses.every(res => res.ok);

        if (allServicesOk) {
          setIsLoading(false); // All good, stop loading
        } else {
          // If any service fails (e.g., returns 503), throw an error
          throw new Error('One or more services are not healthy.');
        }
      } catch (error) {
        console.error("Service health check failed:", error);
        setIsError(true);      // Set the error state
        setIsLoading(false); // Stop loading to show the error screen
      }
    };

    // Run the health check
    checkServiceHealth();
    
  }, []); // The empty dependency array ensures this effect runs only once on mount

  // Render loading screen while checking services
  if (isLoading) {
    return <LoadingScreen />;
  }

  // Render an error screen if any health check failed
  if (isError) {
    return <ErrorScreen />;
  }

  // Once loading is complete and successful, render the main application
  return (
    <div className="flex flex-col h-screen bg-gray-900">
      <NavBar />
      <main className="flex-grow overflow-y-auto">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/home" element={<Home />} />
          <Route path="/contact" element={<ContactUs />} />
          <Route path="/aboutus" element={<AboutUs />} />
          <Route path="/login" element={<AuthExtraction />} />
          <Route path="/chat" element={<Chat />} />
          <Route path="/user" element={<UserProfile />} />
          <Route path="/create-ride" element={<CreateRideForm />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;