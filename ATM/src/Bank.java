import java.util.*;
public class Bank{
public Map<String, Client> accounts;


        public Bank() {
                accounts = new HashMap<String ,Client>();
                accounts.put("1", new Client("Coen", "1234", 50000));
                accounts.put("2", new Client("Kevin", "9876", 80000));
                accounts.put("209EA3A5", new Client("card", "1472", 100000));
        }
        public Client get(String rekeningnummer){
                if(accounts.containsKey(rekeningnummer)){
                        System.out.println("Account found");
                        return accounts.get(rekeningnummer);
                } else {
                        return null;
                }
        }



}