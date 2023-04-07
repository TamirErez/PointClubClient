package pointclub.shared.model.chat;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pointclub.shared.model.User;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message extends SugarRecord {
    private int serverId;
    private String content;
    private Date sendTime;
    private int roomId;
    private int senderId;

    public User getSender() {
        return User.find(User.class, "server_id=?", String.valueOf(senderId)).iterator().next();
    }

    public Room getRoom() {
        return Room.find(Room.class, "server_id=?", String.valueOf(roomId)).iterator().next();
    }

    public static List<Message> getMessagesOfRoom(int roomId){
        return Message.find(Message.class, "room_id=?", String.valueOf(roomId));
    }
}
