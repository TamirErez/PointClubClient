package pointclub.pointclubclient.rest;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    String BASE_URL = "https://pointclub.herokuapp.com/";

    @GET("admin/isAlive")
    Call<Boolean> getIsAlive();
}