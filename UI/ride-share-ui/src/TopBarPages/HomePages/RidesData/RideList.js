import { useEffect, useState } from 'react';
import './RideList.css';
import axios from 'axios';
import { RideTable } from './RideListTable';


const RideList = () => {
  const [sortBy, setSortBy] = useState('postedAt');
  const [sortOrder, setSortOrder] = useState('asc');
  const [currentPage, setCurrentPage] = useState(1);

  const [error, setError] = useState(null);
  
  const [data, setData] = useState({rides:[]});
  const [screenHeight, setScreenHeight] = useState(window.innerHeight);

  useEffect(() => {
    const handleResize = () => {
      setScreenHeight(window.innerHeight);
    };

    window.addEventListener('resize', handleResize);

    return () => {
      window.removeEventListener('resize', handleResize);
    };
  }, []);
  useEffect(() => {
    const fetchData = async () => {
      
      try {
        const response = await axios.get('http://localhost:8081/api/rs/rides', {
          headers: {
            'Authorization': localStorage.getItem("jwtToken") || "XXX"
          }
        });
        setData(response.data);
      } catch (error) {
        if (error.response.status === 401) {
          localStorage.removeItem("jwtToken");
        }
        console.error('Error fetching rides data:', error);
      }
    };

    fetchData();
  }, []);

  const handleSort = (key) => {
    if (sortBy === key) {
      setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
    } else {
      setSortBy(key);
      setSortOrder('asc');
    }
  };

  const handlePageChange = (pageNumber) => {
    var ridesPerPage = screenHeight / 50;
    setError(null);
    if (pageNumber > Math.ceil(sortedRides.length / ridesPerPage)) {
      setError('You are already on last page!' );
      return;
    } 
    if (pageNumber < 1) {
      setError('You are already on first page!');
      return;
    }
    setCurrentPage(pageNumber);
  };

  const sortedRides = [...data.rides].sort((a, b) => {
    const valueA = typeof a[sortBy] === 'string' ? a[sortBy].toLowerCase() : a[sortBy];
    const valueB = typeof b[sortBy] === 'string' ? b[sortBy].toLowerCase() : b[sortBy];

    if (sortOrder === 'asc') {
      if (valueA < valueB) return -1;
      if (valueA > valueB) return 1;
      return 0;
    } else {
      if (valueA > valueB) return -1;
      if (valueA < valueB) return 1;
      return 0;
    }
  });
  var ridesPerPage = screenHeight / 50;
  const indexOfLastRide = currentPage * ridesPerPage;
  const indexOfFirstRide = indexOfLastRide - ridesPerPage;
  const currentRides = sortedRides.slice(indexOfFirstRide, indexOfLastRide);
  if (ridesPerPage < 10) {
    return (
      RideTable(handleSort, sortBy, sortOrder, currentRides, handlePageChange, currentPage, sortedRides, error, 10)
    );
  } else {
    return (
      RideTable(handleSort, sortBy, sortOrder, currentRides, handlePageChange, currentPage, sortedRides, error, ridesPerPage)
    );
  }
};

export default RideList;

