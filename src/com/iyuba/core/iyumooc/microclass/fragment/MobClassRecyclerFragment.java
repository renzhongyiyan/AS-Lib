package com.iyuba.core.iyumooc.microclass.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.iyuba.core.common.manager.DataManager;
import com.iyuba.core.common.util.MD5;
import com.iyuba.core.common.widget.DividerItemDecoration;
import com.iyuba.core.common.widget.dialog.CustomToast;
import com.iyuba.core.iyumooc.microclass.adatper.MobClassRecyclerAdapter;
import com.iyuba.core.iyumooc.microclass.bean.CoursePackListBean;
import com.iyuba.core.iyumooc.microclass.bean.CourseTypeListBean;
import com.iyuba.core.iyumooc.microclass.bean.SlideShowListBean;
import com.iyuba.core.iyumooc.microclass.network.MicroClassRequestFactory;
import com.iyuba.core.iyumooc.sqlite.dao.CoursePackTypeDao;
import com.iyuba.core.microclass.adapter.MobClassListTypeAdapter;
import com.iyuba.lib.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 作者：renzhy on 16/6/23 11:02
 * 邮箱：renzhongyigoo@gmail.com
 */
public class MobClassRecyclerFragment extends Fragment {

	private static final int PRELOAD_SIZE = 6;

	private Context mContext;

	private View root;
	private Spinner titleSpinner;
	private RecyclerView mRecyclerView;
	private SwipeRefreshLayout mSwipeRefreshLayout;

