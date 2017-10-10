package com.iyuba.core.iyumooc.teacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.core.common.thread.GitHubImageLoader;
import com.iyuba.core.iyumooc.teacher.bean.AnswerListBean;
import com.iyuba.core.iyumooc.teacher.bean.CommentListBean;
import com.iyuba.lib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：renzhy on 17/7/21 16:01
 * 邮箱：renzhongyigoo@gmail.com
 */
public class AnswerRVAdapter extends RecyclerView.Adapter<AnswerRVAdapter.AnswerViewHolder> {

    private Context mContext;
    private ArrayList<AnswerListBean.AnswersBean> mDataList;
    private boolean mAnswerPayed = false;
    private OnAnswerCoverClickListener onAnswerCoverClickListener;

    public AnswerRVAdapter(Context context) {
        mContext = context;
        mDataList = new ArrayList<>();
    }

    public void setOnAnswerCoverClickListener(OnAnswerCoverClickListener onItemClickListener){
        this.onAnswerCoverClickListener = onItemClickListener;
    }

    public void setData(List<AnswerListBean.AnswersBean> mList){
        mDataList.clear();
        mDataList.addAll(mList);
    }

    public void addList(List<AnswerListBean.AnswersBean> mList){
        mDataList.addAll(mList);
    }

    public void clearList(){
        mDataList.clear();
    }

    public void setAnswerPayed(boolean isPayed){
        mAnswerPayed = isPayed;
        notifyDataSetChanged();
    }

    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_answer_detail,parent,false);
        AnswerViewHolder viewHolder = new AnswerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AnswerViewHolder holder, int position) {
        final AnswerListBean.AnswersBean answersBean = mDataList.get(position);

        GitHubImageLoader.Instace(mContext).setCirclePic(
                answersBean.getAuthorid(), holder.teacherImage);

        answersBean.setAnswertime(answersBean.getAnswertime().substring(0,19));
        holder.teacherName.setText(answersBean.getUsername());
        holder.answerTime.setText(answersBean.getAnswertime());
        holder.answerContent.setText(answersBean.getAnswer());

//        holder.mAnswerCover.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAnswerPayed = true;
//                notifyDataSetChanged();
//            }
//        });

        if (onAnswerCoverClickListener != null) {
            holder.mAnswerCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAnswerCoverClickListener.onItemClick(holder.mAnswerCover);
                }
            });
        }

        if(mAnswerPayed){
            holder.mAnswerCover.setVisibility(View.GONE);
            holder.answerContent.setVisibility(View.VISIBLE);
        }else {
            holder.mAnswerCover.setVisibility(View.VISIBLE);
            holder.answerContent.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    //设置被覆盖的答案层的点击监听
    public interface OnAnswerCoverClickListener{
        void onItemClick(View view);
    }

    // 用来设置每个item的接听
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder{
        public ImageView teacherImage;
        public TextView teacherName;
        public TextView answerTime;
        public TextView answerContent;

        public View mViewAsk;
        public View mViewAttention;
        public View mViewPraise;

        public View mAnswerCover;

        public AnswerViewHolder(View itemView) {
            super(itemView);
            teacherImage = (ImageView) itemView.findViewById(R.id.iv_teacher_image);
            teacherName = (TextView) itemView.findViewById(R.id.tv_teacher_name);
            answerTime = (TextView) itemView.findViewById(R.id.tv_answer_time);
            answerContent = (TextView) itemView.findViewById(R.id.tv_answer_content);

            mViewAsk = itemView.findViewById(R.id.rl_ask_item);
            mViewAttention = itemView.findViewById(R.id.rl_attention_item);
            mViewPraise = itemView.findViewById(R.id.rl_praise_item);

            mAnswerCover = itemView.findViewById(R.id.rl_answer_content_cover);
        }
    }
}
