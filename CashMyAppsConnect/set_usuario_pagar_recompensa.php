<?php

/*
 * Script para pagar la recompensa al usuario.
 */

// array para la respuesta JSON

$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';


// connecting to db
$db = new DB_CONNECT();


try
{

$result = mysql_query("UPDATE TAB_USUARIOS SET SALDO = SALDO + '".$_GET["GIFT"]."' WHERE MAIL='".$_GET["MAIL"]."'") or die(mysql_error());


 // success
    $response["success"] = 1;

// echoing JSON response
    echo json_encode($response);
    }
  
catch(PDOException $e)
{
  // no products found
    $response["success"] = 0;
    $response["message"] = "No se pudo actualizar el saldo";
}

?>