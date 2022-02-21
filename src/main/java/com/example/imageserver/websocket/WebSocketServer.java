package com.example.imageserver.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WebSocketServer {
    private SimpMessagingTemplate template;

    @Autowired
    public WebSocketServer(SimpMessagingTemplate template) {
        this.template = template;
    }

    @RequestMapping(path = "/notify", method = RequestMethod.POST)
    public void NotifyClients(String message){
        this.template.convertAndSend("/topic/notify", message);
    }
}
