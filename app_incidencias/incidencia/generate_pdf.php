<?php

require('fpdf/fpdf.php'); 

$connection = mysqli_connect("localhost","id20885853_api","Proyecto_moviles1","id20885853_proyecto_moviles");
$query = "SELECT incidencia.id_incidencia,
                incidencia.descripcion,
                incidencia.fecha,
                incidencia.imagenReferencia,
                usuario.user,
                tipoincidencia.TipoIncidencia,
                estadoincidencia.EstadoIncidencia,
                estadoincidencia.id_EstadoIncidencia
            FROM incidencia
            INNER JOIN usuario ON incidencia.id_usuario = usuario.id_usuario
            INNER JOIN tipoincidencia ON incidencia.id_tipoIncidencia = tipoincidencia.id_tipIncidencia
            INNER JOIN estadoincidencia ON incidencia.estadoIncidencia = estadoincidencia.id_estadoIncidencia";

$result = mysqli_query($connection, $query);

$pdf = new FPDF('L'); 
$pdf->AddPage();

$pdf->SetFont('Arial', 'B', 16);
$pdf->Cell(0, 10, 'Reporte de incidentes', 0, 1, 'C');
$pdf->Ln(10);

$pdf->SetFont('Arial', 'B', 12);
$pdf->Cell(15, 10, 'ID', 1, 0, 'C');
$pdf->Cell(45, 10, 'Usuario', 1, 0, 'C');
$pdf->Cell(50, 10, 'Tipo de incidente', 1, 0, 'C');
$pdf->Cell(60, 10, 'Descripcion', 1, 0, 'C');
$pdf->Cell(30, 10, 'Fecha', 1, 0, 'C');
$pdf->Cell(30, 10, 'Estado', 1, 0, 'C');
$pdf->Cell(50, 10, 'Imagen', 1, 1, 'C');

$pdf->SetFont('Arial', '', 12);
while ($row = mysqli_fetch_assoc($result)) {
    $idIncidencia = $row['id_incidencia'];
    $user = $row['user'];
    $TipoIncidencia = $row['TipoIncidencia'];
    $descripcion = $row['descripcion'];
    $fecha = $row['fecha'];
    $imagenReferencia = $row['imagenReferencia'];
    $estadoIncidencia = $row['EstadoIncidencia'];

    $pdf->Cell(15, 50, $idIncidencia, 1, 0, 'C');
    $pdf->Cell(45, 50, $user, 1, 0, 'C');
    $pdf->Cell(50, 50, $TipoIncidencia, 1, 0, 'C');
    $pdf->Cell(60, 50, $descripcion, 1, 0, 'C');
    $pdf->Cell(30, 50, $fecha, 1, 0, 'C');
    $pdf->Cell(30, 50, $estadoIncidencia, 1, 0, 'C');
    $pdf->Image($imagenReferencia, $pdf->GetX(), $pdf->GetY(), 50, 50);

    $pdf->Ln();
}

$pdf->Output('incident_report.pdf', 'D');

mysqli_close($connection);
?>
