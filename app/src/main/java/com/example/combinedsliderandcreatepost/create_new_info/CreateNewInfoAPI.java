package com.example.combinedsliderandcreatepost.create_new_info;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CreateNewInfoAPI {

    @POST("post")
    Call<CreateNewInfoServerResponse> createNewInfoInServer(@Header("Authorization") String authHeader, @Body RequestBody sendPostDetailsToServer);

    @PUT("post/{postID}")
    Call<CreateNewInfoServerResponse> updateInfoInServer(@Header("Authorization") String authHeader, @Path("postID") String postID, @Body RequestBody createNewPostInServer);

    @DELETE("post/{postID}")
    Call<InfoDeleteResponse> deleteInfoInServer(@Header("Authorization") String authHeader, @Path("postID") String postID);

}
