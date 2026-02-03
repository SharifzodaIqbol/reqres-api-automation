package org.example.reqres.models;
import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("id")
    private Integer id;
    @SerializedName("token")
    private String token;

    public Integer getId() { return id; }
    public String getToken() { return token; }
}