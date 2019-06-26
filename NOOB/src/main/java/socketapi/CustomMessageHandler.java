package socketapi;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

/**
 * MessageHandler
 */
public class CustomMessageHandler {

    /* constructor */
    public CustomMessageHandler(String message, WebsocketClientEndpoint socket) {

        List<String> json = new Gson().fromJson(message, ArrayList.class);
        System.out.println(json.get(0));
    }
}