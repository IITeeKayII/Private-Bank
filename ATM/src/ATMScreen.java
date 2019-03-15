import java.awt.*;

public class ATMScreen extends java.awt.Container {
    public ATMScreen() {
        super();
        setLayout(null);
    }
    public void add(ScreenElement myElement){
        myElement.setContainer(this);
    }
    public void clear(){
        removeAll();
    }
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.RED);
        g.fillRoundRect(750, 406, 35, 35, 10, 10);
        g.fillRect(780, 436, 5, 5);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        g.drawString("HR", 753, 425);
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.drawString("bank", 754, 435);
    }
}
