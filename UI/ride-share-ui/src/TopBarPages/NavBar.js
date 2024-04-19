import "./NavBar.css"
export default function NavBar() {

    return (
        <nav className="nav">
            <a href="/" className="site-title"> RideShare</a>
            <ul>
                <li>
                    <a href="/contact">Contact Us</a>
                </li>
                <li>
                    <a href="/aboutus">About Us</a>
                </li>
                <li>
                    <a href={localStorage.getItem('jwtToken') === null ? "/login" : "/user"}><p>{localStorage.getItem('jwtToken') === null ? "Login" : "Profile"}</p></a>
                </li>
            </ul>
        </nav>
    ); 
}