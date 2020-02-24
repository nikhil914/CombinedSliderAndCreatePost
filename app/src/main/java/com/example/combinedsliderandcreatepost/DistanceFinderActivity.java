package com.example.combinedsliderandcreatepost;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.mvvm.feature2.retrofit.create_new_info.CreateNewInfoServerResponse;
import com.example.login.mvvm.feature2.retrofit.infos_by_radius.GetInfosByRadiusServerResponse;
import com.example.login.mvvm.util.SharedPreferenceService;
import com.example.login.mvvm.feature2.views.fragments.bottom_sheet.CreateNewInfoFragment;
import com.example.login.non_mvvm.distance_finder.bottom_sheet_fragments.ShowInfoOfCurrentUserFragment;
import com.example.login.non_mvvm.distance_finder.bottom_sheet_fragments.ShowInfoOfOtherUserFragment;
import com.example.login.non_mvvm.distance_finder.call_back_interfaces.InfoCallBackInterface;
import com.example.login.non_mvvm.distance_finder.near_by_users_recycler_view.NearByUsersRecyclerViewAdapter;
import com.example.login.non_mvvm.distance_finder.near_by_users_recycler_view.NearByUsersViewModel;
import com.example.login.non_mvvm.distance_finder.retrofit.get_all_infos_by_radius.GetAllInfoByRadiusFromServer;
import com.example.login.non_mvvm.distance_finder.retrofit.get_all_infos_by_radius.GetAllInfoFromServerByRadiusAPI;
import com.example.login.mvvm.feature2.views.SettingsActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.example.login.non_mvvm.distance_finder.call_back_interfaces.onNearByFriendsRecyclerViewClick;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.os.BatteryManager.BATTERY_PROPERTY_CURRENT_NOW;

public class DistanceFinderActivity extends AppCompatActivity implements OnMapReadyCallback, onNearByFriendsRecyclerViewClick, InfoCallBackInterface {


    /**
     *
     * Declare BottomSheet and RecyclerView things Here
     *
     */

    AutocompleteSupportFragment autocompleteSupportFragment;
//    private ImageView _mImageView;
    private ImageView _centerMyMapImageView;
    public View _mBottomSheetView;
    public FrameLayout _mBottomSheetFrameLayout; // this is used for showing different fragments

    private ImageView _mExpandImageView;

    // RecyclerView things
    private TextView _mNoOfNearByFriendsTextView;
    private int _mNearByFriendsCount;
    private RecyclerView _mNearByFriendRecyclerView;
    private List<NearByUsersViewModel> _mNearByUsersViewModelList;
    private NearByUsersRecyclerViewAdapter _mNearByUsersRecyclerViewAdapter;


    // BottomSheet Behaviour
    public BottomSheetBehavior _mBottomSheetBehaviour;

    // Use below variables for animation
//    private Handler _mHandler = new Handler();
//    private Runnable runnable = new Runnable() {
//
//        public void run() {
//            _mHandler.removeCallbacks(runnable);
//
//            Log.d("isForward", String.valueOf(isForward));
//
//            if (isForward) {
//                // Set as reverse animation
//                _reverseAnimation();
//
//            } else {
//                // set as forward animation
//                _forwardAnimation();
//
//            }
//
//        }
//
//    };
//    private AnimationDrawable _mAnimationDrawable;
//    private AnimationDrawable _mAnimationDrawableReverse;
//    private boolean isForward = true;


    /**
     *
     * Declare Maps Fragment Views here
     *
     */
    private ProgressBar _mProgressBar;
    private TextView _mCurrUserLocationNameTextView;
//    private TextView _mNoOfFriendsNearByTextView;


//    private Spinner _mDistanceSelectorSpinner;
//    private ImageView _mDistanceSelectorSpinnerDropDownImageView;
    private double _mSelectedDistanceRadius = 500.0;

//    private LinearLayout _mDistanceSelectorSpinnerLinearLayout;
//    private LinearLayout _mShowRadiusSpinnerLinearLayout;
//    private TextView _mShowSelectedRadiusTextView;
//    private TextView _m50MtsRadiusTextView;
//    private TextView _m100MtsRadiusTextView;
//    private TextView _m200MtsRadiusTextView;



    private TextView _displayDisAndTimeTextView;


    // Variables used for GoogleMap
    private GoogleMap _mGoogleMap;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    public Marker _mCurrLocationMarker = null;
    private HashMap<String, Marker> _mVisibleMarkersMap;
    private Circle _mCircle;
    private boolean _isCameraAnimatedOnce = false;


    public static final int REQUEST_LOCATION_PERMISSION_CODE = 99;
    public static final int ADD_NEW_PROFILE_PICTURE_SIDE_NAV = 1024;
    /**
     *
     * SideNavigation Bar
     *
     */
    private DrawerLayout _mDrawerLayout;
    private Drawer _mSideNavigationView;
//    private ImageView _mSideNavigationImageView;
    private File _mSideProfilePictureFile;
    private ActionBarDrawerToggle _mDrawerToggle;

    /**
     *
     *
     * Declare things used for pop_up_text
     *
     */
    private Animation _shrinkRightPopUpText;
    private Animation _expandRightPopUpText;
    private boolean _isReverseRightPopUpText = true;


    private FragmentManager _mFragmentManager;



    @Override
    protected void onStart() {
        super.onStart();

        /**
         *
         * Set the fragment as EditableDetailsFragment by default
         *
         */
//        bFragment(EditableDetailsFragment.class, CurrentFragment.EDITABLE_DETAILS);
    }


    public static final int ADD_NEW_IMAGE_BOTTOM_SHEET = 123;
    public static final int ADD_NEW_VIDEO_BOTTOM_SHEET = 321;

    @Override
    protected void onResume() {
        super.onResume();
        _setProfilePicture();
    }

    // Volley Variables
//    private RequestQueue _mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance_finder);

        _initializeVariables();
        _setupSideNavigationBar();


        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place
                Log.i("Place: ", place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle error
                Log.i("AutoComplete", "Error occurred" + status.getStatusMessage());
            }
        });
        autocompleteSupportFragment.setHint("Search Here");


//        setupDrawerContent(_mSideNavigationView);



//        _mSideNavigationImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (_mSideProfilePictureFile.exists()) {
//                    Intent intent = new Intent(DistanceFinderActivity.this, PreviewProfilePictureActivity.class);
//                    intent.putExtra("pictureUri", _mSideProfilePictureFile.getPath());
//                    ActivityOptionsCompat options = ActivityOptionsCompat.
//                            makeSceneTransitionAnimation(DistanceFinderActivity.this, _mSideNavigationImageView, "sideNavProfileImage");
//                    startActivity(intent, options.toBundle());
//                } else {
//                    // ask him to upload new Image
//                    _showProfilePictureUpload();
//                }
//            }
//        });

        _doThingsRelatedToBottomSheet();
        _doMapsLayoutThings();
        _rightPopUpText();

        // do some customizations
        _setProfilePicture();



        _displayBatteryConsumption();
    }

    private void _initializeVariables() {

        _mFragmentManager = getSupportFragmentManager();

//        if (!Places.isInitialized()) {
//            Places.initialize(this, getString(R.string.google_maps_key));
//        }
//        PlacesClient placesClient = Places.createClient(this);

        autocompleteSupportFragment = (AutocompleteSupportFragment) _mFragmentManager.findFragmentById(R.id.autoCompleteFragmentID);

        _mDrawerLayout = findViewById(R.id.drawerLayoutID);
//        _mSideNavigationImageView = _mSideNavigationView.getHeaderView(0).findViewById(R.id.sideNavCircleImageViewID);

        findViewById(R.id.relativeLayoutToolbarImageViewID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });






        _mBottomSheetView = findViewById(R.id.bottomSheetNestedScrollViewID);
        _mBottomSheetFrameLayout = findViewById(R.id.bottomSheetFrameLayoutID);
        _mExpandImageView = findViewById(R.id.expandImageView);

        // Recycler View things
        _mNearByFriendRecyclerView = findViewById(R.id.nearByFriendsRecyclerViewID);
        _mNearByUsersViewModelList = new ArrayList<>();
        _mNearByUsersRecyclerViewAdapter = new NearByUsersRecyclerViewAdapter(this, _mNearByUsersViewModelList, this);
        _mNearByFriendRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        _mNearByFriendRecyclerView.setAdapter(_mNearByUsersRecyclerViewAdapter);

        _centerMyMapImageView = findViewById(R.id.centerMyCurrentPositionImageViewID);


        _mProgressBar = findViewById(R.id.progressBarID);
        _mCurrUserLocationNameTextView = findViewById(R.id.usersCurrentLocationNameTextViewID);
