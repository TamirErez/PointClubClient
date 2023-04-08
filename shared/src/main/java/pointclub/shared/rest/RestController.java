package pointclub.shared.rest;

import java.util.List;
import java.util.function.Consumer;

import androidx.annotation.NonNull;

import pointclub.shared.model.User;
import pointclub.shared.model.chat.Room;
import pointclub.shared.model.chat.RoomWithUser;
import pointclub.shared.service.log.LogService;
import pointclub.shared.service.log.LogTag;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestController {
    private static RestController instance = null;
    private static final RequestsApi requestsApi = RetrofitClient.getInstance().getRequestsApi();

    public void isAlive(Consumer<Response<Boolean>> result) {
        callRest(requestsApi.getIsAlive(), result);
    }

    public void updateToken(String token, Consumer<Response<Void>> result) {
        User currentUser = User.getCurrentUser();
        currentUser.setToken(token);
        callRest(requestsApi.updateToken(currentUser), result);
    }

    public void registerUser(String username, Consumer<Response<Integer>> result) {
        callRest(requestsApi.registerUser(new User(username)), result);
    }

    public void registerRoom(String roomName, Consumer<Response<Integer>> result) {
        callRest(requestsApi.registerRoom(new Room(roomName)), result);
    }

    public void getAllRooms(Consumer<Response<List<Room>>> result) {
        callRest(requestsApi.getAllRooms(), result);
    }

    public void addUserToRoom(RoomWithUser roomWithUser, Consumer<Response<Void>> result) {
        callRest(requestsApi.addUserToRoom(roomWithUser), result);
    }

    public void getRoomUsers(Room room, Consumer<Response<List<User>>> result) {
        callRest(requestsApi.getRoomUsers(room), result);
    }

    protected <T> void callRest(Call<T> call, Consumer<Response<T>> result) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                result.accept(response);
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                LogService.warn(LogTag.REST_ERROR, t.getMessage());
                result.accept(null);
            }
        });
    }

    public static RestController getInstance() {
        if (instance == null) {
            instance = new RestController();
        }
        return instance;
    }
}
