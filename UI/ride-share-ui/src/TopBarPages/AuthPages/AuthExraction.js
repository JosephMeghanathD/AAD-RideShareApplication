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
                <input id="contrasts-on"
                    type="radio"
                    name="contrasts"
                    value="login"
                    checked={selectedOption === 'login'}
                    onChange={handleOptionChange}
                    defaultChecked
                />
                <label for="contrasts-on" >Login</label>
                <input id="contrasts-off"
                    type="radio"
                    name="contrasts" ß
                    value="signup"
                    checked={selectedOption === 'signup'}
                    onChange={handleOptionChange}
                />
                <label for="contrasts-off" class="off-label">SignUp</label>
                <span class="selected" aria-hidden="true"></span>
            </div>
            {selectedOption === 'login' && <Login />}
            {selectedOption === 'signup' && <SignUp />}
        </div>
    );
};