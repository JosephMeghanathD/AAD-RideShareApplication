// RideListRow.js
import React from 'react';

const RideListRow = ({ ride }) => {
  return (
    <tr key={ride.rideId}>
              <td>{ride.startingFromLocation}</td>
              <td>{ride.destination}</td>
              <td>{ride.numberOfPeople}</td>
              <td>{ride.fare}</td>
              <td>{new Date(ride.timeOfRide).toLocaleString()}</td>
              {localStorage.getItem('jwtToken') !== null && <td>{ride.postedBy}</td>}
              <td>{new Date(ride.postedAt).toLocaleString()}</td>
              {localStorage.getItem('jwtToken') !== null && localStorage.getItem("currentUser") !== ride.postedBy && <td><button onClick={() => window.location.href = `/chat?activechat=${ride.postedBy}`}>Chat</button></td>}
            </tr>
  );
};

export default RideListRow;
