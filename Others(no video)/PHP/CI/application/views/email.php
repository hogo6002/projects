<?php
$to = "hogo6002@gmail.com";
$subject = "Test mail";
$message = "Hello! Testing!";
$from = "hogo6002@gmail.com";
$headers = "From:" .$from;
$mail_set = mail($to, $subject, $message, $headers);
echo $mail_set ? "Mail sent." : "Mail failed";
?>