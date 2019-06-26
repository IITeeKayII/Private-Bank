package socketapi;

import java.net.URI;
import javax.websocket.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main
 */
@SpringBootApplication
@ClientEndpoint
public class Main {

    private static Object waitLock = new Object();

    private static String URL = "ws://145.24.222.24:8080";

    public static Session slave;
    public static Session master;

    public static boolean status;

    @OnMessage
    public void onMessage(String message, Session slave) throws InterruptedException {

        /* Remove all escaping characters */
        message = message.replaceAll("\\\\", "");

        /* Cut off the JSON object */
        if (message.startsWith("[\"{")) {
            message = message.substring(2, message.length() - 2);

            /* Convert to GSON Object */
            JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();

            /* Get important parameters */
            if (jsonToString(jsonObject.get("Func")).equals("pinCheck")) {

                /* Set up the URL Parameters */
                String iban = jsonToString(jsonObject.get("IBAN"));
                String pin = jsonToString(jsonObject.get("PIN"));
                String UrlParameters = "iban=" + iban + "&pin=" + pin;

                int status = Integer
                        .parseInt(
                                jsonToString(new JsonParser()
                                        .parse(JavaPostRequest.sendPostRequest(
                                                "https://bowero.nl/api/clients/credentials.php", UrlParameters))
                                        .getAsJsonObject().get("status")));

                if (status == 0) {
                    /* Send true so the sender can continue */
                    Main.slave.getAsyncRemote().sendText("[\"True\"]");
                } else {
                    /* Send true so the sender can continue */
                    Main.slave.getAsyncRemote().sendText("[\"False\"]");
                }
            }

            /* Get important parameters */
            if (jsonToString(jsonObject.get("Func")).equals("withdraw")) {

                /* Set up the URL Parameters */
                String iban = jsonToString(jsonObject.get("IBAN"));
                String pin = jsonToString(jsonObject.get("PIN"));
                String amount = jsonToString(jsonObject.get("amount"));
                String UrlParameters = "iban=" + iban + "&pin=" + pin + "&amount=" + amount;

                boolean status = new JsonParser().parse(
                        JavaPostRequest.sendPostRequest("https://bowero.nl/api/clients/transfer.php", UrlParameters))
                        .getAsJsonObject().has("error");

                if (!status) {
                    /* Send true so the sender can continue */
                    Main.slave.getAsyncRemote().sendText("[\"True\"]");
                } else {
                    /* Send true so the sender can continue */
                    Main.slave.getAsyncRemote().sendText("[\"False\"]");
                }
            }
        } else {

            System.out.println(message);

            /* It is true of false */
            if (message.equals("[\"True\"]")) {
                status = true;
            } else {
                status = false;
            }
        }

        /* Sleep because we don't know if that does anything */
        Thread.sleep(500);
    }

    private static void waitForTerminationSignal() {
        synchronized (waitLock) {
            try {
                waitLock.wait();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    private static String jsonToString(JsonElement e) {
        return e.toString().replaceAll("\"", "");
    }

    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println("This application requires you to give one paramater.");
            System.err.println("Please use it like this:");
            System.err.println("java -jar noob-api-0.1.0.jar BANKCODE");
            System.exit(0);
        }

        SpringApplication.run(Main.class, args);

        WebSocketContainer container = null;

        try {
            container = ContainerProvider.getWebSocketContainer();
            slave = container.connectToServer(Main.class, URI.create(URL));
            master = container.connectToServer(Main.class, URI.create(URL));
            slave.getAsyncRemote().sendText("[\"register\", \"slave\", \"" + args[0] + "\"]");
            Thread.sleep(500);
            master.getAsyncRemote().sendText("[\"register\", \"master\", \"" + args[0] + "\"]");
            waitForTerminationSignal();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (slave != null) {
                try {
                    slave.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (master != null) {
                try {
                    master.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}