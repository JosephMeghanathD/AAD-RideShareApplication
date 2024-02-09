import React, { useState } from 'react';
import './RideList.css';
import ErrorToast from '../../../BasicElements/ErrorToast';

const ridesPerPage = 10;


const RideList = ({ rides }) => {
  const [sortBy, setSortBy] = useState('postedAt');
  const [sortOrder, setSortOrder] = useState('asc');
  const [currentPage, setCurrentPage] = useState(1);

  const [error, setError] = useState(null);


  const handleSort = (key) => {
    if (sortBy === key) {
      setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
    } else {
      setSortBy(key);
      setSortOrder('asc');
    }
  };

  const handlePageChange = (pageNumber) => {
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

  const sortedRides = [...rides].sort((a, b) => {
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

  const indexOfLastRide = currentPage * ridesPerPage;
  const indexOfFirstRide = indexOfLastRide - ridesPerPage;
  const currentRides = sortedRides.slice(indexOfFirstRide, indexOfLastRide);

  return (
    <div className="ride-list-container">
      {error && <ErrorToast message={error} />}
      <table className="ride-list-table">
        <thead>
          <tr>
            <th onClick={() => handleSort('startingFromLocation')}> Starting From Location {sortBy === 'startingFromLocation' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
            <th onClick={() => handleSort('destination')}>Destination {sortBy === 'destination' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
            <th onClick={() => handleSort('numberOfPeople')}>Number of People {sortBy === 'numberOfPeople' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
            <th onClick={() => handleSort('fare')}>Fare {sortBy === 'fare' && (sortOrder === 'fare' ? '▲' : '▼')}</th>
            <th onClick={() => handleSort('timeOfRide')}>Time of Ride {sortBy === 'timeOfRide' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
            <th onClick={() => handleSort('postedBy')}>Posted By {sortBy === 'postedBy' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
            <th onClick={() => handleSort('postedAt')}>Posted At {sortBy === 'postedAt' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
            {localStorage.getItem('jwtToken') !== null && <th>Actions</th>}
          </tr>
        </thead>
        <tbody>
          {currentRides.map((ride) => (
            <tr key={ride.rideId}>
              <td>{ride.startingFromLocation}</td>
              <td>{ride.destination}</td>
              <td>{ride.numberOfPeople}</td>
              <td>{ride.fare}</td>
              <td>{new Date(ride.timeOfRide * 1000).toLocaleString()}</td>
              <td>{ride.postedBy}</td>
              <td>{new Date(ride.postedAt * 1000).toLocaleString()}</td>
              {localStorage.getItem('jwtToken') !== null && <td>{ride.postedBy.startsWith('user') && (<button onClick={() => window.location.href = `/chat?${ride.postedBy}`}>Chat</button>)}</td>}
            </tr>
          ))}
        </tbody>
      </table>
      <div className="pagination">
        <button onClick={() => handlePageChange(1)}>First</button>
        <button onClick={() => handlePageChange(currentPage - 1)}>Previous</button>
        <button onClick={() => handlePageChange(currentPage + 1)}>Next</button>
        <button onClick={() => handlePageChange(Math.ceil(sortedRides.length / ridesPerPage))}>Last</button>

      </div>
    </div>
  );
};

export default RideList;
