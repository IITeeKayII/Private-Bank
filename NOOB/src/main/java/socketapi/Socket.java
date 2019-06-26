package socketapi;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class Socket {

    public String func, iban, revBank, senBank, pin, amount;
    public boolean status;

    public Socket(String revBank, String senBank, String func, String iban, String pin, String amount) {
        this.func = func;
        this.iban = iban;
        this.revBank = revBank;
        this.senBank = senBank;
        this.pin = pin;
        this.amount = amount;

        Request data = new Request();
        data.Func = func;
        data.IBAN = iban;
        data.IDRecBank = revBank;
        data.IDSenBank = senBank;
        data.PIN = pin;
        data.amount = amount;
        String json = new Gson().toJson(data);

        List<String> mainData = new ArrayList<String>();
        mainData.add(revBank);
        mainData.add(json);
        String mainJson = new Gson().toJson(mainData);

        Main.master.getAsyncRemote().sendText(mainJson);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setStatus(Main.status);
    }

    /* setters */

    public void setFunc(String func) {
        this.func = func;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setRevBank(String revBank) {
        this.revBank = revBank;
    }

    public void setSenBank(String senBank) {
        this.senBank = senBank;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    /* getters */

    public String setFunc() {
        return func;
    }

    public String setIban() {
        return iban;
    }

    public String setRevBank() {
        return revBank;
    }

    public String setSenBank() {
        return senBank;
    }

    public String setPin() {
        return pin;
    }

    public String getAmount() {
        return amount;
    }

    public boolean getStatus() {
        return status;
    }
}