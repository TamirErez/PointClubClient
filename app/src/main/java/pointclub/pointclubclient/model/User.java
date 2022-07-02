package pointclub.pointclubclient.model;

import com.orm.SugarRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class User extends SugarRecord<User> {
    private int userId;
    private String name;

    public User(String name) {
        this.name = name;
    }

    public User(int userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}
