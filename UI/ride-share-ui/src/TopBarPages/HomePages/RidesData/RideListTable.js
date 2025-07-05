import React from 'react';
import ErrorToast from '../../../BasicElements/ErrorToast';
import RideListRow from './RideListRow';

// Converted to a standard functional component with destructured props for clarity
export const RideTable = ({
  handleSort,
  sortBy,
  sortOrder,
  currentRides,
  handlePageChange,
  currentPage,
  totalPages, // <-- New prop
  error,
  isLoading,  // <-- New prop
}) => {
  return (
    <div className="ride-list-container">
      <table className="ride-list-table">
        <thead>
          <tr>
            {/* Table headers remain the same */}
            <th onClick={() => handleSort('startingFromLocation')}>Starting From Location {sortBy === 'startingFromLocation' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
            <th onClick={() => handleSort('destination')}>Destination {sortBy === 'destination' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
            <th onClick={() => handleSort('numberOfPeople')}>Number of People {sortBy === 'numberOfPeople' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
            <th onClick={() => handleSort('fare')}>Fare {sortBy === 'fare' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
            <th onClick={() => handleSort('timeOfRide')}>Time of Ride {sortBy === 'timeOfRide' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
            {localStorage.getItem('jwtToken') !== null && <th onClick={() => handleSort('postedBy.name')}>Posted By {sortBy === 'postedBy.name' && (sortOrder === 'asc' ? '▲' : '▼')}</th>}
            <th onClick={() => handleSort('postedAt')}>Posted At {sortBy === 'postedAt' && (sortOrder === 'asc' ? '▲' : '▼')}</th>
            {localStorage.getItem('jwtToken') !== null && <th>Actions</th>}
          </tr>
        </thead>
        <tbody>
          {isLoading ? (
            <tr>
              {/* Use colSpan to make the cell span the entire width of the table */}
              <td colSpan="8" style={{ textAlign: 'center' }}>Loading...</td>
            </tr>
          ) : currentRides && currentRides.length > 0 ? (
            currentRides.map((ride) => (
              <RideListRow key={ride.id} ride={ride} /> // It's crucial to add a unique key
            ))
          ) : (
            <tr>
              <td colSpan="8" style={{ textAlign: 'center' }}>No rides found.</td>
            </tr>
          )}
        </tbody>
      </table>
      <div className="pagination">
        {/* Added disabled state for better UX */}
        <button onClick={() => handlePageChange(1)} disabled={currentPage === 1 || isLoading}>
          First
        </button>
        <button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1 || isLoading}>
          Previous
        </button>
        
        {/* Added a page indicator */}
        <span className="page-indicator">
          Page {currentPage} of {totalPages > 0 ? totalPages : 1}
        </span>

        {/* The 'Last' button now uses the totalPages prop */}
        <button onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPages || isLoading}>
          Next
        </button>
        <button onClick={() => handlePageChange(totalPages)} disabled={currentPage === totalPages || isLoading}>
          Last
        </button>
      </div>
      {error && <ErrorToast message={error} />}
    </div>
  );
};