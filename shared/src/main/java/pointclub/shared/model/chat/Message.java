package pointclub.shared.model.chat;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pointclub.shared.model.PointclubRecord;
import pointclub.shared.model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message extends PointclubRecord {
    private String content;
    private Date sendTime;
    private int roomId = -1;
    private int senderId = -1;

    public Message(int serverId, String content, Date sendTime, int roomId, int senderId) {
        super(serverId);
        this.content = content;
        this.sendTime = sendTime;
        this.roomId = roomId;
        this.senderId = senderId;
    }

    public User getSender() {
        return User.find(User.class, "server_id=?", String.valueOf(senderId)).iterator().next();
    }

    public Room getRoom() {
        return Room.find(Room.class, "server_id=?", String.valueOf(roomId)).iterator().next();
    }

    public static List<Message> getMessagesOfRoom(int roomId){
        return Message.find(Message.class, "room_id=?", String.valueOf(roomId));
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
