import React from 'react';
import ErrorToast from '../../../BasicElements/ErrorToast';
import RideListRow from './RideListRow';
import { ridesPerPage } from './RideList';

export function RideTable(handleSort, sortBy, sortOrder, currentRides, handlePageChange, currentPage, sortedRides, error) {
  return <div className="ride-list-container">
    <table className="ride-list-table">
      <thead>
        <tr>
          <th onClick={() => handleSort('startingFromLocation')}> Starting From Location {sortBy === 'startingFromLocation' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
          <th onClick={() => handleSort('destination')}>Destination {sortBy === 'destination' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
          <th onClick={() => handleSort('numberOfPeople')}>Number of People {sortBy === 'numberOfPeople' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
          <th onClick={() => handleSort('fare')}>Fare {sortBy === 'fare' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
          <th onClick={() => handleSort('timeOfRide')}>Time of Ride {sortBy === 'timeOfRide' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
          {localStorage.getItem('jwtToken') !== null && <th onClick={() => handleSort('postedBy')}>Posted By {sortBy === 'postedBy' && (sortOrder === 'asc' ? '▲' : '▼')}</th>}
          <th onClick={() => handleSort('postedAt')}>Posted At {sortBy === 'postedAt' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
          {localStorage.getItem('jwtToken') !== null && <th>Actions</th>}
        </tr>
      </thead>
      <tbody>
        {currentRides.map((ride) => (
          <RideListRow ride={ride} />
        ))}
      </tbody>
    </table>
    <div className="pagination">
      <button onClick={() => handlePageChange(1)}>First</button>
      <button onClick={() => handlePageChange(currentPage - 1)}>Previous</button>
      <button onClick={() => handlePageChange(currentPage + 1)}>Next</button>
      <button onClick={() => handlePageChange(Math.ceil(sortedRides.length / ridesPerPage))}>Last</button>

    </div>
    {error && <ErrorToast message={error} />}
  </div>;
}
