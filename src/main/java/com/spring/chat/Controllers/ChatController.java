package com.spring.chat.Controllers;

import com.spring.chat.entities.Message;
import com.spring.chat.entities.Room;
import com.spring.chat.payload.MessageRequest;
import com.spring.chat.repositories.RoomRepository;
import org.springframework.cglib.core.Local;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@CrossOrigin(value = "https://chipper-cobbler-256032.netlify.app", allowCredentials = "true")
public class ChatController {

    private RoomRepository roomRepository;

    public ChatController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage(@DestinationVariable String roomId, @RequestBody MessageRequest messageRequest){

        Room room = roomRepository.findByRoomId(messageRequest.getRoomId());

        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setSender(messageRequest.getSender());
        message.setTimeStamp(LocalDateTime.now());
        if(room != null){
            room.getMessages().add(message);
            roomRepository.save(room);
        }
        else{
            throw new RuntimeException("Room not found!!");
        }

        return message;
    }
}
