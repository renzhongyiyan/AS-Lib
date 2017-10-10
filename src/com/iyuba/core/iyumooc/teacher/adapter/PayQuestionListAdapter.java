package com.iyuba.core.iyumooc.teacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuba.core.common.thread.GitHubImageLoader;
import com.iyuba.core.common.widget.dialog.CustomToast;
import com.iyuba.core.iyumooc.teacher.bean.PayQuestionListBean;
import com.iyuba.core.teacher.activity.ShowLargePicActivity;
import com.iyuba.core.teacher.sqlite.op.CommentAgreeOp;
import com.iyuba.lib.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.message.proguard.O;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PayQuestionListAdapter extends RecyclerView.Adapter {
	private Context mContext;
	private int adapterType;

	private static final int REWARD_TYPE = 0;
	private static final int DISCUSS_TYPE = 1;

	private ArrayList<PayQuestionListBean.PayQuestionDataBean> mList = new ArrayList<>();
	public	HashMap<String ,String> abilityTypeCatalog=new HashMap<>();
	public	HashMap<String ,String> appTypeCatalog=new HashMap<>();

	private OnRecyclerViewItemClickListener mOnItemClickListener = null;

	public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
		mOnItemClickListener = listener;
	}

	public PayQuestionListAdapter(Context context, ArrayList<PayQuestionListBean.PayQuestionDataBean> quesList) {
		mContext = context;
		mList = quesList;
		setAbilityTypeCatalog();
		setAppTypeCatalog();
	}

	public PayQuestionListAdapter(Context context,int type) {
		mContext = context;
		adapterType = type;
		setAbilityTypeCatalog();
		setAppTypeCatalog();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if(viewType == REWARD_TYPE){
			View v = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.item_reward_payquestion_list,parent,false);
			return new RewardViewHolder(v);
		}else if(viewType == DISCUSS_TYPE){
			View v = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.item_discuss_payquestion_list,parent,false);
			return new DiscussViewHolder(v);
		}

		return null;
	}

	@Override
	public int getItemViewType(int position) {
		return adapterType;
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

		if(viewHolder instanceof RewardViewHolder){
			RewardViewHolder rewardViewHolder = (RewardViewHolder) viewHolder;
			final PayQuestionListBean.PayQuestionDataBean ques = mList.get(position);
			if(mOnItemClickListener != null){
				RelativeLayout relativeLayout = (RelativeLayout) viewHolder.itemView;
				relativeLayout.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v){
						int pos = viewHolder.getLayoutPosition();
						mOnItemClickListener.onItemClick(viewHolder.itemView,pos);
					}
				});
				relativeLayout.setOnLongClickListener(new View.OnLongClickListener(){
					@Override
					public boolean onLongClick(View v) {
						int pos = viewHolder.getLayoutPosition();
						mOnItemClickListener.onLongClick(viewHolder.itemView,pos);
						return true;
					}
				});
			}

			GitHubImageLoader.Instace(mContext).setCirclePic(
					mList.get(position).getUid(), rewardViewHolder.userImage);

			rewardViewHolder.userName.setText(ques.getUsername());
			rewardViewHolder.quesPrice.setText(ques.getPrice());
			if (ques.getCreatetime() == null || "null".equals(ques.getCreatetime())) {
				ques.setCreatetime("");
			}

			if (ques.getLocation() == null || "null".equals(ques.getLocation())) {
				ques.setLocation("");
			}
			ques.setCreatetime(ques.getCreatetime().substring(0,19));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				long time = sdf.parse(ques.getCreatetime()).getTime();
				rewardViewHolder.leftTime.setText(formatTime(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (ques.getQuestion() != null && !ques.getQuestion().trim().equals("")) {
				rewardViewHolder.quesContent.setText(ques.getQuestion());
				rewardViewHolder.quesContent.setVisibility(View.VISIBLE);
			} else {
				rewardViewHolder.quesContent.setVisibility(View.INVISIBLE);
			}

			if (ques.getImg() != null && !ques.getImg().trim().equals("")) {
				rewardViewHolder.quesImage.setVisibility(View.VISIBLE);
				rewardViewHolder.quesImage.setAdjustViewBounds(true);
				rewardViewHolder.quesImage.setMaxHeight(360);
				rewardViewHolder.quesImage.setMaxWidth(240);

				ImageLoader.getInstance().displayImage("http://www.iyuba.com/question/"
						+ ques.getImg().replaceAll("_b.jpg", "_s.jpg"), rewardViewHolder.quesImage);

				rewardViewHolder.quesImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setClass(mContext, ShowLargePicActivity.class);
						intent.putExtra("pic", "http://www.iyuba.com/question/"
								+ ques.getImg());
						mContext.startActivity(intent);
					}
				});
			} else {
				rewardViewHolder.quesImage.setVisibility(View.GONE);
			}

			rewardViewHolder.quesAppType.setText(appTypeCatalog.get(ques.getCategory2()));
			rewardViewHolder.quesAbilityType.setText(abilityTypeCatalog.get(ques.getCategory1()));

			rewardViewHolder.commentNum.setText(ques.getCommentcount() + "");
			rewardViewHolder.agreeNum.setText(ques.getAgreecount() + "");
		}else if(viewHolder instanceof DiscussViewHolder){
			DiscussViewHolder discussViewHolder = (DiscussViewHolder) viewHolder;
			final PayQuestionListBean.PayQuestionDataBean ques = mList.get(position);
			if(mOnItemClickListener != null){
				RelativeLayout relativeLayout = (RelativeLayout) viewHolder.itemView;
				relativeLayout.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v){
						int pos = viewHolder.getLayoutPosition();
						mOnItemClickListener.onItemClick(viewHolder.itemView,pos);
					}
				});
				relativeLayout.setOnLongClickListener(new View.OnLongClickListener(){
					@Override
					public boolean onLongClick(View v) {
						int pos = viewHolder.getLayoutPosition();
						mOnItemClickListener.onLongClick(viewHolder.itemView,pos);
						return true;
					}
				});
			}

			GitHubImageLoader.Instace(mContext).setCirclePic(
					mList.get(position).getUid(), discussViewHolder.userImage);

			discussViewHolder.userName.setText(ques.getUsername());
			discussViewHolder.quesPrice.setText(ques.getPrice());

			if (ques.getCreatetime() == null || "null".equals(ques.getCreatetime())) {
				ques.setCreatetime("");
			}

			if (ques.getLocation() == null || "null".equals(ques.getLocation())) {
				ques.setLocation("");
			}
			ques.setCreatetime(ques.getCreatetime().substring(0,19));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				long time = sdf.parse(ques.getCreatetime()).getTime();
				discussViewHolder.leftTime.setText(formatTime(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (ques.getQuestion() != null && !ques.getQuestion().trim().equals("")) {
				discussViewHolder.quesContent.setText(ques.getQuestion());
				discussViewHolder.quesContent.setVisibility(View.VISIBLE);
			} else {
				discussViewHolder.quesContent.setVisibility(View.INVISIBLE);
			}

			if (ques.getImg() != null && !ques.getImg().trim().equals("")) {


				discussViewHolder.quesImage.setVisibility(View.VISIBLE);
				discussViewHolder.quesImage.setAdjustViewBounds(true);
				discussViewHolder.quesImage.setMaxHeight(360);
				discussViewHolder.quesImage.setMaxWidth(240);

				ImageLoader.getInstance().displayImage("http://www.iyuba.com/question/"
						+ ques.getImg().replaceAll("_b.jpg", "_s.jpg"), discussViewHolder.quesImage);

				discussViewHolder.quesImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setClass(mContext, ShowLargePicActivity.class);
						intent.putExtra("pic", "http://www.iyuba.com/question/"
								+ ques.getImg());
						mContext.startActivity(intent);
					}
				});
			} else {
				discussViewHolder.quesImage.setVisibility(View.GONE);
			}

			discussViewHolder.quesAppType.setText(appTypeCatalog.get(ques.getCategory2()));

			discussViewHolder.quesAbilityType.setText(abilityTypeCatalog.get(ques.getCategory1()));

			discussViewHolder.answeredNum.setText(ques.getAnswercount() + "");
			discussViewHolder.commentNum.setText(ques.getCommentcount() + "");
			discussViewHolder.agreeNum.setText(ques.getAgreecount() + "");
		}
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	// 查看此用户是否已点赞
	private int checkAgree(String commentId, String uid) {
		return new CommentAgreeOp(mContext).findDataByAll(commentId, uid);
	}

	public void setData(ArrayList<PayQuestionListBean.PayQuestionDataBean> quesList) {
		mList = quesList;
	}

	public void addList(ArrayList<PayQuestionListBean.PayQuestionDataBean> quesList) {
		mList.addAll(quesList);
	}

	public void clearList(){
		mList.clear();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 用来设置每个item的接听
	public interface OnRecyclerViewItemClickListener {
		void onItemClick(View view, int position);
		void onLongClick(View view, int position);
	}

	private String formatTime(long time) {
		Date date = new Date(time);
		Date date2 = new Date(System.currentTimeMillis());
		Calendar calendar = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();

		calendar.setTime(date);
		calendar2.setTime(date2);

		if(calendar2.get(Calendar.YEAR)
				- calendar.get(Calendar.YEAR) > 0){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			return format.format(date);
		}else if (calendar2.get(Calendar.DAY_OF_YEAR)
				- calendar.get(Calendar.DAY_OF_YEAR) >= 7) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			return format.format(date);
		}else if (System.currentTimeMillis() - time > 48 * 60 * 60 * 1000) {
			return (System.currentTimeMillis() - time) / (24 * 60 * 60 * 1000)
					+ "天前";
		} else if (System.currentTimeMillis() - time > 24 * 60 * 60 * 1000) {
			return "昨天";
		} else if (System.currentTimeMillis() - time > 60 * 60 * 1000) {
			return (System.currentTimeMillis() - time) / (60 * 60 * 1000)
					+ "小时";
		} else if (System.currentTimeMillis() - time > 3 * 60 * 1000) {
			return (System.currentTimeMillis() - time) / (60 * 1000) + "分钟";
		} else if (System.currentTimeMillis() - time > 60 * 1000) {
			return "刚刚";
		}else{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			return format.format(date);
		}
	}

	public  void setAbilityTypeCatalog(){
		abilityTypeCatalog=new HashMap<String, String>();
		abilityTypeCatalog.put("0", "其他");
		abilityTypeCatalog.put("1", "口语");
		abilityTypeCatalog.put("2", "听力");
		abilityTypeCatalog.put("3", "阅读");
		abilityTypeCatalog.put("4", "写作");
		abilityTypeCatalog.put("5", "翻译");
		abilityTypeCatalog.put("6", "单词");
		abilityTypeCatalog.put("7", "语法");
		abilityTypeCatalog.put("8", "其他");
	}

	public  void setAppTypeCatalog(){
		appTypeCatalog=new HashMap<>();
		appTypeCatalog.put("0", "其他");
		appTypeCatalog.put("101", "VOA");
		appTypeCatalog.put("102", "BBC");
		appTypeCatalog.put("103", "听歌");
		appTypeCatalog.put("104", "CET4");
		appTypeCatalog.put("105", "CET6");
		appTypeCatalog.put("106", "托福");
		appTypeCatalog.put("107", "N1");
		appTypeCatalog.put("108", "N2");
		appTypeCatalog.put("109", "N3");
		appTypeCatalog.put("110", "微课");
		appTypeCatalog.put("111", "雅思");
		appTypeCatalog.put("112", "初中");
		appTypeCatalog.put("113", "高中");
		appTypeCatalog.put("114", "考研");
		appTypeCatalog.put("115", "新概念");
		appTypeCatalog.put("116", "走遍美国");
		appTypeCatalog.put("117", "英语头条");
	}

	public class RewardViewHolder extends RecyclerView.ViewHolder{
		public ImageView userImage;
		public TextView userName;
		public TextView quesPrice;
		public TextView quesContent;
		public ImageView quesImage;
		public TextView quesAppType;
		public TextView quesAbilityType;
		public TextView commentNum;
		public TextView agreeNum;
		public TextView leftTime;
		public Button quickAnswer;

		public RewardViewHolder(View itemView) {
			super(itemView);
			userImage = (ImageView) itemView
					.findViewById(R.id.iv_user_image);
			userName = (TextView) itemView
					.findViewById(R.id.tv_user_name);
			quesPrice = (TextView) itemView
					.findViewById(R.id.tv_question_price);
			quesContent = (TextView) itemView
					.findViewById(R.id.tv_question_content);
			quesImage = (ImageView) itemView
					.findViewById(R.id.ques_image);
			quesAppType = (TextView) itemView
					.findViewById(R.id.tv_ques_app_type);
			quesAbilityType = (TextView) itemView
					.findViewById(R.id.tv_ques_ability_type);
			commentNum = (TextView) itemView
					.findViewById(R.id.comment_num);
			agreeNum = (TextView) itemView
					.findViewById(R.id.agree_num);
			leftTime = (TextView) itemView
					.findViewById(R.id.tv_left_time);
			quickAnswer = (Button) itemView
					.findViewById(R.id.btn_quick_answer);

		}
	}

	public class DiscussViewHolder extends RecyclerView.ViewHolder{
		public ImageView userImage;
		public TextView userName;
		public TextView quesPrice;
		public TextView quesContent;
		public ImageView quesImage;
		public TextView quesAppType;
		public TextView quesAbilityType;
		public TextView leftTime;
		public TextView answeredNum;
		public TextView commentNum;
		public TextView agreeNum;

		public DiscussViewHolder(View itemView) {
			super(itemView);
			userImage = (ImageView) itemView
					.findViewById(R.id.iv_user_image);
			userName = (TextView) itemView
					.findViewById(R.id.tv_user_name);
			quesPrice = (TextView) itemView
					.findViewById(R.id.tv_question_price);
			quesContent = (TextView) itemView
					.findViewById(R.id.tv_question_content);
			quesImage = (ImageView) itemView
					.findViewById(R.id.ques_image);
			quesAppType = (TextView) itemView
					.findViewById(R.id.tv_ques_app_type);
			quesAbilityType = (TextView) itemView
					.findViewById(R.id.tv_ques_ability_type);
			leftTime = (TextView) itemView
					.findViewById(R.id.tv_left_time);
			answeredNum = (TextView) itemView
					.findViewById(R.id.tv_answered_num);
			commentNum = (TextView) itemView
					.findViewById(R.id.tv_comment_num);
			agreeNum = (TextView) itemView
					.findViewById(R.id.tv_agree_num);
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				notifyDataSetChanged();
				break;

			case 1:
				notifyDataSetChanged();
				break;
			case 3:
				CustomToast.showToast(mContext, R.string.agree_already);
				break;
			case 4:
				CustomToast.showToast(mContext, R.string.comment_agree);
				break;

			}
		}
	};

}
