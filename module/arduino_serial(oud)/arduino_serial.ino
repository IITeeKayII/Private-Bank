#include <Keypad.h>
#include <SPI.h>
#include <MFRC522.h>

#define SS_Pin 10
#define RST_Pin 9

const byte ROWS = 4;
const byte COLS = 3;

char Keys [ROWS][COLS] = {
  {'1', '2', '3'},
  {'4', '5', '6'},
  {'7', '8', '9'},
  {'*', '0', '#'}
};
byte rowPins[ROWS] = {4, 3, 2, 5}; //connect to the row pins
byte colPins[COLS] = {8, 7, 6};

String keypadinput;
String cardinput;

Keypad keypad = Keypad( makeKeymap(Keys), rowPins, colPins, ROWS, COLS );

MFRC522 rfid(SS_Pin, RST_Pin); // Instance of the class

MFRC522::MIFARE_Key key;

int a = 0;

// Init array that will store new NUID
byte nuidPICC[4];

void setup() {
  Serial.begin(9600);
  SPI.begin(); // Init SPI bus
  rfid.PCD_Init(); // Init MFRC522

  for (byte i = 0; i < 6; i++) {
    key.keyByte[i] = 0xFF;
  }

  cardinput = "";
}

void loop() {

  if (cardinput.length() < 1) {
    getNUID();
  } else {

    char key = keypad.getKey();

    if (key != NO_KEY) {
      String keypressed = (String) key;
      Serial.print(keypressed);

      if (keypressed == "#") {
        cardinput = "";
      }

      if (keypressed == "*") {
        cardinput = "";
      }
    }
  }
}
void getNUID() {

  // Look for new cards
  if ( ! rfid.PICC_IsNewCardPresent()) {
    return;
  }

  // Verify if the NUID has been read
  if ( ! rfid.PICC_ReadCardSerial()) {
    return;
  }

  // Store NUID into nuidPICC array
  for (byte i = 0; i < 4; i++) {
    nuidPICC[i] = rfid.uid.uidByte[i];
  }


  printDec(rfid.uid.uidByte, rfid.uid.size);

  // Halt PICC
  rfid.PICC_HaltA();

  // Stop encryption on PCD
  rfid.PCD_StopCrypto1();
}



void printDec(byte * buffer, byte bufferSize) {
  for (byte i = 0; i < bufferSize; i++) {
    // Serial.print(buffer[i] < 0x10 ? " 0" : " ");
    Serial.print(buffer[i], DEC);
    cardinput += buffer[i];
  }
}
