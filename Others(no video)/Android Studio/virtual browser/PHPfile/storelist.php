<?php
    require "conn.php";
    
    $mysql_qry = "select logo_url, storename, store_desc, store_location, weektime, weekendtime from Store ;";
    $result = mysqli_query($conn, $mysql_qry);
    $store_data = "";
    while($row = mysqli_fetch_row($result)){
        $store_data = $store_data . $row[0] . "*" . $row[1] . "*" . $row[2] . "*" . $row[3] . "*" . $row[4] . "*" . $row[5] .  "^";
    };
    $output = "storelist" . "^" . substr($store_data,0,strlen($store_data)-1); 
    if (mysqli_num_rows($result) > 0) {
        echo $output;
        // echo "storelist success";
    } else {
        echo "storelist not success";
    }

?>
