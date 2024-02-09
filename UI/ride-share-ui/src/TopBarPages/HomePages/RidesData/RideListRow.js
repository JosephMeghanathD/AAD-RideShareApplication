// RideListRow.js
import React from 'react';

const RideListRow = ({ ride }) => {
  const handleChatRedirect = () => {
    if (ride.postedBy.startsWith('user')) {
      window.location.href = `/chat?${ride.postedBy}`;
    }
  };

  return (
    <tr>
      <td>{ride.rideId}</td>
      <td>{ride.startingFromLocation}</td>
      <td>{ride.destination}</td>
      <td>{ride.numberOfPeople}</td>
      <td>{ride.fare}</td>
      <td>{new Date(ride.timeOfRide * 1000).toLocaleString()}</td>
      <td>{ride.postedBy}</td>
      <td>{new Date(ride.postedAt * 1000).toLocaleString()}</td>
      <td><button onClick={handleChatRedirect}>Chat</button></td>
    </tr>
  );
};

export default RideListRow;
