<?php
    require "conn.php";
     $user_name = $_POST["user_name"];
     $user_pass = $_POST["password"];
     $user_email = $_POST["email"];
     $user_address1 = $_POST["address1"];
     $user_address2 = $_POST["address2"];

//    $user_name = "12342";
//    $user_pass = "12345678";
//    $user_email = "12321@gmail.com";
//    $user_firstname = "holly11";
//    $user_lastname = "gong";

     $mysql_qry_username = "select *  from User where '$user_name' = username";
     $checkusername = mysqli_query($conn, $mysql_qry_username);
     $mysql_qry_email = "select * from User where '$user_email' = email";
     $checkuseremail = mysqli_query($conn, $mysql_qry_email);
    
    
    $mysql_qry_count = "SELECT count(*) as total from User;";
    $getUserAmount = mysqli_query($conn, $mysql_qry_count);
    $row = mysqli_fetch_row($getUserAmount);
    $user_id = $row[0] + 1;

    

     if (mysqli_num_rows($checkusername) > 0 || mysqli_num_rows($checkuseremail) > 0) {
         echo "already exist";
     } else{
        $sql = "INSERT INTO User (`user_id`, `username`, `password`, `first_name`, `last_name`, `email`, `dob`, `address line1`, `address line2`, `type`, `hash`) VALUES
        ('$user_id', '$user_name', '$user_pass', '$user_firstname', '$user_lastname','$user_email', '2019-09-01', '$user_address1', '$user_address2', NULL, NULL)";
        if(mysqli_query ($conn, $sql)){
            $mysql_qry = "select * from User where username = '$user_name' and password = '$user_pass';";
            $result = mysqli_query($conn, $mysql_qry);
            $row = mysqli_fetch_row($result);
            echo "1" . $row[1] . "," . $row[5] . "," . $row[7];
        };
    
     }
?>
