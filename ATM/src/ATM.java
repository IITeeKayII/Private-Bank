import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
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

    private ScreenButton W1000, W5000, W10000, W50000, W100000, W500000;

    private ScreenButton Stop, Back;

    private String pinInput = "";
    private Client client;
    private int pinLength;
    private boolean pinMode;

    Keypad keypad = new Keypad("keypad");

    DisplayText displayText = new DisplayText("DisplayText", new Point(275, 200));
    DisplayText ActionPin = new DisplayText("ActionPin", new Point(275, 75));
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

        but0 = new ScreenButton("0", new Point(385, 255));
        but1 = new ScreenButton("1", new Point(350, 150));
        but2 = new ScreenButton("2", new Point(385, 150));
        but3 = new ScreenButton("3", new Point(420, 150));
        but4 = new ScreenButton("4", new Point(350, 185));
        but5 = new ScreenButton("5", new Point(385, 185));
        but6 = new ScreenButton("6", new Point(420, 185));
        but7 = new ScreenButton("7", new Point(350, 220));
        but8 = new ScreenButton("8", new Point(385, 220));
        but9 = new ScreenButton("9", new Point(420, 220));
        Correct = new ScreenButton("cor", new Point(320, 255));
        OK = new ScreenButton("OK", new Point(420, 255));

        Withdraw = new ScreenButton("Withdraw", new Point(235, 200));
        Deposit = new ScreenButton("Deposit", new Point(420, 200));
        GetBalance = new ScreenButton("Get Balance", new Point(235, 255));

        W1000 = new ScreenButton("1000", new Point(290, 180));
        W5000 = new ScreenButton("5000", new Point(415, 180));
        W10000 = new ScreenButton("10000", new Point(275, 215));
        W50000 = new ScreenButton("50000", new Point(415, 215));
        W100000 = new ScreenButton("100000", new Point(260, 250));
        W500000 = new ScreenButton("500000", new Point(415, 250));

        Stop = new ScreenButton("Stop", new Point(620, 400));
        Back = new ScreenButton("Back", new Point(580, 400));

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
        AmountWithdraw.add(W100000);
        AmountWithdraw.add(W500000);

        Actions = new ArrayList<>();
        Actions.add(Stop);
        Actions.add(Back);


        doTransactions();
    }

    private void doTransactions() {
        pinMode = false;
        pinLength = 0;
        pinInput = "";
        checkCard();
    }

    private void checkCard() {
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
        ActionPin.giveOutput("Please enter your pin: ");
        as.add(ActionPin);
        pinMode = true;
        pinCheck();
    }

    private void welcome() {
        InOut.giveOutput("Welcome " + client.getName());
        as.add(InOut);
        sleep(2);
        as.clear();
        home();
    }

    private void home() {
        ActionPin.giveOutput("Choose an action");
        as.add(ActionPin);
        addElement("Choice");
        while (true){
            if (Withdraw.getInput() == "Withdraw") {
                withdraw();
            } else if (Deposit.getInput() == "Deposit") {
                deposit();
            } else if (GetBalance.getInput() == "Get Balance") {
                amountText.giveOutput("Your balance is: " + client.getBalance(pinInput));
                as.add(amountText);
                home();
            } else if (Stop.getInput() == "Stop") {
                goodbye();
            }
        }
    }

    private void withdraw() {
        as.clear();
        ActionPin.giveOutput("Choose amount to withdraw");
        as.add(ActionPin);
        addElement("Withdraw");
        String temp;
        int Amount = 0;
        while (Amount < client.getBalance(pinInput)) {
            for (int i = 0; i < AmountWithdraw.size(); i++) {
                temp = AmountWithdraw.get(i).getInput();
                if (temp == "1000"){
                    Amount = 1000;
                } else if (temp == "5000"){
                    Amount = 5000;
                } else if (temp == "10000"){
                    Amount = 10000;
                } else if (temp == "50000"){
                    Amount = 50000;
                } else if (temp == "100000"){
                    Amount = 100000;
                } else if (temp == "500000"){
                    Amount = 500000;
                }
                if (Stop.getInput() == "Stop") {
                    goodbye();
                } else if (Back.getInput() == "Back") {
                    home();
                }
                displayText.giveOutput("You've withdrawn" + temp);
                as.add(displayText);
                client.Withdraw(Amount, pinInput);
                sleep(1);
                as.clear();
                displayText.giveOutput("Your new balance is: " + client.getBalance(pinInput));
                sleep(3);
                doTransactions();

            }
        }
    }

    private void deposit() {

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
            as.add(W100000);
            as.add(W500000);
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
                            amountText.giveOutput("");
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
                } else if (Correct.getInput() == "Cor"){
                    pinLength = 0;
                    s = "";
                }
            }
        }
    }



    public void pinCheck() {
        addElement("Keypad");
        String temp = buttonInput();
        if (client.checkPin(temp)){
            as.clear();
            welcome();
        } else {
            as.clear();
            displayText.giveOutput("Wrong pin entered");
            as.add(displayText);
            sleep(2);
            as.clear();
            login();
        }


    }
}

