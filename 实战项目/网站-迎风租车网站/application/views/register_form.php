            <form id="register-form" method = "POST" action = "<?php echo base_url();?>index.php/register/submit">
                <a class="close" <?php echo 'href="'.base_url().'index.php/home/"'?>>&times;</a>
                <h2>注册</h2>
                <p class="ask">已经有账号了? <a <?php echo 'href="'.base_url().'index.php/login/"'?> class="login-button">登陆</a></p>
                <button class="google-login"><img src="<?php echo base_url("images/google.png")?>" width=5%> Sign in with Google</button>
                <div class="horizontal-line"><span>Or</span></div>
                <div class="form-value">
                    <p>姓名：</p>
                    <input type="text" name="name" id="firstname">
                </div>
                <div class="form-value">
                    <p>微信号：</p>
                    <input type="text" name="wechatId" id="lastname">
                </div>
                <div class="form-value">
                    <p>手机号：</p>
                    <input type="number" inputmode="numeric" name="phone" class="email">
                    <p id="email-reminder">请输入正确的手机号</p>
                </div>
                <div class="form-value">
                    <p>密码：</p>
                    <input type="password" name="password" id="pass1">
                    <p id="pass-reminder">密码太短了，请输入八个或以上字符</p>
                 </div>
                 <input type="submit" class="submit" id="reg-btn">
                 <p class="form-law">我已经阅读并接受迎凤汽车租赁的 <br>
                 <a href="#">Terms of Service</a>  and <a href="#">Privacy Policy</a></p>
            </form>
