/*
    Coen Schutte
    0976553
    TI1A
    21/06/2019
*/

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.sql.Timestamp;


public class ATM {
    private ATMScreen as;
    private DisplayText displayText;
    private DisplayText amountText;
    private DisplayText exitText;
    private DisplayText text10;
    private DisplayText text20;
    private DisplayText text50;


    private ReceiptPrinter printer;
    private DisplayText counter;
    private DisplayText balance;
    private Timestamp timeStamp;
    private Serial arduino;

    //All buttons of the ATM
    private ScreenButton but1, but2, but3, but4, but5, but6, but7, but8, but9, but0;
    private ScreenButton ok, yes, no, withdraw, deposit, checkBalanceBut, exit, back, but10, but20, but50, but50s,
            butPlus10, butMin10, butPlus20, butMin20, butPlus50,butMin50, butAmount;

    private ArrayList<InputDevice> inputDevices;    //Arraylist for buttons 0-9 + ok
    private ArrayList<InputDevice> choice;          //Arraylist for buttons withdrawal-deposit
    private ArrayList<InputDevice> amountButtons;   //Arraylist for amount buttons
    private ArrayList<InputDevice> yesNo;           //Arraylist for buttons yes/no
    private ArrayList<InputDevice> billChoice;      //Arraylist for bill choices;

    private boolean pinMode;                        //Stores whether or not user is using the keypad for a pincode or not
    private boolean RFIDUse;

    private int flag;                               //Stores the amount of log in attempts
    private int tempBalance;                        //Saves amount to be added to the balance
    private int pinLength;                          //Stores the amount of numbers entered of the pin
    private int amount10;
    private int amount20;
    private int amount50;
    private int bills10 = 30;
    private int bills20 = 30;
    private int bills50 = 30;

    private String RFID = "";                       //Stores arduino rfid input
    private String key;
    private String pinCode = "";
    private String pinCut2;
    private String getPin;                          //Saves the users pincode after login
    private String amount10String;
    private String amount20String;
    private String amount50String;


    private ImageIcon img = new ImageIcon("Bank_Logo_project_3_4.png");       //stores an image from the parent directory

    private Connection conn;

    private Date date;

