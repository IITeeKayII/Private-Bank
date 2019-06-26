/*
    Coen Schutte
    0976553
    TI1A
    21/06/2019
*/
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;

public class MyWindowAdapter extends WindowAdapter {

    Frame f;

    MyWindowAdapter(Frame f){
        this.f = f;
    }

    //method that disposes the screen
    public void windowClosing(WindowEvent e){
        f.dispose();
        System.exit(0);
    }
}
