import React, { useState } from 'react';
import './ErrorToast.css'; // CSS file for styling

const ErrorToast = ({ message }) => {
    const [showToast, setShowToast] = useState(true);

    const handleClose = () => {
        setShowToast(false);
    };

    return (
        <>
            {showToast && (
                <div className="error-toast">
                    <span className="message">{message}</span>
                    <button className="close-button" onClick={handleClose}>
                        &times;
                    </button>
                </div>
            )}
        </>
    );
};

export default ErrorToast;
