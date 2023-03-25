package pointclub.pointclubclient.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pointclub.shared.model.User;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChatUser extends User {
    private List<Room> rooms;

    public ChatUser(String name) {
        super(name);
    }

    public ChatUser(int serverId, String name) {
        super(serverId, name);
    }

    public ChatUser(User currentUser) {
        super(currentUser.getServerId(), currentUser.getName());
    }
}
