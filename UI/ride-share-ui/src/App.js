
import "./App.css";
import NavBar from "./TopBarPages/NavBar";
import "./styles.css"; 
import Home from "./HomePages/Home";
import AboutUs from "./TopBarPages/AboutUs";
import ContactUs from "./TopBarPages/ContactUs";
import AuthExtraction from "./AuthPages/AuthExraction";

function App() {
    let Component = Home;
    switch(window.location.pathname) {
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
        default:
            Component = Home;
            break;
    }
    return <div>
        <NavBar/>
        <Component/>
    </div>;
}

export default App;
