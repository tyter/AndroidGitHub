package com.lazier.androidgithub;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lazier.githubsdk.model.Repository;
import com.lazier.githubsdk.model.RepositoryForSearch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhoukaifeng(zhoukaifeng@meituan.com) on 16/10/20.
 */

public class RepositoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
 implements View.OnClickListener {

    private final Activity mActivity;
    private List<RepositoryForSearch> mItems = new ArrayList<>();

    public RepositoriesAdapter(Activity activity) {
        if (activity == null) {
            throw new RuntimeException("init error: activity object can not be null");
        }
        mActivity = activity;
    }

    public void clear() {
        this.mItems.clear();
    }

    public void addItems(List<RepositoryForSearch> mItems) {
        if (mItems == null) {
            return ;
        }
        this.mItems.addAll(mItems);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_repository, parent, false);
        return new RepositoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RepositoryForSearch repositoryForSearch = mItems.get(position);
        RepositoryViewHolder holderReal = (RepositoryViewHolder) holder;
        holderReal.mUserLogo.setImageURI(repositoryForSearch.mOwner.mImageUrl);
        holderReal.mRepoName.setText(repositoryForSearch.mFullName);
        holderReal.mRepoDes.setText(repositoryForSearch.mDescription);
        holderReal.mLanguage.setText(repositoryForSearch.mLanguage);
        holderReal.mStarCount.setText(String.valueOf(repositoryForSearch.mStargazersCount));
        holderReal.mForkCount.setText(String.valueOf(repositoryForSearch.mForksCount));

        String updateTipText = getUpdateTipText(repositoryForSearch);
        if (TextUtils.isEmpty(updateTipText)) {
            holderReal.mUpdateTip.setVisibility(View.GONE);
        } else {
            holderReal.mUpdateTip.setText(updateTipText);
            holderReal.mUpdateTip.setVisibility(View.VISIBLE);
        }

        holder.itemView.setTag(repositoryForSearch);
        holder.itemView.setOnClickListener(this);
    }

    private String getUpdateTipText(RepositoryForSearch repositoryForSearch) {
        Repository.Time time = Repository.Time.parse(repositoryForSearch.mPushAt);
        if (time != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(System.currentTimeMillis()));

            String ext = "";
            String count = "";

            Repository.Time nowTime = Repository.Time.parse(calendar);
            if (nowTime.mYear > time.mYear) {
                count = String.valueOf(nowTime.mYear - time.mYear);
                ext = mActivity.getText(R.string.year).toString();
            } else if (nowTime.mMonth > time.mMonth) {
                count = String.valueOf(nowTime.mMonth - time.mMonth);
                ext = mActivity.getText(R.string.month).toString();
            } else if (nowTime.mDay > time.mDay) {
                count = String.valueOf(nowTime.mDay - time.mDay);
                ext = mActivity.getText(R.string.day).toString();
            } else if (nowTime.mHour > time.mHour) {
                count = String.valueOf(nowTime.mHour - time.mHour);
                ext = mActivity.getText(R.string.hour).toString();
            } else if (nowTime.mMinute > time.mMinute) {
                count = String.valueOf(nowTime.mMinute - time.mMinute);
                ext = mActivity.getText(R.string.minute).toString();
            } else {
                return mActivity.getText(R.string.updated_seconds).toString();
            }

            ext += mActivity.getText(R.string.before);

            return mActivity.getText(R.string.updated) + count + ext;
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        if (mActivity.isFinishing()
                || v == null
                || v.getTag() == null
                || !(v.getTag() instanceof RepositoryForSearch)) {
            return ;
        }

        RepositoryForSearch repositoryForSearch = (RepositoryForSearch)v.getTag();
        Intent intent = new Intent(mActivity, WebViewActivity.class);
        intent.setData(Uri.parse(repositoryForSearch.mHtmlUrl));
        intent.putExtra(WebViewActivity.KEY_TITLE, repositoryForSearch.mFullName);
        mActivity.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class RepositoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_user_logo)
        SimpleDraweeView mUserLogo;

        @BindView(R.id.tv_repo_name)
        TextView mRepoName;

        @BindView(R.id.iv_repo_action)
        ImageView mRepoAction;

        @BindView(R.id.tv_repo_des)
        TextView mRepoDes;

        @BindView(R.id.tv_language)
        TextView mLanguage;

        @BindView(R.id.tv_star_count)
        TextView mStarCount;

        @BindView(R.id.tv_fork_count)
        TextView mForkCount;

        @BindView(R.id.tv_update_tip)
        TextView mUpdateTip;

        public RepositoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
