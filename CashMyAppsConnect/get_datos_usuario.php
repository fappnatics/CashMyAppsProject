?php

/*
 * Following code will list all the products
 */

// array for JSON response
$response = array();

// include db connect class
require_once __DIR__ . '/db_connect.php';


// connecting to db
$db = new DB_CONNECT();

// get all products from products table
$result = mysql_query("SELECT *FROM TAB_USUARIOS") or die(mysql_error());

// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // products node
    $response["usuarios"] = array();

    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $product = array();
        $product["ID_USUARIO"] = $row["ID_USUARIO"];
        $product["NOMBRE"] = $row["NOMBRE"];
        $product["MAIL"] = $row["MAIL"];
        $product["FECH_ALTA"] = $row["FECH_ALTA"];
        // push single product into final response array
        array_push($response["usuarios"], $product);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No users found";

    // echo no users JSON
    echo json_encode($response);
}
?>