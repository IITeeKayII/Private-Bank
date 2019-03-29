import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ATM {
    ATMScreen as;
    Bank MyBank;
    CardReader cardReader = new CardReader("cardreader");
    int KeyLocation;
    int Row2 = 35;
    int Row3 = 70;
    boolean withdraw = false;

    ArrayList<InputDevice> KeyButtons;      //Keypad buttons
    ArrayList<InputDevice> Choice;          //Choice buttons after log in
    ArrayList<InputDevice> AmountWithdraw;  //Buttons to choose amount to withdraw
    ArrayList<InputDevice> Actions;         //Actions

    private ScreenButton but0, but1, but2, but3, but4, but5, but6, but7, but8, but9, Correct, OK;

    private ScreenButton Withdraw, Deposit, GetBalance;

    private ScreenButton W1000, W5000, W10000, W50000;

    private ScreenButton Stop, Back, Yes, No;

    private String pinInput = "";
    private Client client;
    private int pinLength;
    private boolean pinMode;

    Keypad keypad = new Keypad("keypad");

    DisplayText displayText = new DisplayText("DisplayText", new Point(275, 200));
    DisplayText ActionPin = new DisplayText("ActionPin", new Point(235, 75));
    DisplayText InOut = new DisplayText("InOut", new Point(350, 200));
    DisplayText amountText = new DisplayText("amountText", new Point(50, 100));
    public ATM(Bank bank) {
        this.MyBank = bank;

        as = new ATMScreen();

        Frame f = new Frame("My ATM");
        f.setBounds(500, 300, 800, 480);
        f.setBackground(Color.GRAY);
        f.addWindowListener(new MyWindowAdapter(f));
        f.add(as);
        f.setVisible(true);

        if (withdraw == true){
            KeyLocation = 100;
        } else {
            KeyLocation = 350;
        }
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

        Withdraw = new ScreenButton("Withdraw", new Point(235, 200));
        Deposit = new ScreenButton("Deposit", new Point(420, 200));
        GetBalance = new ScreenButton("Get Balance", new Point(235, 255));

        W1000 = new ScreenButton("1000", new Point(455, 150));
        W5000 = new ScreenButton("5000", new Point(455, 185));
        W10000 = new ScreenButton("10000", new Point(455, 220));
        W50000 = new ScreenButton("50000", new Point(455, 255));

        Stop = new ScreenButton("Stop", new Point(620, 400));
        Back = new ScreenButton("Back", new Point(530, 400));
        Yes = new ScreenButton("Yes", new Point(530, 400));
        No = new ScreenButton("No", new Point(580, 400));

        KeyButtons = new ArrayList<>();
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

        Choice = new ArrayList<>();
        Choice.add(Withdraw);
        Choice.add(Deposit);
        Choice.add(GetBalance);

        AmountWithdraw = new ArrayList<>();
        AmountWithdraw.add(W1000);
        AmountWithdraw.add(W5000);
        AmountWithdraw.add(W10000);
        AmountWithdraw.add(W50000);

        Actions = new ArrayList<>();
        Actions.add(Stop);
        Actions.add(Back);
        Actions.add(Yes);
        Actions.add(No);


        doTransactions();
    }

    private void doTransactions() {
        pinLength = 0;
        pinInput = "";
        checkCard();
    }

    private void checkCard() {
        as.clear();
        displayText.giveOutput("Please insert your card");
        as.add(displayText);
        String cardnumber = cardReader.getInput();
        client = MyBank.get(cardnumber);
        if (client != null) {
            as.clear();
            login();
        } else {
            as.clear();
            displayText.giveOutput("Card unknown");
            as.add(displayText);
            sleep(3);
            doTransactions();
        }
    }

    private void login() {
        as.clear();
        ActionPin.giveOutput("Please enter your pin: ");
        as.add(ActionPin);
        pinMode = true;
        pinCheck();

    }

    private void welcome() {
        as.clear();
        pinMode = false;
        InOut.giveOutput("Welcome " + client.getName());
        as.add(InOut);
        sleep(2);
        home();
    }

    private void home() {
        as.clear();
        ActionPin.giveOutput("Choose an action");
        as.add(ActionPin);
        addElement("Choice");
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

    private void getBalance(){
        as.clear();
        amountText.giveOutput("Your balance is: " + client.getBalance(pinInput));
        as.add(amountText);
        as.add(Back);
        as.add(Stop);
        while (true){
            if (Back.getInput() == "Back"){
                home();
            } else if (Stop.getInput() == "Stop"){
                goodbye();
            }
        }
    }

    private void withdraw() {
        as.clear();
        withdraw = true;
        ActionPin.giveOutput("Choose amount to withdraw");
        as.add(ActionPin);
        addElement("Keypad");
        addElement("Withdraw");
        String s = "";
        int Amount;
        while (true) {
            for (int i = 0; i < AmountWithdraw.size(); i++) {
                String temp = AmountWithdraw.get(i).getInput();
                if (temp == "1000") {
                    Amount = 1000;
                    FinishWithdraw(Amount);
                } else if (temp == "5000") {
                    Amount = 5000;
                    FinishWithdraw(Amount);
                } else if (temp == "10000") {
                    Amount = 10000;
                    FinishWithdraw(Amount);
                } else if (temp == "50000") {
                    Amount = 50000;
                    FinishWithdraw(Amount);
                }

            }
            for (int i = 0; i < KeyButtons.size(); i++){
                String temp = KeyButtons.get(i).getInput();
                if(temp != null){
                    s += temp;
                }

            }
            if (Stop.getInput() == "Stop") {
                goodbye();
            } else if (Back.getInput() == "Back") {
                as.clear();
                home();
            } else if (OK.getInput() == "OK"){
                Amount = Integer.parseInt(s);
                FinishWithdraw(Amount);
            }
        }
    }
    private void FinishWithdraw(int Pressed){
        if (Pressed <= client.getBalance(pinInput)) {
            String temp = Integer.toString(Pressed);
            as.clear();
            displayText.giveOutput("You've withdrawn " + temp);
            as.add(displayText);
            client.Withdraw(Pressed, pinInput);
            sleep(2);
            as.clear();
            displayText.giveOutput("Your new balance is: " + client.getBalance(pinInput));
            as.add(displayText);
            sleep(3);
            goodbye();
        } else {
            as.clear();
            displayText.giveOutput("Balance is too low!");
            as.add(displayText);
            sleep(3);
            withdraw();
        }


    }

    private void deposit() {
        as.clear();
        ActionPin.giveOutput("Please enter amount to deposit: ");
        addElement("Keypad");
        String s = "";
        as.add(ActionPin);
        as.add(OK);
        as.add(Back);
        as.add(Stop);
        as.add(Correct);
        while (true){
            for (int i = 0; i < KeyButtons.size(); i++ ) {
                String temp = KeyButtons.get(i).getInput();
                if (temp != null){
                    s += temp;
                }
            }
            if (OK.getInput() == "OK"){
                as.clear();
                int toDeposit = Integer.parseInt(s);
                client.Deposit(toDeposit);
                displayText.giveOutput("Your new saldo is: "+ client.getBalance(pinInput));
                as.add(displayText);
                sleep(2);
                goodbye();
            } else if (Back.getInput() == "Back"){
                home();
            } else if (Stop.getInput() == "Stop"){
                goodbye();
            } else if (Correct.getInput() == "cor"){
                s = "";
            }
        }
    }

    private void goodbye() {
        as.clear();
        InOut.giveOutput("Goodbye!");
        as.add(InOut);
        sleep(3);
        as.clear();
        doTransactions();
    }

    private void addElement(String Type) {
        if (Type == "Keypad") {
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
            as.add(W1000);
            as.add(W5000);
            as.add(W10000);
            as.add(W50000);
            as.add(Stop);
            as.add(Back);
        } else if (Type == "Choice"){
            as.add(Withdraw);
            as.add(Deposit);
            as.add(GetBalance);
            as.add(Stop);
        }
    }

    private void sleep(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private String buttonInput() {
        String s = "";
        as.add(OK);
        while(true) {
            for (int i = 0; i < KeyButtons.size(); i++ ) {
                String temp;
                temp = KeyButtons.get(i).getInput();

                if (temp != null) {
                    s += temp;

                    pinLength++;
                    System.out.println(pinLength);
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
                        } else {
                            amountText.giveOutput("Amount of numbers entered:                ");
                        }
                    }
                }
                if (OK.getInput() == "OK") {
                    return s;
                } else if (Stop.getInput() == "Stop") {
                    goodbye();
                } else if (Back.getInput() == "back") {
                    as.clear();
                    home();
                } else if (Correct.getInput() == "cor"){
                    pinLength = 0;
                    pinInput = "";
                    s = "";
                }
            }
        }
    }



    public void pinCheck() {
        addElement("Keypad");
        String temp = buttonInput();
        if (client.checkPin(temp)){
            welcome();
        } else {
            as.clear();
            displayText.giveOutput("Wrong pin entered");
            as.add(displayText);
            sleep(2);
            login();
        }


    }
}

