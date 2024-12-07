package com.spring.chat.Controllers;

import com.spring.chat.entities.Message;
import com.spring.chat.entities.Room;
import com.spring.chat.repositories.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin("*")
public class RoomController {

    private RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    //create room

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody String roomId){

        if(roomRepository.findByRoomId(roomId) != null){
            return ResponseEntity.badRequest().body("Room already exists");
        }

        Room room = new Room();
        room.setRoomId(roomId);
        Room savedRoom = roomRepository.save(room);

        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }



    //get room

    @GetMapping("/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId){
        if(roomRepository.findByRoomId(roomId) == null){
            return ResponseEntity.badRequest().body("Room not found");
        }
        return ResponseEntity.ok(roomRepository.findByRoomId(roomId));
    }


    //get messages of the room

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String roomId,
                                                     @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                     @RequestParam(value = "size", defaultValue = "20", required = false) int size
    ){

        Room room = roomRepository.findByRoomId(roomId);
        if(room == null){
            return ResponseEntity.badRequest().build();
        }

        List<Message> messages = room.getMessages();
        int start = Math.max(0, messages.size() - (page + 1) * size);
        int end = Math.min(messages.size(), start + size);
        List<Message> paginatedMessages = messages.subList(start, end);
        return ResponseEntity.ok(paginatedMessages);


    }

}
