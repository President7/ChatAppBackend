package com.master.chat.controllers;

import com.master.chat.comfig.AppConstants;
import com.master.chat.entities.Message;
import com.master.chat.entities.Room;
import com.master.chat.repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(("/api/v1/rooms"))
@CrossOrigin(AppConstants.FRONT_END_BASE_URL)
public class RoomController {

    private RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // we can extends userInfo save in db and // eg. kis user ne kaun sa room create kiya etc.
    // create Room
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody String roomId) {
        if (roomRepository.findByRoomId(roomId) != null) {
            return ResponseEntity.badRequest().body("Room Already exists!");
        }
        Room room = new Room();
        room.setRoomId(roomId);
        Room roomSave = roomRepository.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomSave);
    }

    // he we can extend this project by
    // when user joined , details like which user join whihc user
    //get room
    @GetMapping("/{roomID}")
    public ResponseEntity<?> joinRoom(@PathVariable String roomID) {
        Room room = roomRepository.findByRoomId(roomID);
        if (room == null)
            return ResponseEntity.badRequest().body("Room Not Found!!");
        return ResponseEntity.ok(room);

    }

    // get message from room
    @GetMapping("/{roomID}/messages")
    public ResponseEntity<List<Message>> getMessages(
            @PathVariable String roomID,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "20", required = false) int size) {

        Room room = roomRepository.findByRoomId(roomID);
        if (room == null) {
            return ResponseEntity.badRequest().build();
        }
        // get mesage
        List<Message> messages = room.getMessages();
        //Pagination
        int start = Math.max(0, messages.size() - (page + 1) * size);
        int end = Math.min(messages.size(), start + size);
        List<Message> messagesPaginated = messages.subList(start, end);
       // save after pagination dome
        return ResponseEntity.ok(messagesPaginated);



    }
}
