/*
    Coen Schutte
    0976553
    TI1A
    21/06/2019
*/
import java.awt.*;

public abstract class ScreenElement extends ATMElement {

    public ScreenElement(String name, Point pos){
        super(name);
    }

    abstract void setContainer(Container MyContainer);
}
