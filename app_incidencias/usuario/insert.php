<?php

$connection = mysqli_connect("localhost","id20885853_api","Proyecto_moviles1","id20885853_proyecto_moviles");
    
     $name = $_POST["nombrecompleto"];
     $user = $_POST["user"];
     $password = $_POST["password"];
     $id_rol = $_POST["id_rol"];
     $id_estadoEmpleado = $_POST["id_estadoEmpleado"];
     
     $sql = "INSERT INTO usuario(nombrecompleto,user,password,id_rol,id_estadoEmpleado) VALUES ('$name','$user','$password','$id_rol','$id_estadoEmpleado')";
     
     $result = mysqli_query($connection,$sql);
     $error_message = mysqli_error($connection);
     
     if($result){
         echo "Data Inserted";
        
     }
     else{
         echo "Failed".$error_message;
     }
     mysqli_close($connection);
     
          
    
    


?>