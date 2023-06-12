<?php 
// Conexión a la base de datos
$servername = "localhost";
$username = "id20885853_api";
$password = "Proyecto_moviles1";
$dbname = "id20885853_proyecto_moviles";

$conn = new mysqli($servername, $username, $password, $dbname);
if ($conn->connect_error) {
    die("Error de conexión: " . $conn->connect_error);
}
?>
