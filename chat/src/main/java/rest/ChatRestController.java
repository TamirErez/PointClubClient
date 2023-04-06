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

    public void sendMessage(Message newMessage, Consumer<Response<Integer>> result) {
        callRest(requestsApi.addMessage(newMessage), result);
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