    ATM() {
        conn = new Connection("https://coenschutte.nl/api/clients/");
        as = new ATMScreen();

        //ATM frame
        Frame f = new Frame("UwU Bank");
        f.setBounds(0, 0, 720, 480);
        Color myColor = Color.decode("#64dbaf");
        f.setBackground(myColor);
        f.addWindowListener(new MyWindowAdapter(f));
        f.add(as);
        f.setVisible(true);

        f.setIconImage(img.getImage());                                             //Gives the window a cute icon
        f.setResizable(false);                                                      //Disables the user from resizing the screen

        //Create instances
        displayText =  new DisplayText("displayText", new Point(25, 25)); //Text
        amountText = new DisplayText("amountText", new Point(25,75));
        counter = new DisplayText("counter", new Point(25, 350));
        balance = new DisplayText("balance", new Point (280, 200));
        exitText = new DisplayText("exitText", new Point(300, 400));
        text10 = new DisplayText("text10", new Point(100,175));
        text20 = new DisplayText("text20", new Point(100,225));
        text50 = new DisplayText("text50", new Point(100,275));
        printer = new ReceiptPrinter();
        timeStamp = new Timestamp(System.currentTimeMillis());
        arduino = new Serial();

        but1 = new ScreenButton("1", new Point(324, 120));
        but2 = new ScreenButton("2", new Point(360, 120));
        but3 = new ScreenButton("3", new Point(396, 120));
        but4 = new ScreenButton("4", new Point(324, 165));
        but5 = new ScreenButton("5", new Point(360, 165));
        but6 = new ScreenButton("6", new Point(396, 165));
        but7 = new ScreenButton("7", new Point(324, 210));
        but8 = new ScreenButton("8", new Point(360, 210));
        but9 = new ScreenButton("9", new Point(396, 210));
        but0 = new ScreenButton("0", new Point(360, 255));
        ok = new ScreenButton("ok", new Point(396, 255));
        ok.setColor("green");
        but10 = new ScreenButton("10", new Point(200, 175));
        but20 = new ScreenButton("20", new Point(300, 175));
        but50 = new ScreenButton("50", new Point(400, 175));
        but50s = new ScreenButton("Quick €50", new Point(290, 200));

        butPlus10 = new ScreenButton("10+", new Point(275, 175));
        butMin10 = new ScreenButton("10-", new Point(200, 175));
        butPlus20 = new ScreenButton("20+", new Point(275, 225));
        butMin20 = new ScreenButton("20-", new Point(200, 225));
        butPlus50 = new ScreenButton("50+", new Point(275, 275));
        butMin50 = new ScreenButton("50-", new Point(200, 275));

        butAmount = new ScreenButton("Enter amount", new Point(225, 250));
        withdraw = new ScreenButton("withdraw", new Point(100, 200));
        checkBalanceBut = new ScreenButton("Check balance", new Point(100, 250));
        deposit = new ScreenButton("deposit", new Point(500, 200));
        exit = new ScreenButton("exit", new Point(620, 400));
        exit.setColor("red");
        yes = new ScreenButton("yes", new Point(150, 200));
        yes.setColor("green");
        no = new ScreenButton("no", new Point (450, 200));
        no.setColor("yellow");
        back = new ScreenButton("back", new Point(150, 400));
        back.setColor("red");

        date = new Date();

        //Creates and fills arraylists
        inputDevices = new ArrayList<>();
        inputDevices.add(but1);
        inputDevices.add(but2);
        inputDevices.add(but3);
        inputDevices.add(but4);
        inputDevices.add(but5);
        inputDevices.add(but6);
        inputDevices.add(but7);
        inputDevices.add(but8);
        inputDevices.add(but9);
        inputDevices.add(but0);
        inputDevices.add(back);
        inputDevices.add(ok);

        amountButtons = new ArrayList<>();
        amountButtons.add(but10);
        amountButtons.add(but20);
        amountButtons.add(but50);
        amountButtons.add(butAmount);
        amountButtons.add(back);

        choice = new ArrayList<>();
        choice.add(withdraw);
        choice.add(deposit);
        choice.add(but50s);
        choice.add(exit);
        choice.add(checkBalanceBut);

        yesNo = new ArrayList<>();
        yesNo.add(yes);
        yesNo.add(no);
        yesNo.add(back);

        billChoice = new ArrayList<>();
        billChoice.add(butPlus10);
        billChoice.add(butMin10);
        billChoice.add(butPlus20);
        billChoice.add(butMin20);
        billChoice.add(butPlus50);
        billChoice.add(butMin50);
        billChoice.add(ok);
        billChoice.add(back);

        arduino.openPort();
        arduino.listenSerial();

        while (true) {
            doTransaction();
        }
    }

    public void doTransaction() {

        //Makes sure a few values are cleared before the login procedure starts
        as.clear();
        pinLength = 0;
        pinMode = false;
        RFID = "";
        arduino.resetRFID();
        sleep(100);

        startScreen();

    }

    private void startScreen(){

        as.add(displayText);
        displayText.giveOutput("Press A to start: ");
        String startButton = amountInput();

        if(startButton.contains("A")){
            getPin = "";
            login();
            homeScreen();
        }

    }



    //Enters iban and checks if account is blocked
    private void login() {

        arduino.listenSerial();
        as.clear();
        RFID = "";
        getPin = "";
        displayText.giveOutput("Please enter your debit card");
        as.add(displayText);

        while(!RFIDUse) {
            arduino.listenSerial();
            RFID = arduino.getRFID();
            if(!RFID.isEmpty()) {
                login2();
                RFIDUse = false;
            }
        }
    }

    //Screen for entering pincode
    private void login2() {
        as.clear();
        getPin = "";
        pinLength = 0;
        displayText.giveOutput("Please enter your Pin:");    //Set text
        counter.giveOutput("Incorrect tries: ");             //Set counter text
        exitText.giveOutput("Press # to confirm and * to exit ");
        as.add(displayText);                                 //Add text
        as.add(counter);                                     //Add error counter
        as.add(amountText);                                  //Add amount of numbers entered
        as.add(exitText);                                    //Add exit information

        if (pinLength == 0){
            amountText.giveOutput("Amount of numbers entered:");
        }

        if (flag == 1) {                                     //Flag is used to save amount of login attempts
            counter.giveOutput("Incorrect tries: X ");
        } else if (flag == 2) {
            counter.giveOutput("Incorrect tries: X X ");
        }
        pinMode = true;

        getPin = buttonInput();

        as.clear();

        displayText.giveOutput("Please wait...");
        as.add(displayText);

        int status = conn.checkCredentials(RFID, getPin);

        //Gives the user feedback based on API response
        if(status == 1){
            System.out.println("Wrong pin");
            displayText.giveOutput("You have entered the wrong pincode");
            flag++;
            sleep(5000);
            login2();
        } else if(status == 2){
            blockScreen();

        } else {
            homeScreen();
        }

    }

