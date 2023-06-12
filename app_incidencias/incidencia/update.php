<?php
$connection = mysqli_connect("localhost","id20885853_api","Proyecto_moviles1","id20885853_proyecto_moviles");
    
     $id_incidencia = $_POST["id_incidencia"];
     $descripcion = $_POST["descripcion"];
     $user = $_POST["id_usuario"];
     $tipo= $_POST["id_tipoIncidencia"];
     $estado = $_POST["id_estadoIncidencia"];
     
     $sql = "UPDATE incidencia SET descripcion = '$descripcion', id_usuario = '$user', id_tipoIncidencia = '$tipo', 
     id_estadoIncidencia = '$estado' WHERE id_incidencia = '$id_incidencia'";
    
     $result = mysqli_query($connection,$sql);
     $error_message = mysqli_error($connection);
     
     if($result){
         echo "Datos actualziados";
        
     }
     else{
         echo "Falló";
     }
     mysqli_close($connection);
     
        
?>