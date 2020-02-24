package com.example.combinedsliderandcreatepost.create_new_info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateNewInfoServerResponse {

    @SerializedName("id")
    @Expose
    private String _mUserID;

    @SerializedName("uservisibility")
    @Expose

    private HashMap<String, Object> _mUserVisibilityBooleans;

    @SerializedName("options")
    @Expose
    private HashMap<String, Object> _mSelectedOptions;

    @SerializedName("actions")
    @Expose
    private ArrayList<HashMap<String, Object>> _mSelectedActions;

    @SerializedName("content")
    @Expose
    private String _mContentText;

    @SerializedName("photo")
    @Expose
    private ArrayList<String> _mUploadedImageURLS;

    @SerializedName("video")
    @Expose
    private ArrayList<String> _mUploadedVideoURLS;

    @SerializedName("locationStatic")
    @Expose
    private HashMap<String, Object> _mLocationStatic;

    @SerializedName("_id")
    @Expose
    private String _infoID;

    public String get_mUserID() {
        return _mUserID;
    }

    public HashMap<String, Object> get_mUserVisibilityBooleans() {
        return _mUserVisibilityBooleans;
    }

    public HashMap<String, Object> get_mSelectedOptions() {
        return _mSelectedOptions;
    }

    public ArrayList<HashMap<String, Object>> get_mSelectedActions() {
        return _mSelectedActions;
    }

    public String get_mContentText() {
        return _mContentText;
    }

    public ArrayList<String> get_mUploadedImageURLS() {
        return _mUploadedImageURLS;
    }

    public ArrayList<String> get_mUploadedVideoURLS() {
        return _mUploadedVideoURLS;
    }

    public HashMap<String, Object> get_mLocationStatic() {
        return _mLocationStatic;
    }

    public String get_infoID() {
        return _infoID;
    }
}
