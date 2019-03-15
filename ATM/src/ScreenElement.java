import java.awt.*;

public abstract class ScreenElement extends ATMElement {

    public ScreenElement(String Name, Point pos) {
        super(Name);

    }
    abstract void setContainer(Container myContainer);
}

