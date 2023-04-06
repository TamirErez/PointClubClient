package rest;

import lombok.Getter;
import pointclub.shared.rest.RetrofitClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ChatRetrofitClient extends RetrofitClient {
    private static ChatRetrofitClient instance = null;
    @Getter
    private final ChatRequestsApi requestsApi;

    private ChatRetrofitClient() {
        super();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RetrofitClient.SERVER_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        requestsApi = retrofit.create(ChatRequestsApi.class);
    }

    public static synchronized ChatRetrofitClient getInstance() {
        if (instance == null) {
            instance = new ChatRetrofitClient();
        }
        return instance;
    }
}
