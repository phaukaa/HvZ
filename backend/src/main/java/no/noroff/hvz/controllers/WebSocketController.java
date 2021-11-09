package no.noroff.hvz.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/websocket")
    @SendTo("/socket/notify")
    public String socketNotification(String msg) {
        return "";
    }
}
