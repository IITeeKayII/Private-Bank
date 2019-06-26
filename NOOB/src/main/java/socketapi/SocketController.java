package socketapi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class SocketController {

    @RequestMapping("/test")
    public Socket socket(@RequestParam(value = "revBank") String revBank,
            @RequestParam(value = "senBank") String senBank, @RequestParam(value = "func") String func,
            @RequestParam(value = "iban") String iban, @RequestParam(value = "pin") String pin, @RequestParam(value = "amount", required = false, defaultValue = "0") String amount) {
        return new Socket(revBank, senBank, func, iban, pin, amount);
    }

}