//        _mDistanceSelectorSpinner = findViewById(R.id.setDistanceSpinnerID);
//        _mDistanceSelectorSpinnerDropDownImageView = findViewById(R.id.spinnerDropDownArrowImageView);
//        _mShowRadiusSpinnerLinearLayout = findViewById(R.id.showRadiusSpinnerOptionsLinearLayoutID);
//        _mShowSelectedRadiusTextView = findViewById(R.id.showSelectedRadiusTextViewID);
//        _m50MtsRadiusTextView = findViewById(R.id.radius50MtsTextViewID);
//        _m100MtsRadiusTextView = findViewById(R.id.radius100MtsTextViewID);
//        _m200MtsRadiusTextView = findViewById(R.id.radius200MtsTextViewID);
//        _mNoOfFriendsNearByTextView = findViewById(R.id.noOfFriendsNearByTextViewID);
        _displayDisAndTimeTextView = findViewById(R.id.textView);

        _mNoOfNearByFriendsTextView = findViewById(R.id.noOfFriendsNearByCountTextViewID);


        _mVisibleMarkersMap = new HashMap<>();

    }

    private void _showProfilePictureUpload() {
        Intent intent = new Intent(this, FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowImages(true)
                .setShowFiles(false)
                .setShowAudios(false)
                .setShowVideos(false)
                .enableImageCapture(true)
                .enableVideoCapture(false)
                .setMaxSelection(1)
                .setSkipZeroSizeFiles(true)
                .build());
        this.startActivityForResult(intent, ADD_NEW_PROFILE_PICTURE_SIDE_NAV);
    }

    private void _setupSideNavigationBar() {

        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(new ProfileDrawerItem().withName("TejaSimha Kumar"))
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        SwitchDrawerItem item1 = new SwitchDrawerItem().withIdentifier(0).withName("Night Mode");
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(1).withName("Two");
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(2).withName("Three");
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(3).withName("Four");
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(4).withName("Five");
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(5).withName("Six");
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(6).withName("Seven");
        PrimaryDrawerItem item8 = new PrimaryDrawerItem().withIdentifier(7).withName("Eight");
        PrimaryDrawerItem item9 = new PrimaryDrawerItem().withIdentifier(8).withName("Nine");
        PrimaryDrawerItem item10 = new PrimaryDrawerItem().withIdentifier(9).withName("Ten");
        PrimaryDrawerItem item11 = new PrimaryDrawerItem().withIdentifier(10).withName("Eelven");
        PrimaryDrawerItem item12 = new PrimaryDrawerItem().withIdentifier(11).withName("Settings");
        PrimaryDrawerItem item13 = new PrimaryDrawerItem().withIdentifier(12).withName("Logout");




        _mSideNavigationView = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(accountHeader)
                .withSliderBackgroundColor(Color.parseColor("#FFFF95"))
                .withSelectedItem(-1)
                .withCloseOnClick(true)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        item5,
                        item6,
                        item7,
                        item8,
                        item9,
                        item10,
                        item11,
                        item12,
                        item13
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            switch (position) {
                                case 12:
                                    Intent intent = new Intent(DistanceFinderActivity.this, SettingsActivity.class);
                                    startActivity(intent);
                                    break;
                            }
                        }

                        return false;
                    }
                })
//                .withGenerateMiniDrawer(true)
                .build();

//        MiniDrawer miniDrawer = _mSideNavigationView.getMiniDrawer();
//
//        int firstWidth = (int) UIUtils.convertDpToPixel(300, this);
//        int secondWidth = (int) UIUtils.convertDpToPixel(72, this);
//
//        miniDrawer.build(this);
//        Crossfader crossfader = new Crossfader()
//                .withContent(findViewById(R.id.tempMiniDrawerID))
//                .withFirst(_mSideNavigationView.getSlider(), firstWidth)
//                .withSecond(miniDrawer.build(this), secondWidth)
//                .build();
////
//        miniDrawer.withCrossFader(new CrossfadeWrapper(crossfader));




    }

    private void _setProfilePicture() {
        _mSideProfilePictureFile = new File(getCacheDir(), "tempCrop.jpg");
//        if (_mSideProfilePictureFile.exists()) {
//            Glide.with(this)
//                    .applyDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
//                    .load(_mSideProfilePictureFile)
//                    .into(_mSideNavigationImageView);
//
//            Glide.with(this)
//                    .applyDefaultRequestOptions(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true))
//                    .load(_mSideProfilePictureFile)
//                    .into((ImageView)findViewById(R.id.userProfileImageID));
//
//        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Intent intent;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                break;
            case R.id.nav_second_fragment:
//                fragmentClass = SecondFragment.class;
                break;
            case R.id.nav_third_fragment:
//                fragmentClass = ThirdFragment.class;
                break;
            default:
//                fragmentClass = FirstFragment.class;
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        // Close the navigation drawer
        _mDrawerLayout.closeDrawers();
    }

    /**
     *
     *
     *
     * Call Functions related for pop_up_text
     *
     *
     *
     */
    private void _rightPopUpText() {
        ImageView imageView = findViewById(R.id.closeButtonID);
        final TextView textView = findViewById(R.id.pop_up_textViewID);

        /**
         * https://stackoverflow.com/questions/32992221/android-animation-to-shrink-and-fade-out-a-view-at-the-same-time
         */
        _shrinkRightPopUpText = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shrink_anim);
        _expandRightPopUpText = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.expand_anim);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!_isReverseRightPopUpText)
                    textView.startAnimation(_shrinkRightPopUpText);
                else
                    textView.startAnimation(_expandRightPopUpText);
            }
        });

        _shrinkRightPopUpText.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d("Animation", "Started");
                textView.setVisibility(View.VISIBLE);
                _isReverseRightPopUpText = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("Animation", "end");

                textView.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d("Animation", "repeat");

            }
        });

        _expandRightPopUpText.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                textView.setVisibility(View.VISIBLE);
                _isReverseRightPopUpText = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                textView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     *
     *
     *
     *
     * Call Functions required for the BottomSlider here
     *
     *
     *
     *
     *
     */

    public void _calculateBottomSheetWidthAndHeight(int width, int height) {
        _mBottomSheetView.getLayoutParams().height = (int) (height / 1.5);
        _mBottomSheetView.getLayoutParams().width = (int) (width / 2 * 1.5);
        _mBottomSheetView.setLayoutParams(_mBottomSheetView.getLayoutParams());

        Fragment fragment = _mFragmentManager.findFragmentById(_mBottomSheetFrameLayout.getId());

        if (fragment instanceof CreateNewInfoFragment) {
//            ((CreateNewInfoFragment) fragment)._isExpanded = false;
//            ((CreateNewInfoFragment) fragment)._toggleExpandShrinkImageView(false, getResources().getDrawable(R.drawable.ic_action_enter_full_screen_black));
            return;
        }

        if (fragment instanceof ShowInfoOfCurrentUserFragment) {
//            ((ShowInfoOfCurrentUserFragment) fragment)._isExpanded = false;
            ((ShowInfoOfCurrentUserFragment) fragment)._toggleExpandShrinkImageView(false, getResources().getDrawable(R.drawable.ic_action_enter_full_screen_black));
            return;
        }

        if (fragment instanceof ShowInfoOfOtherUserFragment) {
//            ((ShowInfoOfCurrentUserFragment) fragment)._isExpanded = false;
            ((ShowInfoOfOtherUserFragment) fragment)._toggleExpandShrinkImageView(false, getResources().getDrawable(R.drawable.ic_action_enter_full_screen_black));
        }
    }

    private void _doThingsRelatedToBottomSheet() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        // height = 335dp

        _calculateBottomSheetWidthAndHeight(width, height);


//        _blurBackground();

        final LinearLayout linearLayout = findViewById(R.id.temp);
        linearLayout.getLayoutParams().width = width - _mBottomSheetView.getLayoutParams().width;
        // set the width also for the viewID to make things accordingly
//        findViewById(R.id.viewID).getLayoutParams().width = _mBottomSheetView.getLayoutParams().width;
        _mBottomSheetBehaviour = BottomSheetBehavior.from(_mBottomSheetView);
//        _mImageView = findViewById(R.id.topDividerID);
//        _mImageView.setOnClickListener(null); // this will remove the changing animation of imageView if it is clicked
//        _forwardAnimation();

        // set the marginTop of the constraint layout that contains recyclerView that shows the nearByUsers
        // to the half of the height of clipCorner because to arrange the recyclerView just below of the clipCorner
        // in an neat way
        // TODO : Uncomment it while using clipCorner
//        int clipCornerHeight = findViewById(R.id.clipCorner).getLayoutParams().height;
//        ((ConstraintLayout.LayoutParams)findViewById(R.id.recyclerViewConstraintLayoutID).getLayoutParams()).topMargin = clipCornerHeight/2;

        /**
         *
         * Animate ImageView up and down using the below callBack
         * Here we used handler to make or animation do reverseAnimation also by settings
         * some things when the duration of the animation completes
         *
         */
        // TODO: See this deprecation once
        _mBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

                if (i == BottomSheetBehavior.STATE_COLLAPSED) {
                    _calculateBottomSheetWidthAndHeight(width, height);

                }


