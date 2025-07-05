import "./NavBar.css";
import { Link } from "react-router-dom";

export default function NavBar() {
  return (
    <nav className="nav">
      <Link to="/" className="site-title">RideShare</Link>
      <ul>
        <li>
          <Link to="/contact">Contact Us</Link>
        </li>
        <li>
          <Link to="/aboutus">About Us</Link>
        </li>
        <li>
          <Link to={localStorage.getItem('jwtToken') === null ? "/login" : "/user"}>
            {localStorage.getItem('jwtToken') === null ? "Login" : "Profile"}
          </Link>
        </li>
      </ul>
    </nav>
  );
}