package com.sas.dhop.site.repository.nosql;

import com.sas.dhop.site.model.nosql.Room;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RoomRepository extends MongoRepository<Room, String> {

	Optional<Room> findByRoomId(String roomId);

	@Query("{ 'roomId' : {$regex:  ?0}}")
	List<Room> findByRoomIdRegex(String regex);

	List<Room> findByRoomIdContaining(String roomId);
}
