package com.example.combinedsliderandcreatepost.near_by_users_recycler_view;

import java.util.ArrayList;
import java.util.HashMap;

public class NearByUsersViewModel {

    private String _userID;

    private String _userName;

    private boolean _isGenderVisible;

    private String _isProfileImageVisible;

    private HashMap<String, Object> _selectedOptions;

    private ArrayList<HashMap<String, Object>> _selectedActions;

    private String _contentText;

    private ArrayList<String> _uploadedImageURLS;

    private ArrayList<String> _uploadedVideoURLS;

    private boolean _isPhoneCallVisibile;

    private boolean _isChatVisible;

    private boolean _isLocationVisible;

    private double _latitude;

    private double _longitude;

    private  String _infoID;

    // used for showing the timer in the recyclerView
    private long _remainingMilliSeconds;

    private boolean isFull;

    public NearByUsersViewModel(
            String _userID,
            String _userName,
            boolean _isGenderVisible,
            String _isProfileImageVisible,
            HashMap<String, Object> _selectedOptions,
            ArrayList<HashMap<String, Object>> _selectedActions,
            String _contentText,
            ArrayList<String> _uploadedImageURLS,
            ArrayList<String> _uploadedVideoURLS,
            boolean _isPhoneCallVisibile,
            boolean _isChatVisible,
            boolean _isLocationVisible,
            double _latitude,
            double _longitude,
            long _remainingMilliSeconds,
            String _infoID,
            boolean isFull
    ) {
        this._userID = _userID;
        this._userName = _userName;
        this._isGenderVisible = _isGenderVisible;
        this._isProfileImageVisible = _isProfileImageVisible;
        this._selectedOptions = _selectedOptions;
        this._selectedActions = _selectedActions;
        this._contentText = _contentText;
        this._uploadedImageURLS = _uploadedImageURLS;
        this._uploadedVideoURLS = _uploadedVideoURLS;
        this._isPhoneCallVisibile = _isPhoneCallVisibile;
        this._isChatVisible = _isChatVisible;
        this._isLocationVisible = _isLocationVisible;
        this._latitude = _latitude;
        this._longitude = _longitude;
        this._remainingMilliSeconds = _remainingMilliSeconds;
        this._infoID = _infoID;
        this.isFull = isFull;
    }

    public NearByUsersViewModel(
            String _userID,
            String _userName,
            String _isProfileImageVisible,
            double _latitude,
            double _longitude,
            String _infoID,
            long _remainingMilliSeconds,
            boolean isFull
    ) {
        this._userID = _userID;
        this._userName = _userName;
        this._isProfileImageVisible = _isProfileImageVisible;
        this._latitude = _latitude;
        this._longitude = _longitude;
        this._infoID = _infoID;
        this._remainingMilliSeconds = _remainingMilliSeconds;
        this.isFull = isFull;
    }

    public String get_userID() {
        return _userID;
    }

    public void set_userID(String _userID) {
        this._userID = _userID;
    }

    public String get_userName() {
        return _userName;
    }

    public void set_userName(String _userName) {
        this._userName = _userName;
    }

    public boolean is_isGenderVisible() {
        return _isGenderVisible;
    }

    public void set_isGenderVisible(boolean _isGenderVisible) {
        this._isGenderVisible = _isGenderVisible;
    }

    public String get_isProfileImageVisible() {
        return _isProfileImageVisible;
    }

    public void set_isProfileImageVisible(String _isProfileImageVisible) {
        this._isProfileImageVisible = _isProfileImageVisible;
    }

    public HashMap<String, Object> get_selectedOptions() {
        return _selectedOptions;
    }

    public void set_selectedOptions(HashMap<String, Object> _selectedOptions) {
        this._selectedOptions = _selectedOptions;
    }

    public ArrayList<HashMap<String, Object>> get_selectedActions() {
        return _selectedActions;
    }

    public void set_selectedActions(ArrayList<HashMap<String, Object>> _selectedActions) {
        this._selectedActions = _selectedActions;
    }

    public String get_contentText() {
        return _contentText;
    }

    public void set_contentText(String _contentText) {
        this._contentText = _contentText;
    }

    public ArrayList<String> get_uploadedImageURLS() {
        return _uploadedImageURLS;
    }

    public void set_uploadedImageURLS(ArrayList<String> _uploadedImageURLS) {
        this._uploadedImageURLS = _uploadedImageURLS;
    }

    public ArrayList<String> get_uploadedVideoURLS() {
        return _uploadedVideoURLS;
    }

    public void set_uploadedVideoURLS(ArrayList<String> _uploadedVideoURLS) {
        this._uploadedVideoURLS = _uploadedVideoURLS;
    }

    public boolean is_isPhoneCallVisibile() {
        return _isPhoneCallVisibile;
    }

    public void set_isPhoneCallVisibile(boolean _isPhoneCallVisibile) {
        this._isPhoneCallVisibile = _isPhoneCallVisibile;
    }

    public boolean is_isChatVisible() {
        return _isChatVisible;
    }

    public void set_isChatVisible(boolean _isChatVisible) {
        this._isChatVisible = _isChatVisible;
    }

    public boolean is_isLocationVisible() {
        return _isLocationVisible;
    }

    public void set_isLocationVisible(boolean _isLocationVisible) {
        this._isLocationVisible = _isLocationVisible;
    }

    public double get_latitude() {
        return _latitude;
    }

    public void set_latitude(double _latitude) {
        this._latitude = _latitude;
    }

    public double get_longitude() {
        return _longitude;
    }

    public void set_longitude(double _longitude) {
        this._longitude = _longitude;
    }

    public String get_infoID() {
        return _infoID;
    }

    public void set_infoID(String _infoID) {
        this._infoID = _infoID;
    }

    public long get_remainingMilliSeconds() {
        return _remainingMilliSeconds;
    }

    public void set_remainingMilliSeconds(long _remainingMilliSeconds) {
        this._remainingMilliSeconds = _remainingMilliSeconds;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }
}
