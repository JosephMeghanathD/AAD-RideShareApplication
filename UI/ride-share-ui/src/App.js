
import "./App.css";
import NavBar from "./NavBar";
import "./styles.css"; 
import Home from "./Home";
import AboutUs from "./AboutUs";
import ContactUs from "./ContactUs";

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
        default:
            Component = Home;
    }
    return <div>
        <NavBar/>
        <Component/>
    </div>;
}

export default App;
