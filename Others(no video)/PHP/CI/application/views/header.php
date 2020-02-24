<!DOCTYPE html>
    <head>
        <meta charset="utf-8">
        <title>Buy one Or Sell one</title>
        <link rel="stylesheet" href="<?php echo base_url('css/style.css');?>">
        <link href="https://fonts.googleapis.com/css?family=Lato" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Domine:700|Inknut+Antiqua:500|Playfair+Display+SC:700" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    </head>
    <body>
        <div id="cover"></div>
        <header>
            <a id="logo" href="<?php echo base_url();?>home"><h1>Buy or Sell</h1></a>
            
            <form class="search-bar" action = "<?php echo base_url();?>search" method = "get" autocomplete="off">
                 <input id = "search" name = "search" class = "search-box" autocomplete="off">
                 <ul id = "searchDropDown" role="menu" aria-labelledby="dropdownMenu">
                 </ul>
                 
</form>

           
            <ul id="header-right">
				<li><?php 
            if (isset($_SESSION["username"])) {
                echo '<a class="btn btn-outline-primary" href="' . base_url() .'users/logout"> Sign out</a>';
            } else {
                echo '<a class="login-button" href="'.base_url().'users/">Login</a> | <a class="register-button" href="'.base_url().'register/">Register</a>';
            }
        ?></li>
                <?php echo '<li><a class="login-button" href="'.base_url().'wishlist/">wishlist</a></li>'; ?>
                <li><a class="login-button" <?php echo 'href="'.base_url().'profile"'?>>profile</a></li>
            </ul>
        </header>