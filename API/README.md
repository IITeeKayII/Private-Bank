# API

This API is meant to make it easier for all of the banks in the USSR to work together. An important part of this is a clear standard. To maintain this, we have developed a standard API.

## How to build for development purposes

### Create the database `clients`
```sql
CREATE TABLE `bankapi`.`clients` ( 
    `id` INT NOT NULL AUTO_INCREMENT , 
    `nuid` VARCHAR(8) NOT NULL , 
    `iban` VARCHAR(14) NOT NULL , 
    `name` VARCHAR(40) NOT NULL , 
    `saldo` INT NOT NULL , 
    `pin_attempts` INT NOT NULL , 
    `pin` VARCHAR(32) NOT NULL , 
    `date_created` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , 

    PRIMARY KEY (`id`), 
    UNIQUE (`nuid`), 
    UNIQUE (`iban`)
) 

ENGINE = InnoDB 
CHARSET=utf8 
COLLATE utf8_bin 
COMMENT = 'This table is meant to keep track of all the clients.';
```

### Create the database `transactions`

```sql
CREATE TABLE `bankapi`.`transactions` ( 
    `id` INT NOT NULL AUTO_INCREMENT , 
    `iban_sender` VARCHAR(14) NOT NULL , 
    `iban_recipient` VARCHAR(14) NOT NULL , 
    `amount` INT NOT NULL , 
    `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , 
    
    PRIMARY KEY (`id`)
) 

ENGINE = InnoDB 
CHARSET=utf8 
COLLATE utf8_bin 
COMMENT = 'This table is meant to keep track of all the transactions.';
```
