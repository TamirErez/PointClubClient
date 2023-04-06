package pointclub.shared.rest;

import java.util.List;

import pointclub.shared.model.User;
import pointclub.shared.model.chat.Room;
import pointclub.shared.model.chat.RoomWithUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RequestsApi {
    @GET("admin/isAlive")
    Call<Boolean> getIsAlive();

    @PUT("user/updateToken")
    Call<Void> updateToken(@Body User user);

    @POST("user/add")
    Call<Integer> registerUser(@Body User user);

    @POST("room/add")
    Call<Integer> registerRoom(@Body Room room);

    @GET("room")
    Call<List<Room>> getAllRooms();

    @POST("room/addUser")
    Call<Void> addUserToRoom(@Body RoomWithUser roomWithUser);
}