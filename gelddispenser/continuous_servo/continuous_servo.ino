#include <Servo.h>
#include "Adafruit_Thermal.h"
#include "SoftwareSerial.h"

#define TX_PIN 6
#define RX_PIN 5
#define IR_M 2
#define IR_L 4
#define IR_R 3

SoftwareSerial mySerial(RX_PIN, TX_PIN);
Adafruit_Thermal printer(&mySerial); 

Servo myservoL;
Servo myservoM;
Servo myservoR;

int incomingByte = 0;

char inputBuffer[200];
String input;
int inputInt;
boolean flag = false;
boolean peer = false;

void setup() {
  mySerial.begin(9600);
  Serial.begin(9600);
  printer.begin();

  pinMode(IR_M, INPUT);
  pinMode(IR_L, INPUT);
  pinMode(IR_R, INPUT);
  
  myservoL.attach(8);
  myservoM.attach(9);
  myservoR.attach(10);

  printer.justify('C');
}

void loop() {

  input = "";
  if (Serial.available() > 0) {
    Serial.readBytes(inputBuffer, 200);
    delay(200);
    input += inputBuffer;
//    Serial.println(input);
    if (input.startsWith("#")) {
//      Serial.println(input);
      input.remove(0, 1);
//      Serial.println("cutinput: " + input);
      inputInt = input.toInt();
//      Serial.print("integer: ");
//      Serial.println(inputInt);
      while(inputInt != -1){
        int IR_50 = digitalRead(IR_R);
        myservoR.write(140);
        Serial.println(inputInt);
        delay(500);
        if(IR_50 == 0 && peer == true){
          inputInt--;
          peer = false ;
        }
        if(IR_50 == 1 && peer == false){
          peer = true;}
      }
      myservoR.write(90);
      input = "";
      
    } else if (input.startsWith("!")) {
//      Serial.println(input);
      input.remove(0, 1);
//      Serial.println("cutinput: " + input);
      inputInt = input.toInt();
//      Serial.print("integer: ");
//      Serial.println(inputInt);
      while(inputInt != -4){
        int IR_10 = digitalRead(IR_L);
        myservoL.write(140);
        Serial.println(inputInt);
        delay(500);
        if(IR_10 == 0 && peer == true){
          inputInt--;
          peer = false ;
        }
        if(IR_10 == 1 && peer == false){
          peer = true;}
      }
      myservoL.write(90);

      input = "";
    } else if (input.startsWith("@")) {
//      Serial.println(input);
      input.remove(0, 1);
//      Serial.println("cutinput: " + input);
      inputInt = input.toInt();
//      Serial.print("integer: ");
//      Serial.println(inputInt);
      while(inputInt != -1){
        int IR_20 = digitalRead(IR_M);
        myservoM.write(140);
        Serial.println(inputInt);
        delay(500);
        if(IR_20 == 0 && peer == true){
          inputInt--;
          peer = false ;
        }
        if(IR_20 == 1 && peer == false){
          peer = true;}
      }
      
      Serial.println("appel");
      myservoM.write(90);

      input = "";
    } else if (input.startsWith("=")) {
      printer.wake();
//        Serial.println(input);
//        Serial.println(inputBuffer);
        printer.print(inputBuffer);
        printer.feed(2);
        printer.sleep();
    } else {
      myservoL.write(90);
      myservoM.write(90);
      myservoR.write(90);
    }
  }
}
