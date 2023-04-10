package pointclub.shared.model.chat;

import lombok.Data;
import lombok.NoArgsConstructor;
import pointclub.shared.model.PointclubRecord;

@Data
@NoArgsConstructor
public class Room extends PointclubRecord {
    private String name;

    public Room(String name) {
        this.name = name;
    }

    public Room(int serverId, String name) {
        super(serverId);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}