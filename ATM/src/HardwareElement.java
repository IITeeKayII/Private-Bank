public abstract class HardwareElement extends ATMElement {
    public HardwareElement(String Name) {
        super(Name);
    }
    boolean power = false;
    public void PowerOn(){
        power = true;
    }
    public void PowerOff(){
        power = false;
    }
}
