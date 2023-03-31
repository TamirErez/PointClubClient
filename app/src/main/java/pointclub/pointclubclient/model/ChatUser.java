package pointclub.pointclubclient.model;

import com.orm.dsl.Table;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pointclub.shared.model.User;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "User")
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
