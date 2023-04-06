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
    private int serverId;
    private String name;
    @Ignore
    private List<ChatUser> users = new ArrayList<>();

    public Room(String name) {
        this.name = name;
    }

    public Room(int serverId, String name) {
        this.serverId = serverId;
        this.name = name;
    }

    public List<Message> getMessages() {
        return Message.find(Message.class, "room = ?", String.valueOf(serverId));
    }
}