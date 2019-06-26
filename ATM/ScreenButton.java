/*
    Coen Schutte
    0976553
    TI1A
    21/06/2019
*/
import java.awt.*;
import java.awt.event.ActionEvent;

public class ScreenButton extends ScreenElement implements InputDevice, java.awt.event.ActionListener{

    Button button;
    boolean inputAvailable;

    public ScreenButton(String name, Point pos) {
        super(name, pos);
        button = new Button(name);
        button.setName(name);
        button.setBounds(pos.x,pos.y, 10 + 15 * name.length(), 25);
        button.addActionListener(this);
    }

    void setContainer(Container MyContainer) {
        MyContainer.add(button);
    }

    //returns the name of the pressed button
    public String getInput() {
        if(inputAvailable){
            inputAvailable = false;
            return this.name;
        }
        return null;
    }

    //Sets the color of the buttons
    public void setColor(String color) {
        if (color.equals("red")) {
            button.setBackground(Color.red);
        } else if (color.equals("yellow")) {
            button.setBackground(Color.yellow);
        } else if (color.equals("green")) {
            button.setBackground(Color.green);
        } else {
            button.setBackground(Color.gray);
        }
    }

    //activates when the button is clicked
    public void actionPerformed(ActionEvent e) {
        inputAvailable = true;
    }
}
