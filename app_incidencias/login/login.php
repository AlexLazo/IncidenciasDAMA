<?php
include ("config.php");

    $user = $_POST["user"];
    $password=$_POST["password"];

    $sql="SELECT * FROM usuario WHERE user='$user' AND password='$password' ";
    $result=$conn->query($sql);

    if($result === false){
        die("Error en la consulta".$conn->error);
        return;
    }

    if($result->num_rows>0){
        $row = $result->fetch_assoc();
        $rol=$row["id_rol"];
        $idUser=$row["id_rol"];

        //RESPUESTA DE EXITO

        $response["status"]="Exitoso";
        $response["rol"]=$rol;
        $response["idusuario"]=$idUser;
        echo json_encode($response);
    }else{

        //RESPUESTA DE ERROR 
        $response["status"]="error";
        $response["message"]="Credenciales incorrectas";
        echo json_encode($response);
        
    }
 
?>