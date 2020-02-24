package com.example.combinedsliderandcreatepost;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.AndroidViewModel;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.databinding.ShapedImagesRecyclerViewItemBinding;
import com.example.login.databinding.ShapedVideosRecyclerViewItemBinding;
import com.example.login.mvvm.feature2.recycler_view.models.ImageVideoModel;
import com.example.login.mvvm.feature2.viewmodel.fragments.bottom_sheet.CreateNewInfoFragmentViewModel;
import com.example.login.non_mvvm.distance_finder.call_back_interfaces.onRecyclerItemClickListener;

public class ImagesVideosRecyclerViewAdapter extends ListAdapter<ImageVideoModel, RecyclerView.ViewHolder> {

    private Context _mContext;
    private AndroidViewModel mViewModel;
    private PassedViewModel mPassedViewModel;

    public enum PassedViewModel {
        CREATE_NEW_INFO,
        SHOW_INFO_OF_CURR,
        POSTS_RECYCLER_VIEW_ADAPTER,
        SHOW_COMMENTS_FOR_POSTS,
        SHOW_INFO_OF_OTHERS
    }

    public ImagesVideosRecyclerViewAdapter(Context _mContext, AndroidViewModel mViewModel, PassedViewModel mPassedViewModel) {
        super(DIFF_CALLBACK);
        this._mContext = _mContext;
        this.mViewModel = mViewModel;
        this.mPassedViewModel = mPassedViewModel;

//        if (_mImageVideoModelList.isEmpty())
//            _mRecyclerView.setVisibility(View.GONE);
//        else
//            _mRecyclerView.setVisibility(View.VISIBLE);
    }

