package com.example.combinedsliderandcreatepost;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.login.mvvm.feature2.recycler_view.models.ImageVideoModel;
import com.example.login.mvvm.feature2.repository.CreateInfoRepository;
import com.example.login.mvvm.feature2.retrofit.create_new_info.CreateNewInfoServerResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

public class CreateNewInfoFragmentViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> userNameVisibleMutableLiveData;


    private MutableLiveData<Boolean> userProfilePictureVisibleMutableLiveData;
    private MutableLiveData<Boolean> userGenderVisibleMutableLiveData;

    private MutableLiveData<Integer> selectedOptionMutableLiveData;
    private MutableLiveData<String> textTypedInOthersMutableLiveData;


    private MutableLiveData<Boolean> sirenEnabledOptionMutableLiveData;
    private MutableLiveData<Boolean> redAlertEnabledMutableLiveData;
    private MutableLiveData<Boolean> alertAroundEnabledOptionMutableLiveData;
    private MutableLiveData<Boolean> smsFamilyEnabledOptionMutableLiveData;

    private MutableLiveData<String> textTypedInContentMutableLiveData;

    private MutableLiveData<Boolean> phoneCallEnabledMutableLiveData;
    private MutableLiveData<Boolean> locationEnabledMutableLiveData;
    private MutableLiveData<Boolean> chatEnabledMutableLiveData;


    // Below liveData is used to store an value so that if configuration changes it stills shows the alertDialog
    private MutableLiveData<Boolean> isFullScreenLiveData;
    private MutableLiveData<Boolean> isAlertDialogVisibleLiveData;
    private MutableLiveData<List<ImageVideoModel>> selectedImageVideoModelsList;


    // Repository
    private CreateInfoRepository createInfoRepository;
    private MutableLiveData<CreateNewInfoServerResponse> createNewInfoServerResponseMutableLiveData;
    private MutableLiveData<Boolean> isPostBeingCreated;

    public CreateNewInfoFragmentViewModel(@NonNull Application application) {
        super(application);
        init();
    }


    private void init() {
        // name, profilePic and Gender are visible by default
        userNameVisibleMutableLiveData = new MutableLiveData<>(true);

        userProfilePictureVisibleMutableLiveData = new MutableLiveData<>(true);
        userGenderVisibleMutableLiveData = new MutableLiveData<>(true);

        selectedOptionMutableLiveData = new MutableLiveData<>(-1); // no option is selected by default
        textTypedInOthersMutableLiveData = new MutableLiveData<>("");

        // no actions are selected at first
        sirenEnabledOptionMutableLiveData = new MutableLiveData<>(false);
        redAlertEnabledMutableLiveData = new MutableLiveData<>(false);
        alertAroundEnabledOptionMutableLiveData = new MutableLiveData<>(false);
        smsFamilyEnabledOptionMutableLiveData = new MutableLiveData<>(false);


        textTypedInContentMutableLiveData = new MutableLiveData<>("");

        phoneCallEnabledMutableLiveData = new MutableLiveData<>(false);
        locationEnabledMutableLiveData = new MutableLiveData<>(false);
        chatEnabledMutableLiveData = new MutableLiveData<>(false);



        isFullScreenLiveData = new MutableLiveData<>(false);
        isAlertDialogVisibleLiveData = new MutableLiveData<>(false);

        selectedImageVideoModelsList = new MutableLiveData<>(new ArrayList<>());

        createNewInfoServerResponseMutableLiveData = new MutableLiveData<>();
        createInfoRepository = new CreateInfoRepository(getApplication(), createNewInfoServerResponseMutableLiveData);
        isPostBeingCreated = new MutableLiveData<>(false);
    }



    public void createPostInServer(RequestBody requestBody) {
        createInfoRepository.postInfoInServer(requestBody);
    }


    /*

        Getter for Mutable Live Data

     */

    public MutableLiveData<Boolean> getUserNameVisibleMutableLiveData() {
        return userNameVisibleMutableLiveData;
    }

    public MutableLiveData<Boolean> getUserProfilePictureVisibleMutableLiveData() {
        return userProfilePictureVisibleMutableLiveData;
    }

    public MutableLiveData<Boolean> getUserGenderVisibleMutableLiveData() {
        return userGenderVisibleMutableLiveData;
    }

    public MutableLiveData<Integer> getSelectedOptionMutableLiveData() {
        return selectedOptionMutableLiveData;
    }

    public MutableLiveData<String> getTextTypedInOthersMutableLiveData() {
        return textTypedInOthersMutableLiveData;
    }

    public MutableLiveData<Boolean> getSirenEnabledOptionMutableLiveData() {
        return sirenEnabledOptionMutableLiveData;
    }

    public MutableLiveData<Boolean> getRedAlertEnabledMutableLiveData() {
        return redAlertEnabledMutableLiveData;
    }

    public MutableLiveData<Boolean> getAlertAroundEnabledOptionMutableLiveData() {
        return alertAroundEnabledOptionMutableLiveData;
    }

    public MutableLiveData<Boolean> getSmsFamilyEnabledOptionMutableLiveData() {
        return smsFamilyEnabledOptionMutableLiveData;
    }

    public MutableLiveData<String> getTextTypedInContentMutableLiveData() {
        return textTypedInContentMutableLiveData;
    }

    public MutableLiveData<Boolean> getPhoneCallEnabledMutableLiveData() {
        return phoneCallEnabledMutableLiveData;
    }

    public MutableLiveData<Boolean> getLocationEnabledMutableLiveData() {
        return locationEnabledMutableLiveData;
    }

    public MutableLiveData<Boolean> getChatEnabledMutableLiveData() {
        return chatEnabledMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsFullScreenLiveData() {
        return isFullScreenLiveData;
    }

    public MutableLiveData<Boolean> getIsAlertDialogVisibleLiveData() {
        return isAlertDialogVisibleLiveData;
    }

    public MutableLiveData<List<ImageVideoModel>> getSelectedImageVideoModelsList() {
        return selectedImageVideoModelsList;
    }

    public MutableLiveData<CreateNewInfoServerResponse> getCreateNewInfoServerResponseMutableLiveData() {
        return createNewInfoServerResponseMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsPostBeingCreated() {
        return isPostBeingCreated;
    }
}
