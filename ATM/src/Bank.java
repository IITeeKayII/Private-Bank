import java.util.*;
public class Bank{
public Map<String, Client> accounts;


        public Bank() {
                accounts = new HashMap<String ,Client>();
                accounts.put("32158163165", new Client("card", "1472", 100000));
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