    private static final DiffUtil.ItemCallback<ImageVideoModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<ImageVideoModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull ImageVideoModel oldItem, @NonNull ImageVideoModel newItem) {
            // TODO: Here are we are telling by default as the items are not same everytime but here we need to add some id so that it check that id first then go for contents
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ImageVideoModel oldItem, @NonNull ImageVideoModel newItem) {
            return TextUtils.equals(oldItem.getURL(), newItem.getURL())
                    && oldItem.isVideo() == newItem.isVideo()
                    && oldItem.isImage() == newItem.isImage()
                    && oldItem.isFromServer() == newItem.isFromServer();
        }
    };

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if (viewType == 0)
            return new BottomSheetImagesViewHolder(DataBindingUtil.inflate(LayoutInflater.from(_mContext), R.layout.shaped_images_recycler_view_item, parent, false));

        return new BottomSheetVideosViewHolder(DataBindingUtil.inflate(LayoutInflater.from(_mContext), R.layout.shaped_videos_recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == 0) {
            BottomSheetImagesViewHolder imagesViewHolder = (BottomSheetImagesViewHolder) holder;
            imagesViewHolder.imagesRecyclerViewItemBinding.setModel(getItem(position));
            // inflate Images
//            final BottomSheetImagesViewHolder bottomSheetImagesViewHolder = (BottomSheetImagesViewHolder) holder;
//            ViewCompat.setTransitionName(bottomSheetImagesViewHolder._mImageView, String.valueOf(position));
//
//            /**
//             *
//             * Is from server so add extra url in front
//             *
//             */
//            if (_mImageVideoModelList.get(position).isFromServer()) {
//                System.out.println("URL " + BaseClass._mBaseImageURL + _mImageVideoModelList.get(position).getURL());
//
//                Glide.with(_mContext)
//                        .load(BaseClass._mBaseImageURL + _mImageVideoModelList.get(position).getURL())
//                        .addListener(new RequestListener<Drawable>() {
//                            @Override
//                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                bottomSheetImagesViewHolder._mCloseImageView.setVisibility(_isImagesEditable ? View.VISIBLE : View.GONE);
//                                bottomSheetImagesViewHolder._mProgressBar.setVisibility(View.GONE);
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                bottomSheetImagesViewHolder._mProgressBar.setVisibility(View.GONE);
//                                bottomSheetImagesViewHolder._mCloseImageView.setVisibility(_isImagesEditable ? View.VISIBLE : View.GONE);
//
//                                return false;
//                            }
//                        })
//                        .into(bottomSheetImagesViewHolder._mImageView);
//            } else {
//                Glide.with(_mContext)
//                        .load(_mImageVideoModelList.get(position).getURL())
//                        .addListener(new RequestListener<Drawable>() {
//                            @Override
//                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                bottomSheetImagesViewHolder._mProgressBar.setVisibility(View.GONE);
//                                bottomSheetImagesViewHolder._mCloseImageView.setVisibility(_isImagesEditable ? View.VISIBLE : View.GONE);
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                bottomSheetImagesViewHolder._mProgressBar.setVisibility(View.GONE);
//                                bottomSheetImagesViewHolder._mCloseImageView.setVisibility(_isImagesEditable ? View.VISIBLE : View.GONE);
//                                return false;
//                            }
//                        })
//                        .into(bottomSheetImagesViewHolder._mImageView);
//            }
//
////            bottomSheetImagesViewHolder._mCloseImageView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    _mBottomSheetImageRecyclerViewCallBack.onDelete(_mImageVideoModelList.get(position).getURL());
////                }
////            });
//
//            bottomSheetImagesViewHolder.setOnRecyclerViewClickListener(new onRecyclerItemClickListener() {
//                @Override
//                public void onClick(View view, int position, boolean isLongClick) {
//
//                    // https://mikescamell.com/shared-element-transitions-part-4-recyclerview/
//
//                    notifyDataSetChanged();
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            System.out.println(_mImageVideoModelList.size());
//
//                            System.out.println(_mImageVideoModelList.get(position).getURL());
//                            Intent intent = new Intent(_mContext, ImagesVideosCarouselActivity.class);
//                            intent.putExtra("ClickedPosition", position);
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("List", (Serializable) _mImageVideoModelList);
//                            intent.putExtra("ImagesVideosList",  bundle);
////                intent.putExtra("DrawableInt", _mRecyclerViewModelList.get(position).getDrawable());
//
//                            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                    (Activity) _mContext,
//                                    bottomSheetImagesViewHolder._mImageView,
//                                    ViewCompat.getTransitionName(bottomSheetImagesViewHolder._mImageView)
//                            );
//
//
////                _mTransparentGrayCoverView.setVisibility(View.VISIBLE);
//                            _mContext.startActivity(intent, optionsCompat.toBundle());
//                        }
//                    }, 200);
//                }
//            });
        }
        else {
            BottomSheetVideosViewHolder videosViewHolder = (BottomSheetVideosViewHolder) holder;
            videosViewHolder.shapedVideosRecyclerViewItemBinding.setModel(getItem(position));
            // inflate videos
//            final BottomSheetVideosViewHolder bottomSheetVideosViewHolder = (BottomSheetVideosViewHolder) holder;
//            ViewCompat.setTransitionName(bottomSheetVideosViewHolder._mVideoThumbnailImageView, String.valueOf(position));
//
//            /**
//             *
//             * Is from server so add extra url in front
//             *
//             */
//            if (_mImageVideoModelList.get(position).isFromServer()) {
//                System.out.println(BaseClass._mBaseImageURL + _mImageVideoModelList.get(position).getURL());
//
////                bottomSheetVideosViewHolder
////                        ._mVideoThumbnailImageView
////                        .setSource(BaseClass._mBaseImageURL + _mImageVideoModelList.get(position).getURL());
////                bottomSheetVideosViewHolder._mProgressBar.setVisibility(View.GONE);
//
//                Glide.with(_mContext)
//                        .load(BaseClass._mBaseImageURL + _mImageVideoModelList.get(position).getURL())
//                        .addListener(new RequestListener<Drawable>() {
//                            @Override
//                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                bottomSheetVideosViewHolder._mCloseImageView.setVisibility(_isImagesEditable ? View.VISIBLE : View.GONE);
//                                bottomSheetVideosViewHolder._mProgressBar.setVisibility(View.GONE);
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                bottomSheetVideosViewHolder._mProgressBar.setVisibility(View.GONE);
//                                bottomSheetVideosViewHolder._mCloseImageView.setVisibility(_isImagesEditable ? View.VISIBLE : View.GONE);
//                                return false;
//                            }
//                        })
//                        .into(bottomSheetVideosViewHolder._mVideoThumbnailImageView);
//            } else {
////                bottomSheetVideosViewHolder
////                        ._mVideoThumbnailImageView
////                        .setSource(BaseClass.getRealPathFromURI(_mContext, Uri.parse(_mImageVideoModelList.get(position).getURL())));
////                bottomSheetVideosViewHolder._mProgressBar.setVisibility(View.GONE);
//
//
//                Glide.with(_mContext)
//                        .load(_mImageVideoModelList.get(position).getURL())
//                        .addListener(new RequestListener<Drawable>() {
//                            @Override
//                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                bottomSheetVideosViewHolder._mProgressBar.setVisibility(View.GONE);
//                                bottomSheetVideosViewHolder._mCloseImageView.setVisibility(_isImagesEditable ? View.VISIBLE : View.GONE);
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                bottomSheetVideosViewHolder._mProgressBar.setVisibility(View.GONE);
//                                bottomSheetVideosViewHolder._mCloseImageView.setVisibility(_isImagesEditable ? View.VISIBLE : View.GONE);
//                                return false;
//                            }
//                        })
//                        .into(bottomSheetVideosViewHolder._mVideoThumbnailImageView);
//            }
//
////            bottomSheetImagesViewHolder._mCloseImageView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    _mBottomSheetImageRecyclerViewCallBack.onDelete(_mImageVideoModelList.get(position).getURL());
////                }
////            });
//
//            bottomSheetVideosViewHolder.setOnRecyclerViewClickListener(new onRecyclerItemClickListener() {
//                @Override
//                public void onClick(View view, int position, boolean isLongClick) {
//
//                    // https://mikescamell.com/shared-element-transitions-part-4-recyclerview/
//
//                    notifyDataSetChanged();
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            System.out.println(_mImageVideoModelList.size());
//
//                            System.out.println(_mImageVideoModelList.get(position).getURL());
//                            Intent intent = new Intent(_mContext, ImagesVideosCarouselActivity.class);
//                            intent.putExtra("ClickedPosition", position);
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable("List", (Serializable) _mImageVideoModelList);
//                            intent.putExtra("ImagesVideosList",  bundle);
////                intent.putExtra("DrawableInt", _mRecyclerViewModelList.get(position).getDrawable());
//
//                            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                    (Activity) _mContext,
//                                    bottomSheetVideosViewHolder._mVideoThumbnailImageView,
//                                    ViewCompat.getTransitionName(bottomSheetVideosViewHolder._mVideoThumbnailImageView)
//                            );
//
//
////                _mTransparentGrayCoverView.setVisibility(View.VISIBLE);
//                            _mContext.startActivity(intent, optionsCompat.toBundle());
//                        }
//                    }, 200);
//                }
//            });
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isImage())
            return 0;

        return 1;
    }

    public class BottomSheetImagesViewHolder extends RecyclerView.ViewHolder {

        private ShapedImagesRecyclerViewItemBinding imagesRecyclerViewItemBinding;

//        private onRecyclerItemClickListener onRecyclerViewClickListener;


        public BottomSheetImagesViewHolder(@NonNull ShapedImagesRecyclerViewItemBinding itemBinding) {
            super(itemBinding.getRoot());


            imagesRecyclerViewItemBinding = itemBinding;
            imagesRecyclerViewItemBinding.setClickHandlers(new ClickHandlers());

//            _mImageView = itemView.findViewById(R.id.shapedImageViewID);
//            _mProgressBar = itemView.findViewById(R.id.tempImageLoadingProgressBar);
//            _mCloseImageView = itemView.findViewById(R.id.shapedImagesRecyclerViewDeleteImageViewID);
//            itemView.setOnClickListener(this);


//            _mCloseImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    _mImageVideoModelList.remove(getAdapterPosition());
//                    notifyItemRemoved(getAdapterPosition());
//
//                    if (_mImageVideoModelList.isEmpty()) {
//                        _mAddImageView.setImageDrawable(_mContext.getDrawable(R.drawable.ic_action_add_photo_blue));
//                        _mRecyclerView.setVisibility(View.GONE);
//                    }
//                }
//            });
        }

//        public void setOnRecyclerViewClickListener(onRecyclerItemClickListener onRecyclerViewClickListener) {
//            this.onRecyclerViewClickListener = onRecyclerViewClickListener;
//        }
//
//        @Override
//        public void onClick(View v) {
//            onRecyclerViewClickListener.onClick(v, getAdapterPosition(), false);
//        }

        public class ClickHandlers {
            public void removeImage(View view) {
                switch (mPassedViewModel) {
                    case CREATE_NEW_INFO:
                        ((CreateNewInfoFragmentViewModel) mViewModel).getSelectedImageVideoModelsList().getValue().remove(getAdapterPosition());
                        ((CreateNewInfoFragmentViewModel) mViewModel).getSelectedImageVideoModelsList().setValue(
                                ((CreateNewInfoFragmentViewModel) mViewModel).getSelectedImageVideoModelsList().getValue()
                        );
                        break;
                    case SHOW_INFO_OF_CURR:
                        break;
                    case POSTS_RECYCLER_VIEW_ADAPTER:
                        break;
                    case SHOW_COMMENTS_FOR_POSTS:
                        break;
                    case SHOW_INFO_OF_OTHERS:
                        break;
                }
            }
        }
    }

    class BottomSheetVideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ShapedVideosRecyclerViewItemBinding shapedVideosRecyclerViewItemBinding;

//        private ImageView _mVideoThumbnailImageView;
//        private ProgressBar _mProgressBar;
//        private ImageView _mCloseImageView;
        private onRecyclerItemClickListener onRecyclerViewClickListener;


        public BottomSheetVideosViewHolder(@NonNull ShapedVideosRecyclerViewItemBinding itemBinding) {
            super(itemBinding.getRoot());

            shapedVideosRecyclerViewItemBinding = itemBinding;
        }

        public void setOnRecyclerViewClickListener(onRecyclerItemClickListener onRecyclerViewClickListener) {
            this.onRecyclerViewClickListener = onRecyclerViewClickListener;
        }

        @Override
        public void onClick(View v) {
            onRecyclerViewClickListener.onClick(v, getAdapterPosition(), false);
        }
    }


}
