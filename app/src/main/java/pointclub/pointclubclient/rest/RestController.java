package pointclub.pointclubclient.rest;

import java.util.function.Consumer;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestController {
    private static RestController instance = null;

    public void isAlive(Consumer<Response<Boolean>> result){
        callRest(RetrofitClient.getInstance().getRequestsApi().getIsAlive(), result);
    }

    private <T> void callRest(Call<T> call, Consumer<Response<T>> result) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                result.accept(response);
            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                throw new RuntimeException(t);
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
