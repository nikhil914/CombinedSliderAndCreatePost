package com.example.combinedsliderandcreatepost;

import com.example.login.mvvm.feature2.retrofit.create_new_info.CreateNewInfoServerResponse;

import java.util.ArrayList;
import java.util.HashMap;

public interface InfoCallBackInterface {

    void _insertNewInfo(
            CreateNewInfoServerResponse createNewInfoServerResponse
    );

//    void _insertNewInfo(
//            String _userID,
//            String _userName,
//            boolean _isGenderVisible,
//            String _isProfileImageVisible,
//            HashMap<String, Object> _selectedOptions,
//            ArrayList<HashMap<String, Object>> _selectedActions,
//            String _contentText,
//            ArrayList<String> _uploadedImageURLS,
//            ArrayList<String> _uploadedVideoURLS,
//            boolean _isPhoneCallVisible,
//            boolean _isChatVisible,
//            boolean _isLocationVisible,
//            double _latitude,
//            double _longitude,
//            String _infoID
//    );


    void _updateInfo(
            CreateNewInfoServerResponse createNewInfoServerResponse
    );

//    void _updateInfo(
//            String _userID,
//            String _userName,
//            boolean _isGenderVisible,
//            String _isProfileImageVisible,
//            HashMap<String, Object> _selectedOptions,
//            ArrayList<HashMap<String, Object>> _selectedActions,
//            String _contentText,
//            ArrayList<String> _uploadedImageURLS,
//            ArrayList<String> _uploadedVideoURLS,
//            boolean _isPhoneCallVisible,
//            boolean _isChatVisible,
//            boolean _isLocationVisible,
//            double _latitude,
//            double _longitude,
//            String _infoID
//    );


    void _deleteInfo(
            String _infoID
    );
}
