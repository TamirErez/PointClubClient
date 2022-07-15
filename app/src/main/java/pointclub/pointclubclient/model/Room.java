package pointclub.pointclubclient.model;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Room extends SugarRecord<Room> {

    private int roomId;
    private String name;
    @Ignore
    private List<User> users = new ArrayList<>();

    public Room(String name) {
        this.name = name;
    }

    public Room(int roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public List<Message> getMessages() {
        return Message.find(Message.class, "room = ?", String.valueOf(roomId));
    }
}