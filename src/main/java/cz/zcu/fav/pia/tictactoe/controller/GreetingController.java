package cz.zcu.fav.pia.tictactoe.controller;

import cz.zcu.fav.pia.tictactoe.model.Greeting;
import cz.zcu.fav.pia.tictactoe.model.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import static org.springframework.web.util.HtmlUtils.htmlEscape;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/client/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        return new Greeting("Hello, " + htmlEscape(message.getName()) + "!");
    }

}
