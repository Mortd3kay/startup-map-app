package com.skyletto.startappbackend.repositories;

import com.skyletto.startappbackend.entities.Message;
import com.skyletto.startappbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "select * from messages m where (m.sender_id=?1 and m.receiver_id = ?2) or (m.sender_id=?2 and m.receiver_id = ?1)", nativeQuery = true)
    List<Message> getMessagesByUsers(long id, long friendId);
    @Query(value =
            "select * from messages " +
            "where id in " +
            "(select max(m.id) id from messages m, " +
            "(select * from " +
            "(select receiver_id a1, sender_id a2 from messages where receiver_id > sender_id " +
            "union " +
            "select sender_id a1, receiver_id a2 from messages where sender_id >= receiver_id) b " +
            "where a1 = ?1 or a2 = ?1) a " +
            "where (m.sender_id = a.a1 or m.receiver_id = a.a1) and (m.sender_id = a.a2 or m.receiver_id = a.a2) " +
            "group by a.a1, a.a2)",
            nativeQuery = true)
    List<Message> getLastMessages(long id);
}