    private void blockScreen(){
        as.clear();
        as.add(displayText);
        displayText.giveOutput("Your account is blocked... Please contact customer support");
        sleep(5000);
        doTransaction();
    }

    private String buttonInput() {
        String input = "";

        while(true) {
            arduino.listenSerial();
            key = arduino.getKey();
            pinMode = false;
            if (!key.isEmpty()) {
                pinCode += key;
                pinLength++;
                arduino.resetKey();
                pinMode = true;

                if (key.equals("*")){
                    pinLength = 0;
                    pinCode = "";
                    exit();
                }
            }

            if (pinCode.contains("#")) {
                StringBuffer pinCut = new StringBuffer(pinCode);
                pinCut.deleteCharAt(pinCut.length() - 1);
                pinCut2 = pinCut.toString();
                pinCode = "";
                pinMode = false;
                amountText.giveOutput("");
                return pinCut2;
            }


            for (int i = 0; i < inputDevices.size(); i++) {
                String temp;                                                                            //Stores input from digital keypad
                temp = inputDevices.get(i).getInput();

                if (temp != null) {
                    input += temp;
                    pinLength++;
                    pinMode = true;
                }
                if (ok.getInput() == "ok") {                                                            //returns the current pin
                    return input;
                } else if (exit.getInput() == "exit") {
                    exit();
                } else if (back.getInput() == "back") {
                    as.clear();
                    homeScreen();
                }
            }

            if(pinMode) {
                as.add(amountText);
                if (pinLength == 1) {
                    amountText.giveOutput("Amount of numbers entered:      *");
                } else if (pinLength == 2) {
                    amountText.giveOutput("Amount of numbers entered:      *  *");
                } else if (pinLength == 3) {
                    amountText.giveOutput("Amount of numbers entered:      *  *  *");
                } else if (pinLength == 4) {
                    amountText.giveOutput("Amount of numbers entered:      *  *  *  *");
                }
            }
        }
    }



    //Gives the user the option to choose between withdrawing and depositing money or withdraw 50 immediately
    private void homeScreen() {
        as.clear();
        displayText.giveOutput("What would you like to do?");
        as.add(displayText);
        as.add(but50s);
        addElement("choice");
        addElement("exit");

        System.out.println("rfid scan: " + RFID);
        if(RFID.contains("UVVU")) {
            as.add(checkBalanceBut);
        }


        as.add(exit);

        String temp;
        while (true) {                                                                      //Checks which button is pressed and does the corresponding action
            for (int i = 0; i < choice.size(); i++) {
                temp = choice.get(i).getInput();
                if (temp != null) {
                    if (temp == "withdraw") {
                        as.clear();
                        withdraw();                                                         //Goes to exit screen
                    } else if (temp == "exit") {
                        exit();
                    } else if (temp == "Quick €50") {                                       //Dispenses 50 if the user has a high enough balance
                        this.tempBalance = 50;
                        selectBills(50);
                    } else if (temp == "Check balance"){
                        checkBalanceScreen();
                    }
                }
            }
        }
    }

    private void checkBalanceScreen(){
        as.clear();
        checkBalance();
        as.add(back);

        String temp;
        while (true) {
            for (int i = 0; i < yesNo.size(); i++) {
                temp = yesNo.get(i).getInput();
                if (temp != null) {
                    if (temp == "back") {
                        as.clear();
                        homeScreen();                                         //go back to the homescreen screen
                    }
                }
            }
        }
    }

    //Displays user's balance
    //todo fix this
    private String checkBalance(){
        String balancetest = conn.getBalance(RFID, getPin);
        int balance = Integer.parseInt(balancetest);

        as.add(displayText);
        displayText.giveOutput("Your current balance is: " + balance);
        System.out.println(balancetest + " dit is balance");

        return null;
    }

