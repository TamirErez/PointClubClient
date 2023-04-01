package enums;

import activity.ChessActivity;
import activity.RoomListActivity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MenuOptions {
    Chat(RoomListActivity.class),
    Chess(ChessActivity.class);

    @Getter
    private final Class activityClass;
}