	private String reqPackId = "-2";
	private String reqPackDesc = "class.all";
	private String reqPackType = "-2";
	private int iLastPage = 0;
	private int pageNum = 1;
	private boolean isLast = false;
	private boolean mIsFirstTimeTouchBottom = true;
	private ArrayList<CourseTypeListBean.CourseTypeDataBean> coursePackTypes = new ArrayList<>();
	private ArrayList<CoursePackListBean.CoursePackDataBean> coursePackArrayList = new ArrayList<>();
	private ArrayList<SlideShowListBean.SlideShowDataBean> slideShowList = new ArrayList<>();
	private MobClassRecyclerAdapter mobClassRecyclerAdapter;
	private MobClassListTypeAdapter mobClassListTypeAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.lib_microclass_course_recycler, container, false);
		findViews(root);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initWidgets();
		new GetPackTypeDataTask().execute();
		new GetHeaderDataTask().execute();
		new GetSlidePicDataTask().execute();
	}

	public void findViews(View rootView) {
		titleSpinner = (Spinner) rootView.findViewById(R.id.titleSpinner);
		mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.microclass_swipe_refresh_widget);
		mRecyclerView = (RecyclerView) rootView.findViewById(R.id.microclass_recyclerview);
	}

	public void initWidgets() {
		mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new GetPackTypeDataTask().execute();
				new GetHeaderDataTask().execute();
				new GetSlidePicDataTask().execute();
			}
		});

		// 确定每个item的排列方式
		LinearLayoutManager layoutManager = new LinearLayoutManager(mContext) {
			@Override
			public RecyclerView.LayoutParams generateDefaultLayoutParams() {
				// 这里要复写一下，因为默认宽高都是wrap_content
				// 这个不复写，你点击的背景色就只充满你的内容
				return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
			}
		};
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

		mRecyclerView.setLayoutManager(layoutManager);
		mRecyclerView.setHasFixedSize(true);
		mobClassRecyclerAdapter = new MobClassRecyclerAdapter(mContext);
		mobClassRecyclerAdapter.clearList();
		mRecyclerView.setAdapter(mobClassRecyclerAdapter);
		mRecyclerView.addItemDecoration(new DividerItemDecoration(
				mContext, R.drawable.recycler_rectangle, DividerItemDecoration.VERTICAL_LIST));
		mRecyclerView.addOnScrollListener(getOnBottomListener(layoutManager));
		mobClassRecyclerAdapter.setOnItemClickListener(new MobClassRecyclerAdapter.OnRecyclerViewItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Toast.makeText(mContext, "点击", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLongClick(View view, int position) {
				Toast.makeText(mContext, "长点击", Toast.LENGTH_SHORT).show();
			}
		});

		mobClassListTypeAdapter = new MobClassListTypeAdapter(mContext, coursePackTypes);
		titleSpinner.setAdapter(mobClassListTypeAdapter);

		titleSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
											   View view, int position, long id) {
						// TODO Auto-generated method stub
						reqPackId = coursePackTypes.get(position).getId() + "";
						reqPackType = coursePackTypes.get(position).getType() + "";
						reqPackDesc = coursePackTypes.get(position).getDesc(); // 对应的是轮播图片对应的请求字段，如"class.all"
						pageNum = 1;
						isLast = false;
						new GetHeaderDataTask().execute();
						new GetSlidePicDataTask().execute();
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});
	}

	RecyclerView.OnScrollListener getOnBottomListener(final LinearLayoutManager layoutManager) {
		return new RecyclerView.OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView rv, int dx, int dy) {
				boolean isBottom =
						layoutManager.findFirstVisibleItemPosition() >=
								mobClassRecyclerAdapter.getItemCount() -
										PRELOAD_SIZE;
				if (!mSwipeRefreshLayout.isRefreshing() && isBottom) {
					if (!mIsFirstTimeTouchBottom) {
						mSwipeRefreshLayout.setRefreshing(true);
						new GetFooterDataTask().execute();
					} else {
						mIsFirstTimeTouchBottom = false;
					}
				}
			}
		};
	}

	private class GetPackTypeDataTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... params) {
			getPackTypeData();
			return null;
		}
	}

	private class GetSlidePicDataTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... params) {
			getSlidePicData();
			return null;
		}
	}

	private class GetHeaderDataTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... params) {
			pageNum = 1;
			isLast = false;
			getRefreshPackData(true);
			return null;
		}
	}

	private class GetFooterDataTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... params) {
			getRefreshPackData(false);
			return null;
		}
	}

	public void getPackTypeData() {

		Call<CourseTypeListBean> call = MicroClassRequestFactory
				.getCourseTypeApi().getCourseTypeList("10103", "0", "806e43f1d3416670861ef3b187f6a27c");
		call.enqueue(new Callback<CourseTypeListBean>() {
			@Override
			public void onResponse(Response<CourseTypeListBean> response) {
				if (response != null && response.body() != null
						&& response.body().getResult() == 1) {
					if (response.body().getData().size() > 0) {
						int flag = 0;
						if (DataManager.Instance().courseTypeList.size() == 0) {
							flag = 1;
						} else {
							if (response.body().getData().size() > DataManager
									.Instance().courseTypeList.size()) {
								flag = 1;
							}
						}
						if (flag == 1) {
							coursePackTypes.clear();
							coursePackTypes.addAll(response.body().getData());
							CoursePackTypeDao.getInstance().insertCoursePackType(
									response.body().getData());
							mobClassListTypeAdapter.clearList();// 清除原来的记录
							mobClassListTypeAdapter.addList(response.body().getData());
							handler.sendEmptyMessage(4);
						}
					}
				}
			}

			@Override
			public void onFailure(Throwable t) {

			}
		});

	}

	/**
	 * 初始化相关Data
	 */
	private void getSlidePicData() {
		// 一步任务获取图片
		Call<SlideShowListBean> call = MicroClassRequestFactory.getSlidePicApi().getSlidePicList(reqPackDesc);
		call.enqueue(new Callback<SlideShowListBean>() {
			@Override
			public void onResponse(Response<SlideShowListBean> response) {

				if (response != null && response.body() != null
						&& response.body().getData() != null && response.body().getData().size() != 0) {

					if (response.body().getResult().equals("1")) {
						slideShowList.clear();
						slideShowList.addAll(response.body().getData());
						mobClassRecyclerAdapter.setAdData(slideShowList);
						handler.sendEmptyMessage(1);
						Log.e("执行轮播图片请求正常,data size：", response.body().getData().size() + "");
					}
				} else {
					Log.e("执行轮播图片请求异常：", "异常中2！！！");
				}
			}

			@Override
			public void onFailure(Throwable t) {
				Log.e("执行轮播图片请求异常：", "异常中3！！！");
			}
		});
	}

	public void getRefreshPackData(final boolean isClean) {
		if (isLast) {
			handler.sendEmptyMessage(0);
			handler.sendEmptyMessage(2);
			return;
		}

		Call<CoursePackListBean> call = MicroClassRequestFactory
				.getCoursePackApi().getCoursePackList(
						"10102", reqPackId, "1", pageNum, 20, MD5.getMD5ofStr("10102class" + reqPackId));
		call.enqueue(new Callback<CoursePackListBean>() {
			@Override
			public void onResponse(Response<CoursePackListBean> response) {
				if (response != null && response.body() != null
						&& response.body().getResult() == 1) {
					iLastPage = response.body().getLastPage();
					if (iLastPage != pageNum) {
						isLast = false;
					} else if (iLastPage == 0) {
						isLast = true;
					}
					pageNum += 1;
					if (response.body().getData().size() > 0) {
						if (isClean) {
							coursePackArrayList.clear();
							coursePackArrayList.addAll(response.body().getData());
							mobClassRecyclerAdapter.clearList();
							mobClassRecyclerAdapter.addList(response.body().getData());
							handler.sendEmptyMessage(1);
						} else {
							mobClassRecyclerAdapter.clearList();
							coursePackArrayList.addAll(response.body().getData());
							mobClassRecyclerAdapter.addList(coursePackArrayList);
						}
						handler.sendEmptyMessage(0);
						handler.sendEmptyMessage(1);
					}
				} else {
					handler.sendEmptyMessage(0);
					handler.sendEmptyMessage(3);
				}
				handler.sendEmptyMessage(0);
				handler.sendEmptyMessage(1);
			}

			@Override
			public void onFailure(Throwable t) {
				handler.sendEmptyMessage(0);
				handler.sendEmptyMessage(3);
			}
		});

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
				case 0:
					mSwipeRefreshLayout.setRefreshing(false);
					break;
				case 1:
					if (mobClassRecyclerAdapter == null) {
						mobClassRecyclerAdapter = new MobClassRecyclerAdapter(mContext,
								coursePackArrayList, slideShowList);
					} else {
						mobClassRecyclerAdapter.notifyDataSetChanged();
					}
					break;
				case 2:
					CustomToast.showToast(mContext, "已经是最后一页！", 1000);
					break;
				case 3:
					CustomToast.showToast(mContext, R.string.check_network, 1000);
					break;
				case 4:
					mobClassListTypeAdapter.notifyDataSetChanged();
			}
		}
	};

}
