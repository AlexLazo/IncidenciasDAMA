<?php 

$connection = mysqli_connect("localhost","id20885853_api","Proyecto_moviles1","id20885853_proyecto_moviles");
	
	$result = array();
	$result['usuario'] = array();
	//$selectt = "SELECT usuario.*, rol.Rol as id_rol FROM usuario JOIN rol ON usuario.id_rol = rol.id_rol";
	$select= "SELECT *from usuario";
	$responce = mysqli_query($connection,$select);
	
	while($row = mysqli_fetch_array($responce))
		{
			$index['id_usuario']      = $row['0'];
			$index['nombrecompleto']    = $row['1'];
			$index['user']   = $row['2'];
			$index['password'] = $row['3'];
			$index['id_rol'] = $row['4'];
			$index['id_estadoEmpleado'] = $row['5'];

			array_push($result['usuario'], $index);
		}
			
			$result["success"]="1";
			echo json_encode($result);
			mysqli_close($connection);

 ?>