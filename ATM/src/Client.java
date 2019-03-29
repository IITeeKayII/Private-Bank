public class Client {
    private String Name;
    private String pin;
    private int balance;

    public Client(String Name, String pin, int balance) {
        this.Name = Name;
        this.pin = pin;
        this.balance = balance;
    }

    public String getName() {
        return Name;
    }

    public boolean checkPin(String pin) {
        if (this.pin.equals(pin)) {
            System.out.println("Pin Correct");
            return true;
        } else{
            System.out.println("Pin incorrect");
            return false;
        }
    }

    public int getBalance(String pin) {
        if (checkPin(this.pin) == true) {
            return balance;
        } else {
            return Integer.MIN_VALUE;
        }
    }
    public void Deposit(int deposited){
    balance = balance + deposited;
    }
    public boolean Withdraw(int ToWithdraw, String pin){
        checkPin(this.pin);
        if (checkPin(this.pin) == true && balance >= ToWithdraw){
            balance = balance - ToWithdraw;
            return true;
        } else if(checkPin(this.pin) == false){
            System.out.println("Pin incorrect");
            return false;
        } else if (balance < ToWithdraw){
            System.out.println("Balance too low");
            return false;
        }else {
            return false;
        }
    }

    public String getPin() {
        return pin;
    }
}
