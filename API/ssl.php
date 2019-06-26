<?php

header('Content-Type: application/json');

$response = isset($_SERVER['HTTPS']);

echo json_encode(array('status' => $response));

?>
