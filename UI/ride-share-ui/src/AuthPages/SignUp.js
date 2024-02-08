import "./authPages.css"
const SignUp = () => {
	return (
		<div>
			<h1>Sign up</h1>
            <div>
                <p>User Id</p>
                <input type="text" id="fname" name="fname"></input>
            </div>
            <div>
                <p>Name</p>
                <input type="text" id="fname" name="fname"></input>
            </div>
            <div>
                <p>Email-Id</p>
                <input type="text" id="fname" name="fname"></input>
            </div>
            <div>
                <p>Password</p>
                <input type="password" id="fname" name="fname"></input>
            </div>
		</div>
	);
};

export default SignUp;
