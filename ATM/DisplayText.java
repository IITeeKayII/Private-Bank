/*
    Coen Schutte
    0976553
    TI1A
    21/06/2019
*/
import java.awt.*;

public class DisplayText extends ScreenElement implements OutputDevice {

    Label label;

    //properties of the label
    public DisplayText(String name, Point pos){
        super(name,pos);
        label = new Label();
        label.setForeground(Color.white);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setBounds(pos.x,pos.y,600,35);
    }

    //Adds the label the the screen
    void setContainer(Container MyContainer) {
        MyContainer.add(label);
    }

    //adds text to the label
    public String giveOutput(String output) {
        label.setText(output);
        return null;
    }
}
