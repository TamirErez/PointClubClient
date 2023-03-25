package pointclub.pointclubclient.rest;

import java.util.List;

import pointclub.pointclubclient.model.ChatUser;
import pointclub.pointclubclient.model.Message;
import pointclub.pointclubclient.model.Room;
import pointclub.pointclubclient.model.RoomWithUser;
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