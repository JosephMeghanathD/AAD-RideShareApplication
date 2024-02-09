import "./NavBar.css"
export default function NavBar() {
    const handleLogout = (e) => {
        e.preventDefault();
        localStorage.removeItem('jwtToken');
        window.location.href = "./login";
      };
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
                    <a href="/login">{localStorage.getItem('jwtToken') === null ? <p>Login</p> : 
                    <form onSubmit={handleLogout}><button class='no-radius' type="submit">Logout</button></form>}</a>
                </li>
            </ul>
        </nav>
    ); 
}