package pointclub.pointclubclient.model;

import com.orm.SugarRecord;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message extends SugarRecord<Message> {
    private int serverId;
    private String content;
    private Date sendTime;
    private Room room;
    private User sender;
}
