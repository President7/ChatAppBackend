package com.master.chat.repository;

import com.master.chat.entities.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {

    //get Room Using Room Id
    Room findByRoomId( String roomID);
}
