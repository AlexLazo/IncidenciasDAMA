<?php
$connection = mysqli_connect("localhost","id20885853_api","Proyecto_moviles1","id20885853_proyecto_moviles");
    
     $id = $_POST["id_usuario"];
     $nombre = $_POST["nombrecompleto"];
     $user = $_POST["user"];
     $password = $_POST["password"];
     $rol= $_POST["id_rol"];
     $estado = $_POST["id_estadoEmpleado"];
     
     $sql = "UPDATE usuario SET nombrecompleto = '$nombre', user = '$user', password = '$password', id_rol = '$rol', id_estadoEmpleado = '$estado' WHERE id_usuario = '$id'";
     
     $result = mysqli_query($connection,$sql);
     $error_message = mysqli_error($connection);
     
     if($result){
         echo "Datos Actualizados";
        
     }
     else{
         echo "Failed!!".$error_message;
     }
     mysqli_close($connection);
     
?>