package com.example.combinedsliderandcreatepost.near_by_users_recycler_view;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.non_mvvm.distance_finder.DistanceFinderActivity;
import com.example.login.non_mvvm.distance_finder.call_back_interfaces.onNearByFriendsRecyclerViewClick;
import com.example.login.non_mvvm.distance_finder.call_back_interfaces.onRecyclerItemClickListener;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class NearByUsersRecyclerViewAdapter extends RecyclerView.Adapter<NearByUsersRecyclerViewAdapter.NearByFriendsViewHolder> {

    private Context _mContext;
    private List<NearByUsersViewModel> _mNearByUsersViewModelList;
    private onNearByFriendsRecyclerViewClick _mOnNearByFriendsRecyclerViewClick;
    private HashSet<String> _mMasterTimerStartedFriendsList;
    private List<String> _mTimerStartedFriendsList;

    public NearByUsersRecyclerViewAdapter(Context _mContext, List<NearByUsersViewModel> _mNearByUsersViewModelList, onNearByFriendsRecyclerViewClick _mOnNearByFriendsRecyclerViewClick) {
        this._mContext = _mContext;
        this._mNearByUsersViewModelList = _mNearByUsersViewModelList;
        this._mOnNearByFriendsRecyclerViewClick = _mOnNearByFriendsRecyclerViewClick;
        this._mMasterTimerStartedFriendsList = new HashSet<>();
        this._mTimerStartedFriendsList = new ArrayList<>();

        startTimerForAll();
    }

    @NonNull
    @Override
    public NearByFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NearByFriendsViewHolder(LayoutInflater.from(_mContext).inflate(R.layout.near_by_user_recycler_view_item, parent, false));
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull NearByFriendsViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder._mTimer != null) {
            System.out.println("Called timer stopped");
            try {
                _mTimerStartedFriendsList.remove(_mNearByUsersViewModelList.get(holder.getAdapterPosition()).get_infoID());
                holder._mTimer.cancel();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull NearByFriendsViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        System.out.println("Called timer started");
        // TODO : Implement count down timers correctly when the uses scrolled

        if (!_mTimerStartedFriendsList.contains(_mNearByUsersViewModelList.get(holder.getAdapterPosition()).get_infoID())) {

            holder._mTimer = new Timer();
            holder._mTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
//                    if (holder.getAdapterPosition() < 0) {
//                        holder._mTimer.cancel();
//                        return;
//                    }
                    ((Activity)_mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

//                            if (holder.getAdapterPosition() < 0) {
//                                holder._mTimer.cancel();
//                                return;
//                            }
                            if (holder.getAdapterPosition() == -1) {
                                return;
                            }

//                            double[] asldj = new double[2];
//                            asldj[0] = 76;
//                            asldj[1] = 50;
//
//                            HashMap<String, Object> location = new HashMap<>();
//                            location.put("thype", "Point");
//                            try {
//                                location.put("sadkf", new JSONArray(asldj));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            HashMap<String, Object> temp = new HashMap<>();
//                            temp.put("id", "shit");
//                            temp.put("laoction", new JSONObject(location));
//
//                            new JSONObject(temp);

                            int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(_mNearByUsersViewModelList.get(holder.getAdapterPosition()).get_remainingMilliSeconds());

                            if (minutes == 0) {
                                holder._mPostedTimeTextView.setText(TimeUnit.MILLISECONDS.toSeconds(_mNearByUsersViewModelList.get(holder.getAdapterPosition()).get_remainingMilliSeconds()) + "sec");
//                                long sec = TimeUnit.MILLISECONDS.toSeconds();
//                                if (sec <= 0) {
//                                    holder._mTimer.cancel();
//                                } else {
//
//                                }
                            }
                            else if (minutes == 1) {
//                        _mNearByUsersViewModelList.get(position).set_time(String.valueOf(minutes));
//                        holder._mPostedTimeTextView.setText(_mNearByUsersViewModelList.get(position).get_time() + min");
                                holder._mPostedTimeTextView.setText(String.valueOf(minutes) + "min");
                            } else {
//                        _mNearByUsersViewModelList.get(position).set_time(String.valueOf(minutes));
//                        holder._mPostedTimeTextView.setText(_mNearByUsersViewModelList.get(position).get_time() + "min");
                                holder._mPostedTimeTextView.setText(String.valueOf(minutes) + "min");
                            }

                        }
                    });
                }
            }, 0, 1000);

