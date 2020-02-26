<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no"> 
		<title>秀山迎凤租车</title>
		<link rel="stylesheet" href="<?php echo base_url('css/style.css');?>"/>
	</head>
	<body>
		<div id="cover"></div>
		<header>
			<figure id="logo-figure">
				<h1>迎凤租车</h1>
				<p id="nav-btn" class="show-phone">==</p>
			</figure>
			<nav id="nav-pc">
				<ul>
					<li><a href="<?php echo base_url("index.php/home/")?>">首页</a></li>
					<li><a href="<?php echo base_url("index.php/car/")?>">所有车型</a></li>
					<li><a href="#">常见问题</a></li>
					<li><a href="<?php echo base_url("index.php/contact/")?>">联系我们</a></li>
				</ul>
				<p>
					<?php 
					if(!isset($_SESSION["phone"])) {
                    ?>
					<a class="login-button" href="<?php echo base_url("index.php/login/")?>">登陆</a> | <a class="register-button" href="<?php echo base_url("index.php/register/")?>">注册</a>
                    <?php
					} else {
						echo '<a href="'. base_url("index.php/profile/") .'">个人中心</a> | <a href="' . base_url() .'index.php/login/logout">登出</a>';
					}
					?>
				</p>
			</nav>

			<nav> 
				<ul>
					<li><a href="<?php echo base_url("index.php/home/")?>">首页</a></li>
					<li><a href="<?php echo base_url("index.php/car/")?>">所有车型</a></li>
					<li><a href="#">常见问题</a></li>
					<li><a href="<?php echo base_url("index.php/contact/")?>">联系我们</a></li>
				</ul>
				<p>
					<?php 
					if(!isset($_SESSION["phone"])) {
                    ?>
					<a class="login-button" href="<?php echo base_url("index.php/login/")?>">登陆</a> | <a class="register-button" href="<?php echo base_url("index.php/register/")?>">注册</a>
                    <?php
					} else {
						echo '<a href="'. base_url("index.php/profile/") .'">个人中心</a> | <a href="' . base_url() .'index.php/login/logout">登出</a>';
					}
					?>
				</p>
			</nav>
		</header>
