package pointclub.pointclubclient.model;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RoomWithUser {
   private int user;
   private int room;
}