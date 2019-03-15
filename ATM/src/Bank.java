import java.util.*;
public class Bank{
public Map<String, Client> accounts;


        public Bank() {
                accounts = new HashMap<String ,Client>();
                accounts.put("1", new Client("Coen", "1234", 500));
                accounts.put("2", new Client("Kevin", "9876", 800));
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