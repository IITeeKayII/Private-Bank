import java.awt.*;

public class ATMScreen extends java.awt.Container {
    public ATMScreen() {
        super();
        setLayout(null); //allow location to be set
    }
    public void add(ScreenElement myElement){
        myElement.setContainer(this);
    }
    public void clear(){
        removeAll();
    }
    public void paint(Graphics g) {
        super.paint(g);

    }
}