//            holder._mCountDownTimer = new CountDownTimer(60000, 1000) {
//
//                public void onTick(long millisUntilFinished) {
//                    int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
//
//                    if (minutes == 0) {
//                        holder._mPostedTimeTextView.setText(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + "secs");
//                    }
//                    else if (minutes == 1) {
////                        _mNearByUsersViewModelList.get(position).set_time(String.valueOf(minutes));
////                        holder._mPostedTimeTextView.setText(_mNearByUsersViewModelList.get(position).get_time() + "min");
//                        holder._mPostedTimeTextView.setText(String.valueOf(minutes) + "min");
//                    } else {
////                        _mNearByUsersViewModelList.get(position).set_time(String.valueOf(minutes));
////                        holder._mPostedTimeTextView.setText(_mNearByUsersViewModelList.get(position).get_time() + "min");
//                        holder._mPostedTimeTextView.setText(String.valueOf(minutes) + "min");
//                    }
//                }
//
//                public void onFinish() {
//                    holder._mPostedTimeTextView.setText("Timer finished");
//                }
//            }.start();

            _mTimerStartedFriendsList.add(_mNearByUsersViewModelList.get(holder.getAdapterPosition()).get_infoID());
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final NearByFriendsViewHolder holder, final int position) {
        holder._mFriendNameTextView.setText(String.valueOf(_mNearByUsersViewModelList.get(position).get_userName()));
        holder._mDistanceBetweenTextView.setText(
                getDistance(
                        ((DistanceFinderActivity) _mContext)._mCurrLocationMarker,
                        _mNearByUsersViewModelList.get(position).get_latitude(),
                        _mNearByUsersViewModelList.get(position).get_longitude()
                )
        );
//        holder._mPostedTimeTextView.setText(_mNearByUsersViewModelList.get(position).get_time() + "min");





        holder.set_mOnRecyclerItemClickListener(new onRecyclerItemClickListener() {
            @Override
            public void onClick(View v, int position, boolean isLongClick) {
                _mOnNearByFriendsRecyclerViewClick.onNearByFriendRecyclerViewClick(
                        _mNearByUsersViewModelList.get(position).get_userID(),
                        _mNearByUsersViewModelList.get(position).get_userName(),
                        _mNearByUsersViewModelList.get(position).is_isGenderVisible(),
                        _mNearByUsersViewModelList.get(position).get_isProfileImageVisible(),
                        _mNearByUsersViewModelList.get(position).get_selectedOptions(),
                        _mNearByUsersViewModelList.get(position).get_selectedActions(),
                        _mNearByUsersViewModelList.get(position).get_contentText(),
                        _mNearByUsersViewModelList.get(position).get_uploadedImageURLS(),
                        _mNearByUsersViewModelList.get(position).get_uploadedVideoURLS(),
                        _mNearByUsersViewModelList.get(position).is_isPhoneCallVisibile(),
                        _mNearByUsersViewModelList.get(position).is_isChatVisible(),
                        _mNearByUsersViewModelList.get(position).is_isLocationVisible(),
                        _mNearByUsersViewModelList.get(position).get_latitude(),
                        _mNearByUsersViewModelList.get(position).get_longitude(),
                        _mNearByUsersViewModelList.get(position).get_infoID()
                );
            }
        });

    }

    @Override
    public int getItemCount() {
        return _mNearByUsersViewModelList.size();
    }

    private void startTimerForAll() {

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //your method
                for (int i = _mNearByUsersViewModelList.size() - 1; i >= 0; --i) {
                    NearByUsersViewModel model = _mNearByUsersViewModelList.get(i);
                    model.set_remainingMilliSeconds(model.get_remainingMilliSeconds() - 1000); // decrease one sec for everyIteration

                    if (model.get_remainingMilliSeconds() <= 0) {
                        // TODO : remove him from the recyclerView and server and delete from master timer list
                        // the above line will remove the model from both nearByFriendsList and also from the masterTimerList
                        _mMasterTimerStartedFriendsList.remove(_mNearByUsersViewModelList.get(i).get_infoID());
                        _mOnNearByFriendsRecyclerViewClick.timerFinishedPosition(i);
//                        int finalI = i;
//                        ((Activity)_mContext).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                notifyItemRemoved(finalI);
//                            }
//                        });
                    } else {
                        _mMasterTimerStartedFriendsList.add(model.get_infoID());
                    }

                    // ass we are using hashSet it will only insert once if try to insert multiple times also
                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (Power.isConnected(DistanceFinderActivity.this)){
//                            _displayDisAndTimeTextView.setText("The battery is charging with " + String.valueOf(batteryCurrent) + "mah");
//                        } else {
//                            _displayDisAndTimeTextView.setText("The battery is draining " + String.valueOf(batteryCurrent) + "mah");
//                        }
//
//                    }
//                });
            }
        }, 0, 1000);



        // 30 mins in milli-seconds = 1800000
