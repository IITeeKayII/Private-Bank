/*
    Coen Schutte
    0976553
    TI1A
    21/06/2019
*/
import java.awt.*;

public class ATMScreen extends java.awt.Container{
    public ATMScreen(){
        super();                                        //roept de constructor van de super class aan
        setLayout(null);                                //Hierdoor is posititie van elementen aanpasbaar
    }

    //adds an element to the screen
    public void add(ScreenElement myElement) {
        myElement.setContainer(this);

    }

    //clears the screen
    public void clear(){
        removeAll();
    }

    //override de paint method van container
    public void paint(Graphics g) {
        super.paint(g);
    }
}
