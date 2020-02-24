package com.example.combinedsliderandcreatepost;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.login.mvvm.feature2.retrofit.create_new_info.CreateNewInfoAPI;
import com.example.login.mvvm.feature2.retrofit.create_new_info.CreateNewInfoServerResponse;
import com.example.login.mvvm.util.RetrofitService;
import com.example.login.mvvm.util.SharedPreferenceService;

import java.io.IOException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateInfoRepository {

    private static String TAG = "CreateInfoRepo";

    private CreateNewInfoAPI createNewInfoAPI;
    private Context context;

    private MutableLiveData<CreateNewInfoServerResponse> createNewInfoServerResponseMutableLiveData;
    public CreateInfoRepository(Context context, MutableLiveData<CreateNewInfoServerResponse> createNewInfoServerResponseMutableLiveData) {
        createNewInfoAPI = RetrofitService.createService(CreateNewInfoAPI.class);
        this.context = context;
        this.createNewInfoServerResponseMutableLiveData = createNewInfoServerResponseMutableLiveData;
    }

    public void postInfoInServer(RequestBody requestBody) {
        createNewInfoAPI.createNewInfoInServer(
                SharedPreferenceService.getAuthHeader(context),
                requestBody
        ).enqueue(new Callback<CreateNewInfoServerResponse>() {
            @Override
            public void onResponse(Call<CreateNewInfoServerResponse> call, Response<CreateNewInfoServerResponse> response) {
                if (!response.isSuccessful()) {
                    try {
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                createNewInfoServerResponseMutableLiveData.setValue(response.body());
                Toast.makeText(context, "Info created successfully.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CreateNewInfoServerResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
