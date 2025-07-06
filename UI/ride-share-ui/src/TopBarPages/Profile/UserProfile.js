import React, { useState, useEffect } from 'react';
import axios from 'axios';
import RideList from '../HomePages/RidesData/RideList';

// Skeleton loader component for a better loading experience
const UserProfileSkeleton = () => (
    <div className="animate-pulse">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-6">
            {[...Array(4)].map((_, i) => (
                <div key={i}>
                    <div className="h-4 bg-slate-700 rounded w-1/4 mb-2"></div>
                    <div className="h-6 bg-slate-700 rounded w-3/4"></div>
                </div>
            ))}
        </div>
        <div className="mt-8 h-12 bg-slate-700 rounded-lg w-full"></div>
    </div>
);

const UserProfile = () => {
    const [userData, setUserData] = useState(null);

    useEffect(() => {
        const fetchUserData = async () => {
            const currentUserName = localStorage.getItem("currentUser");
            if (!currentUserName) return;

            try {
                const response = await axios.get(`https://auth-service-1002278726079.us-central1.run.app/api/rs/get/${currentUserName}`, {
                    headers: { 'Authorization': localStorage.getItem("jwtToken") || "XXX" },
                });
                setUserData(response.data);
            } catch (error) {
                if (error.response?.status === 401) {
                    localStorage.removeItem("jwtToken");
                    window.location.href = '/login';
                }
                console.error('Error fetching user data:', error);
            }
        };

        fetchUserData();
    }, []);
    
    const formatDate = (date) => new Date(date).toLocaleString([], {
        year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit'
    });

    const handlePostRideClick = () => {
        window.location.href = '/create-ride';
    };

    const handleLogout = (e) => {
        e.preventDefault();
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('currentUser');
        window.location.href = "/login";
    };

    const UserDataItem = ({ label, value }) => (
        <div>
            <p className="text-sm font-medium text-slate-400">{label}</p>
            <p className="text-lg text-slate-100">{value}</p>
        </div>
    );

    return (
        <div className="bg-slate-900 min-h-screen">
            {/* REMOVED max-w-7xl to make the layout full-width */}
            <div className="w-full mx-auto px-4 sm:px-6 lg:px-8 py-12 space-y-12">

                {/* --- User Profile Card --- */}
                <div className="bg-slate-800 rounded-2xl shadow-xl p-8 relative">
                    <div className="absolute top-4 right-4">
                        {/* UPDATED logout button to be red */}
                        <button 
                            onClick={handleLogout} 
                            className="text-sm font-medium text-white bg-red-600 hover:bg-red-700 py-2 px-4 rounded-lg transition-colors"
                        >
                            Logout
                        </button>
                    </div>
                    
                    <h2 className="text-3xl font-bold text-white mb-8">User Profile</h2>
                    
                    {userData ? (
                        <div>
                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-x-8 gap-y-6">
                                <UserDataItem label="Username" value={userData.userName} />
                                <UserDataItem label="Full Name" value={userData.name} />
                                <UserDataItem label="Email Address" value={userData.emailId} />
                                <UserDataItem label="Role" value={userData.role} />
                                <UserDataItem label="Last Seen" value={formatDate(userData.lastSeen)} />
                            </div>
                            <div className="mt-8">
                                <button onClick={handlePostRideClick} className="w-full text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-3 text-center transition-colors">
                                    Post a New Ride
                                </button>
                            </div>
                        </div>
                    ) : (
                        <UserProfileSkeleton />
                    )}
                </div>

                {/* --- User's Rides List --- */}
                <div>
                    <h3 className="text-2xl font-bold text-white mb-4">Your Posted Rides</h3>
                    <RideList forUser={'User'} />
                </div>
            </div>
        </div>
    );
};

export default UserProfile;