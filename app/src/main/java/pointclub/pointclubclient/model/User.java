package pointclub.pointclubclient.model;

import com.orm.SugarRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class User extends SugarRecord<User> {
    private int serverId;
    private String name;

    public User(String name) {
        this.name = name;
    }

    public User(int serverId, String name) {
        this.serverId = serverId;
        this.name = name;
    }

    public static User getCurrentUser(){
        return findAll(User.class).next();
    }
}
