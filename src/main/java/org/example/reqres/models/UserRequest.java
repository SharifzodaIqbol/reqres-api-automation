package org.example.reqres.models;
import com.google.gson.annotations.SerializedName;

public class UserRequest {
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public UserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}