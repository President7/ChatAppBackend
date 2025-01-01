package com.master.chat.controllers;
import com.master.chat.comfig.AppConstants;
import com.master.chat.entities.Message;
import com.master.chat.entities.Room;
import com.master.chat.payload.MessageRequest;
import com.master.chat.repository.RoomRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import java.time.LocalDateTime;

@Controller
@CrossOrigin(AppConstants.FRONT_END_BASE_URL)
public class ChatController {
    private RoomRepository roomRepository;

    public ChatController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @MessageMapping("/sendMessage/{roomId}")   //chat/sendMessage/roomId
    @SendTo("/topic/room/{roomId}")        // subscribe
    public Message sendMessage(
            @DestinationVariable String roomId,
            @RequestBody MessageRequest messageRequest) {

        Room room = roomRepository.findByRoomId(messageRequest.getRoomId());
       // Message message = new Message(messageRequest.getSender(), messageRequest.getContent());
        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setSender(messageRequest.getSender());
        message.setTimeStamp(LocalDateTime.now());
        if (room != null) {
            room.getMessages().add(message);
            roomRepository.save(room);
        } else
            throw new RuntimeException("Room Not Found !! ");
        return message;
    }
}
