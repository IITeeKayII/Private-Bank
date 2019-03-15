public class RecieptPrinter extends HardwareElement implements OutputDevice{
    public RecieptPrinter(String Name) {
        super(Name);
    }

    public String giveOutput(String Output) {
        System.out.println(Output);
        return null;
    }
}
