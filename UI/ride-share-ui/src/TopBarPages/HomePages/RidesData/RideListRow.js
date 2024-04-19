// RideListRow.js
import React from 'react';
import axios from 'axios';


const RideListRow = ({ ride }) => {

  const handleDelete = async () => {
    try {
      const response = await axios.delete(`http://localhost:8081/api/rs/ride/${ride.rideId}`, {
        headers: {
          'Authorization': localStorage.getItem("jwtToken") || "XXX",
        },
      });
      console.log('Ride deleted successfully:', response.data);
      // Optionally: Add logic to update UI after successful deletion
      window.location.reload();
    } catch (error) {
      if (error.response.status === 401) {
        localStorage.removeItem("jwtToken");
      }
      console.error('Error deleting ride:', error);
      // Optionally: Add logic to handle error
    }
  };


  return (
    <tr key={ride.rideId}>
              <td>{ride.startingFromLocation}</td>
              <td>{ride.destination}</td>
              <td>{ride.numberOfPeople}</td>
              <td>{ride.fare}</td>
              <td>{new Date(ride.timeOfRide).toLocaleString()}</td>
              {localStorage.getItem('jwtToken') !== null && <td>{ride.postedBy.name}</td>}
              <td>{new Date(ride.postedAt).toLocaleString()}</td>
              <td>
                {localStorage.getItem('jwtToken') !== null && localStorage.getItem("currentUser") !== ride.postedBy.name && <button onClick={() => window.location.href = `/chat?activechat=${ride.postedBy.name}`}>Chat</button>}
                {localStorage.getItem('jwtToken') !== null && localStorage.getItem("currentUser") === ride.postedBy.name && <button onClick={() => window.location.href = `/create-ride?rideId=${ride.rideId}`}>Edit</button>}
                {localStorage.getItem('jwtToken') !== null && localStorage.getItem("currentUser") === ride.postedBy.name && <button onClick={handleDelete}>Delete</button>}
              </td>
            </tr>
  );
};

export default RideListRow;
