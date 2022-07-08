package pointclub.pointclubclient.model;

import com.orm.SugarRecord;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class Message extends SugarRecord<User> {
    private int messageId;
    private User sender;
    private String content;
    private Date sendTime;
    private int room;

    public Message(int messageId, User sender, String content, Date sendTime) {
        this.messageId = messageId;
        this.sender = sender;
        this.content = content;
        this.sendTime = sendTime;
    }
}
