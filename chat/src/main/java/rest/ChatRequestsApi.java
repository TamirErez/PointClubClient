package rest;

import java.util.List;

import model.ChatUser;
import model.Message;
import model.Room;
import model.RoomWithUser;
import pointclub.shared.rest.RequestsApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ChatRequestsApi extends RequestsApi {
    @POST("user/add")
    Call<Integer> registerUser(@Body ChatUser user);

    @POST("room/add")
    Call<Integer> registerRoom(@Body Room room);

    @GET("room")
    Call<List<Room>> getAllRooms();

    @POST("room/addUser")
    Call<Void> addUserToRoom(@Body RoomWithUser roomWithUser);

    @POST("message/add")
    Call<Integer> addMessage(@Body Message message);
}