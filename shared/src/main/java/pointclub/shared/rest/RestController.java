package pointclub.shared.rest;

import java.util.function.Consumer;

import androidx.annotation.NonNull;
import pointclub.shared.model.User;
import pointclub.shared.service.log.LogService;
import pointclub.shared.service.log.LogTag;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestController {
    private static RestController instance = null;

    public void isAlive(Consumer<Response<Boolean>> result){
        callRest(RetrofitClient.getInstance().getRequestsApi().getIsAlive(), result);
    }

    public void updateToken(String token, Consumer<Response<Void>> result) {
        User currentUser = User.getCurrentUser();
        currentUser.setToken(token);
        callRest(RetrofitClient.getInstance().getRequestsApi().updateToken(currentUser), result);
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
