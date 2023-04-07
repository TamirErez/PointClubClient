package pointclub.shared.model.chat;

import com.orm.SugarRecord;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Room extends SugarRecord implements Serializable {
    private int serverId;
    private String name;

    public Room(String name) {
        this.name = name;
    }

    public Room(int serverId, String name) {
        this.serverId = serverId;
        this.name = name;
    }
}