    private void selectBills(int amount){
        as.clear();
        addElement("bills");
        as.add(displayText);
        as.add(text10);
        as.add(text20);
        as.add(text50);
        as.add(back);

        int remainder = amount;
        amount10 = 0;
        amount20 = 0;
        amount50 = 0;
        String amount10String = "0";
        String amount20String = "0";
        String amount50String = "0";


        while(true) {
            String amount2 = Integer.toString(remainder);
            amount10String= Integer.toString(amount10);
            amount20String= Integer.toString(amount20);
            amount50String= Integer.toString(amount50);

            displayText.giveOutput("Amount remaining: " + amount2);
            text10.giveOutput(amount10String);
            text20.giveOutput(amount20String);
            text50.giveOutput(amount50String);

            String temp = selectBillInput();
            if(temp == "10+" && !(remainder - 10 < 0) && !(bills10 <= 0)){
                remainder -= 10;
                bills10--;
                amount10++;
            } else if(temp == "10-" && amount10 != 0 && !(bills10 < 0)){
                remainder += 10;
                bills10++;
                amount10--;
            } else if(temp == "20+" && !(remainder - 20 < 0) && !(bills20 <= 0) ){
                remainder -= 20;
                bills20--;
                amount20++;
            } else if(temp == "20-"&& amount20 != 0 && !(bills20 < 0)){
                remainder += 20;
                bills20++;
                amount20--;
            } else if(temp == "50+"&& !(remainder - 50 < 0) &&!(bills50 <= 0)){
                remainder -= 50;
                bills50--;
                amount50++;
            } else if(temp == "50-" && amount50 != 0 && !(bills50 < 0)){
                remainder += 50;
                bills50++;
                amount50--;
            } else if(temp == "ok" && remainder == 0){
                conn.withdraw(RFID, getPin, amount);
                receipt();
            } else if (temp == "back") {
                as.clear();
                homeScreen();
            }
        }
    }

    private String selectBillInput(){

        String input = "";

        for (int i = 0; i < billChoice.size(); i++) {
            String temp;
            temp = billChoice.get(i).getInput();

            if (temp != null) {
                input += temp;

            }

            if (butPlus10.getInput() == "10+") {
                input = "10+";
            } else if (butMin10.getInput() == "10-") {
                input = "10-";
            } else if (butPlus20.getInput() == "20+") {
                input = "20+";
            } else if (butMin20.getInput() == "20-") {
                input = "20-";
            } else if (butPlus50.getInput() == "50+") {
                input = "50+";
            } else if (butMin50.getInput() == "50-") {
                input = "50-";
            } else if (ok.getInput() == "ok") {
                input = "ok";
            } else if (back.getInput() == "back"){
                input = "back";
            }
        }
        return input;
    }

    //Allows the user to withdraw money
    private void withdraw() {
        displayText.giveOutput("Choose amount:");
        as.add(displayText);
        as.add(back);
        addElement("money");                                                              //Adds buttons of different values for the user to pick from

        String temp;

        //Checks the input from the buttons and withdraws that amount
        while(true) {
            for (int i = 0; i < amountButtons.size(); i++ ) {
                temp = amountButtons.get(i).getInput();

                if (temp != null) {
                    if (temp == "10") {
                        tempBalance = 10;
                        selectBills(10);
                        break;
                    } else if (temp == "20") {
                        tempBalance = 20;
                        selectBills(tempBalance);
                        break;
                    } else if (temp == "50") {
                        tempBalance = 50;
                        selectBills(tempBalance);
                        break;
                    } else if (temp == "Enter amount"){
                        otherAmountScreen();

                    } else if (temp == "back") {
                        as.clear();
                        homeScreen();
                    }  else {
                        as.clear();
                        displayText.giveOutput("Your current balance is too low.");
                        as.add(displayText);
                        sleep(2000);
                        as.clear();
                        withdraw();
                    }
                }
            }
        }
    }

    private void otherAmountScreen(){

        String otheramount = "";
        as.clear();
        as.add(displayText);
        as.add(amountText);
        as.add(back);
        as.add(exitText);
        displayText.giveOutput("Please enter a multiple of 10");
        amountText.giveOutput("Your current input is: ");
        exitText.giveOutput("Press # to confirm and * to restart input ");


        String balancetest = conn.getBalance(RFID, getPin);
        int userBalance = Integer.parseInt(balancetest);

        while(!otheramount.contains("#")){
            amountText.giveOutput("Your current input is: " + otheramount);
            otheramount += amountInput();
            System.out.println("tempbalance: " + tempBalance);

            if(otheramount.contains("*")){
                otheramount = "";
            }
        }


        StringBuffer cutTempBalance = new StringBuffer(otheramount);
        cutTempBalance.deleteCharAt(cutTempBalance.length() - 1);
        otheramount = cutTempBalance.toString();



        tempBalance = Integer.parseInt(otheramount);
        if((tempBalance % 10) == 0) {

            if(tempBalance <= userBalance) {
                selectBills(tempBalance);
            } else{
                displayText.giveOutput("Your balance is not high enough");
                sleep(2000);
                homeScreen();
            }
        } else{
            displayText.giveOutput(tempBalance + " is not divisible by 10");
            sleep(2000);
            otherAmountScreen();
        }
    }

