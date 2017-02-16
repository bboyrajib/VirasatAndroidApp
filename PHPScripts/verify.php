<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
 
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>VIRASAT > ACTIVATION</title>
</head>
<body>
    <!-- start header div --> 
    <div id="header">
        <h3>The Account has been successfully activated! *.*</h3>
    </div>
    <!-- end header div -->   
     
    <!-- start wrap div -->   
    <div id="wrap">
        <!-- start PHP code -->
        <?php
         
            $con = mysqli_connect("mysql.hostinger.co.uk", "u163104628_viras", "virasat2017", "u163104628_viras");
            $email=$_GET["email"];
            $hash=$_GET["token"];
            $statement1=mysqli_query($con,"SELECT * FROM VIRASAT WHERE email='$email'");
            $result=mysqli_fetch_array($statement1);
            $En_pass=$result["En_password"];
            $hashR=md5($En_pass);
            
            if($hash==$hashR){
            
            $statement=mysqli_query($con,"UPDATE VIRASAT SET isValidated=1 WHERE email='$email'");
            }
             
        ?>
        <!-- stop PHP Code -->
 
         
    </div>
    <!-- end wrap div --> 
</body>
</html>	