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

// Loading screen component
const LoadingScreen = () => (
  <div className="flex flex-col items-center justify-center h-screen w-full bg-gray-900">
    <div className="w-12 h-12 rounded-full animate-spin
                    border-4 border-solid border-blue-500 border-t-transparent"></div>
    <p className="mt-4 text-lg text-gray-300">Connecting to services...</p>
    <p className="mt-1 text-sm text-gray-500">(This may take a moment)</p>
  </div>
);

// Error screen component
const ErrorScreen = () => (
  <div className="flex items-center justify-center h-screen w-full bg-gray-900">
    <div className="p-8 text-center bg-gray-800 border border-red-500 rounded-lg shadow-xl">
       <p className="text-xl font-bold text-red-400">Services Unavailable</p>
       <p className="mt-2 text-gray-300">Could not connect to backend services after several attempts. Please try again later.</p>
    </div>
  </div>
);


function App() {
  const [isLoading, setIsLoading] = useState(true);
  const [isError, setIsError] = useState(false);

  useEffect(() => {
    let isMounted = true; // Flag to prevent state updates if the component unmounts
    
    const serviceUrls = [
      'https://chat-service-1002278726079.us-central1.run.app/api/rs/health',
      'https://ride-service-1002278726079.us-central1.run.app/api/rs/health',
      'https://auth-service-1002278726079.us-central1.run.app/api/rs/health'
    ];

    const retryDelay = 500; // Wait 3 seconds between retries
    const maxRetries = 15;   // Try a maximum of 10 times
    let attemptCount = 0;

    // This function will poll the services until they are all healthy
    const pollServices = async () => {
      attemptCount++;
      console.log(`Attempt ${attemptCount}: Checking service health...`);

      try {
        const responses = await Promise.all(
          serviceUrls.map(url => fetch(url))
        );

        const allServicesOk = responses.every(res => res.ok);

        if (allServicesOk) {
          console.log("All services are healthy. Application is starting.");
          if (isMounted) {
            setIsLoading(false); // SUCCESS: Stop loading and render the app
          }
        } else {
          // If not all services are OK, it's a failure for this attempt
          throw new Error('One or more services are not ready.');
        }
      } catch (error) {
        console.warn(`Attempt ${attemptCount} failed: ${error.message}`);
        
        if (isMounted) {
          if (attemptCount >= maxRetries) {
            // If we've reached the max number of retries, give up
            console.error("Max retries reached. Displaying error screen.");
            setIsError(true);
            setIsLoading(false);
          } else {
            // If we haven't maxed out, wait and try again
            setTimeout(pollServices, retryDelay);
          }
        }
      }
    };

    // Start the first attempt
    pollServices();

    // Cleanup function: This runs when the component unmounts.
    // It prevents the polling from continuing in the background.
    return () => {
      isMounted = false;
    };
    
  }, []); // Empty dependency array ensures this effect runs only once on mount

  // Render loading screen while polling
  if (isLoading) {
    return <LoadingScreen />;
  }

  // Render an error screen if polling failed after max retries
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