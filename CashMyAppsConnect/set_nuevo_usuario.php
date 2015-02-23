<?php

/*
 * Script para insertar un nuevo usuario en la base de datos.
 */

// array para la respuesta JSON

$response = array();

// include db connect class
require_once __DIR__ . '/db_connect.php';


// connecting to db
$db = new DB_CONNECT();


try
{

$result = mysql_query("INSERT INTO TAB_USUARIOS(ID_USUARIO, NOMBRE, MAIL, SALDO, FECH_ALTA) VALUES ('".$_GET["ID_USUARIO"]."','".$_GET["NOMBRE"]."','".$_GET["MAIL"]."','".$_GET["SALDO"]."','".$_GET["FECHA_ALTA"]."')") or die(mysql_error());


 // success
    $response["success"] = 1;

// echoing JSON response
    echo json_encode($response);
    }
  
catch(PDOException $e)
{
  // no products found
    $response["success"] = 0;
    $response["message"] = "No se insertaron nuevas filas";
}

?>