package rest;

import java.util.List;

import pointclub.shared.model.User;
import pointclub.shared.model.chat.Message;
import pointclub.shared.model.chat.Room;
import pointclub.shared.model.chat.RoomWithUser;
import pointclub.shared.rest.RequestsApi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ChatRequestsApi extends RequestsApi {

    @POST("message/add")
    Call<Integer> addMessage(@Body Message message);
}