package pointclub.shared.model.chat;
import com.orm.SugarRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pointclub.shared.model.User;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class RoomWithUser extends SugarRecord {
   private User user;
   private Room room;
}