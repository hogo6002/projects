        <form id="login-form" method = "POST" action = "<?php echo base_url();?>index.php/login/submit">
            <a class="close" <?php echo 'href="'.base_url().'index.php/home/"'?>>&times;</a>
            <h2>登陆</h2>
            <p class="ask">还没有账户？ <a <?php echo 'href="'.base_url().'index.php/register/"'?> class="register-button">注册</a></p>
            <button class="google-login"><img  src="<?php echo base_url("images/google.png")?>" width=5%> Sign in with Google</button>
            <div class="horizontal-line"><span>或者</span></div>
            <div class="form-value">
                <p>手机号：</p>
                <input type="number" name="phone" inputmode="numeric" class="email" required>
            </div>
            <div class="form-value">
                <p>密码：</p>
                <input type="password" name="password" id="pass0" placeholder="<?php if (isset($_COOKIE["password"])): echo $_COOKIE["password"]; endif ?>" value="<?php if (isset($_COOKIE["password"])): echo $_COOKIE["password"]; endif ?>" required>
                <!-- <a href="changePass.php">忘记密码了?</a> -->
                <input type="checkbox" name="remember" id = "remember" <?php if (isset($_COOKIE["email"])): echo "checked"; endif ?>>
                    <label for="remember">记住我</label>
             </div>
             <input type="submit" class="submit" id="login-btn">
             <p id="form-law">我已经阅读并接受迎凤汽车租赁的 <br>
             <a href="#">Terms of Service</a>  and <a href="#">Privacy Policy</a></p>
        </form>
