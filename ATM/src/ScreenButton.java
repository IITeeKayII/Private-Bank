import javax.naming.Name;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ScreenButton extends ScreenElement implements InputDevice, java.awt.event.ActionListener {

    Button button;
    boolean inputAvailable;

    public ScreenButton(String Name, Point pos) {
        super(Name, pos);
        button = new Button(Name);
        button.setName(Name);
        button.setBounds(pos.x, pos.y, 10 + 15 * Name.length(), 25);
        button.addActionListener(this);
    }

    void setContainer(Container myContainer) {
        myContainer.add(button);
    }

    public String getInput() {
        if(inputAvailable == true){
            inputAvailable = false;
            return this.Name;
            } else {
            return null;
        }
    }

    public void actionPerformed(ActionEvent e){
        inputAvailable = true;
        System.out.println(Name);
    }

    public void setColor(String color){
        Color myColor = Color.decode(color);
        button.setBackground(myColor);
    }
}
