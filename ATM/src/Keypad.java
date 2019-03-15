import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Keypad extends HardwareElement implements InputDevice{

    BufferedReader buffer;
    public Keypad(String Name) {
        super(Name);
        buffer = new BufferedReader(new InputStreamReader(System.in));
    }

    public String getInput() {
        try {
            if(buffer.ready()){
                String Input = buffer.readLine();
                return Input;
            } else {
                return null;
            }
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

}
