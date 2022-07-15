package pointclub.pointclubclient.rest;

import pointclub.pointclubclient.model.Room;
import pointclub.pointclubclient.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RequestsApi {
    @GET("admin/isAlive")
    Call<Boolean> getIsAlive();

    @POST("user/add")
    Call<Integer> registerUser(@Body User user);

    @POST("room/add")
    Call<Integer> registerRoom(@Body Room room);
}