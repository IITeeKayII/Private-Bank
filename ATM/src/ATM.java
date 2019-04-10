import java.awt.*;
import java.sql.Timestamp;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ATM {
    ATMScreen as;
    Bank MyBank;
    CardReader cardReader = new CardReader("cardreader");

    ArrayList<InputDevice> KeyButtons;      //Keypad buttons
    ArrayList<InputDevice> Choice;          //Choice buttons after log in
    ArrayList<InputDevice> AmountWithdraw;  //Buttons to choose amount to withdraw
    ArrayList<InputDevice> Actions;         //Actions

    private ScreenButton but0, but1, but2, but3, but4, but5, but6, but7, but8, but9, Correct, OK;

    private ScreenButton Withdraw, Deposit, GetBalance;

    private ScreenButton W1000, W5000, W10000, W50000, Other;

    private ScreenButton Stop, Back, Yes, No;

    private String pinInput = "";
    private Client client;
    private Timestamp timestamp;
    private int pinLength;
    private int pinMode = 0;
    int KeyLocation;
    int Row2 = 35;
    int Row3 = 70;

    Keypad keypad = new Keypad("keypad");

    // initialize a few texts
    DisplayText displayText = new DisplayText("DisplayText", new Point(275, 200));
    DisplayText ActionPin = new DisplayText("ActionPin", new Point(275, 75));
    DisplayText InOut = new DisplayText("InOut", new Point(350, 200));
    DisplayText amountText = new DisplayText("amountText", new Point(50, 100));


    public ATM(Bank bank) {
        this.MyBank = bank;

        as = new ATMScreen();

        //set the frame size and colour
        Frame f = new Frame("My ATM");
        f.setBounds(500, 300, 800, 480);
        f.setBackground(Color.GRAY);
        f.addWindowListener(new MyWindowAdapter(f));
        f.add(as);
        f.setVisible(true);

        //initialize most of the screenbuttons, devided per arraylist
        Withdraw = new ScreenButton("Withdraw", new Point(235, 200));
        Deposit = new ScreenButton("Deposit", new Point(420, 200));
        GetBalance = new ScreenButton("Get Balance", new Point(235, 255));

        W1000 = new ScreenButton("1000", new Point(250, 180));
        W5000 = new ScreenButton("5000", new Point(450, 185));
        W10000 = new ScreenButton("10000", new Point(250, 220));
        W50000 = new ScreenButton("50000", new Point(450, 220));
        Other = new ScreenButton("Other Amount", new Point(240, 255));

        Stop = new ScreenButton("Stop", new Point(620, 400));
        Back = new ScreenButton("Back", new Point(530, 400));
        Yes = new ScreenButton("Yes", new Point(275, 300));
        No = new ScreenButton("No", new Point(450, 300));

        KeyButtons = new ArrayList<>();

        //add buttons to the arraylist
        Choice = new ArrayList<>();
        Choice.add(Withdraw);
        Choice.add(Deposit);
        Choice.add(GetBalance);

        AmountWithdraw = new ArrayList<>();
        AmountWithdraw.add(W1000);
        AmountWithdraw.add(W5000);
        AmountWithdraw.add(W10000);
        AmountWithdraw.add(W50000);
        AmountWithdraw.add(Other);

        Actions = new ArrayList<>();
        Actions.add(Stop);
        Actions.add(Back);
        Actions.add(Yes);
        Actions.add(No);




        doTransactions();
    }

    private void doTransactions() {
        //reset all data and go to checkcard
        pinLength = 0;
        pinInput = "";
        KeyLocation = 350;
        checkCard();
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private void checkCard() {
        //clear screen
        as.clear();
        displayText.giveOutput("Please insert your card");
        as.add(displayText);
        String cardnumber = cardReader.getInput(); //check input
        client = MyBank.get(cardnumber);
        if (client != null) {
            as.clear(); // if input exists go to login
            login();
        } else {
            as.clear(); // else retry
            displayText.giveOutput("Card unknown");
            as.add(displayText);
            sleep(3);
            doTransactions();
        }
    }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private void login() {
        as.clear(); //enter the pin, check this at pincheck
        ActionPin.giveOutput("Please enter your pin: ");
        as.add(ActionPin);
        pinMode = 1;
        pinCheck();

    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private void welcome() {
        as.clear();
        pinMode = 0; //give welcome text
        InOut.giveOutput("Welcome " + client.getName());
        as.add(InOut);
        sleep(2); //go to sleep for 2 seconds before continuing on
        home();
    }

    private void home() {
        as.clear();
        pinMode = 0;
        ActionPin.giveOutput("Choose an action");
        as.add(ActionPin);
        addElement("Choice"); //choose action, while true to always keep looping
        while (true){
            if (Withdraw.getInput() == "Withdraw") {
                withdraw();
            } else if (Deposit.getInput() == "Deposit") {
                deposit();
            } else if (GetBalance.getInput() == "Get Balance") {
                getBalance();
            } else if (Stop.getInput() == "Stop") {
                goodbye();
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private void getBalance(){
        as.clear();
        amountText.giveOutput("Your balance is: " + client.getBalance(pinInput)); //if getbalance is pressed check the current balance using entered pincode
        as.add(amountText);
        as.add(Back);
        as.add(Stop);
        while (true){
            if (Back.getInput() == "Back"){ //go to the previous screen being home when button pressed
                home();
            } else if (Stop.getInput() == "Stop"){ //go to goodbye and restart the atm
                goodbye();
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private void withdraw() {
        as.clear();
        pinMode = 0;
        ActionPin.giveOutput("Choose amount to withdraw");
        as.add(ActionPin);
        addElement("Withdraw");
        String s = ""; //initialize temporary string
        String temp = "";
        while (true) {
            for (int i = 0; i < AmountWithdraw.size(); i++) { //choose between some default amounts to withdraw
                temp = AmountWithdraw.get(i).getInput();
                if (temp == "1000") {
                    System.out.println("boot");
                    FinishWithdraw(temp);
                } else if (temp == "5000"){
                    FinishWithdraw(temp);
                } else if (temp == "10000"){
                    FinishWithdraw(temp);
                } else if (temp == "50000"){
                    FinishWithdraw(temp);
                } else if (temp == "Other Amount"){
                    pinMode = 2;
                    as.clear();
                    ActionPin.giveOutput("Please enter amount: ");
                    as.add(ActionPin);
                    addElement("Keypad");
                    as.add(Back);
                    s = buttonInput(); //check keypad button input and store in s
                    if (s.length() < 12){
                        FinishWithdraw(s);
                    } else {
                        as.clear();
                        s = "";
                        displayText.giveOutput("Balance too low");
                        as.add(displayText);
                        sleep(2);
                        withdraw();
                    }
                }
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private void FinishWithdraw(String Pressed){
        if (Integer.parseInt(Pressed) <= client.getBalance(pinInput)) { //extra check if input amount is able to be withdrawn
            int temp = Integer.parseInt(Pressed);
            timestamp = new Timestamp(System.currentTimeMillis()); //initialize timestamp
            as.clear();
            ActionPin.giveOutput("Do you want a receipt?"); //ask for receipt
            as.add(ActionPin);
            as.add(Yes);
            as.add(No);
            while (true){
                if (Yes.getInput() == "Yes"){ //print receipt and dispense money
                    as.clear();
                    System.out.println("----------------------------------------------");
                    System.out.println(timestamp);
                    System.out.println(client.getName());
                    System.out.println(Pressed);
                    System.out.println("----------------------------------------------");
                    displayText.giveOutput("now dispensing €" + temp);
                    as.add(displayText);
                    client.Withdraw(temp, pinInput); //perform the withdrawl
                    sleep(2); //set screen for 2 seconds before wiping it
                    as.clear();
                    displayText.giveOutput("Your new balance is: €" + client.getBalance(pinInput)); //print new balance
                    as.add(displayText);
                    sleep(3); //set screen for 3 seconds before turning off atm
                    goodbye();
                } else if (No.getInput() == "No"){ //only dispense money
                    as.clear();
                    displayText.giveOutput("now dispensing €" + temp);
                    as.add(displayText);
                    client.Withdraw(temp, pinInput); //perform withdrawl
                    sleep(2); //set screen for 2 seconds before wiping ir
                    as.clear();
                    displayText.giveOutput("Your new balance is: €" + client.getBalance(pinInput)); //print new balance
                    as.add(displayText);
                    sleep(3); //set screen for 3 seconds before turning off the atm
                    goodbye();
                }
            }
        } else {
            as.clear();
            displayText.giveOutput("Balance is too low!"); //enter new amount if balance isn't enough for withdrawl
            as.add(displayText);
            sleep(3);
            withdraw();
        }


    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private void deposit() {
        as.clear();
        pinMode = 0;
        ActionPin.giveOutput("Please enter amount to deposit: ");

        addElement("Keypad"); //quickadd the keypad
        as.add(ActionPin); //add all needed buttons and displaytexts
        as.add(Back);

        client.Deposit(Integer.parseInt(buttonInput()));    //deposit the input
        as.clear();
        displayText.giveOutput("Your new saldo is: "+ client.getBalance(pinInput));
        as.add(displayText);

        sleep(2); //sleep for 2 seconds before wiping screen
        goodbye();
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private void goodbye() {
        as.clear(); //clear screen
        InOut.giveOutput("Goodbye!");
        as.add(InOut); //give output goodbye
        sleep(3); //sleep for 3 seconds
        as.clear();
        doTransactions(); //return to doTransactions to simulate a restart
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private void addElement(String Type) { //method to quickly add certain buttons, type to set the sort of buttons requested to add
        if (Type == "Keypad") {

            //initialize all keypad buttons with set and variable locations to be able to change x location
            but0 = new ScreenButton("0", new Point(KeyLocation + Row2, 255));
            but1 = new ScreenButton("1", new Point(KeyLocation, 150));
            but2 = new ScreenButton("2", new Point(KeyLocation + Row2, 150));
            but3 = new ScreenButton("3", new Point(KeyLocation + Row3, 150));
            but4 = new ScreenButton("4", new Point(KeyLocation, 185));
            but5 = new ScreenButton("5", new Point(KeyLocation + Row2, 185));
            but6 = new ScreenButton("6", new Point(KeyLocation + Row3, 185));
            but7 = new ScreenButton("7", new Point(KeyLocation, 220));
            but8 = new ScreenButton("8", new Point(KeyLocation + Row2, 220));
            but9 = new ScreenButton("9", new Point(KeyLocation + Row3, 220));
            Correct = new ScreenButton("cor", new Point(KeyLocation - 30, 255));
            OK = new ScreenButton("OK", new Point(KeyLocation + Row3, 255));

            //put all keybuttons in the arraylist
            KeyButtons.add(but0);
            KeyButtons.add(but1);
            KeyButtons.add(but2);
            KeyButtons.add(but3);
            KeyButtons.add(but4);
            KeyButtons.add(but5);
            KeyButtons.add(but6);
            KeyButtons.add(but7);
            KeyButtons.add(but8);
            KeyButtons.add(but9);
            KeyButtons.add(Correct);
            KeyButtons.add(OK);
            KeyButtons.add(keypad);

            //put all buttons on the screen
            as.add(but0);
            as.add(but1);
            as.add(but2);
            as.add(but3);
            as.add(but4);
            as.add(but5);
            as.add(but6);
            as.add(but7);
            as.add(but8);
            as.add(but9);
            as.add(Correct);
            as.add(OK);
            as.add(Stop);
        } else if (Type == "Withdraw"){
            //put all withdrawl buttons on the screen
            as.add(W1000);
            as.add(W5000);
            as.add(W10000);
            as.add(W50000);
            as.add(Other);
            as.add(Stop);
            as.add(Back);
        } else if (Type == "Choice"){
            //put all buttons for homescreen on the screen
            as.add(Withdraw);
            as.add(Deposit);
            as.add(GetBalance);
            as.add(Stop);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private void sleep(int time) {
        try {
            TimeUnit.SECONDS.sleep(time); //sleep for given amount of seconds
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    private String buttonInput() { //check keypad buttons in case of needing pin to check
        String s = "";
        as.add(OK);
        while(true) {
            for (int i = 0; i < KeyButtons.size(); i++ ) { //cycle through the keypad buttons to check if pressed
                String temp = KeyButtons.get(i).getInput();
                if (temp != null) {
                    s += temp;
                    amountText.giveOutput("Entered amount: €" + s);
                    as.add(amountText);
                    if (pinMode == 1) {
                        pinLength++;
                        System.out.println(pinLength);
                        if (pinLength == 1) {
                            amountText.giveOutput("Amount of numbers entered:      *");
                        } else if (pinLength == 2) {
                            amountText.giveOutput("Amount of numbers entered:      *  *");
                        } else if (pinLength == 3) {
                            amountText.giveOutput("Amount of numbers entered:      *  *  *");
                        } else if (pinLength == 4) {
                            amountText.giveOutput("Amount of numbers entered:      *  *  *  *");
                        } else {
                            amountText.giveOutput("");
                        }
                        as.add(amountText); //print a star for each number added
                    }
                } else if (pinMode == 2) {
                    if (Back.getInput() == "Back") {
                        s = "";
                        withdraw();
                    }
                } else if(pinMode == 0){
                    if (Back.getInput() == "Back") {
                        s = "";
                        home();
                    }
                }
                if (OK.getInput() == "OK") { //return pressed keys when OK is pressed
                    return s;
                } else if (Stop.getInput() == "Stop") { //restart the atm if pressed
                    goodbye();
                }else if (Correct.getInput() == "cor") { //clear all inputs
                    pinLength = 0;
                    pinInput = "";
                    s = "";
                }
            }
        }
    }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public void pinCheck() {
        addElement("Keypad");
        String temp = buttonInput();
        if (client.checkPin(temp)){
            welcome();
        } else {
            as.clear();
            displayText.giveOutput("Wrong pin entered");
            pinInput = "";
            pinLength = 0;
            as.add(displayText);
            sleep(2);
            login();
        }


    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

}

