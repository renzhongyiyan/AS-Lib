package com.iyuba.core.iyumooc.teacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.core.common.thread.GitHubImageLoader;
import com.iyuba.core.iyumooc.teacher.bean.CommentListBean;
import com.iyuba.lib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：renzhy on 17/7/20 22:20
 * 邮箱：renzhongyigoo@gmail.com
 */
public class CommentRVAdapter extends RecyclerView.Adapter<CommentRVAdapter.CommentViewHolder>{

    private Context mContext;
    private ArrayList<CommentListBean.AnswersBean> mDataList;

    public CommentRVAdapter(Context context) {
        mContext = context;
        mDataList = new ArrayList<>();
    }

    public void setData(List<CommentListBean.AnswersBean> mList){
        mDataList.clear();
        mDataList.addAll(mList);
    }

    public void addList(List<CommentListBean.AnswersBean> mList){
        mDataList.addAll(mList);
    }

    public void clearList(){
        mDataList.clear();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_comment_detail,parent,false);
        CommentViewHolder viewHolder = new CommentViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        final CommentListBean.AnswersBean answersBean = mDataList.get(position);

        GitHubImageLoader.Instace(mContext).setCirclePic(
                answersBean.getAuthorid(), holder.userImage);

        answersBean.setAnswertime(answersBean.getAnswertime().substring(0,19));
        holder.userName.setText(answersBean.getUsername());
        holder.commentTime.setText(answersBean.getAnswertime());
        holder.commentContent.setText(answersBean.getAnswer());

//        if(!answersBean.getVip().equals("0")){
//            holder.vipStatus.setVisibility(View.VISIBLE);
//        }else{
//            holder.vipStatus.setVisibility(View.INVISIBLE);
//        }
//
//        if(answersBean.getType().equals("1")) {
//            holder.otherContentc.setVisibility(View.GONE);
//            holder.commentDisc.setText(answersBean.getAnswer());
//        }else{
//            holder.otherContentc.setVisibility(View.VISIBLE);
//            holder.commentDisc.setVisibility(View.GONE);
//        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        public ImageView userImage;
        public TextView userName;
        public TextView commentTime;
        public TextView commentContent;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userImage = (ImageView) itemView.findViewById(R.id.iv_user_image);
            userName = (TextView) itemView.findViewById(R.id.tv_user_name);
            commentTime = (TextView) itemView.findViewById(R.id.tv_comment_time);
            commentContent = (TextView) itemView.findViewById(R.id.tv_comment_content);
        }
    }
}
