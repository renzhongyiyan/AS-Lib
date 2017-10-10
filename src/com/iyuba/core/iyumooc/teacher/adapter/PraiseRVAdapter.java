package com.iyuba.core.iyumooc.teacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.core.common.thread.GitHubImageLoader;
import com.iyuba.core.iyumooc.teacher.bean.AgreeListBean;
import com.iyuba.core.iyumooc.teacher.bean.QuestionListBean;
import com.iyuba.lib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：renzhy on 17/7/15 18:10
 * 邮箱：renzhongyigoo@gmail.com
 */
public class PraiseRVAdapter extends RecyclerView.Adapter<PraiseRVAdapter.PraiseViewHolder>{

    private Context mContext;
    private ArrayList<AgreeListBean.AgreeDataBean> mDataList;

    public PraiseRVAdapter(Context context) {
        mContext = context;
        mDataList = new ArrayList<>();
    }

    public void setData(List<AgreeListBean.AgreeDataBean> mList){
        mDataList.clear();
        mDataList.addAll(mList);
    }

    public void addList(List<AgreeListBean.AgreeDataBean> mList){
        mDataList.addAll(mList);
    }

    public void clearList(){
        mDataList.clear();
    }

    @Override
    public PraiseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_praise_detail,parent,false);
        PraiseViewHolder viewHolder = new PraiseViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PraiseViewHolder holder, int position) {
        final AgreeListBean.AgreeDataBean agreeDataBean = mDataList.get(position);

        GitHubImageLoader.Instace(mContext).setCirclePic(
                agreeDataBean.getUid(), holder.userImage);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class PraiseViewHolder extends RecyclerView.ViewHolder{
        public ImageView userImage;

        public PraiseViewHolder(View itemView) {
            super(itemView);
            userImage = (ImageView) itemView
                    .findViewById(R.id.iv_user_image);
        }
    }
}
