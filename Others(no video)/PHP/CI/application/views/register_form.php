<form id="register-form" method = "POST" action = "<?php echo base_url();?>register/register">
    <a class="close"<?php echo 'href="'.base_url().'home/"'?>>&times;</a>
                <h2>Sign Up</h2>
                <p class="ask">Already have an account? <a <?php echo 'href="'.base_url().'users/"'?> class="login-button">log in</a></p>
                <button class="google-login"><?php echo '<img src="'.base_url("./images/google.png").'" width=5%>'?> Sign in with Google</button>
                <div class="horizontal-line"><span>Or</span></div>
                <div class="form-value">
                    <p>First Name:</p>
                    <input type="text" name="firstname" id="firstname" required>
                </div>
                <div class="form-value">
                    <p>Last Name:</p>
                    <input type="text" name="lastname" id="lastname" required>
                </div>
                <div class="form-value">
                    <p>Email:</p>
                    <input type="text" name="username" class="email" required>
                    <p id="email-reminder">Please enter a valid email address</p>
                </div>
                <div class="form-value">
                    <p>Password:</p>
                    <input type="password" name="password" id="pass1" inputmode="numeric" required>
                    <p id="pass-reminder">Too short. Please enter at least 8 characters</p>
                 </div>
                 <input type="submit" class="submit" id="reg-btn">
                 <p class="form-law">By signing up, I accept BOSO’s <br>
                 <a href="#">Terms of Service</a>  and <a href="#">Privacy Policy</a></p>
            </form>
            <script src="<?php echo base_url('js/jquery-3.3.1.min.js');?>"></script>
        <script src="<?php echo base_url('js/script.js');?>"></script>