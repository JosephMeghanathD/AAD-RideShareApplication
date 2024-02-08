import "./authPages.css"
import Login from "./Login";
import SignUp from "./SignUp";
let AuthDisplay = Login;

function changeDisplay(e) {
    console.log(e)
    switch (e) {
        case "login":
            AuthDisplay = Login;
            break;
        case "signup":
            AuthDisplay = SignUp;
            break;
        default:
            AuthDisplay = Login;

    }
    
}
const AuthExtraction = () => {


    return (
        <div class="center-screen">
            <form action="">
                <div class="radio-block">
                    <input type="radio" name="contrasts" id="contrasts-on" checked="checked" onChange={e=>changeDisplay("login")} />
                    <label for="contrasts-on" >Login</label>
                    <input type="radio" name="contrasts" id="contrasts-off" onChange={e=>changeDisplay("signup")}  ></input>
                    <label for="contrasts-off" class="off-label" checked="">Signup</label>
                    <span class="selected" aria-hidden="true"></span>
                </div>
            </form>
            <AuthDisplay/>
        </div>
    );
};
export default AuthExtraction;
