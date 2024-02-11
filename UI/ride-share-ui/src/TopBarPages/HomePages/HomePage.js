import React, { useState, useEffect } from 'react';
import axios from 'axios';
import ErrorToast from '../../BasicElements/ErrorToast';

const HomePageText = () => {
  const [data, setData] = useState(null);

  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      
      try {
        const response = await axios.get('http://localhost:8080/api/rs/home/text', {
          headers: {
            'Authorization': localStorage.getItem("jwtToken")
          }
        });
        setData(response.data);
      } catch (error) {
        if (error.response.status === 401) {
          localStorage.removeItem("jwtToken");
        }
        try {
          setErrorMessage("You are viewing public data, login to unlock more features.")
          const response = await axios.get('http://localhost:8080/api/rs/public/home/text');
        setData(response.data);
        } catch {
          console.error('Error fetching data:', error);
        }
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, []);

  return (
    <div>
      {data && (
        <div>
          <pre>{data}</pre>
        </div>
      )}
      {errorMessage && <ErrorToast message={errorMessage} />}
    </div>
  );
};

export default HomePageText;
