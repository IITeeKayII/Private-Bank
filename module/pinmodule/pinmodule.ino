/* This code is used to write a bank number to a RFID card, on a specified block.

   Created by   Rudy Schlaf for www.makecourse.com
   DATE:    2/2014
   Editted by : Coen Schutte
*/

#include <SPI.h>
#include <MFRC522.h>
#include <Keypad.h>

#define SS_PIN 10
#define RST_PIN 9
MFRC522 mfrc522(SS_PIN, RST_PIN);
MFRC522::MIFARE_Key key;

const byte ROWS = 4; //four rows
const byte COLS = 4; //four columns
char keys[ROWS][COLS] = {
  {'1', '2', '3', 'A'},
  {'4', '5', '6', 'B'},
  {'7', '8', '9', 'C'},
  {'*', '0', '#', 'D'}
};
byte rowPins[ROWS] = {6, 7, 8, 9}; //connect to the row pinouts of the keypad
byte colPins[COLS] = {2, 3, 4, 5}; //connect to the column pinouts of the keypad

String keypadinput;
String content;
bool check = false;

Keypad keypad = Keypad( makeKeymap(keys), rowPins, colPins, ROWS, COLS );


// Change te block where the bank number is stored.
int block = 1;

byte readBackBlock[14];

void setup() {
  Serial.begin(9600);
  SPI.begin();
  mfrc522.PCD_Init();

  for (byte i = 0; i < 6; i++) {
    key.keyByte[i] = 0xFF;
  }

  content = "";

}

void loop() {

  if (check == false) {
    char key1 = keypad.getKey();

    if (key1 != NO_KEY) {
      String keypressed = (String) key1;
      Serial.print(keypressed);

      if (keypressed == "A") {
        content = "";
        check = false;

      }

    }

    if (!mfrc522.PICC_IsNewCardPresent()) {
      return;
    }

    if (!mfrc522.PICC_ReadCardSerial()) {
      return;
    }

    // Set authentication key
    for (byte index = 0; index < 6; index++) {
      key.keyByte[index] = 0xFF;
    }


    // Read the block
    readBlock(block, readBackBlock);


    String content = "";
    for (int index = 0; index < sizeof(readBackBlock); index++) {
      content += char(readBackBlock[index]);
    }
    Serial.print(content);

    // End authentication
    mfrc522.PICC_HaltA();
    mfrc522.PCD_StopCrypto1();

    check = true;

  } else {
    char key = keypad.getKey();

    if (key != NO_KEY) {
      String keypressed = (String) key;
      Serial.print(keypressed);

      if (keypressed == "*") {
        content = "";
        check = false;
      }

      if (keypressed == "A") {
        content = "";
        check = false;
      }

    }
  }
}

void readBlock(int blockNumber, byte arrayAddress[]) {
  authenticateBlockAction(block);

  byte buffersize = 18;

  byte status = mfrc522.MIFARE_Read(blockNumber, arrayAddress, &buffersize); \
  if (status != MFRC522::STATUS_OK) {
    Serial.println("Read failed");
    return;
  }
}

void authenticateBlockAction(int blockNumber) {
  int largestModulo4Number = blockNumber / 4 * 4;
  int trailerBlock = largestModulo4Number + 3;

  byte status = mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, trailerBlock, &key, &(mfrc522.uid));
  if (status != MFRC522::STATUS_OK) {
    Serial.println("Authentication failed");
    return;
  }
}
