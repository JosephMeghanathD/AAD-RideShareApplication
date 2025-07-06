import React from "react";
import Login from "./Login";
import SignUp from "./SignUp";

const AuthToggle = ({ options, selectedOption, onOptionChange }) => {
  const [option2] = options;

  return (
    <div className="relative w-full p-1 bg-slate-700 rounded-xl flex">
      <span
        className="absolute top-1 left-1 w-[calc(50%-4px)] h-[calc(100%-8px)] bg-blue-600 rounded-lg transition-transform duration-300 ease-in-out"
        style={{
          transform:
            selectedOption === option2.value
              ? "translateX(100%)"
              : "translateX(0)",
        }}
        aria-hidden="true"
      ></span>

      {options.map((opt) => (
        <div key={opt.value} className="w-1/2">
          <input
            type="radio"
            id={opt.id}
            name="auth-toggle"
            value={opt.value}
            checked={selectedOption === opt.value}
            onChange={onOptionChange}
            className="sr-only"
          />
          <label
            htmlFor={opt.id}
            className={`relative z-10 block w-full py-2.5 text-center text-sm font-semibold rounded-lg cursor-pointer transition-colors duration-300 ${
              selectedOption === opt.value
                ? "text-white"
                : "text-slate-300 hover:text-white"
            }`}
          >
            {opt.label}
          </label>
        </div>
      ))}
    </div>
  );
};

export default function AuthExtraction() {
  const [selectedOption, setSelectedOption] = React.useState("login");

  const handleOptionChange = (event) => {
    setSelectedOption(event.target.value);
  };

  const toggleOptions = [
    { id: "login", value: "login", label: "Login" },
    { id: "signup", value: "signup", label: "Sign Up" },
  ];

  return (
    <div className="min-h-screen bg-slate-900 flex items-center justify-center p-4">
      <div className="w-full max-w-md bg-slate-800 rounded-2xl shadow-xl p-8 space-y-8">
        <AuthToggle
          options={toggleOptions}
          selectedOption={selectedOption}
          onOptionChange={handleOptionChange}
        />

        <div>{selectedOption === "login" ? <Login /> : <SignUp />}</div>
      </div>
    </div>
  );
}
