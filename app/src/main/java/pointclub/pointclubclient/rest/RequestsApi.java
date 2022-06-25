package pointclub.pointclubclient.rest;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestsApi {
    @GET("admin/isAlive")
    Call<Boolean> getIsAlive();
}