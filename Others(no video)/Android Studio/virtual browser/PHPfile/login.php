<?php
    require "conn.php";
//    $user_name = "holly";
//    $user_pass = "12345678";
    $user_name = $_POST["user_name"];
    $user_pass = $_POST["password"];
//    echo $user_name;
    $mysql_qry = "select * from User where username = '$user_name' and password = '$user_pass';";
    $result = mysqli_query($conn, $mysql_qry);
    
    if (mysqli_num_rows($result) > 0) {
        $row = mysqli_fetch_row($result);
        echo "1" . $row[1] . "," . $row[5] . "," . $row[7];
//        echo "login success";
    } else {
        echo "login not success";
    }
?>
