import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ATM{
    ATMScreen as;
    Bank MyBank;
    CardReader cardReader = new CardReader("cardreader");
    ArrayList<InputDevice> buttons;

    ScreenButton but1 = new ScreenButton("1", new Point(350, 150));
    ScreenButton but2 = new ScreenButton("2", new Point(385, 150));
    ScreenButton but3 = new ScreenButton("3", new Point(420, 150));
    ScreenButton but4 = new ScreenButton("4", new Point(350, 185));
    ScreenButton but5 = new ScreenButton("5", new Point(385, 185));
    ScreenButton but6 = new ScreenButton("6", new Point(420, 185));
    ScreenButton but7 = new ScreenButton("7", new Point(350, 220));
    ScreenButton but8 = new ScreenButton("8", new Point(385, 220));
    ScreenButton but9 = new ScreenButton("9", new Point(420, 220));
    ScreenButton but0 = new ScreenButton("0", new Point(385, 255));
    ScreenButton withdraw = new ScreenButton("withdraw", new Point(200, 200));
    ScreenButton deposit = new ScreenButton("deposit", new Point(400, 200));
    ScreenButton Correct = new ScreenButton("Cor", new Point(320, 255));
    ScreenButton Stop = new ScreenButton("Stop", new Point(420, 255));
    ScreenButton Get_Saldo = new ScreenButton("Get Saldo", new Point(200, 235));


    Keypad keypad = new Keypad("keypad");;
    public ATM(Bank bank) {
        this.MyBank = bank;
        as = new ATMScreen();
        Frame f = new Frame("My ATM");
        f.setBounds(500, 300, 800, 480);
        f.setBackground(Color.GRAY);
        f.addWindowListener(new MyWindowAdapter(f));
        f.add(as);
        f.setVisible(true);

        buttons = new ArrayList<>();


        doTransactions();
    }
    private void doTransactions() {
        String pinInput = "";
        String checkInput;
        String checkButtonInput = "";
        Client getClient;
        Boolean pinCheck;

        DisplayText enterCard = new DisplayText("enterCard", new Point(275, 200));
        DisplayText enterPin = new DisplayText("enterpin", new Point(275, 75));
        DisplayText StopBut = new DisplayText("StopBut", new Point(300, 200));
        DisplayText number1 = new DisplayText("Pin1", new Point(350, 100));
        DisplayText number2 = new DisplayText("Pin1", new Point(375, 100));
        DisplayText number3 = new DisplayText("Pin1", new Point(400, 100));
        DisplayText number4 = new DisplayText("Pin1", new Point(425, 100));
        number1.giveOutput("*");
        number2.giveOutput("*");
        number3.giveOutput("*");
        number4.giveOutput("*");
        StopBut.giveOutput("GoodBye!");
        enterCard.giveOutput("Please enter your card...");
        enterPin.giveOutput("Please enter your pin....");
        as.add(enterCard);
        String cardNumber = cardReader.getInput();
        getClient = MyBank.get(cardNumber);
        System.out.println(keypad.getInput());
        System.out.println(getClient.getName());
        System.out.println(getClient.getPin());

        buttons.add(but1);
        buttons.add(but2);
        buttons.add(but3);
        buttons.add(but4);
        buttons.add(but5);
        buttons.add(but6);
        buttons.add(but7);
        buttons.add(but8);
        buttons.add(but9);
        buttons.add(but0);
        buttons.add(Correct);
        buttons.add(Stop);
        buttons.add(keypad);
        buttons.add(withdraw);
        buttons.add(deposit);
        buttons.add(Correct);

        if(getClient != null) {
            as.clear();

            as.add(enterPin);


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
            as.add(Correct);
            as.add(Stop);

            do {
                for (int i = 0; i < 16; i++) {
                    checkButtonInput = buttons.get(i).getInput();
                    as.add(number1);

                   try {
                       if (checkButtonInput == null) {
                           checkButtonInput = "";
                       } else if (checkButtonInput == "Stop") {

                           as.clear();
                           as.add(StopBut);
                           TimeUnit.SECONDS.sleep(3);
                           as.clear();
                           doTransactions();
                       } else if(checkButtonInput == "cor"){
                           pinInput = "";

                       }else {
                           pinInput += checkButtonInput;
                           checkButtonInput = "";
                           System.out.println(pinInput);
                       }
                   }catch (Exception e){
                       System.out.println(e);
                   }
                }


                checkInput = keypad.getInput();
                if (checkInput == null) {
                    checkInput = "";
                } else if (checkInput.length() > 1) {
                    System.out.println("Please enter 1 digit at a time");
                    checkInput = "";
                } else {
                    pinInput += checkInput;
                    System.out.println(pinInput);
                    checkInput = "";
                }
                pinInput += checkButtonInput;
                checkButtonInput = "";
            } while (pinInput.length() < 4);

            pinCheck = getClient.checkPin(pinInput);
            try{
                if (pinCheck) {
                    getClient.getBalance(pinInput);
                    DisplayText CorrectPin = new DisplayText("CorrectPin", new Point(275,150));
                    CorrectPin.giveOutput("Pin Correct");
                    as.clear();
                    as.add(CorrectPin);
                    as.add(withdraw);
                    as.add(deposit);
                    as.add(Stop);
                    as.add(Get_Saldo);
                    if(checkButtonInput == "Stop"){
                        as.clear();
                        as.add(StopBut);
                        TimeUnit.SECONDS.sleep(3);
                        as.clear();
                        doTransactions();
                    } else if (checkButtonInput == "Get Saldo"){
                        DisplayText Saldo = new DisplayText("Saldo", new Point(200, 200));
                        Saldo.giveOutput("your saldo is: ...");
                        getClient.getBalance(pinInput);
                    }
                } else {
                    pinInput = "";
                    System.out.println("Account not found");
                    as.clear();
                    DisplayText Wrongcard = new DisplayText("wrongCard", new Point(275, 200));
                    Wrongcard.giveOutput("Wrong pin entered");
                    as.add(Wrongcard);
                    TimeUnit.SECONDS.sleep(5);
                    as.clear();
                    doTransactions();
                }
            } catch (Exception e){
                System.out.println(e);
            }
        }
    }
}