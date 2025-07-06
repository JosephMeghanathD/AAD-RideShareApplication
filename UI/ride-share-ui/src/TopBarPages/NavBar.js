import { Link } from "react-router-dom";

export default function NavBar() {
  return (
    <nav className="bg-gray-900 border-b border-gray-700 w-full">
      <div className="container mx-auto px-6 py-3 flex justify-between items-center">
        <Link
          to="/"
          className="text-2xl font-bold text-white hover:text-blue-500 transition-colors"
        >
          RideShare
        </Link>

        <ul className="flex items-center list-none gap-6">
          <li>
            <Link
              to="/contact"
              className="text-gray-300 hover:text-blue-500 transition-colors"
            >
              Contact Us
            </Link>
          </li>
          <li>
            <Link
              to="/aboutus"
              className="text-gray-300 hover:text-blue-500 transition-colors"
            >
              About Us
            </Link>
          </li>
          <li>
            <Link
              to={
                localStorage.getItem("jwtToken") === null ? "/login" : "/user"
              }
              className="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded-full transition-all duration-300"
            >
              {localStorage.getItem("jwtToken") === null ? "Login" : "Profile"}
            </Link>
          </li>
        </ul>
      </div>
    </nav>
  );
}
