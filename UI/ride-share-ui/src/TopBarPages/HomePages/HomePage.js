import React, { useState, useEffect } from 'react';
import axios from 'axios';

const HomePageText = () => {
  const [data, setData] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/rs/home/text');
        setData(response.data);
      } catch (error) {
        try {
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
    </div>
  );
};

export default HomePageText;
