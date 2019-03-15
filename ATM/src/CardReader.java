import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CardReader extends HardwareElement implements InputDevice {

    BufferedReader buffer;

    public CardReader(String Name) {
        super(Name);
        buffer = new BufferedReader(new InputStreamReader(System.in));
    }

    public String getInput(){
        try {
            System.out.println("Please enter your card number: ");
            String Input = buffer.readLine();
            return Input;
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }

    }
}