package pointclub.shared.rest;

import pointclub.shared.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface RequestsApi {
    @GET("admin/isAlive")
    Call<Boolean> getIsAlive();

    @PUT("user/updateToken")
    Call<Void> updateToken(@Body User user);
}