    private String amountInput(){
        while(true){
            arduino.listenSerial();
            key = arduino.getKey();
            if (!key.isEmpty()) {
                arduino.resetKey();
                return key;
            }
        }
    }

    //Asks if the user wants a receipt and gives a receipt based on user input
    private void receipt() {
        as.clear();
        displayText.giveOutput("Would you like a receipt?");
        as.add(displayText);
        as.add(yes);
        as.add(no);
        as.add(back);

        String temp;
        while (true) {
            for (int i = 0; i < yesNo.size(); i++) {
                temp = yesNo.get(i).getInput();
                if (temp != null) {

                    //Bon op GUI
                    as.clear();
                    printer.giveOutput("Now dispensing € " + tempBalance);
                    printer.giveOutput("New balance: € " + checkBalance());
                    displayText.giveOutput("Now dispensing € " + tempBalance);
                    as.add(displayText);
                    sleep(100);

                    dispenseMoney();
                    sleep(5000);
                    if (temp == "yes") {

                        //Bon via printer
                        StringBuffer RFIDBuffer = new StringBuffer(RFID);

                        for(int j = 1; j < 4; j++) {
                            RFIDBuffer.deleteCharAt(RFID.length() - j);
                        }
                        String cutRFID = RFIDBuffer.toString();
                        String receipt = "====================\n" + date.toString() + "\n" + "IBAN: " + cutRFID +"***\n" + "Dispensed: " + tempBalance + "\n" + "UwU Bank\n" + "====================\n\n\n";
                        //arduino.sendDispenser(receipt);



                        //Bon als printline
                        System.out.println("===============================");
                        System.out.println(timeStamp);
                        System.out.println("RFID: " + RFID + "***");
                        System.out.println("Dispensed: " + tempBalance);
                        System.out.println("===============================");

                        sleep(1000);
                        as.clear();
                        RFID = "";
                        arduino.resetRFID();
                        doTransaction();
                    } else if (temp == "no") {
                        as.clear();
                        displayText.giveOutput("Now dispensing € " + tempBalance);
                        as.add(displayText);
                        sleep(1000);
                        as.clear();
                        RFID = "";
                        arduino.resetRFID();
                        doTransaction();
                    } else if (temp == "back") {
                        as.clear();
                        withdraw();                                         //go back to the withdraw screen
                    } else {
                        receipt();
                        break;
                    }
                }
            }
        }
    }

    private void dispenseMoney(){
        if(amount10 != 0) {
            arduino.sendDispenser("!" + amount10);
            sleep(2000 * amount10 + 200);
            sleep(200);
        }

        if(amount20 != 0) {
            arduino.sendDispenser("@" + amount20);
            sleep(2000 * amount20 + 200);
            sleep(200);
        }

        if(amount50 != 0) {
            arduino.sendDispenser("#" + amount50);
            sleep(2000 * amount50 + 200);
        }
    }
    //Makes it easy to reuse common used buttons to avoid duplicate code
    private void addElement(String kind) {
        if (kind == "keypad") {
            as.add(but1);
            as.add(but2);
            as.add(but3);
            as.add(but4);
            as.add(but5);
            as.add(but6);
            as.add(but7);
            as.add(but8);
            as.add(but9);
            as.add(but0);
        } else if (kind == "choice") {
            as.add(withdraw);
            //as.add(deposit);
        } else if (kind == "yn") {
            as.add(yes);
            as.add(no);
        } else if (kind == "money") {
            as.add(but10);
            as.add(but20);
            as.add(but50);
            as.add(butAmount);
        } else if (kind == "exit") {
            as.add(exit);
        } else if (kind == "balance") {
            as.add(balance);
        } else if (kind == "bills"){
            as.add(butPlus10);
            as.add(butMin10);
            as.add(butPlus20);
            as.add(butMin20);
            as.add(butPlus50);
            as.add(butMin50);
            as.add(ok);
        }
    }

    //"pauses" to program for x seconds
    private void sleep(int timer) {
        try {
            TimeUnit.MILLISECONDS.sleep(timer);
        } catch (Exception e) {
            System.out.println("Sleep failed.");
        }
    }

    //Exits the atm process and restarts it
    private void exit() {
        as.clear();
        displayText.giveOutput("Have a good day.");
        as.add(displayText);
        sleep(2000);
        as.clear();
        doTransaction();
    }

}