package co.develhope.websocket1.controller;

import co.develhope.websocket1.entities.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController{

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // invio messaggio
    @PostMapping("/broadcast")
    public ResponseEntity broadcastMessage(@RequestBody MessageDTO messageDTO){
        // ricevo oggetti di tipo MessageDTO e le inoltro in un canale /broadcast
    simpMessagingTemplate.convertAndSend("/topic/broadcast", messageDTO);
    return  ResponseEntity.accepted().body("Messagio "+ messageDTO.getMessage() + " inviato al /topic/broadcast");
    }

    // ricezione messaggio e inoltro verso un altro canale(usa la precedente)
    @MessageMapping("/hello")
    @SendTo("/topic/broadcast")
    public MessageDTO sendNameToChat(ClientMessageDTO clientMessage){
        return new MessageDTO("Frontend", "Messaggio da " + clientMessage.getFrom() + ": " + clientMessage.getText());
    }

}

class ClientMessageDTO{

    private String from;
    private String text;

    public ClientMessageDTO() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}