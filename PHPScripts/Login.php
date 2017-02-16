<?php
$connect=mysqli_connect("mysql.hostinger.co.uk", "u163104628_viras", "", "u163104628_viras");


$email=$_POST["email"];
$password=$_POST["password"];
$en_password=md5($password);

$response=array();
$response["success"]="false";
$response["name"]="null";




	
        $statement=mysqli_query($connect,"SELECT * FROM VIRASAT WHERE email='$email'");
        $count=mysqli_num_rows($statement);
        
        if($count==1){
        $result=mysqli_fetch_array($statement);
	
        if($result["isValidated"]==0){
              $response["success"]="not validated";

         }
         else{
   	 if($result["En_password"]==$en_password){
		$response["success"]=true;
                $response["name"]=$result["Name"];
        
	}

}
}

        



echo json_encode($response);


?>				