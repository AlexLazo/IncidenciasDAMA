<?php 

$connection = mysqli_connect("localhost","id20885853_api","Proyecto_moviles1","id20885853_proyecto_moviles");
	
	$result = array();
	$result['estadoincidencia'] = array();
	$select= "SELECT *from estadoincidencia";
	$responce = mysqli_query($connection,$select);
	
	while($row = mysqli_fetch_array($responce))
		{
			$index['id_estadoIncidencia'] = $row['0'];
			$index['EstadoIncidencia'] = $row['1'];

			array_push($result['estadoincidencia'], $index);
		}
			
			$result["success"]="1";
			echo json_encode($result);
			mysqli_close($connection);

 ?>