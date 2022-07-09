package pointclub.pointclubclient.model;

import com.orm.SugarRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Room extends SugarRecord<Room> {

    private int roomId;
    private String name;

    public Room(String name) {
        this.name = name;
    }

    public Room(int roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }
}