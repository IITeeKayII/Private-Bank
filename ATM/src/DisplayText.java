import java.awt.*;

public class DisplayText extends ScreenElement implements OutputDevice {

    Label label;
    public DisplayText(String Name, Point pos) {
        super(Name, pos);
        label = new Label();
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setBounds(pos.x, pos.y, 400, 35);
    }

    void setContainer(Container myContainer) {
        myContainer.add(label);
    }

    public String giveOutput(String Output) {
        label.setText(Output);
        return null;
    }
}
