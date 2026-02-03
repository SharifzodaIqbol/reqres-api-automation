package org.example.reqres.api;

import org.example.reqres.models.UserRequest;
import org.example.reqres.models.UserResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReqresApi {
    @POST("api/register")
    Call<UserResponse> registerUser(@Body UserRequest request);
}