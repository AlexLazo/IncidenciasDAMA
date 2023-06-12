<?php

    $connection = mysqli_connect("localhost","id20885853_api","Proyecto_moviles1","id20885853_proyecto_moviles");
    $query =

    ("SELECT incidencia.id_incidencia,
                incidencia.descripcion,
                incidencia.fecha,
                incidencia.imagenReferencia,
                usuario.user,
                tipoincidencia.TipoIncidencia,
                estadoincidencia.EstadoIncidencia
            FROM incidencia
            INNER JOIN usuario ON incidencia.id_usuario = usuario.id_usuario
            INNER JOIN tipoincidencia ON incidencia.id_tipoIncidencia = tipoincidencia.id_tipIncidencia
            INNER JOIN estadoincidencia ON incidencia.estadoIncidencia = estadoincidencia.id_estadoIncidencia");
    $resultado = mysqli_query($connection, $query);

    while ($row = mysqli_fetch_assoc($resultado)){
        $datos[] = array_map('utf8_encode',$row); 
    }
    mysqli_close($connection);
    $json_string = json_encode($datos, JSON_PRETTY_PRINT);

    echo $json_string;
?>