package com.iyuba.core.iyumooc.microclass.adatper;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.iyuba.configation.Constant;
import com.iyuba.core.common.thread.GitHubImageLoader;
import com.iyuba.core.iyumooc.microclass.bean.CoursePackListBean;
import com.iyuba.core.iyumooc.microclass.bean.SlideShowListBean;
import com.iyuba.lib.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * 作者：renzhy on 16/6/23 15:19
 * 邮箱：renzhongyigoo@gmail.com
 */
public class MobClassRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;

	private Context mContext;
	private List<CoursePackListBean.CoursePackDataBean> mList = new ArrayList<>();
	private List<SlideShowListBean.SlideShowDataBean> mSlideList = new ArrayList<>();
	private List<String> adPicUrl;
	private String allPicUrl;

	private OnRecyclerViewItemClickListener mOnItemClickListener = null;

	public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
		mOnItemClickListener = listener;
	}

	public MobClassRecyclerAdapter(Context mContext) {
		this.mContext = mContext;

//		String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
//				"http://img2.3lian.com/2014/f2/37/d/40.jpg",
//				"http://d.3987.com/sqmy_131219/001.jpg",
//				"http://img2.3lian.com/2014/f2/37/d/39.jpg",};
//		adPicUrl = Arrays.asList(images);
		mList = new ArrayList<>();
		mSlideList = new ArrayList<>();
	}

	public MobClassRecyclerAdapter(Context mContext, ArrayList<CoursePackListBean.CoursePackDataBean> mList,
								   ArrayList<SlideShowListBean.SlideShowDataBean> mSlideList) {
		this.mContext = mContext;
		this.mList = mList;
		this.mSlideList = mSlideList;

//		String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
//				"http://img2.3lian.com/2014/f2/37/d/40.jpg",
//				"http://d.3987.com/sqmy_131219/001.jpg",
//				"http://img2.3lian.com/2014/f2/37/d/39.jpg",};
//		adPicUrl = Arrays.asList(images);
	}

	public void setData(List<CoursePackListBean.CoursePackDataBean> cpList) {
		mList = cpList;
	}

	public void addList(List<CoursePackListBean.CoursePackDataBean> cpList) {
		mList.addAll(cpList);
	}

	public void clearList() {
		mList.clear();
	}

	public void setAdData(List<SlideShowListBean.SlideShowDataBean> adList) {
		mSlideList = adList;
		notifyItemChanged(0);
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_ITEM) {
			//inflate your layout and pass it to view holder
			return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.lib_microclass_item_common_mobclasslist, parent, false));
		} else if (viewType == TYPE_HEADER) {
			//inflate your layout and pass it to view holder
			return new AdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_ad, parent, false));
		}
		return null;
	}

	@Override
	public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof ItemViewHolder) {
			final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
			final CoursePackListBean.CoursePackDataBean coursePack = mList.get(position);

			if (mOnItemClickListener != null) {
				itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int pos = holder.getLayoutPosition();
						mOnItemClickListener.onItemClick(holder.itemView, pos);
					}
				});
				itemViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						int pos = holder.getLayoutPosition();
						mOnItemClickListener.onLongClick(holder.itemView, pos);
						return true;
					}
				});
			}

			itemViewHolder.oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

			itemViewHolder.title.setText(coursePack.getName());
			itemViewHolder.content.setText(coursePack.getDesc());
			itemViewHolder.tvclassNum.setText(coursePack.getClassNum() + "");

			itemViewHolder.oldPrice.setText("原:" + coursePack.getRealprice());
			itemViewHolder.newPrice.setText("现:" + coursePack.getPrice() + "爱语币");

			DecimalFormat df = new DecimalFormat("#.0");

			if (coursePack.getViewCount() > 10000) {
				itemViewHolder.viewCount.setText(df.format((float) (coursePack.getViewCount() / 10000.0)) + "万");
			} else {
				itemViewHolder.viewCount.setText(coursePack.getViewCount() + "");
			}

			allPicUrl = Constant.MOB_CLASS_PACK_IMAGE + coursePack.getPic() + ".jpg";
			GitHubImageLoader.Instace(mContext).setPic(allPicUrl, itemViewHolder.pic, R.drawable.nearby_no_icon);
		} else if (holder instanceof AdViewHolder) {
			AdViewHolder headerViewHolder = (AdViewHolder) holder;
			headerViewHolder.convenientBanner.setPages(headerViewHolder.adHolder, mSlideList);
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position))
			return TYPE_HEADER;
		return TYPE_ITEM;
	}

	private boolean isPositionHeader(int position) {
		return position == 0;
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	// 用来设置每个item的接听
	public interface OnRecyclerViewItemClickListener {
		void onItemClick(View view, int position);

		void onLongClick(View view, int position);
	}

	public class ItemViewHolder extends RecyclerView.ViewHolder {
		public TextView title;
		public TextView tvclassNum;
		public ImageView courseNum;
		public TextView content;
		public ImageView pic;

		public TextView oldPrice;
		public TextView newPrice;
		public TextView viewCount;

		public ItemViewHolder(View itemView) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.coursePack_title);
			tvclassNum = (TextView) itemView.findViewById(R.id.tv_courseNum1);
			courseNum = (ImageView) itemView.findViewById(R.id.coursePack_courseNum);
			content = (TextView) itemView.findViewById(R.id.coursePackDesc_content);
			pic = (ImageView) itemView.findViewById(R.id.coursePack_pic);

			oldPrice = (TextView) itemView.findViewById(R.id.tv_coursePack_oldprice);
			newPrice = (TextView) itemView.findViewById(R.id.tv_coursePack_newprice);
			viewCount = (TextView) itemView.findViewById(R.id.tv_coursePack_viewCount);


		}
	}

	class AdViewHolder extends RecyclerView.ViewHolder {
		ConvenientBanner convenientBanner;
		CBViewHolderCreator<NetworkImageHolderView> adHolder;

		public AdViewHolder(View view) {
			super(view);
//			ABaseTransformer transforemer = null;
//			try {
//				Class cls = Class.forName("com.ToxicBakery.viewpager.transforms." + DefaultTransformer.class.getSimpleName());
//				transforemer = (ABaseTransformer) cls.newInstance();
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}

			convenientBanner = (ConvenientBanner) view.findViewById(R.id.header_convenientBanner);
			convenientBanner
					.startTurning(5000)
					.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
//					.setPageTransformer(transforemer);
			adHolder = new CBViewHolderCreator<NetworkImageHolderView>() {
				@Override
				public NetworkImageHolderView createHolder() {
					return new NetworkImageHolderView();
				}
			};
		}
	}

	class NetworkImageHolderView implements Holder<SlideShowListBean.SlideShowDataBean> {
		private LinearLayout linearLayout;
		private ImageView imageView;
		private TextView textView;

		@Override
		public View createView(Context context) {
			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT
			);
			linearLayout = new LinearLayout(context);
			imageView = new ImageView(context);
			textView = new TextView(context);
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			return imageView;
		}

		@Override
		public void UpdateUI(final Context context, final int position, final SlideShowListBean.SlideShowDataBean data) {
			ImageLoader.getInstance().displayImage( "http://app.iyuba.com/dev/" + data.getPic(), imageView);
//			Log.w("UpdateUI:","http://app.iyuba.com/dev/" + data.getPic());
			imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					Toast.makeText(mContext, "Click " + position, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
