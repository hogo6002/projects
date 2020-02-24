<form id="register-form" method = "POST" >
                <a class="close" href="#">&times;</a>
                <h2>Sign Up</h2>
                <p class="ask">Already have an account? <a href="#" class="login-button">log in</a></p>
                <button class="google-login"><img src="./images/google.png" width=5%> Sign in with Google</button>
                <div class="horizontal-line"><span>Or</span></div>
                <div class="form-value">
                    <p>First Name:</p>
                    <input type="text" name="firstname" id="firstname">
                </div>
                <div class="form-value">
                    <p>Last Name:</p>
                    <input type="text" name="lastname" id="lastname">
                </div>
                <div class="form-value">
                    <p>Email:</p>
                    <input type="text" name="username" class="email">
                    <p id="email-reminder">Please enter a valid email address</p>
                </div>
                <div class="form-value">
                    <p>Password:</p>
                    <input type="password" name="password" id="pass1" inputmode="numeric">
                    <p id="pass-reminder">Too short. Please enter at least 8 characters</p>
                 </div>
                 <input type="submit" class="submit" id="reg-btn">
                 <p class="form-law">By signing up, I accept BOSO’s <br>
                 <a href="#">Terms of Service</a>  and <a href="#">Privacy Policy</a></p>
            </form>