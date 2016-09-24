package com.iyuba.core.iyulive.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.common.manager.AccountManager;
import com.iyuba.core.iyulive.adapter.CourseContentAdapter;
import com.iyuba.core.iyulive.bean.IyuStreamInfo;
import com.iyuba.core.iyulive.bean.LiveContentBean;
import com.iyuba.core.iyulive.listener.ILiveAvailableListener;
import com.iyuba.core.iyulive.listener.ILiveListListener;
import com.iyuba.core.iyulive.manager.ConstantManager;
import com.iyuba.core.iyulive.network.IyuLiveRequestFactory;
import com.iyuba.core.iyulive.util.MD5;
import com.iyuba.core.iyulive.util.T;
import com.iyuba.core.iyulive.widget.DividerItemDecoration;
import com.iyuba.lib.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.youdao.sdk.nativeads.YouDaoInterstitialActivity.getActivity;


/**
 * 作者：renzhy on 16/7/25 21:18
 * 邮箱：renzhongyigoo@gmail.com
 */
public class LiveListFragment extends Fragment implements ILiveListListener,ILiveAvailableListener {

	SwipeRefreshLayout mSwipeRefreshLayout;
	RecyclerView mRecyclerView;

	private Context mContext;
	private boolean isAllowedWatch;
	private List<LiveContentBean.LiveTitleBean.LiveDetailBean> liveDetailList;
	private CourseContentAdapter courseContentAdapter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_course_list, container, false);
		findViews(view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initWidgets();
	}

	public void findViews(View root) {
		mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_course_list);
		mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview_course_list);
	}

	public void initWidgets() {
		mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {

			}
		});

		// 确定每个item的排列方式
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext) {
			@Override
			public RecyclerView.LayoutParams generateDefaultLayoutParams() {
				return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
			}
		};
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setHasFixedSize(true);
		courseContentAdapter = new CourseContentAdapter(mContext);
		courseContentAdapter.clearList();
		mRecyclerView.setAdapter(courseContentAdapter);
		mRecyclerView.addItemDecoration(new DividerItemDecoration(
				mContext, R.drawable.recycler_live_list_rectangle, DividerItemDecoration.VERTICAL_LIST));
	}

	private void setRecyclerViewHeight(RecyclerView recyclerView) {
		RecyclerView.Adapter adapter = recyclerView.getAdapter();
		int count = adapter.getItemCount();

		ViewGroup.LayoutParams mParams = recyclerView.getLayoutParams();
		mParams.height = (RuntimeManager.getWindowWidth() * 480 / 720 + RuntimeManager.dip2px((77)) * count + RuntimeManager.dip2px(8));
		mParams.width = RuntimeManager.getWindowWidth();
		recyclerView.setLayoutParams(mParams);
	}

	private void getStreamDetail(final int titleId) {

		Call<IyuStreamInfo> call = IyuLiveRequestFactory.getIyuStreamInfoApi().getIyuStreamInfo(
				titleId+"",
				AccountManager.Instace(mContext).USERID,
				ConstantManager.USER_AVATAR_PREFIX+AccountManager.Instace(mContext).USERID+ConstantManager.USER_AVATAR_SUFFIX,
				AccountManager.Instace(mContext).USERNAME,
				MD5.getMD5ofStr(
						"avatarUrl=" +ConstantManager.USER_AVATAR_PREFIX+AccountManager.Instace(mContext).USERID+ConstantManager.USER_AVATAR_SUFFIX
						+ "&nickname=" +AccountManager.Instace(mContext).USERNAME
						+ "&tid="+titleId
						+ "&uid=" + AccountManager.Instace(mContext).USERID
						+"&secretkey=OCX3LBO15L"
				).toUpperCase()
		);

		call.enqueue(new Callback<IyuStreamInfo>() {
			@Override
			public void onResponse(Response<IyuStreamInfo> response) {
				if (response != null && response.body() != null) {
					IyuStreamInfo model = response.body();
					if(model.getStatus().equals(ConstantManager.LIVE_STATUS_READY)
							|| model.getStatus().equals(ConstantManager.LIVE_STATUS_LIVE)){
						T.s(mContext,"直播课程还没开始哟~");
					}else if(model.getStatus().equals(ConstantManager.LIVE_STATUS_LIVE)){
						T.s(mContext,"课程直播正在进行……!");
					}else if(model.getStatus().equals(ConstantManager.LIVE_STATUS_OVER)){
						T.s(mContext,"课程直播已结束!");
					}
//					Intent intent = new Intent(mContext, NewPlayActivity.class);
//					intent.putExtra(NewPlayActivity.BEAN, model);
//					intent.putExtra("titleId",titleId);
//					mContext.startActivity(intent);
				}else{
					T.s(mContext,"sorry,直播请求出现异常~");
				}
			}

			@Override
			public void onFailure(Throwable t) {
				T.s(mContext,"sorry,打开直播课程出现错误~");
			}
		});
	}

	@Override
	public void onFragmentListUpdate(final ArrayList<LiveContentBean.LiveTitleBean.LiveDetailBean> liveDetailList) {
		if (liveDetailList != null && courseContentAdapter != null) {
			this.liveDetailList = liveDetailList;
			courseContentAdapter.clearList();
			courseContentAdapter.setLiveDetailData(liveDetailList);
			courseContentAdapter.setOnItemClickListener(new CourseContentAdapter.OnRecyclerViewItemClickListener() {
				@Override
				public void onItemClick(View view, int position) {
					if(AccountManager.Instace(mContext).checkUserLogin()){
						if(isAllowedWatch){
							if (liveDetailList != null) {
								getStreamDetail(liveDetailList.get(position).getId());
							}
						}else {
//							final MaterialDialog materialDialog = new MaterialDialog(mContext);
//							materialDialog
//									.setPositiveButton(R.string.ok, new View.OnClickListener() {
//										@Override
//										public void onClick(View v) {
//											materialDialog.dismiss();
//										}
//									})
//									.setNegativeButton(R.string.cancel, new View.OnClickListener() {
//										@Override
//										public void onClick(View view) {
//											materialDialog.dismiss();
//										}
//									});
//							materialDialog.setTitle(R.string.live_apply_first).setMessage(R.string.live_apply_first_tip);
//							materialDialog.show();
						}
					}else{
//						final MaterialDialog materialDialog = new MaterialDialog(mContext);
//						materialDialog
//								.setPositiveButton(R.string.ok, new View.OnClickListener() {
//									@Override
//									public void onClick(View v) {
//										materialDialog.dismiss();
////										startActivity(LoginActivity.getIntent2Me(mContext));
//									}
//								})
//								.setNegativeButton(R.string.cancel, new View.OnClickListener() {
//									@Override
//									public void onClick(View view) {
//										materialDialog.dismiss();
//									}
//								});
//						materialDialog.setTitle(R.string.live_login_first).setMessage(R.string.live_login_first_tip);
//						materialDialog.show();
					}
				}

				@Override
				public void onLongClick(View view, int position) {

				}
			});
		}
	}

	@Override
	public void onAvailableStatusUpdate(boolean isAvailable) {
		isAllowedWatch = isAvailable;
	}
}
