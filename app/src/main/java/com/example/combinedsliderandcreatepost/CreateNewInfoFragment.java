package com.example.combinedsliderandcreatepost;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.databinding.CreateNewInfoFragment1Binding;
import com.example.login.mvvm.feature2.viewmodel.fragments.bottom_sheet.CreateNewInfoFragmentViewModel;
import com.example.login.non_mvvm.distance_finder.BaseClass;
import com.example.login.non_mvvm.distance_finder.DistanceFinderActivity;
import com.example.login.mvvm.feature2.recycler_view.models.ImageVideoModel;
import com.example.login.mvvm.feature2.recycler_view.adapters.ImagesVideosRecyclerViewAdapter;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CreateNewInfoFragment extends Fragment  {

    private CreateNewInfoFragment1Binding mBinding;
    private CreateNewInfoFragmentViewModel mViewModel;

    private RecyclerView mRecyclerView;
    private List<ImageVideoModel> mImagesVideosList;
    private ImagesVideosRecyclerViewAdapter mImagesVideosAdapter;


    private android.app.AlertDialog mSpotsDialog;

    public static CreateNewInfoFragment newInstance(
            String userName,
            boolean _isGenderVisible,
            String _isProfileImageVisible,
            HashMap<String, Object> _selectedOptions,
            ArrayList<HashMap<String, Object>> _selectedActions,
            String _contentText,
            ArrayList<String> _uploadedImageURLS,
            ArrayList<String> _uploadedVideoURLS,
            boolean _isPhoneCallVisible,
            boolean _isChatVisible,
            boolean _isLocationVisible,
            String _infoID,
            boolean _isEditing

    ) {
        CreateNewInfoFragment createNewInfoFragment = new CreateNewInfoFragment();

        // as the fragment is not editing one so simply return an new fragment instead of
        // passing values to the fragment
        if (!_isEditing)
            return createNewInfoFragment;

        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        bundle.putBoolean("isGenderVisible", _isGenderVisible);
        bundle.putString("isProfileImageVisible", _isProfileImageVisible);
        bundle.putSerializable("options", _selectedOptions);
        bundle.putSerializable("actions", _selectedActions);
        bundle.putString("content", _contentText);
        bundle.putStringArrayList("uploadedImageURLS", _uploadedImageURLS);
        bundle.putStringArrayList("uploadedVideoURLS", _uploadedVideoURLS);
        bundle.putBoolean("isPhoneCallVisible", _isPhoneCallVisible);
        bundle.putBoolean("isChatVisible", _isChatVisible);
        bundle.putBoolean("isLocationVisible", _isLocationVisible);
        bundle.putString("infoID", _infoID);
        bundle.putBoolean("isEditing", _isEditing);

        createNewInfoFragment.setArguments(bundle);

        return createNewInfoFragment;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.create_new_info_fragment1, container, false);


        return  mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CreateNewInfoFragmentViewModel.class);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setViewModel(mViewModel);
        mBinding.setClickHandlers(new ClickHandlers());

        mRecyclerView = mBinding.createNewInfoFragment3IncludeID.createNewInfoFragmentImagesVideosRecyclerViewID;
        mImagesVideosAdapter = new ImagesVideosRecyclerViewAdapter(getContext(), mViewModel, ImagesVideosRecyclerViewAdapter.PassedViewModel.CREATE_NEW_INFO);
        mRecyclerView.setAdapter(mImagesVideosAdapter);
        mImagesVideosList = new ArrayList<>();


        // this is used to show the alertDialog if the boolean is set to true even deviceConfiguration changes it still persists
        mViewModel.getIsAlertDialogVisibleLiveData().observe(getViewLifecycleOwner(), isAlertDialogVisible -> {
            if (isAlertDialogVisible)
                showInsertDialog();
        });

        mViewModel.getIsPostBeingCreated().observe(getViewLifecycleOwner(), isInfoBeingCreated -> {
            if (isInfoBeingCreated) {
                showCreatingInfoDialog();
            } else {
                if (mSpotsDialog != null)
                    mSpotsDialog.cancel();
            }
        });

        mViewModel.getSelectedImageVideoModelsList().observe(getViewLifecycleOwner(), imageVideoModelsList -> {
            mImagesVideosAdapter.submitList(new ArrayList<>(imageVideoModelsList));
        });

        mViewModel.getCreateNewInfoServerResponseMutableLiveData().observe(getViewLifecycleOwner(), serverResponse -> {
            ((DistanceFinderActivity) getActivity())._insertNewInfo(
                    serverResponse
            );
            mViewModel.getIsPostBeingCreated().setValue(false);
            System.out.println("Hello baby");
        });
    }

    private void showInsertDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
            .setTitle(R.string.feature2_create_new_info_insert_dialog_title)
            .setMessage(R.string.feature2_create_new_info_insert_dialog_description)
            .setPositiveButton(R.string.camera, (dialog, which) -> {
                Intent intent = new Intent(getContext(), FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setShowImages(true)
                        .setShowFiles(false)
                        .setShowAudios(false)
                        .setShowVideos(false)
                        .enableImageCapture(true)
                        .enableVideoCapture(false)
                        .setMaxSelection(4)
                        .setSkipZeroSizeFiles(true)
                        .build());
                ((Activity)getContext()).startActivityForResult(intent, DistanceFinderActivity.ADD_NEW_IMAGE_BOTTOM_SHEET);
                dialog.cancel();
            })
            .setNegativeButton(R.string.video, (dialog, which) -> {
                Intent intent = new Intent(getContext(), FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setShowImages(false)
                        .setShowFiles(false)
                        .setShowAudios(false)
                        .setShowVideos(true)
                        .setImageSize(25)
                        .enableImageCapture(false)
                        .enableVideoCapture(true)
                        .setMaxSelection(4)
                        .setSkipZeroSizeFiles(true)
                        .build());
                ((Activity)getContext()).startActivityForResult(intent, DistanceFinderActivity.ADD_NEW_VIDEO_BOTTOM_SHEET);
                dialog.cancel();
            })
            .setNeutralButton(R.string.cancel, (dialog, which) -> dialog.cancel())
            .create();

        alertDialog.show();

        alertDialog.setOnDismissListener(dialog -> mViewModel.getIsAlertDialogVisibleLiveData().setValue(false));
    }

    // this is used to show creating info dialog while the data is being submitted
    private void showCreatingInfoDialog() {
        mSpotsDialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setMessage(R.string.feature2_create_new_info_creating_info_message)
                .setCancelable(false)
                .build();

        mSpotsDialog.show();

        mSpotsDialog.setOnDismissListener(dialog -> mViewModel.getIsPostBeingCreated().setValue(false));
    }


    // this class is used to define custom types to that toggles and be done easily
    public enum ToggleThings {
        USERNAME,
        USERPROFILEPIC,
        USERGENDER,
        SIREN,
        REDALERT,
        ALERTAROUND,
        SMSFAMILY,
        PHONECALL,
        LOCATION,
        CHAT
    }


    // this class is used to set the onClick handlers for the fragment
    public class ClickHandlers {

        public void toggleVisibility(ToggleThings toggleThings) {
            switch (toggleThings) {
                case USERNAME:
                    mViewModel.getUserNameVisibleMutableLiveData().setValue(!mViewModel.getUserNameVisibleMutableLiveData().getValue());
                    break;
                case USERPROFILEPIC:
                    mViewModel.getUserProfilePictureVisibleMutableLiveData().setValue(!mViewModel.getUserProfilePictureVisibleMutableLiveData().getValue());
                    break;
                case USERGENDER:
                    mViewModel.getUserGenderVisibleMutableLiveData().setValue(!mViewModel.getUserGenderVisibleMutableLiveData().getValue());
                    break;
                case SIREN:
                    mViewModel.getSirenEnabledOptionMutableLiveData().setValue(!mViewModel.getSirenEnabledOptionMutableLiveData().getValue());
                    break;
                case REDALERT:
                    mViewModel.getRedAlertEnabledMutableLiveData().setValue(!mViewModel.getRedAlertEnabledMutableLiveData().getValue());
                    break;
                case ALERTAROUND:
                    mViewModel.getAlertAroundEnabledOptionMutableLiveData().setValue(!mViewModel.getAlertAroundEnabledOptionMutableLiveData().getValue());
                    break;
                case SMSFAMILY:
                    mViewModel.getSmsFamilyEnabledOptionMutableLiveData().setValue(!mViewModel.getSmsFamilyEnabledOptionMutableLiveData().getValue());
                    break;
                case PHONECALL:
                    mViewModel.getPhoneCallEnabledMutableLiveData().setValue(!mViewModel.getPhoneCallEnabledMutableLiveData().getValue());
                    break;
                case LOCATION:
                    mViewModel.getLocationEnabledMutableLiveData().setValue(!mViewModel.getLocationEnabledMutableLiveData().getValue());
                    break;
                case CHAT:
                    mViewModel.getChatEnabledMutableLiveData().setValue(!mViewModel.getChatEnabledMutableLiveData().getValue());
                    break;
            }
        }

        public void setOption(Integer optionID) {
            if (Objects.equals(mViewModel.getSelectedOptionMutableLiveData().getValue(), optionID))
                mViewModel.getSelectedOptionMutableLiveData().setValue(-1);
            else
                mViewModel.getSelectedOptionMutableLiveData().setValue(optionID);
        }

        public void insertImageOrVideo(View view) {
            mViewModel.getIsAlertDialogVisibleLiveData().setValue(true);
        }

        public void createInfoInServer(View view) {

            if (mViewModel.getSelectedOptionMutableLiveData().getValue() == -1) {
                Toast.makeText(getContext(), "An option must be selected.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mViewModel.getTextTypedInContentMutableLiveData().getValue().trim().isEmpty()) {
                Toast.makeText(getContext(), "Content can't be empty", Toast.LENGTH_SHORT).show();
                return;
            }



            // set this to true so that spots dialog will popUp
            mViewModel.getIsPostBeingCreated().setValue(true);


            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            builder.addFormDataPart("uservisibility[username]", String.valueOf(mViewModel.getUserNameVisibleMutableLiveData().getValue()));
            builder.addFormDataPart("uservisibility[profilePic]", String.valueOf(mViewModel.getUserProfilePictureVisibleMutableLiveData().getValue()));
            builder.addFormDataPart("uservisibility[gender]", String.valueOf(mViewModel.getUserGenderVisibleMutableLiveData().getValue()));
            builder.addFormDataPart("uservisibility[phoneCall]", String.valueOf(mViewModel.getPhoneCallEnabledMutableLiveData().getValue()));
            builder.addFormDataPart("uservisibility[chat]", String.valueOf(mViewModel.getChatEnabledMutableLiveData().getValue()));
            builder.addFormDataPart("uservisibility[location]", String.valueOf(mViewModel.getLocationEnabledMutableLiveData().getValue()));


            if (mViewModel.getSelectedOptionMutableLiveData().getValue() == 0) {
                builder.addFormDataPart("options[id]", "0");
                builder.addFormDataPart("options[visible]", "true");
                builder.addFormDataPart("options[title]", "Lonely/IsolatedPlace");
            }
            else if (mViewModel.getSelectedOptionMutableLiveData().getValue() == 1) {
                builder.addFormDataPart("options[id]", "1");
                builder.addFormDataPart("options[visible]", "true");
                builder.addFormDataPart("options[title]", "Unsafe Crowd");
            }
            else if (mViewModel.getSelectedOptionMutableLiveData().getValue() == 2) {
                builder.addFormDataPart("options[id]", "2");
                builder.addFormDataPart("options[visible]", "true");
                builder.addFormDataPart("options[title]", "Unmanaged traffic");
            }
            else if (mViewModel.getSelectedOptionMutableLiveData().getValue() == 3) {
                builder.addFormDataPart("options[id]", "3");
                builder.addFormDataPart("options[visible]", "true");
                builder.addFormDataPart("options[title]", "Animal Attack");
            }
            else if (mViewModel.getSelectedOptionMutableLiveData().getValue() == 4) {
                builder.addFormDataPart("options[id]", "4");
                builder.addFormDataPart("options[visible]", "true");
                builder.addFormDataPart("options[title]", "Eve-teasing");
            }
            else if (mViewModel.getSelectedOptionMutableLiveData().getValue() == 5) {
                builder.addFormDataPart("options[id]", "5");
                builder.addFormDataPart("options[visible]", "true");
                if (mViewModel.getTextTypedInOthersMutableLiveData().getValue().trim().isEmpty())
                    builder.addFormDataPart("options[title]", "Others");
                else
                    builder.addFormDataPart("options[title]", mViewModel.getTextTypedInOthersMutableLiveData().getValue().trim());
            } else {
                Toast.makeText(getContext(), "Select any option in order to proceed", Toast.LENGTH_SHORT).show();
            }



            builder.addFormDataPart("actions[0][id]", "0");
            builder.addFormDataPart("actions[0][visible]", String.valueOf(mViewModel.getSirenEnabledOptionMutableLiveData().getValue()));
            builder.addFormDataPart("actions[0][title]", "Siren");


            builder.addFormDataPart("actions[1][id]", "1");
            builder.addFormDataPart("actions[1][visible]", String.valueOf(mViewModel.getRedAlertEnabledMutableLiveData().getValue()));
            builder.addFormDataPart("actions[1][title]", "Red Alert");


            builder.addFormDataPart("actions[2][id]", "2");
            builder.addFormDataPart("actions[2][visible]", String.valueOf(mViewModel.getAlertAroundEnabledOptionMutableLiveData().getValue()));
            builder.addFormDataPart("actions[2][title]", "Alert Around");


            builder.addFormDataPart("actions[3][id]", "3");
            builder.addFormDataPart("actions[3][visible]", String.valueOf(mViewModel.getSmsFamilyEnabledOptionMutableLiveData().getValue()));
            builder.addFormDataPart("actions[3][title]", "SMS Family");


            builder.addFormDataPart("content", mViewModel.getTextTypedInContentMutableLiveData().getValue().trim());


            for (int i = 0; i < mImagesVideosList.size(); i++) {

                String fileRealPath =  BaseClass.getRealPathFromURI(
                        getContext(),
                        Uri.parse(mImagesVideosList.get(i).getURL())
                );
                String extension = BaseClass._getFileExtension(fileRealPath);

//                            Uri.parse(mImagesVideosList.get(i).getURL())

                Log.i("Extension", extension);
                if (mImagesVideosList.get(i).isImage()) {
                    if (mImagesVideosList.get(i).isFromServer()) {
                        builder.addFormDataPart("photo[]", mImagesVideosList.get(i).getURL());
                    } else {
                        builder.addFormDataPart(
                                "photo",
                                fileRealPath,
                                RequestBody.create(
                                        MediaType.parse("image/" + extension),
                                        new File(
                                                fileRealPath
                                        )
                                )
                        );
                    }
                }
                else if (mImagesVideosList.get(i).isVideo() && !mImagesVideosList.get(i).isFromServer()) {
                    builder.addFormDataPart(
                            "video",
                            fileRealPath,
                            RequestBody.create(
                                    MediaType.parse("video/" + extension),
                                    new File(
                                            fileRealPath
                                    )
                            )
                    );
                }

            }

            // TODO : get current user location and add it here
            builder.addFormDataPart("locationStatic[coordinates]", String.valueOf("70.67"));
            builder.addFormDataPart("locationStatic[coordinates]", String.valueOf("74.52"));
            builder.addFormDataPart("locationStatic[type]", "Point");

            mViewModel.createPostInServer(builder.build());

        }

    }

    public void _addNewImage(String imageURL) {
        mImagesVideosList.add(new ImageVideoModel(imageURL, true, false,true,false));
        mViewModel.getSelectedImageVideoModelsList().setValue(mImagesVideosList);
    }

    public void _addNewVideo(String imageURL) {
        mImagesVideosList.add(new ImageVideoModel(imageURL, false, true,true,false));
        mViewModel.getSelectedImageVideoModelsList().setValue(mImagesVideosList);

    }


}
