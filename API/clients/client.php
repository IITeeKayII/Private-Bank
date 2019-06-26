<?php
class Client
{
    /* Details that every client should have */
    private $iban, $pin, $conn;
    /* Set the default details */
    public function __construct($iban, $pin)
    {
        /* Set the iban and PIN of the client globally */
        $this->iban = $iban;
        /* Hash the pin to match the database */
        $this->pin = $pin;
        /* Include connection details */
        include_once '../conn.php';
        $this->conn = $conn;
    }
    /* Can also be used by external clients */
    public function checkLogin(): int
    {
        /* This function can return 3 different integers:
        0. The user credentials are OK
        1. The user credentials are wrong
        2. The user has entered his PIN wrong 3 times
         */
        /* Get the bank code from the pass */
        $bankcode = strtolower(substr($this->iban, 4, 4));
        $countrycode = strtolower(substr($this->iban,0,2));
        if ($bankcode == "uvvu") {
            /* Create a query to select all of the necessary data from the database */
            $sql = "SELECT saldo, pin, pin_attempts FROM clients WHERE iban = '$this->iban'";
            /* Store the result of that query in an object */
            $result = $this->conn->query($sql);
            $obj = $result->fetch_object();
            if ($obj->pin_attempts < 3) {
                if ($obj->pin == md5($this->pin)) {
                    /* Reset the amount of login attempts */
                    $sql = "UPDATE clients SET pin_attempts = 0 WHERE iban='$this->iban'";
                    $this->conn->query($sql);
                    $response = 0;
                } else {
                    /* Update the amount of login attempts */
                    $sql = "UPDATE clients SET pin_attempts = pin_attempts + 1 WHERE iban='$this->iban'";
                    $this->conn->query($sql);
                    $response = 1;
                }
            } else {
                $response = 2;
            }
        } else {
            /* Set up the GET request */
            $url = 'http://185.224.89.242:8080/test?func=pinCheck&iban=' . $this->iban . '&revBank=' .$countrycode . $bankcode . '&senBank=suuvvu&pin=' . $this->pin;

            $result = file_get_contents($url);
            if ($result === false) {
                die('Error');
            }

            $result = json_decode($result);

            if ($result->status == true) {
                $response = 0;
            } else {
                $response = 1;
            }
        }
        return $response;
    }
    public function checkSaldo()
    {
        if ($this->checkLogin() == 0) {
            /* Create a query to select all of the necessary data from the database */
            $sql = "SELECT saldo FROM clients WHERE iban = '$this->iban'";
            /* Store the result of that query in an object */
            $result = $this->conn->query($sql);
            $obj = $result->fetch_object();
            $response = $obj->saldo;
        } else {
            /* It is the responsibility of the bank to check whether the credentials are correct */
            $response = null;
        }
        return $response;
    }
    /* Can also be used by external clients */
    public function transfer($amount, $recipient = "SU38UVVU988557"): int
    {
        /* Get the bank code from the pass */
        $bankcode = strtolower(substr($this->iban, 4, 4));
        if ($bankcode == "uvvu") {
            $saldo = $this->checkSaldo();
            if ($amount <= $saldo) {
                /* Select the recipient */
                $sql = "SELECT iban FROM clients WHERE iban = '$recipient'";
                $result = $this->conn->query($sql);
                $obj = $result->fetch_object();
                if ($obj != null) {
                    $newSaldo = $saldo - $amount;
                    /* Set the balance of the sender */
                    $sql = "UPDATE clients SET saldo = '$newSaldo' WHERE iban = '$this->iban'";
                    $this->conn->query($sql);
                    /* Select the IBAN of the sender */
                    $sql = "SELECT iban FROM clients WHERE iban = '$this->iban'";
                    $result = $this->conn->query($sql);
                    $obj = $result->fetch_object();
                    /* Insert the transaction */
                    $sql = "INSERT INTO transactions (iban_sender, iban_recipient, amount) VALUES ('$obj->iban', '$recipient', '$amount')";
                    $this->conn->query($sql);
                    $response = $newSaldo;
                    /* Set the balance of the recipient */
                    $sql = "UPDATE clients SET saldo = saldo + '$amount' WHERE iban = '$recipient'";
                    $this->conn->query($sql);
                } else {
                    $response = -1;
                }
            } else {
                $response = -2;
            }
        } else {
            /* Set up the GET request */
            $url = 'http://185.224.89.242:8080/test?func=withdraw&iban=' . $this->iban . '&revBank=' . $countrycode . $bankcode . '&senBank=suuvvu&pin=' . $this->pin . '&amount=' . $amount;

            $result = file_get_contents($url);
            if ($result === false) {
                die('Error');
            }

            $result = json_decode($result);

            if ($result->status == true) {
                $response = 0;
            } else {
                $response = 1;
            }
        }
        return $response;
    }
}