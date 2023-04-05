package rest;

import java.util.List;
import java.util.function.Consumer;

import pointclub.shared.model.User;
import pointclub.shared.model.chat.Message;
import pointclub.shared.model.chat.Room;
import pointclub.shared.model.chat.RoomWithUser;
import pointclub.shared.rest.RestController;
import retrofit2.Response;

public class ChatRestController extends RestController {

    private static ChatRestController instance;
    private final ChatRequestsApi requestsApi;

    public void registerUser(String username, Consumer<Response<Integer>> result) {
        callRest(requestsApi.registerUser(new User(username)), result);
    }

    public void registerRoom(String roomName, Consumer<Response<Integer>> result) {
        callRest(requestsApi.registerRoom(new Room(roomName)), result);
    }

    public void getAllRooms(Consumer<Response<List<Room>>> result) {
        callRest(requestsApi.getAllRooms(), result);
    }

    public void sendMessage(Message newMessage, Consumer<Response<Integer>> result) {
        callRest(requestsApi.addMessage(newMessage), result);
    }

    public void addUserToRoom(RoomWithUser roomWithUser, Consumer<Response<Void>> result) {
        callRest(requestsApi.addUserToRoom(roomWithUser), result);
    }

    public static ChatRestController getInstance() {
        if (instance == null) {
            instance = new ChatRestController();
        }
        return instance;
    }

    private ChatRestController() {
        requestsApi = ChatRetrofitClient.getInstance().getRequestsApi();
    }
}
