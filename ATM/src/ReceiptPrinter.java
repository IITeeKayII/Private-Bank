public class ReceiptPrinter extends HardwareElement implements OutputDevice{
    public ReceiptPrinter(String Name) {
        super(Name);
    }

    public String giveOutput(String Output) {
        System.out.println(Output);
        return null;
    }
}
