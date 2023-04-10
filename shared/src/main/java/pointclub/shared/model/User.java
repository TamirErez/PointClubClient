package pointclub.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends PointclubRecord {
    private String name;
    private String token;

    @Ignore
    @JsonIgnore
    private static User currentUser;

    public User(String name) {
        this.name = name;
    }

    public User(int serverId, String name, String token) {
        super(serverId);
        this.name = name;
        this.token = token;
    }

    public User(int serverId, String name) {
        super(serverId);
        this.name = name;
    }

    public static User getCurrentUser() {
        if (currentUser == null) {
            currentUser = SugarRecord.findWithQuery(User.class,
                            "SELECT * FROM USER WHERE ID=(SELECT MIN(ID) FROM USER)")
                    .get(0);
        }
        return currentUser;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
