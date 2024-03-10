
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

function App() {
    let Component = Home;
    switch (window.location.pathname) {
        case "/":
            Component = Home;
            break;
        case "/contact":
            Component = ContactUs;
            break;
        case "/aboutus":
            Component = AboutUs;
            break;
        case "/login":
            Component = AuthExtraction;
            break;
        case "/chat":
            Component = Chat;
            break;
        case "/user":
            Component = UserProfile;
            break;
        case "/create-ride":
            Component = CreateRideForm;
            break;
        default:
            Component = Home;
            break;
    }
    return <div>
        <NavBar />
        <Component />
    </div>;
}

export default App;
