import React, { useState } from "react";
// You can now remove the import for ErrorToast.css
// import "./ErrorToast.css"; 

const ErrorToast = ({ message }) => {
  // This state now controls whether the component is rendered at all
  const [showToast, setShowToast] = useState(true);

  if (!showToast) {
    return null; // Render nothing if the toast is closed
  }

  return (
    // Main toast container
    <div 
      className="fixed top-5 right-5 w-full max-w-sm bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded-lg shadow-lg flex items-center justify-between z-50"
      role="alert"
    >
      {/* Message */}
      <span className="font-medium mr-4">{message}</span>
      
      {/* Close Button */}
      <button 
        onClick={() => setShowToast(false)}
        className="text-red-700 hover:text-red-900 text-2xl font-bold leading-none focus:outline-none"
        aria-label="Close"
      >
        ×
      </button>
    </div>
  );
};

export default ErrorToast;