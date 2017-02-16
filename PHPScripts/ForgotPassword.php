<?php

$connect=mysqli_connect("mysql.hostinger.co.uk", "u163104628_viras", "", "u163104628_viras");

$email=$_POST["email"];

$response=array();
$response["success"]="false";



function sendMail($password){
global $email;

$from="admin@VIRASAT";
        $to=$email;
        $subject="VIRASAT Old Password";
        $message="
                This Is an auto generated email. 
                
                Your Old password is: $password

                For any queries contact us in this email.
                          

        ";
        $headers = 'From:admin@VIRASAT' . "\r\n";
        mail($to,$subject,$message,$headers);


}
$statement=mysqli_query($connect,"SELECT * FROM VIRASAT WHERE Email='$email'");
$count=mysqli_num_rows($statement);
if($count==1){
$result=mysqli_fetch_array($statement);
$password=$result["Password"];
sendMail($password);
$response["success"]="true";
}


echo json_encode($response);