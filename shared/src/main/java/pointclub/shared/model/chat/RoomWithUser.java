package pointclub.shared.model.chat;
import com.orm.SugarRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
public class RoomWithUser extends SugarRecord {
   private int user;
   private int room;
}