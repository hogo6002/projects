<form id="login-form" method = "POST" action="">
<a class="close" href="#">&times;</a>
            <h2>Login</h2>
            <p class="ask">Don't have an account? <a href="#" class="register-button">sign up</a></p>
            <button class="google-login"><img src="./images/google.png" width=5%> Sign in with Google</button>
            <div class="horizontal-line"><span>Or</span></div>
            <div class="form-value">
                <p>Email:</p>
                <input type="text" name="username" class="email" placeholder="" value="<?php if (isset($_COOKIE["username"])): echo $_COOKIE["username"]; endif ?>" required>
            </div>
            <div class="form-value">
                <p>Password:</p>
                <input type="password" name="password" id="pass0" placeholder="<?php if (isset($_COOKIE["password"])): echo $_COOKIE["password"]; endif ?>" value="<?php if (isset($_COOKIE["password"])): echo $_COOKIE["password"]; endif ?>" required>
                <a href="changePass.php">Forgot Password?</a>
                <input type="checkbox" name="remember" id = "remember" <?php if (isset($_COOKIE["email"])): echo "checked"; endif ?>>
                    <label for="remember">Remember my email</label>
             </div>
             <input type="submit" class="submit" id="login-btn">
             <p id="form-law">By signing in, I accept BOSO’s <br>
             <a href="#">Terms of Service</a>  and <a href="#">Privacy Policy</a></p>
        </form>