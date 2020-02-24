package com.example.combinedsliderandcreatepost;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceService {
    public static SharedPreferences getSharedPreference(Context context) {
        return  context.getSharedPreferences("com.osos.app", Context.MODE_PRIVATE);
    }

    public static String getToken(Context context) {
        return  context.getSharedPreferences("com.osos.app", Context.MODE_PRIVATE).getString("token", "");
    }

    public static String getAuthHeader(Context context) {
        return "Bearer " + context.getSharedPreferences("com.osos.app", Context.MODE_PRIVATE).getString("token", "");
    }

    public static String getUserName(Context context) {
        return context.getSharedPreferences("com.osos.app", Context.MODE_PRIVATE).getString("userName", "");
    }

}
