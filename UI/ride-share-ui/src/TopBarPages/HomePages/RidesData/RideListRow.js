import React from "react";
import axios from "axios";

const ActionButton = ({ onClick, className, children }) => (
  <button
    onClick={onClick}
    className={` bg-black px-4 py-1.5 rounded-lg hover:bg-white text-s transition-colors duration-150 ${className}`}
  >
    {children}
  </button>
);

const RideListRow = ({ ride }) => {
  const isLoggedIn = localStorage.getItem("jwtToken") !== null;
  const currentUser = localStorage.getItem("currentUser");
  const isOwner = isLoggedIn && currentUser === ride.postedBy.name;

  const handleDelete = async () => {
    if (!window.confirm("Are you sure you want to delete this ride?")) return;
    try {
      await axios.delete(
        `https://ride-service-1002278726079.us-central1.run.app/api/rs/ride/${ride.rideId}`,
        {
          headers: { Authorization: localStorage.getItem("jwtToken") || "XXX" },
        }
      );
      window.location.reload();
    } catch (error) {
      if (error.response?.status === 401) {
        localStorage.removeItem("jwtToken");
      }
      console.error("Error deleting ride:", error);
      alert("Failed to delete ride.");
    }
  };

  const formatDate = (date) =>
    new Date(date).toLocaleString([], {
      year: "numeric",
      month: "short",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });

  return (
    <tr className="hover:bg-slate-700/50 transition-colors duration-150">
      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-slate-100">
        {ride.startingFromLocation}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-slate-100">
        {ride.destination}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-center text-slate-300">
        {ride.numberOfPeople}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-300">
        ${ride.fare}
      </td>
      <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-300">
        {formatDate(ride.timeOfRide)}
      </td>
      {isLoggedIn && (
        <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-300">
          {ride.postedBy.name}
        </td>
      )}
      <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-300">
        {formatDate(ride.postedAt)}
      </td>
      {isLoggedIn && (
        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
          <div className="flex items-center gap-4">
            {!isOwner && (
              <ActionButton
                onClick={() =>
                  (window.location.href = `/chat?activechat=${ride.postedBy.userName}`)
                }
                className="text-white hover:text-black"
              >
                Chat
              </ActionButton>
            )}
            {isOwner && (
              <>
                <ActionButton
                  onClick={() =>
                    (window.location.href = `/create-ride?rideId=${ride.rideId}`)
                  }
                  className="text-yellow-500 hover:text-black"
                >
                  Edit
                </ActionButton>
                <ActionButton
                  onClick={handleDelete}
                  className="text-red-500 hover:text-black"
                >
                  Delete
                </ActionButton>
              </>
            )}
          </div>
        </td>
      )}
    </tr>
  );
};

export default RideListRow;
