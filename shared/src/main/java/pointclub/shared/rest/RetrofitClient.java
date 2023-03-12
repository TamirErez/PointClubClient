package pointclub.shared.rest;

import lombok.Getter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitClient {
    public static final String SERVER_URL = "http://93.173.96.248:8080/";
    private static RetrofitClient instance = null;
    @Getter
    private final RequestsApi requestsApi;

    protected RetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(SERVER_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        requestsApi = retrofit.create(RequestsApi.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }
}