//                try {
//                    if (i == BottomSheetBehavior.STATE_EXPANDED) {
////                        _mAnimationDrawable.start();
////                        _mHandler.postDelayed(runnable, 100);
//
//                        Fragment fragment = _mFragmentManager.findFragmentById(_mBottomSheetFrameLayout.getId());
//                        if (fragment instanceof CreateNewInfoFragment) {
//                            ((CreateNewInfoFragment) fragment)._blurBackground();
//                            System.out.println("Called Stateback Expanded");
//                        }
//                    } else if (i == BottomSheetBehavior.STATE_COLLAPSED) {
////                        _mAnimationDrawableReverse.start();
////                        _mHandler.postDelayed(runnable, 100);
//                        System.out.println("Called Stateback Reverse");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {


            }
        });
//        _mBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View view, int i) {
//
//
//
//                try {
//                    if (i == BottomSheetBehavior.STATE_EXPANDED) {
////                        _mAnimationDrawable.start();
////                        _mHandler.postDelayed(runnable, 100);
//
//                        Fragment fragment = _mFragmentManager.findFragmentById(_mBottomSheetFrameLayout.getId());
//                        if (fragment instanceof CreateNewInfoFragment) {
//                            ((CreateNewInfoFragment) fragment)._blurBackground();
//                            System.out.println("Called Stateback Expanded");
//                        }
//                    } else if (i == BottomSheetBehavior.STATE_COLLAPSED) {
////                        _mAnimationDrawableReverse.start();
////                        _mHandler.postDelayed(runnable, 100);
//                        System.out.println("Called Stateback Reverse");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onSlide(@NonNull View view, float v) {
//
//
//            }
//        });


        _showCreateNewPostFragmentBottomSheet();

//        _mBottomSheetRecyclerView = findViewById(R.id.bottomSheetRecyclerViewID);
//        _mBottomSheetViewModelArray = new BottomSheetViewModel[1];
//        _mBottomSheetViewModelArray[0] = new BottomSheetViewModel(1);
        // https://stackoverflow.com/questions/30321691/recyclerview-with-only-one-item-displayed-on-screen
//        SnapHelper mSnapHelper = new PagerSnapHelper();
//        mSnapHelper.attachToRecyclerView(_mBottomSheetRecyclerView);
//        _mBottomSheetRecyclerView.addItemDecoration(new LinePagerIndicatorDecoration());
//        _mBottomSheetAdapter = new BottomSheetAdapter(this, _mBottomSheetViewModelArray);
//        _mBottomSheetRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        _mBottomSheetRecyclerView.setAdapter(_mBottomSheetAdapter);



        _mExpandImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                _mCurrentVisibleUserDetails = _mPreviousVisibleUserDetails;


                if (_mBottomSheetBehaviour.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    _mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);

//                    if (_isHidden) {
//                        _mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
//                        _isHidden = false;
//                        System.out.println("Called this");
//                    }
                }

                else
                    _mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);


                _showCreateNewPostFragmentBottomSheet();
            }
        });




        // center your position

        _centerMyMapImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_mCurrLocationMarker != null) {
                    // move the camera
                    //move map camera only at the firstTime
                    _mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(_mCurrLocationMarker.getPosition(), 15.0f));
                } else {
                    // don't move the camera
                    Toast.makeText(DistanceFinderActivity.this, "Sorry to say, please wait still getting location.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public void onNearByFriendRecyclerViewClick(
            String _userID,
            String _userName,
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
            double _latitude,
            double _longitude,
            String _infoID
    ) {

        // if bottom sheet is collapsed expanded it
        if (_mBottomSheetBehaviour.getState() != BottomSheetBehavior.STATE_EXPANDED)
            _mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);

        if (BaseClass.getSharedPreference(this).getString("userID", "").equals(_userID)) {
            // show as same user post
            _replaceFragment(
                    ShowInfoOfCurrentUserFragment.newInstance(
                            _userID,
                            _userName,
                            _isGenderVisible,
                            _isProfileImageVisible,
                            _selectedOptions,
                            _selectedActions,
                            _contentText,
                            _uploadedImageURLS,
                            _uploadedVideoURLS,
                            _isPhoneCallVisible,
                            _isChatVisible,
                            _isLocationVisible,
                            _infoID
                    ));
        } else {
            // show as other user
            _replaceFragment(
                    ShowInfoOfOtherUserFragment.newInstance(
                            _userID,
                            _userName,
                            _isGenderVisible,
                            _isProfileImageVisible,
                            _selectedOptions,
                            _selectedActions,
                            _contentText,
                            _uploadedImageURLS,
                            _uploadedVideoURLS,
                            _isPhoneCallVisible,
                            _isChatVisible,
                            _isLocationVisible,
                            _infoID
                    ));
        }


    }

    @Override
    public void timerFinishedPosition(int pos) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                _mNearByUsersViewModelList.remove(pos);
//                _mNearByUsersRecyclerViewAdapter.notifyItemRemoved(pos);
//                _mNoOfNearByFriendsTextView.setText(String.valueOf(--_mNearByFriendsCount));
            }
        });
    }

    /**
     *
     * ImageView arrow back and forward animation
     *
     */
//    private void _forwardAnimation() {
//        isForward = true;
//        _mImageView.setBackgroundResource(R.drawable.animate_bottom_sheet_icon_forward);
//        _mAnimationDrawable = (AnimationDrawable) _mImageView.getBackground();
//        _mAnimationDrawable.setOneShot(true);
//
////        _checkAndToggleFragments();
//
//    }

//    private void _reverseAnimation() {
//        isForward = false;
//        _mImageView.setBackgroundResource(R.drawable.animate_bottom_sheet_icon_reverse);
//        _mAnimationDrawableReverse = (AnimationDrawable) _mImageView.getBackground();
//        _mAnimationDrawableReverse.setOneShot(true);
//
////        _checkAndToggleFragments();
//
//    }



//    @Override
//    public void onNearByFriendRecyclerViewClick(String userName, String distanceInKms) {
//
//
//
//        if (_mBottomSheetBehaviour.getState() == BottomSheetBehavior.STATE_COLLAPSED)
//            _mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
//
//        _setFragment(ShowDetailsFragmentActivity.class);
//
//
//        Toast.makeText(this, userName + " => " + distanceInKms, Toast.LENGTH_SHORT).show();
//
//    }

//    private void _setFragment(Class fragmentClass) {
//        try {
//
//            Fragment fragment = (Fragment) fragmentClass.newInstance();
//
//            // Insert the fragment by replacing any existing fragment
//
//            _replaceFragment(fragment);
//
////            _mPreviousVisibleUserDetails = _mCurrentVisibleUserDetails;
////            _mCurrentVisibleUserDetails = userName;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void _checkAndToggleFragments() {
//        /**
//         *
//         * Check whether the previous opened fragment is equal to currentFragment
//         * if it is then close and don't do nothing if it's not then close and reopen new fragment
//         *
//         */
//        if (!_mPreviousVisibleUserDetails.equals(_mCurrentVisibleUserDetails)) {
//            _mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
//        }
//    }


    /**
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     * Call Functions required for the mapsLayout here
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */
    private void _doMapsLayoutThings() {

//        final String[] items = new String[]{"50mts", "100mts", "250mts", "500mts", "1000mts", "2000mts", "3000mts", "5000mts"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_drop_down_text_view, items);
//        _mDistanceSelectorSpinner.setAdapter(adapter);
//        _mDistanceSelectorSpinner.setSelection(0); // set by default as 50mts
////        _mRequestQueue = Volley.newRequestQueue(this);
//
//
//        _mDistanceSelectorSpinnerDropDownImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                _mDistanceSelectorSpinner.performClick();
//            }
//        });





//        _mDistanceSelectorSpinnerLinearLayout.setOnClickListener(this);
//        _mShowRadiusSpinnerLinearLayout.setOnClickListener(this);
//        _m50MtsRadiusTextView.setOnClickListener(this);
//        _m100MtsRadiusTextView.setOnClickListener(this);
//        _m200MtsRadiusTextView.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) _mFragmentManager.findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);

//        _mDistanceSelectorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                // TODO : Implement what to do after selecting the meters
//                _mSelectedDistanceRadius = Double.valueOf(items[position].substring(0, items[position].length() - 3));
//
//                if (_mCurrLocationMarker != null) {
//                    // Location got so update the meters also
//
//
//                    // remove the previous circle
//                    _updateCircleInMap(_mSelectedDistanceRadius);
//                } else {
//                    Toast.makeText(DistanceFinderActivity.this, "Sorry to say this, please wait until I fetch location, Thank You.", Toast.LENGTH_SHORT).show();
//                }
//
//
//                delay = 0;
//                _updateGoogleMapMarkers();
//                _requestInfosFromServer();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }






    @Override
    public void onMapReady(GoogleMap googleMap) {
        _mGoogleMap = googleMap;


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(2000L); // two minute interval
        mLocationRequest.setFastestInterval(2000L);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        // ask permission if the current android version is greater than lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            ) {
                //Location Permission already granted
                _requestCurrentUserLocation();
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            _requestCurrentUserLocation();
        }
    }

    private void _requestCurrentUserLocation() {
        /**
         *
         * Write the code what you want to execute after the locationPermissions granted
         *
         */
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                List<Location> locationList = locationResult.getLocations();
                if (locationList.size() > 0) {

                    _mProgressBar.setVisibility(View.GONE);


                    //The last location in the list is the newest
                    Location location = locationList.get(locationList.size() - 1);
                    Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                    if (_mCurrLocationMarker != null) {
                        _mCurrLocationMarker.remove();
                    }

                    //Place current location marker
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Our Location");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    _mCurrLocationMarker = _mGoogleMap.addMarker(markerOptions);

                    if (!_isCameraAnimatedOnce) {
                        _mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(_mCurrLocationMarker.getPosition(), 15.0f));
                        _isCameraAnimatedOnce = true;
                    }


//                    _requestInfosFromServer();
                    _updateCircleInMap();
                    _updateGoogleMapMarkers();


//                if (_mDestinationMarker != null) {
//                    _getLocationAndDrawPolyLine();
//                }

                }
            }
        }, Looper.myLooper());

        _getInfosPeriodically();
    }



    /**
     *
     * Below function will get the locationName by using the latitude and position passed to it
     *
     */
    // https://www.youtube.com/watch?v=Ut_VK92QqEQ
    private String _getCurrLocationName(LatLng position) {
        String currentLocationName = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(position.latitude, position.longitude, 1);
            currentLocationName = addressList.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  currentLocationName;
    }



    // update the circle in the map when it is called
    private void _updateCircleInMap() {
        CircleOptions circleOptions = new CircleOptions()
                .center(_mCurrLocationMarker.getPosition())
                .radius(_mSelectedDistanceRadius) // meters
                .strokeWidth(2)
                .strokeColor(Color.WHITE)
                .fillColor(Color.parseColor("#80FFFF95")); // In meters

        // Get back the mutable Circle
        if (_mCircle != null)
            _mCircle.remove();



        _mCircle = _mGoogleMap.addCircle(circleOptions);
        /**
         *
         * Check whether the friend is in the circle or not
         *
         */
//        _checkWhetherFriendIsWithInTheRange();
    }

    // https://stackoverflow.com/questions/30170271/android-google-map-how-to-check-if-the-gps-location-is-inside-the-circle
