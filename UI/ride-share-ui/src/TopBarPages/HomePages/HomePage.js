import React, { useState, useEffect } from "react";
import axios from "axios";
import ErrorToast from "../../BasicElements/ErrorToast";

const HomePageText = () => {
  const [data, setData] = useState(null);
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(
          "https://auth-service-1002278726079.us-central1.run.app/api/rs/home/text",
          {
            headers: {
              Authorization: localStorage.getItem("jwtToken"),
            },
          }
        );
        setData(response.data);
      } catch (error) {
        if (error.response?.status === 401) {
          localStorage.removeItem("jwtToken");
        }
        try {
          setErrorMessage(
            "You are viewing public data. Log in to unlock more features."
          );
          const response = await axios.get(
            "https://auth-service-1002278726079.us-central1.run.app/api/rs/public/home/text"
          );
          setData(response.data);
        } catch (publicError) {
          console.error("Error fetching public data:", publicError);
          setErrorMessage("Could not load data. Please try again later.");
        }
        console.error("Error fetching private data:", error);
      }
    };

    fetchData();
  }, []);

  return (
    <div className="relative text-center bg-slate-800 p-8 sm:p-12 rounded-2xl">
      {data ? (
        <h1 className="text-3xl md:text-4xl font-extrabold text-white tracking-tight">
          {data}
        </h1>
      ) : (
        <div className="animate-pulse">
          <div className="h-10 bg-slate-700 rounded-md w-3/4 mx-auto"></div>
        </div>
      )}
      {errorMessage && <ErrorToast message={errorMessage} />}
    </div>
  );
};

export default HomePageText;
