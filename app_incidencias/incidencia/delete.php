<?php

$connection = mysqli_connect("localhost","id20885853_api","Proyecto_moviles1","id20885853_proyecto_moviles");
    
     $id = $_POST["id_incidencia"];
     
     $sql = "DELETE FROM incidencia WHERE id_incidencia='$id'";
     
     $result = mysqli_query($connection,$sql);
     
     if($result){
         echo "Data Deleted";
        
     }
     else{
         echo "Failed";
     }
     mysqli_close($connection);

?>