/*
    Coen Schutte
    0976553
    TI1A
    21/06/2019
*/
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Connection{

    private HttpURLConnection con;
    private String url;

    public Connection(String url){
        this.url = url;
    }


    public int checkCredentials(String iban, String pin){
        String temp = this.url + "credentials.php";
        String urlParameters = "iban=" + iban + "&pin=" + pin;
        JSONObject message = this.sendPostJsonResponse(temp, urlParameters);
        if(message.containsKey("error")){
            System.out.println(message.get("error"));
        }
        int status = Math.toIntExact((long)message.get("status"));

        return status;
    }

    public String getBalance(String iban, String pin){
        String temp = this.url + "saldo.php";
        String urlParameters = "iban=" + iban + "&pin=" + pin;
        JSONObject message = this.sendPostJsonResponse(temp, urlParameters);
        if(message.containsKey("error")){
            System.out.println(message.get("error"));
            return "error";
        }
        return String.valueOf(message.get("saldo"));
    }

    public String withdraw(String iban, String pin, int amount){
        String temp = this.url + "transfer.php";
        String urlParameters = "iban=" + iban + "&pin=" + pin + "&amount=" + amount;
        JSONObject message = this.sendPostJsonResponse(temp, urlParameters);
        if(message.containsKey("error")){
            System.out.println(message.get("error"));
            return "error";
        }
        return String.valueOf(message.get("saldo"));
    }

    private JSONObject sendPostJsonResponse(String url, String urlParameters){
        JSONObject message = null;
        try{
            message = (JSONObject) new JSONParser().parse(this.sendPost(url, urlParameters));
        }
        catch(ParseException ex){}
        return message;
    }


    private String sendPost(String url, String urlParameters){
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        String response = null;

        try {

            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            response = content.toString();
        }
        catch(Exception e){}
        finally {
            con.disconnect();
        }
        return response;
    }

}