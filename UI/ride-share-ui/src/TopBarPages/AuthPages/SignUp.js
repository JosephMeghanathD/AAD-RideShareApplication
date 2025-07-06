import axios from 'axios';
import React, { useState } from 'react';
// Spinner component is no longer imported from a separate file
import ErrorToast from '../../BasicElements/ErrorToast';

const FormInput = ({ type, placeholder, value, onChange }) => (
    <input
        type={type}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        required
        className="w-full bg-slate-700 text-slate-100 border-transparent rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder-slate-400"
    />
);

const RoleToggle = ({ role, setRole }) => (
    <div className="relative w-full p-1 bg-slate-700 rounded-xl flex">
        <span
            className="absolute top-1 left-1 w-[calc(50%-4px)] h-[calc(100%-8px)] bg-slate-600 rounded-lg transition-transform duration-300 ease-in-out"
            style={{ transform: role === 'Driver' ? 'translateX(100%)' : 'translateX(0)' }}
            aria-hidden="true"
        ></span>
        {['Rider', 'Driver'].map(r => (
            <div key={r} className="w-1/2">
                <input
                    type="radio"
                    id={r}
                    name="role-toggle"
                    value={r}
                    checked={role === r}
                    onChange={() => setRole(r)}
                    className="sr-only"
                />
                <label
                    htmlFor={r}
                    className="relative z-10 block w-full py-2.5 text-center text-sm font-semibold rounded-lg cursor-pointer transition-colors duration-300 text-slate-300 hover:text-white"
                >
                    {r}
                </label>
            </div>
        ))}
    </div>
);

// A self-contained spinner for use inside the button
const InButtonSpinner = () => (
    <svg className="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
        <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
        <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
    </svg>
);

const Signup = () => {
    const [userName, setUserName] = useState('');
    const [name, setName] = useState('');
    const [emailId, setEmailId] = useState('');
    const [role, setRole] = useState('Rider');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [loading, setLoading] = useState(false);

    const handleSignup = (e) => {
        e.preventDefault();
        setLoading(true);
        setErrorMessage('');

        const signUpReq = async () => {
            try {
                await axios.post('https://auth-service-1002278726079.us-central1.run.app/api/rs/public/signUp', {
                    userName,
                    password,
                    emailId,
                    name,
                    role
                });
                window.location.href = `./login?username=${userName}`;
            } catch (error) {
                console.error('Error fetching data:', error);
                setErrorMessage(error.response?.data?.body?.detail || 'An unknown error occurred.');
            } finally {
                setLoading(false);
            }
        };

        signUpReq();
    };

    return (
        <form onSubmit={handleSignup} className="space-y-6">
            <FormInput
                type="text"
                placeholder="Username"
                value={userName}
                onChange={(e) => setUserName(e.target.value)}
            />
            <FormInput
                type="text"
                placeholder="Full Name"
                value={name}
                onChange={(e) => setName(e.target.value)}
            />
            <FormInput
                type="email"
                placeholder="Email Address"
                value={emailId}
                onChange={(e) => setEmailId(e.target.value)}
            />
            
            <RoleToggle role={role} setRole={setRole} />

            <FormInput
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            
            <div>
                <button type="submit" disabled={loading} className="w-full mt-2 flex items-center justify-center text-white bg-blue-600 hover:bg-blue-700 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-3 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800 disabled:opacity-50 disabled:cursor-wait">
                    {loading ? <InButtonSpinner /> : 'Create Account'}
                </button>
            </div>

            {errorMessage && <ErrorToast message={errorMessage} />}
        </form>
    );
};

export default Signup;