//    private void _checkWhetherFriendIsWithInTheRange() {
//        System.out.println("Calling this hello");
//
//        for (Map.Entry<String, LatLng> entry: _mNearByFriendsMarkersMap.entrySet()) {
//            // iterate and add markers
//            float[] distance = new float[2];
//
//            LatLng latLng = entry.getValue();
//
//            Location.distanceBetween(latLng.latitude, latLng.longitude,
//                    _mCircle.getCenter().latitude, _mCircle.getCenter().longitude, distance);
//
//            if( distance[0] > _mCircle.getRadius() ){
//                // TODO : remove marker if and only if he is present if not present then don't do anything
//                _mRemoveMarkersArrayList.add(entry.getKey());
////                Toast.makeText(getBaseContext(), "Outside, distance from center: " + distance[0] + " radius: " + _mCircle.getRadius(), Toast.LENGTH_LONG).show();
//            } else {
//                // TODO : Add marker if already present don't add
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//                markerOptions.title(entry.getKey());
//                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//
//                _mAddMarkersMap.put(entry.getKey(), markerOptions);
////                Toast.makeText(getBaseContext(), "Inside, distance from center: " + distance[0] + " radius: " + _mCircle.getRadius() , Toast.LENGTH_LONG).show();
//            }
//        }
//
//        /**
//         *
//         * now check, then add or remove markers accordingly
//         *
//          */
//
//        // add markers
//        for (Map.Entry<String, MarkerOptions> entry: _mAddMarkersMap.entrySet()) {
//            if (_mVisibleMarkersMap.isEmpty()) {
//                // visible markers map is empty as add at firstTime
//
//                float[] results = new float[1];
//                Location.distanceBetween(
//                        _mCurrLocationMarker.getPosition().latitude,
//                        _mCurrLocationMarker.getPosition().longitude,
//                        entry.getValue().getPosition().latitude,
//                        entry.getValue().getPosition().longitude,
//                        results
//                );
//
//                String kms = String.format("%.02f", results[0] / 1000) + "Kms";
//                entry.getValue().snippet(kms);
//
//                _mVisibleMarkersMap.put(entry.getKey(), _mGoogleMap.addMarker(entry.getValue()));
//
//                // add in the recycleView
//                _mNearByUsersViewModelList.add(new NearByUsersViewModel(entry.getKey(), kms, "2"));
//                _mNearByUsersRecyclerViewAdapter.notifyItemInserted(_mNearByUsersViewModelList.size() - 1);
//                _mNoOfFriendsNearByTextView.setText(String.valueOf(_mNearByUsersViewModelList.size()));
//            } else  {
//                // iterate and check whether the user is present or not in visible markers map
//                // if present don't add
//                // if not present then add the marker to map
//                boolean isPresentAlready = false;
//                for (Map.Entry<String, Marker> visibleEntry: _mVisibleMarkersMap.entrySet()) {
//                    if (entry.getKey().equals(visibleEntry.getKey())) {
//                        isPresentAlready = true;
//                        break;
//                    }
//                }
//
//                if (!isPresentAlready) {
//                    // add marker to map as it is not present
//
//                    float[] results = new float[1];
//                    Location.distanceBetween(
//                            _mCurrLocationMarker.getPosition().latitude,
//                            _mCurrLocationMarker.getPosition().longitude,
//                            entry.getValue().getPosition().latitude,
//                            entry.getValue().getPosition().longitude,
//                            results
//                    );
//
//                    String kms = String.format("%.02f", results[0] / 1000) + "Kms";
//                    entry.getValue().snippet(kms);
//
//
//                    _mVisibleMarkersMap.put(entry.getKey(), _mGoogleMap.addMarker(entry.getValue()));
//
//                    // add in the recycleView
//                    _mNearByUsersViewModelList.add(new NearByUsersViewModel(entry.getKey(), kms, "2"));
//                    _mNearByUsersRecyclerViewAdapter.notifyItemInserted(_mNearByUsersViewModelList.size() - 1);
//                    _mNoOfFriendsNearByTextView.setText(String.valueOf(_mNearByUsersViewModelList.size()));
//                }
//            }
//        }
//        // clear the map
//        _mAddMarkersMap.clear();
//
//        // remove markers
//        for (String s: _mRemoveMarkersArrayList) {
//            // iterate over visible markers map
//            // if the marker is present remove it from both map and visibleMarkersMap
//            // and break the function and iterate for next user
//            // if not present then don't do anything
//            for (Map.Entry<String, Marker> entry: _mVisibleMarkersMap.entrySet()) {
//
//                if (s.equals(entry.getKey())) {
//                    _mVisibleMarkersMap.remove(entry.getKey()).remove();
//
//                    int removeIndex = -1;
//                    for (NearByUsersViewModel model: _mNearByUsersViewModelList) {
//                        // TODO: Implement any searching algorithm on top of kms to get the index much faster eg:- BST
//                        if (model.get_userName().equals(entry.getKey())) {
//                            // index found
//                            removeIndex = _mNearByUsersViewModelList.indexOf(model);
//                            break;
//                        }
//                    }
//
//                    if (removeIndex != -1) {
//                        // remove from the list and animate recyclerView
//                        _mNearByUsersViewModelList.remove(removeIndex);
//                        _mNearByUsersRecyclerViewAdapter.notifyItemRemoved(removeIndex);
//                        _mNoOfFriendsNearByTextView.setText(String.valueOf(_mNearByUsersViewModelList.size()));
//                    }
//
//                    break;
//                }
//
//            }
//        }
//        // clear the list
//        _mRemoveMarkersArrayList.clear();
//
//
//    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Below if condition is used to show to whether we need anything to display to user
            // in an order to grant permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("!Location Permission Needed!")
                        .setMessage("We are sorry we need to access your location in an order to show your current location.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(DistanceFinderActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_LOCATION_PERMISSION_CODE);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_LOCATION_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION_CODE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    _requestCurrentUserLocation();
                }

            } else {

                // permission denied, so disable the maps functionality and show toast
                Toast.makeText(this, "Sorry to say permission denied, we can't show your location.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Fragment currentFragment = _mFragmentManager.findFragmentById(_mBottomSheetFrameLayout.getId());
//        if (currentFragment instanceof EditableDetailsFragment) {
//            ((EditableDetailsFragment)currentFragment).onKeyDown(keyCode, event);
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void _insertNewInfo(
            CreateNewInfoServerResponse createNewInfoServerResponse
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
    ) {
        // add in the recycleView
        System.out.println("TEMP" + createNewInfoServerResponse.get_mUserVisibilityBooleans());
        System.out.println("TEMP" + createNewInfoServerResponse.get_infoID());
        System.out.println("TEMP" + createNewInfoServerResponse.get_mContentText());
        System.out.println("TEMP" + createNewInfoServerResponse.get_mUserID());
        _mNearByUsersViewModelList.add(0, new NearByUsersViewModel(
                createNewInfoServerResponse.get_mUserID(),
                SharedPreferenceService.getUserName(this),
                (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("gender"),
                (String) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("profilePic"),
                createNewInfoServerResponse.get_mSelectedOptions(),
                createNewInfoServerResponse.get_mSelectedActions(),
                createNewInfoServerResponse.get_mContentText(),
                createNewInfoServerResponse.get_mUploadedImageURLS(),
                createNewInfoServerResponse.get_mUploadedVideoURLS(),
                (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("phoneCall"),
                (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("chat"),
                (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("location"),
                ((ArrayList<Double>) createNewInfoServerResponse.get_mLocationStatic().get("coordinates")).get(1),
                ((ArrayList<Double>) createNewInfoServerResponse.get_mLocationStatic().get("coordinates")).get(0),
                60000,
                createNewInfoServerResponse.get_infoID(),
                true
        ));

        _mNearByUsersRecyclerViewAdapter.notifyItemInserted(0);

        _mNoOfNearByFriendsTextView.setText(String.valueOf(++_mNearByFriendsCount));

        // refresh the app
        this.onNearByFriendRecyclerViewClick(
                createNewInfoServerResponse.get_mUserID(),
                SharedPreferenceService.getUserName(this),
                (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("gender"),
                (String) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("profilePic"),
                createNewInfoServerResponse.get_mSelectedOptions(),
                createNewInfoServerResponse.get_mSelectedActions(),
                createNewInfoServerResponse.get_mContentText(),
                createNewInfoServerResponse.get_mUploadedImageURLS(),
                createNewInfoServerResponse.get_mUploadedVideoURLS(),
                (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("phoneCall"),
                (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("chat"),
                (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("location"),
                ((ArrayList<Double>) createNewInfoServerResponse.get_mLocationStatic().get("coordinates")).get(1),
                ((ArrayList<Double>) createNewInfoServerResponse.get_mLocationStatic().get("coordinates")).get(0),
                createNewInfoServerResponse.get_infoID()
        );



//        _mNoOfFriendsNearByTextView.setText(String.valueOf(_mNearByUsersViewModelList.size()));

    }

    @Override
    public void _updateInfo(
            CreateNewInfoServerResponse createNewInfoServerResponse
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
    ) {
        // add in the recycleView

        int index = -1;
        for (NearByUsersViewModel nearByUsersViewModel: _mNearByUsersViewModelList) {
            if (nearByUsersViewModel.get_infoID().equals(createNewInfoServerResponse.get_infoID())) {
                index = _mNearByUsersViewModelList.indexOf(nearByUsersViewModel);
                break;
            }
        }

        // terminate if the index not found
        if (index == -1) return;

        _mNearByUsersViewModelList.set(index,
                new NearByUsersViewModel(
                    createNewInfoServerResponse.get_mUserID(),
                    SharedPreferenceService.getUserName(this),
                    (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("gender"),
                    (String) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("profilePic"),
                    createNewInfoServerResponse.get_mSelectedOptions(),
                    createNewInfoServerResponse.get_mSelectedActions(),
                    createNewInfoServerResponse.get_mContentText(),
                    createNewInfoServerResponse.get_mUploadedImageURLS(),
                    createNewInfoServerResponse.get_mUploadedVideoURLS(),
                    (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("phoneCall"),
                    (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("chat"),
                    (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("location"),
                    ((ArrayList<Double>) createNewInfoServerResponse.get_mLocationStatic().get("coordinates")).get(1),
                    ((ArrayList<Double>) createNewInfoServerResponse.get_mLocationStatic().get("coordinates")).get(0),
                    60000,
                    createNewInfoServerResponse.get_infoID(),
                        true
                )
        );
        _mNearByUsersRecyclerViewAdapter.notifyItemChanged(index);

        // refresh the app
        this.onNearByFriendRecyclerViewClick(
                createNewInfoServerResponse.get_mUserID(),
                SharedPreferenceService.getUserName(this),
                (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("gender"),
                (String) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("profilePic"),
                createNewInfoServerResponse.get_mSelectedOptions(),
                createNewInfoServerResponse.get_mSelectedActions(),
                createNewInfoServerResponse.get_mContentText(),
                createNewInfoServerResponse.get_mUploadedImageURLS(),
                createNewInfoServerResponse.get_mUploadedVideoURLS(),
                (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("phoneCall"),
                (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("chat"),
                (boolean) createNewInfoServerResponse.get_mUserVisibilityBooleans().get("location"),
                ((ArrayList<Double>) createNewInfoServerResponse.get_mLocationStatic().get("coordinates")).get(1),
                ((ArrayList<Double>) createNewInfoServerResponse.get_mLocationStatic().get("coordinates")).get(0),
                createNewInfoServerResponse.get_infoID()
        );

//        _mNoOfFriendsNearByTextView.setText(String.valueOf(_mNearByUsersViewModelList.size()));

    }

    @Override
    public void _deleteInfo(String _infoID) {

        int index = -1;
        for (NearByUsersViewModel nearByUsersViewModel: _mNearByUsersViewModelList) {
            if (nearByUsersViewModel.get_infoID().equals(_infoID)) {
                index = _mNearByUsersViewModelList.indexOf(nearByUsersViewModel);
                break;
            }
        }

        if (index == -1)
            return;

        _mNearByUsersViewModelList.remove(index);
        _mNearByUsersRecyclerViewAdapter.notifyItemRemoved(index);


        _mNoOfNearByFriendsTextView.setText(String.valueOf(--_mNearByFriendsCount));

        _showCreateNewPostFragmentBottomSheet();
    }

    /**
     *
     *
     * This will create a new fresh enter Details Fragment without editing
     *
     *
     */
    private void _showCreateNewPostFragmentBottomSheet() {

        SharedPreferences sharedPreferences = BaseClass.getSharedPreference(this);

        boolean _isThere = false;
        NearByUsersViewModel currentUserViewModel = null;
        for (NearByUsersViewModel nearByUsersViewModel: _mNearByUsersViewModelList) {
            if (nearByUsersViewModel.get_userID().equals(sharedPreferences.getString("userID", ""))) {
                currentUserViewModel = nearByUsersViewModel;
                _isThere = true;
            }
        }

        _replaceFragment(
                _isThere ?

                        ShowInfoOfCurrentUserFragment.newInstance(
                                currentUserViewModel.get_userID(),
                                currentUserViewModel.get_userName(),
                                currentUserViewModel.is_isGenderVisible(),
                                currentUserViewModel.get_isProfileImageVisible(),
                                currentUserViewModel.get_selectedOptions(),
                                currentUserViewModel.get_selectedActions(),
                                currentUserViewModel.get_contentText(),
                                currentUserViewModel.get_uploadedImageURLS(),
                                currentUserViewModel.get_uploadedVideoURLS(),
                                currentUserViewModel.is_isPhoneCallVisibile(),
                                currentUserViewModel.is_isChatVisible(),
                                currentUserViewModel.is_isLocationVisible(),
                                currentUserViewModel.get_infoID()
                        )

                        :

                        CreateNewInfoFragment.newInstance(
                                null,
                                false,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null,
                                false,
                                false,
                                false,
                                null,
                                false
                        )
        );

    }

    private Fragment _mOverlayFragment = null;
    private Fragment _mHidedFragment = null;
    private int _mScrollX = 0;
    private int _mScrollY = 0;

    public void _replaceFragment(Fragment fragment) {
        Fragment frag = _mFragmentManager.findFragmentById(_mBottomSheetFrameLayout.getId());
        if (frag != null)
            _mFragmentManager.beginTransaction().remove(frag).commit();

//        findViewById(R.id.nestedScrollViewID).scrollTo(0, 0);


        _mFragmentManager.beginTransaction().replace(_mBottomSheetFrameLayout.getId(), fragment).commit();
    }

    public void _overlayFragment(Fragment childFrag) {
        Fragment rootFrag = _mFragmentManager.findFragmentById(_mBottomSheetFrameLayout.getId());
        if (rootFrag instanceof ShowInfoOfCurrentUserFragment || rootFrag instanceof ShowInfoOfOtherUserFragment) {

//            findViewById(R.id.nestedScrollViewID).scrollTo(0, 0);
//            NestedScrollView nestedScrollView = findViewById(R.id.nestedScrollViewID);
//            _mScrollX = nestedScrollView.getScrollX();
//            _mScrollY = nestedScrollView.getScrollY();

            _mOverlayFragment = childFrag;
            _mHidedFragment = rootFrag;

//            nestedScrollView.scrollTo(0, 0);
            _mFragmentManager.beginTransaction().add(_mBottomSheetFrameLayout.getId(), childFrag).hide(rootFrag).show(childFrag).commit();
        }
    }

    public void _removeOverlayFragment() {
//        Fragment frag = _mFragmentManager.findFragmentById(_mBottomSheetFrameLayout.getId());
        if (_mOverlayFragment != null && _mHidedFragment != null) {
            _mFragmentManager.beginTransaction().remove(_mOverlayFragment).show(_mHidedFragment).commit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    findViewById(R.id.nestedScrollViewID).scrollTo(_mScrollX, _mScrollY);
                }
            }, 50);
            _mOverlayFragment = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case ADD_NEW_PROFILE_PICTURE_SIDE_NAV :
                    // side Navigation profilePicture Click
                    ArrayList<MediaFile> profiePictureList = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                    Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "tempCrop.jpg"));
                    UCrop.Options options = new UCrop.Options();
                    options.setFreeStyleCropEnabled(true);
                    UCrop.of(profiePictureList.get(0).getUri(), destinationUri)
                            .withOptions(options)
                            .start(this);

                    break;
                case UCrop.REQUEST_CROP :
                    _setProfilePicture();
                    break;
                case ADD_NEW_IMAGE_BOTTOM_SHEET :
                    {
                        ArrayList<MediaFile> mediaFileArrayList = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);


                        Fragment fragment = _mFragmentManager.findFragmentById(_mBottomSheetFrameLayout.getId());
                        for (MediaFile mediaFile : mediaFileArrayList) {

                            if (fragment instanceof CreateNewInfoFragment) {
                                ((CreateNewInfoFragment) fragment)._addNewImage(
                                        mediaFile.getUri().toString()
                                );
                            } else if (fragment instanceof ShowInfoOfOtherUserFragment) {
                                ((ShowInfoOfOtherUserFragment) fragment)._addNewImage(
                                        mediaFile.getUri().toString()
                                );
                            }


                        }
                    }
                    break;
                case ADD_NEW_VIDEO_BOTTOM_SHEET :
                    {
                        // it is an instance of EnterDetailsFragment ID
                        // so add new image there
                        ArrayList<MediaFile> mediaFileArrayList = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                        Fragment fragment = _mFragmentManager.findFragmentById(_mBottomSheetFrameLayout.getId());
                        for (MediaFile mediaFile : mediaFileArrayList) {

                            if (fragment instanceof CreateNewInfoFragment) {
                                ((CreateNewInfoFragment) fragment)._addNewVideo(
                                        mediaFile.getUri().toString()
                                );
                            } else if (fragment instanceof ShowInfoOfOtherUserFragment) {
                                ((ShowInfoOfOtherUserFragment) fragment)._addNewVideo(
                                        mediaFile.getUri().toString()
                                );
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    Handler handler = new Handler();
    int delay = 0; // 3mins in milli seconds
    private void _getInfosPeriodically() {
        // only call if the location is not equal to null
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _requestInfosFromServer();
                handler.postDelayed(this, delay);
            }
        }, delay);

    }

    private void _requestInfosFromServer() {

        if (_mCurrLocationMarker != null) {

            // if delay equal to 0 make it as 3min
            if (delay == 0)
                delay = 15000;

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BaseClass._mBaseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            GetAllInfoFromServerByRadiusAPI getAllInfoFromServerByRadiusAPI = retrofit.create(GetAllInfoFromServerByRadiusAPI.class);
            Call<List<GetInfosByRadiusServerResponse>> postListServerResponse =  getAllInfoFromServerByRadiusAPI._getAllInfoFromServerByRadius(
                    "Bearer " + SharedPreferenceService.getToken(DistanceFinderActivity.this),
                    new GetAllInfoByRadiusFromServer(
                    _mCurrLocationMarker.getPosition().latitude,
                    _mCurrLocationMarker.getPosition().longitude,
                    _mSelectedDistanceRadius
            ));

            postListServerResponse.enqueue(new Callback<List<GetInfosByRadiusServerResponse>>() {
                @Override
                public void onResponse(Call<List<GetInfosByRadiusServerResponse>> call, Response<List<GetInfosByRadiusServerResponse>> response) {
                    try {
                        if (!response.isSuccessful()) {
                            Toast.makeText(DistanceFinderActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        _mNearByUsersViewModelList.clear();
                        _mNearByFriendsCount = 0;


                        for (Map.Entry<String, Marker> markerEntry: _mVisibleMarkersMap.entrySet()) {
                            markerEntry.getValue().remove();
                        }
                        _mVisibleMarkersMap.clear();

                        for (GetInfosByRadiusServerResponse infoModel : response.body()) {
                            _mNearByUsersViewModelList.add(new NearByUsersViewModel(
                                    infoModel.getUserID(),
                                    infoModel.getUserName(),
                                    infoModel.getProfilePictureURL(),
                                    infoModel.getLatlog().get(1),
                                    infoModel.getLatlog().get(0),
                                    infoModel.getInfoID(),
                                    infoModel.getCreatedAtTimestamo(),
                                    false
                            ));

//                            _mNearByUsersViewModelList.add(new NearByUsersViewModel(
//                                    (String) (infoModel.get_mUserData().get("_id")),
//                                    (String) infoModel.get_mUserVisibilityBooleans().get("username"),
//                                    (boolean) infoModel.get_mUserVisibilityBooleans().get("gender"),
//                                    (String) infoModel.get_mUserVisibilityBooleans().get("profilePic"),
//                                    infoModel.get_mSelectedOptions(),
//                                    infoModel.get_mSelectedActions(),
//                                    infoModel.get_mContentText(),
//                                    infoModel.get_mUploadedImageURLS(),
//                                    infoModel.get_mUploadedVideoURLS(),
//                                    (boolean) infoModel.get_mUserVisibilityBooleans().get("phoneCall"),
//                                    (boolean) infoModel.get_mUserVisibilityBooleans().get("chat"),
//                                    (boolean) infoModel.get_mUserVisibilityBooleans().get("location"),
//                                    ((ArrayList<Double>) infoModel.get_mLocationStatic().get("coordinates")).get(1),
//                                    ((ArrayList<Double>) infoModel.get_mLocationStatic().get("coordinates")).get(0),
//                                    60000,
//                                    infoModel.get_infoID()
//                            ));


//                                    _mVisibleMarkersMap

//                                    _mAddMarkersMap.put(
//                                            (String) (infoModel.get_mUserData().get("_id")),
//                                            new MarkerOptions().position(new LatLng(
//                                                    ((ArrayList<Double>) infoModel.get_mLocationStatic().get("coordinates")).get(1),
//                                                    ((ArrayList<Double>) infoModel.get_mLocationStatic().get("coordinates")).get(0)
//                                            ))
//                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//                                            .title((String) infoModel.get_mUserVisibilityBooleans().get("username"))
//                                    );



                            _mVisibleMarkersMap.put(
                                    (infoModel.getInfoID()),
                                    _mGoogleMap.addMarker(
                                            new MarkerOptions().position(new LatLng(
                                                    infoModel.getLatlog().get(1),
                                                    infoModel.getLatlog().get(0)
                                            ))
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//                                            .title((String) infoModel.get_mUserVisibilityBooleans().get("username"))
                                    )
                            );

                        }



                        _mNearByUsersRecyclerViewAdapter.notifyDataSetChanged();
                        _mNearByFriendsCount = _mNearByUsersViewModelList.size();
                        _mNoOfNearByFriendsTextView.setText(String.valueOf(_mNearByFriendsCount));

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(DistanceFinderActivity.this, "Something went wrong while getting posts near by.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<GetInfosByRadiusServerResponse>> call, Throwable t) {

                    Toast.makeText(DistanceFinderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();

                }
            });



        }


    }

    // https://stackoverflow.com/questions/30170271/android-google-map-how-to-check-if-the-gps-location-is-inside-the-circle
//    private void _checkWhetherFriendIsWithInTheRange() {
//        System.out.println("Calling this hello");
//
//        for (Map.Entry<String, LatLng> entry: _mNearByFriendsMarkersMap.entrySet()) {
//            // iterate and add markers
//            float[] distance = new float[2];
//
//            LatLng latLng = entry.getValue();
//
//            Location.distanceBetween(latLng.latitude, latLng.longitude,
//                    _mCircle.getCenter().latitude, _mCircle.getCenter().longitude, distance);
//
//            if( distance[0] > _mCircle.getRadius() ){
//                // TODO : remove marker if and only if he is present if not present then don't do anything
//                _mRemoveMarkersArrayList.add(entry.getKey());
////                Toast.makeText(getBaseContext(), "Outside, distance from center: " + distance[0] + " radius: " + _mCircle.getRadius(), Toast.LENGTH_LONG).show();
//            } else {
//                // TODO : Add marker if already present don't add
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//                markerOptions.title(entry.getKey());
//                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//
//                _mAddMarkersMap.put(entry.getKey(), markerOptions);
////                Toast.makeText(getBaseContext(), "Inside, distance from center: " + distance[0] + " radius: " + _mCircle.getRadius() , Toast.LENGTH_LONG).show();
//            }
//        }
//
//        /**
//         *
//         * now check, then add or remove markers accordingly
//         *
//          */
//
//        // add markers
//        for (Map.Entry<String, MarkerOptions> entry: _mAddMarkersMap.entrySet()) {
//            if (_mVisibleMarkersMap.isEmpty()) {
//                // visible markers map is empty as add at firstTime
//
//                float[] results = new float[1];
//                Location.distanceBetween(
//                        _mCurrLocationMarker.getPosition().latitude,
//                        _mCurrLocationMarker.getPosition().longitude,
//                        entry.getValue().getPosition().latitude,
//                        entry.getValue().getPosition().longitude,
//                        results
//                );
//
//                String kms = String.format("%.02f", results[0] / 1000) + "Kms";
//                entry.getValue().snippet(kms);
//
//                _mVisibleMarkersMap.put(entry.getKey(), _mGoogleMap.addMarker(entry.getValue()));
//
//                // add in the recycleView
//                _mNearByUsersViewModelList.add(new NearByUsersViewModel(entry.getKey(), kms, "2"));
//                _mNearByUsersRecyclerViewAdapter.notifyItemInserted(_mNearByUsersViewModelList.size() - 1);
//                _mNoOfFriendsNearByTextView.setText(String.valueOf(_mNearByUsersViewModelList.size()));
//            } else  {
//                // iterate and check whether the user is present or not in visible markers map
//                // if present don't add
//                // if not present then add the marker to map
//                boolean isPresentAlready = false;
//                for (Map.Entry<String, Marker> visibleEntry: _mVisibleMarkersMap.entrySet()) {
//                    if (entry.getKey().equals(visibleEntry.getKey())) {
//                        isPresentAlready = true;
//                        break;
//                    }
//                }
//
//                if (!isPresentAlready) {
//                    // add marker to map as it is not present
//
//                    float[] results = new float[1];
//                    Location.distanceBetween(
//                            _mCurrLocationMarker.getPosition().latitude,
//                            _mCurrLocationMarker.getPosition().longitude,
//                            entry.getValue().getPosition().latitude,
//                            entry.getValue().getPosition().longitude,
//                            results
//                    );
//
//                    String kms = String.format("%.02f", results[0] / 1000) + "Kms";
//                    entry.getValue().snippet(kms);
//
//
//                    _mVisibleMarkersMap.put(entry.getKey(), _mGoogleMap.addMarker(entry.getValue()));
//
//                    // add in the recycleView
//                    _mNearByUsersViewModelList.add(new NearByUsersViewModel(entry.getKey(), kms, "2"));
//                    _mNearByUsersRecyclerViewAdapter.notifyItemInserted(_mNearByUsersViewModelList.size() - 1);
//                    _mNoOfFriendsNearByTextView.setText(String.valueOf(_mNearByUsersViewModelList.size()));
//                }
//            }
//        }
//        // clear the map
//        _mAddMarkersMap.clear();
//
//        // remove markers
//        for (String s: _mRemoveMarkersArrayList) {
//            // iterate over visible markers map
//            // if the marker is present remove it from both map and visibleMarkersMap
//            // and break the function and iterate for next user
//            // if not present then don't do anything
//            for (Map.Entry<String, Marker> entry: _mVisibleMarkersMap.entrySet()) {
//
//                if (s.equals(entry.getKey())) {
//                    _mVisibleMarkersMap.remove(entry.getKey()).remove();
//
//                    int removeIndex = -1;
//                    for (NearByUsersViewModel model: _mNearByUsersViewModelList) {
//                        // TODO: Implement any searching algorithm on top of kms to get the index much faster eg:- BST
//                        if (model.get_userName().equals(entry.getKey())) {
//                            // index found
//                            removeIndex = _mNearByUsersViewModelList.indexOf(model);
//                            break;
//                        }
//                    }
//
//                    if (removeIndex != -1) {
//                        // remove from the list and animate recyclerView
//                        _mNearByUsersViewModelList.remove(removeIndex);
//                        _mNearByUsersRecyclerViewAdapter.notifyItemRemoved(removeIndex);
//                        _mNoOfFriendsNearByTextView.setText(String.valueOf(_mNearByUsersViewModelList.size()));
//                    }
//
//                    break;
//                }
//
//            }
//        }
//        // clear the list
//        _mRemoveMarkersArrayList.clear();
//
//
//    }

    private void _updateGoogleMapMarkers() {

        for (Map.Entry<String, Marker> visibleMarkersEntry: _mVisibleMarkersMap.entrySet()) {
            // iterate and add markers
            float[] distance = new float[2];

            LatLng latLng = visibleMarkersEntry.getValue().getPosition();

            Location.distanceBetween(latLng.latitude, latLng.longitude,
                    _mCircle.getCenter().latitude, _mCircle.getCenter().longitude, distance);



            if (distance[0] > _mCircle.getRadius()) {
                // remove the one because he is out side the circleRadius
                visibleMarkersEntry.getValue().remove();
//                System.out.println("Distance " + distance[0] + " => Radius " + _mCircle.getRadius() + " => " + visibleMarkersEntry.getValue().getTitle() +  " Removed");
            }
//            System.out.println("Distance " + distance[0] + " => Radius " + _mCircle.getRadius() + " => " + visibleMarkersEntry.getValue().getTitle() + " Iterated");


        }

    }

    private void _displayBatteryConsumption() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //your method
                // get show current battery details
                BatteryManager batteryManager = (BatteryManager)DistanceFinderActivity.this.getSystemService(Context.BATTERY_SERVICE);
                final int batteryCurrent = batteryManager.getIntProperty(BATTERY_PROPERTY_CURRENT_NOW) / 1000;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Power.isConnected(DistanceFinderActivity.this)){
                            _displayDisAndTimeTextView.setText("The battery is charging with " + String.valueOf(batteryCurrent) + "mah");
                            autocompleteSupportFragment.setHint(batteryCurrent + " mah");
                        } else {
                            _displayDisAndTimeTextView.setText("The battery is draining " + String.valueOf(batteryCurrent) + "mah");
                            autocompleteSupportFragment.setHint(batteryCurrent + " mah");
                        }

                    }
                });
            }
        }, 0, 1000);
    }

    //    private final static int INTERVAL = 1000 * 60 * 2; // 2 minutes
//    Handler mHandler = new Handler();
//
//    Runnable mHandlerTask = new Runnable()
//    {
//        @Override
//        public void run() {
//            _getJSONFromURL();
//            mHandler.postDelayed(mHandlerTask, INTERVAL);
//        }
//    };
//
//    void startRepeatingTask()
//    {
//        mHandlerTask.run();
//    }
//
//    void stopRepeatingTask()
//    {
//        mHandler.removeCallbacks(mHandlerTask);
//    }

//    private void _getJSONFromURL() {
//        // only fetch if the handler is not started
//        System.out.println("Mao Function");
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "http://verificationosos.herokuapp.com/api/location/1", null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("JSON", String.valueOf(response));
//                double latitude = 0;
//                double longitude = 0;
//                String userName = null;
//                try {
//                    latitude = Double.parseDouble(String.valueOf(response.get("latitude")));
//                    longitude = Double.parseDouble(String.valueOf(response.get("longitude")));
//                    userName = String.valueOf(response.get("username"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } finally {
//
//                    if (latitude != 0 && longitude != 0 && userName != null) {
//
//                        // removeDestinationMarker and add new destinationMarker
//
//                        if (_mDestinationMarker != null) {
//                            _mDestinationMarker.remove();
//                        }
//
//                        //Place current location marker
//                        LatLng latLng = new LatLng(latitude, longitude);
//                        MarkerOptions markerOptions = new MarkerOptions();
//                        markerOptions.position(latLng);
//                        markerOptions.title(userName);
//                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//                        _mDestinationMarker = _mGoogleMap.addMarker(markerOptions);
//
//                        // TODO : Implement distance finder
//                        //move map camera
//                        if (_mCurrLocationMarker != null) {
//                            _getLocationAndDrawPolyLine();
//                        }
//
//                    } else {
//                        Toast.makeText(DistanceFinderActivity.this, "Something went wrong while fetching from api.", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        _mRequestQueue.add(jsonObjectRequest);
//
//
//    }

    //    private void _getLocationAndDrawPolyLine() {
//
//        showCurvedPolyline(_mCurrLocationMarker.getPosition(), _mDestinationMarker.getPosition(), 0.1);
//
//
////        if (!_isPolylineDrawn) {
//        //https://stackoverflow.com/questions/46411266/draw-arc-polyline-in-google-map
////            _isPolylineDrawn = true;
//////            startRepeatingTask();
////            String url = getUrl(_mCurrLocationMarker.getPosition(), _mDestinationMarker.getPosition(), "driving");
////            new FetchURL(DistanceFinderActivity.this).execute(url, "driving");
////        }
//    }



//    private void showCurvedPolyline (LatLng p1, LatLng p2, double k) {
//        //Calculate distance and heading between two points
//        double d = SphericalUtil.computeDistanceBetween(p1,p2);
//        double h = SphericalUtil.computeHeading(p1, p2);
//
//        //Midpoint position
//        LatLng p = SphericalUtil.computeOffset(p1, d*0.5, h);
//
//        //Apply some mathematics to calculate position of the circle center
//        double x = (1-k*k)*d*0.5/(2*k);
//        double r = (1+k*k)*d*0.5/(2*k);
//
//        LatLng c = SphericalUtil.computeOffset(p, x, h + 90.0);
//
//        //Polyline options
//        PolylineOptions options = new PolylineOptions();
//        List<PatternItem> pattern = Arrays.<PatternItem>asList(new Dash(30), new Gap(20));
//
//        //Calculate heading between circle center and two points
//        double h1 = SphericalUtil.computeHeading(c, p1);
//        double h2 = SphericalUtil.computeHeading(c, p2);
//
//        //Calculate positions of points on circle border and add them to polyline options
//        int numpoints = 100;
//        double step = (h2 -h1) / numpoints;
//
//        for (int i=0; i < numpoints; i++) {
//            LatLng pi = SphericalUtil.computeOffset(c, r, h1 + i * step);
//            options.add(pi);
//        }
//
//        // stop showing progressBar
//        _mProgressBar.setVisibility(View.GONE);
//
//
//        // get show current battery details
//        BatteryManager batteryManager = (BatteryManager)this.getSystemService(Context.BATTERY_SERVICE);
//        int batteryCurrent = batteryManager.getIntProperty(BATTERY_PROPERTY_CURRENT_NOW) / 1000;
//        float[] results = new float[1];
//        Location.distanceBetween(
//                _mCurrLocationMarker.getPosition().latitude,
//                _mCurrLocationMarker.getPosition().longitude,
//                _mDestinationMarker.getPosition().latitude,
//                _mDestinationMarker.getPosition().longitude,
//                results
//        );
//        _stringToSet = "Distance Between two places is " + String.format("%.02f", results[0] / 1000) + "Kms";
//        if (Power.isConnected(DistanceFinderActivity.this)){
//            _displayDisAndTimeTextView.setText(_stringToSet + " and the battery is charging with " + String.valueOf(batteryCurrent) + "mah");
//        } else {
//            _displayDisAndTimeTextView.setText(_stringToSet + " and the battery is draining " + String.valueOf(batteryCurrent) + "mah");
//        }
//
//
//        // Update battery capacity everySecond
//        if (_callEverySecond) {
//
//            //move map camera only at the firstTime
//            _mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(_mCurrLocationMarker.getPosition(), 15.0f));
//
//            new Timer().scheduleAtFixedRate(new TimerTask() {
//                @Override
//                public void run() {
//                    //your method
//                    // get show current battery details
//                    BatteryManager batteryManager = (BatteryManager)DistanceFinderActivity.this.getSystemService(Context.BATTERY_SERVICE);
//                    final int batteryCurrent = batteryManager.getIntProperty(BATTERY_PROPERTY_CURRENT_NOW) / 1000;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (Power.isConnected(DistanceFinderActivity.this)){
//                                _displayDisAndTimeTextView.setText(_stringToSet + " and the battery is charging with " + String.valueOf(batteryCurrent) + "mah");
//                            } else {
//                                _displayDisAndTimeTextView.setText(_stringToSet + " and the battery is draining " + String.valueOf(batteryCurrent) + "mah");
//                            }
//
//                        }
//                    });
//                }
//            }, 0, 1000);
//            _callEverySecond = false;
//        }
//
//        //Draw polyline
//        if (_mCurrentPolyline != null)
//            _mCurrentPolyline.remove();
//        _mCurrentPolyline = _mGoogleMap.addPolyline(options.width(10).color(Color.MAGENTA).geodesic(false).pattern(pattern));
//
//        // Instantiates a new CircleOptions object and defines the center and radius
//        _updateCircleInMap();
//
//
//
//    }

//    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
//        // Origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//        // Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//        // Mode
//        String mode = "mode=" + directionMode;
//        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + mode;
//        // Output format
//        String output = "json";
//        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
//        return url;
//    }
//
//    @Override
//    public void onTaskDone(Object... values) {
//        if (_mCurrentPolyline != null)
//            _mCurrentPolyline.remove();
//
//        PolylineOptions polyLineOptions  = (PolylineOptions) values[0];
//        List<PatternItem> pattern = Arrays.<PatternItem>asList(new Dash(30), new Gap(20));
//        polyLineOptions.width(10).color(Color.MAGENTA).pattern(pattern)
//                .geodesic(true);
//        _mCurrentPolyline = _mGoogleMap.addPolyline((PolylineOptions) values[0]);
//        // stop showing progressBar
//        _mProgressBar.setVisibility(View.GONE);
//        if (_callEverySecond)
//            //move map camera
//            _mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(_mCurrLocationMarker.getPosition(), 15.0f));
//
//        // set time and distance values
//        String[] distanceAndTimeArray = (String[]) values[1];
//
//        // get show current battery details
//        BatteryManager batteryManager = (BatteryManager)this.getSystemService(Context.BATTERY_SERVICE);
//        int batteryCurrent = batteryManager.getIntProperty(BATTERY_PROPERTY_CURRENT_NOW) / 1000;
//        float[] results = new float[1];
//        Location.distanceBetween(_mCurrLocationMarker.getPosition().latitude, _mCurrLocationMarker.getPosition().longitude, _mDestinationMarker.getPosition().latitude, _mDestinationMarker.getPosition().longitude, results);
////        _stringToSet = "Distance Between two places is " + distanceAndTimeArray[0] + " and duration required is " + distanceAndTimeArray[1];
//
//
//        if (_callEverySecond) {
//            new Timer().scheduleAtFixedRate(new TimerTask() {
//                @Override
//                public void run() {
//                    //your method
//                    // get show current battery details
//                    BatteryManager batteryManager = (BatteryManager)DistanceFinderActivity.this.getSystemService(Context.BATTERY_SERVICE);
//                    final int batteryCurrent = batteryManager.getIntProperty(BATTERY_PROPERTY_CURRENT_NOW) / 1000;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (Power.isConnected(DistanceFinderActivity.this)){
//                                _displayDisAndTimeTextView.setText(_stringToSet + " and the battery is charging with " + String.valueOf(batteryCurrent) + "mah");
//                            } else {
//                                _displayDisAndTimeTextView.setText(_stringToSet + " and the battery is draining " + String.valueOf(batteryCurrent) + "mah");
//                            }
//
//                        }
//                    });
//                }
//            }, 0, 1000);
//            _callEverySecond = false;
//        }
//
//    }

//    protected class yourDataTask extends AsyncTask<Void, Void, JSONObject>
//    {
//        @Override
//        protected JSONObject doInBackground(Void... params)
//        {
//
//            String str="http://verificationosos.herokuapp.com/api/location/2";
//            URLConnection urlConn = null;
//            BufferedReader bufferedReader = null;
//            try
//            {
//                URL url = new URL(str);
//                urlConn = url.openConnection();
//                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
//
//                StringBuffer stringBuffer = new StringBuffer();
//                String line;
//                while ((line = bufferedReader.readLine()) != null)
//                {
//                    stringBuffer.append(line);
//                }
//
//                return new JSONObject(stringBuffer.toString());
//            }
//            catch(Exception ex)
//            {
//                Log.e("App", "yourDataTask", ex);
//                return null;
//            }
//            finally
//            {
//                if(bufferedReader != null)
//                {
//                    try {
//                        bufferedReader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject response)
//        {
//            if(response != null)
//            {
//                Log.e("App", "Success: " + response);
//            }
//        }
//    }

//    private String getRealPathFromURI(Uri contentURI) {
//        String result;
//        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) { // Source is Dropbox or other similar local file path
//            result = contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            result = cursor.getString(idx);
//            cursor.close();
//        }
//        return result;
//    }

//    @Override
//    public void onClick(View v) {

//        switch (v.getId()) {
//            case R.id.showSpinnerLinearLayoutID :
//                _mDistanceSelectorSpinnerLinearLayout.setVisibility(View.GONE);
//                _mShowRadiusSpinnerLinearLayout.setVisibility(View.VISIBLE);
//                break;
//            case R.id.radius50MtsTextViewID :
//                _mDistanceSelectorSpinnerLinearLayout.setVisibility(View.VISIBLE);
//                _mShowRadiusSpinnerLinearLayout.setVisibility(View.GONE);
//                _updateCircleInMap(50.0);
//                _mShowSelectedRadiusTextView.setText("50 mts");
//                break;
//            case R.id.radius100MtsTextViewID :
//                _mDistanceSelectorSpinnerLinearLayout.setVisibility(View.VISIBLE);
//                _mShowRadiusSpinnerLinearLayout.setVisibility(View.GONE);
//                _updateCircleInMap(100.0);
//                _mShowSelectedRadiusTextView.setText("100 mts");
//                break;
//            case R.id.radius200MtsTextViewID :
//                _mDistanceSelectorSpinnerLinearLayout.setVisibility(View.VISIBLE);
//                _mShowRadiusSpinnerLinearLayout.setVisibility(View.GONE);
//                _updateCircleInMap(200.0);
//                _mShowSelectedRadiusTextView.setText("200 mts");
//                break;
//        }

//    }
}


