package rest;

import java.util.List;

import pointclub.shared.model.chat.Message;
import pointclub.shared.model.chat.Room;
import pointclub.shared.rest.RequestsApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatRequestsApi extends RequestsApi {

    @POST("message/add")
    Call<Integer> addMessage(@Body Message message);

    @POST("room/messages")
    Call<List<Message>> getRoomMessages(@Body Room room);
}