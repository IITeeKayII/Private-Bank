<?php

header('Content-Type: application/json');

require_once 'client.php';

/*
To sanitize the input, we:

1. strip the whitespace
2. replace all html characters

After we have done that, we will check if all of the requirements are met.
This will be done for each specific case, so we can return specific error codes.
 */

$iban = str_replace(' ', '', htmlspecialchars($_POST['iban']));
$pin = str_replace(' ', '', htmlspecialchars($_POST['pin']));
$amount = str_replace(' ', '', htmlspecialchars($_POST['amount']));
if (isset($_POST['recipient'])) {
    $recipient = str_replace(' ', '', htmlspecialchars($_POST['recipient']));
}

$client = new Client($iban, $pin);

if (isset($recipient)) {
    $response = $client->transfer($amount, $recipient);
} else {
    $response = $client->transfer($amount);
}

if ($response >= 0) {
    echo json_encode(array('saldo' => $response));
} else if ($response == -1) {
    echo json_encode(array('error' => 'The recipient does not exist.'));
} else if ($response == -2) {
    echo json_encode(array('error' => 'You do not have enough balance.'));
}

?>