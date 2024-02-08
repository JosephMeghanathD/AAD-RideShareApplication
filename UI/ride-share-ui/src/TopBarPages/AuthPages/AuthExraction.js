import React from "react";
import "./authPages.css"
import Login from "./Login";
import SignUp from "./SignUp";


export default function AuthExtraction() {
    const [selectedOption, setSelectedOption] = React.useState('login');
    const handleOptionChange = (event) => {
        setSelectedOption(event.target.value);
    };

    return (
        <div class="center-screen">
            <div class="radio-block">
                <input id="login"
                    type="radio"
                    name="contrasts"
                    value="login"
                    checked={selectedOption === 'login'}
                    onChange={handleOptionChange}
                    defaultChecked
                />
                <label for="login" >Login</label>
                <input id="signup"
                    type="radio"
                    name="contrasts" 
                    value="signup"
                    checked={selectedOption === 'signup'}
                    onChange={handleOptionChange}
                />
                <label for="signup" class="off-label">SignUp</label>
                <span class="selected" aria-hidden="true"></span>
            </div>
            {selectedOption === 'login' && <Login />}
            {selectedOption === 'signup' && <SignUp />}
        </div>
    );
};