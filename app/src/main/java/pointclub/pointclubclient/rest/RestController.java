package pointclub.pointclubclient.rest;

import java.util.List;
import java.util.function.Consumer;

import androidx.annotation.NonNull;
import pointclub.pointclubclient.model.Message;
import pointclub.pointclubclient.model.Room;
import pointclub.pointclubclient.model.RoomWithUser;
import pointclub.pointclubclient.model.User;
import pointclub.pointclubclient.service.log.LogService;
import pointclub.pointclubclient.service.log.LogTag;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestController {
    private static RestController instance = null;

    public void isAlive(Consumer<Response<Boolean>> result){
        callRest(RetrofitClient.getInstance().getRequestsApi().getIsAlive(), result);
    }

    public void registerUser(String username, Consumer<Response<Integer>> result) {
        callRest(RetrofitClient.getInstance().getRequestsApi().registerUser(new User(username)), result);
    }

    public void registerRoom(String roomName, Consumer<Response<Integer>> result) {
        callRest(RetrofitClient.getInstance().getRequestsApi().registerRoom(new Room(roomName)), result);
    }

    public void getAllRooms(Consumer<Response<List<Room>>> result) {
        callRest(RetrofitClient.getInstance().getRequestsApi().getAllRooms(), result);
    }

    public void sendMessage(Message newMessage, Consumer<Response<Integer>> result) {
        callRest(RetrofitClient.getInstance().getRequestsApi().addMessage(newMessage), result);
    }

    public void addUserToRoom(RoomWithUser roomWithUser, Consumer<Response<Void>> result) {
        callRest(RetrofitClient.getInstance().getRequestsApi().addUserToRoom(roomWithUser), result);
    }

    public void updateToken(String token, Consumer<Response<Void>> result) {
        User currentUser = User.getCurrentUser();
        currentUser.setToken(token);
        callRest(RetrofitClient.getInstance().getRequestsApi().updateToken(currentUser), result);
    }

    private <T> void callRest(Call<T> call, Consumer<Response<T>> result) {
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
