import "./App.css";
import NavBar from "./TopBarPages/NavBar";
import "./styles.css";
import Home from "./TopBarPages/HomePages/Home";
import AboutUs from "./TopBarPages/AboutUs";
import ContactUs from "./TopBarPages/ContactUs";
import AuthExtraction from "./TopBarPages/AuthPages/AuthExraction";
import Chat from "./TopBarPages/Chat/Chat";
import UserProfile from "./TopBarPages/Profile/UserProfile";
import CreateRideForm from "./TopBarPages/HomePages/RidesData/CreateRide/CreateRide";

import { Routes, Route } from "react-router-dom";

function App() {
  return (
    <div className="flex flex-col h-screen bg-gray-900">
      <NavBar />
      <main className="flex-grow overflow-y-auto">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/home" element={<Home />} />
          <Route path="/contact" element={<ContactUs />} />
          <Route path="/aboutus" element={<AboutUs />} />
          <Route path="/login" element={<AuthExtraction />} />
          <Route path="/chat" element={<Chat />} />
          <Route path="/user" element={<UserProfile />} />
          <Route path="/create-ride" element={<CreateRideForm />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;