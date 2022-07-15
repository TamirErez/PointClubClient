package pointclub.pointclubclient.rest;

import java.util.function.Consumer;

import androidx.annotation.NonNull;
import pointclub.pointclubclient.model.Room;
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