//        new CountDownTimer(1800000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
////                int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
//
//                for (NearByUsersViewModel model: _mNearByUsersViewModelList) {
//                    model.set_remainingMilliSeconds(millisUntilFinished);
//                    _mMasterTimerStartedFriendsList.add(model.get_infoID());
//                }
//
////                if (minutes == 0) {
//////                    holder._mPostedTimeTextView.setText(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + "sec");
////                }
////                else if (minutes == 1) {
//////                        _mNearByUsersViewModelList.get(position).set_time(String.valueOf(minutes));
//////                        holder._mPostedTimeTextView.setText(_mNearByUsersViewModelList.get(position).get_time() + "min");
//////                    holder._mPostedTimeTextView.setText(String.valueOf(minutes) + " min");
////                } else {
//////                        _mNearByUsersViewModelList.get(position).set_time(String.valueOf(minutes));
//////                        holder._mPostedTimeTextView.setText(_mNearByUsersViewModelList.get(position).get_time() + "min");
//////                    holder._mPostedTimeTextView.setText(String.valueOf(minutes) + "min");
////                }
//            }
//
//            public void onFinish() {
////                holder._mPostedTimeTextView.setText("Timer finished");
//                // TODO: Implement delete options here
//            }
//        }.start();
    }

    class NearByFriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Timer _mTimer;

        private CircleImageView _mNearByFriendCircleImageView;
        private TextView _mFriendNameTextView;
        private TextView _mDistanceBetweenTextView;
        private TextView _mPostedTimeTextView;

        private onRecyclerItemClickListener _mOnRecyclerItemClickListener;


        public NearByFriendsViewHolder(@NonNull View itemView) {
            super(itemView);


            _mNearByFriendCircleImageView = itemView.findViewById(R.id.nearByFriendCircleImageViewID);
            _mFriendNameTextView = itemView.findViewById(R.id.nearByFriendUserNameTextViewID);
            _mDistanceBetweenTextView = itemView.findViewById(R.id.aTextViewID);
            _mPostedTimeTextView = itemView.findViewById(R.id.bTextViewID);

            itemView.setOnClickListener(this);
        }

        public void set_mOnRecyclerItemClickListener(onRecyclerItemClickListener _mOnRecyclerItemClickListener) {
            this._mOnRecyclerItemClickListener = _mOnRecyclerItemClickListener;
        }

        @Override
        public void onClick(View v) {
            _mOnRecyclerItemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }

    private String getDistance(Marker currentUserMarker, double nearByFriendUserLat, double nearByFriendUserLon) {
        float[] results = new float[1];
        Location.distanceBetween(
                currentUserMarker.getPosition().latitude,
                currentUserMarker.getPosition().longitude,
                nearByFriendUserLat,
                nearByFriendUserLon,
                results
        );
        results[0] = results[0] / 1000;
        if ( ((int)results[0]) == 0) {
            results[0] = results[0] * 1000;
            return (int)results[0] + "m";
        } else {
            return (int)results[0] + "km";
        }

//        if ( ((int)results[0]) == 0) {
//            results[0] = results[0] * 1000;
//            return String.format("%.02f", results[0]) + "m";
//        } else {
//            return String.format("%.02f", results[0]) + "km";
//        }

    }
}
