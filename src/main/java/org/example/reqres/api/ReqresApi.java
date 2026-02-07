package org.example.reqres.api;

import org.example.reqres.models.UserRequest;
import org.example.reqres.models.UserResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ReqresApi {
    @POST("api/register")
    Call<UserResponse> registerUser(@Body UserRequest request);
    @GET("api/users")
    Call<Object> getUsers(@Query("page") int page);
    @POST("api/login")
    Call<UserResponse> loginUser(@Body UserRequest request);
}