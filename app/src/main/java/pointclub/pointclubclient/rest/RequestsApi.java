package pointclub.pointclubclient.rest;

import java.util.List;

import pointclub.pointclubclient.model.Message;
import pointclub.pointclubclient.model.Room;
import pointclub.pointclubclient.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RequestsApi {
    @GET("admin/isAlive")
    Call<Boolean> getIsAlive();

    @POST("user/add")
    Call<Integer> registerUser(@Body User user);

    @POST("room/add")
    Call<Integer> registerRoom(@Body Room room);

    @GET("room")
    Call<List<Room>> getAllRooms();

    @POST("message/add")
    Call<Integer> addMessage(@Body Message message);

    @PUT("user/updateToken")
    Call<Void> updateToken(@Body User user);
}