<?php
$connect=mysqli_connect("mysql.hostinger.co.uk", "u163104628_viras", "", "u163104628_viras");

$name=$_POST["name"];
$phone=$_POST["phone"];
$college=$_POST["college"];
$email=$_POST["email"];
$password=$_POST["password"];
$sex=$_POST["sex"];
$en_password=md5($password);
$hash=md5($en_password);

$response=array();
$response["success"]=false;
$response["fields"]=$name;

function registerUser() {
global $connect,$name,$phone,$college,$password,$email,$sex,$en_password;
$statement=mysqli_query($connect, "INSERT INTO `VIRASAT`(`Name`, `Phone`, `College`, `Password`, `Email`, `Sex`, `En_password`, `isValidated`) VALUES ('$name','$phone','$college','$password','$email','$sex','$en_password','0')");
}

function isEmailAvailable(){
	global $connect,$email;
        $statement=mysqli_query($connect,"SELECT * FROM VIRASAT WHERE email='$email'");
	$count=mysqli_num_rows($statement);
   	if($count==0)
		return true;
	else
		return false;
    }
function sendMail(){
  global $connect,$email,$hash;
        $from="admin@VIRASAT";
        $to=$email;
        $subject="VIRASAT Email Verification";
        $message="
                This Is an auto generated email. Please Click on the Link below to activate your account:
                http://virasat.pe.hu/verify.php?email=$email&token=$hash

                For any queries contact us in this email.
                          

        ";
        $headers = 'From:admin@VIRASAT' . "\r\n";
        mail($to,$subject,$message,$headers);

}
if(isEmailAvailable()){
registerUser();
sendMail();
$response["success"]=true;
}


echo json_encode($response);

?>