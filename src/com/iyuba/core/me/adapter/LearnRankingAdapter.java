package com.iyuba.core.me.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.core.common.widget.circularimageview.CircularImageView;
import com.iyuba.core.me.bean.LearnRankingListBean;
import com.iyuba.lib.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：renzhy on 17/1/7 09:54
 * 邮箱：renzhongyigoo@gmail.com
 */
public class LearnRankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_RANKING_LIST = 1;

	private Context mContext;
	private List<LearnRankingListBean.DataBean> learnRankingList;

	public LearnRankingAdapter(Context mContext) {
		this.mContext = mContext;
		learnRankingList = new ArrayList<>();
	}

	public void setLearnRankingList(ArrayList<LearnRankingListBean.DataBean> learnRankings){
		learnRankingList = learnRankings;
		notifyDataSetChanged();
	}

	public void addList(List<LearnRankingListBean.DataBean> learnRankings){
		learnRankingList.addAll(learnRankings);
		notifyDataSetChanged();
	}

	public void clearList(){
		learnRankingList.clear();
		notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		if(viewType == TYPE_HEADER){
			return new HeaderViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_learn_ranking_header,parent,false));
		}else if(viewType == TYPE_RANKING_LIST){
			return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_learn_ranking_list,parent,false));
		}
		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if(holder instanceof ItemViewHolder){
			final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
			final LearnRankingListBean.DataBean ranking = getItem(position);

			ImageLoader.getInstance().displayImage(ranking.getImgSrc(),itemViewHolder.userImage);
			itemViewHolder.userName.setText(ranking.getName());
			itemViewHolder.totalWords.setText(ranking.getTotalWord());
			itemViewHolder.totalTime.setText(ranking.getTotalTime());

		}else if(holder instanceof HeaderViewHolder){
			HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
		}
	}

	@Override
	public int getItemCount() {
		return learnRankingList.size()+1;
	}

	@Override
	public int getItemViewType(int position) {
		if(isPositionHeader(position)){
			return TYPE_HEADER;
		}
		return TYPE_RANKING_LIST;
	}

	private boolean isPositionHeader(int position) {
		return position == 0;
	}

	private LearnRankingListBean.DataBean getItem(int position){
		return learnRankingList.get(position-1);
	}

	public class HeaderViewHolder extends RecyclerView.ViewHolder{

		public CircularImageView championUserImage;

		public HeaderViewHolder(View itemView) {
			super(itemView);
			championUserImage = (CircularImageView) itemView.findViewById(R.id.iv_champion_user_image);
		}
	}

	public class ItemViewHolder extends RecyclerView.ViewHolder{

		public ImageView ranking;
		public CircularImageView userImage;
		public TextView userName;
		public TextView totalWords;
		public TextView totalTime;

		public ItemViewHolder(View itemView) {
			super(itemView);
			ranking = (ImageView) itemView.findViewById(R.id.iv_learning_ranking_order);
			userImage = (CircularImageView) itemView.findViewById(R.id.iv_learn_ranking_userimage);
			userName = (TextView) itemView.findViewById(R.id.tv_learn_ranking_username);
			totalWords = (TextView) itemView.findViewById(R.id.tv_learn_ranking_total_words);
			totalTime = (TextView) itemView.findViewById(R.id.tv_learn_ranking_total_time);

		}
	}

}
