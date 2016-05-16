package com.iyuba.core.homepage.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.common.manager.CommonNewsDataManager;
import com.iyuba.core.common.network.ClientSession;
import com.iyuba.core.common.network.IErrorReceiver;
import com.iyuba.core.common.network.IResponseReceiver;
import com.iyuba.core.common.protocol.BaseHttpRequest;
import com.iyuba.core.common.protocol.BaseHttpResponse;
import com.iyuba.core.common.protocol.ErrorResponse;
import com.iyuba.core.common.sqlite.mode.CommonNews;
import com.iyuba.core.common.widget.RollViewPager;
import com.iyuba.core.discover.activity.news.AudioPlayer;
import com.iyuba.core.discover.activity.news.SimpleNews;
import com.iyuba.core.homepage.adapter.NewsListAdapter;
import com.iyuba.core.homepage.entity.NewsInfo;
import com.iyuba.core.homepage.protocol.BBCNewsRequest;
import com.iyuba.core.homepage.protocol.BBCNewsResponse;
import com.iyuba.core.homepage.protocol.HeadNewsRequest;
import com.iyuba.core.homepage.protocol.HeadNewsResponse;
import com.iyuba.core.homepage.protocol.SongsRequest;
import com.iyuba.core.homepage.protocol.SongsResponse;
import com.iyuba.core.homepage.protocol.VOACNewsRequest;
import com.iyuba.core.homepage.protocol.VOACNewsResponse;
import com.iyuba.core.homepage.protocol.VOANNewsRequest;
import com.iyuba.core.homepage.protocol.VOANNewsResponse;
import com.iyuba.core.homepage.widget.ListViewNoScroll;
import com.iyuba.core.microclass.protocol.SlideShowCourseListRequest;
import com.iyuba.core.microclass.protocol.SlideShowCourseListResponse;
import com.iyuba.core.microclass.sqlite.mode.SlideShowCourse;
import com.iyuba.headnewslib.HeadlineActivity;
import com.iyuba.headnewslib.ReadActivity;
import com.iyuba.headnewslib.model.Article;
import com.iyuba.headnewslib.model.HeadlineTheme;
import com.iyuba.lib.R;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者：renzhy on 16/5/13 16:28
 * 邮箱：renzhongyigoo@gmail.com
 */
public class HomePageFragment extends Fragment {

	private Context mContext;
	private View root;

	private TextView titleDec;
	// 轮播图片的布局
	private LinearLayout top_news_viewpager;
	// 轮播图片指引(圆点)的布局
	private LinearLayout dots_ll;
	// 用于存放轮播图信息的集合
	private List<SlideShowCourse> ssCourseList = new ArrayList<>();
	// 用于存放图片地址的集合
	private ArrayList<String> imageUrls = new ArrayList<>();
	//用于存放轮播图说明文字
	private List<String> decList = new ArrayList<>();
	private ArrayList<CommonNews> newsArrayList = new ArrayList<>();
	View voacMore, bbcMore, songMore, headNewsMore, voanMore;
	String dt;

	private ArrayList<View> dot_list = new ArrayList<>();
	private String reqPackDesc = "class.first";
	private final static String PIC_BASE_URL = "http://app.iyuba.com/dev/";
	private ListViewNoScroll BBCListView, voaSListView, voaNListView, headNewsListView, songListView;
	private List<NewsInfo> BBCList = new ArrayList<>();
	private List<NewsInfo> voaNList = new ArrayList<>();
	private List<NewsInfo> voaSList = new ArrayList<>();
	private List<NewsInfo> headNewsList = new ArrayList<>();
	private List<NewsInfo> songList = new ArrayList<>();
	private List<Article> articles = new ArrayList<Article>();
	private int itemNum = 5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.lib_home_page,container,false);
		return root;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initWidget();
	}

	public void initWidget() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext).memoryCache(new LruMemoryCache(5 * 1024 * 1024)).memoryCacheSize(5 * 1024 * 1024).build();
		ImageLoader.getInstance().init(config);
//        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
//        layout_roll_view = (RelativeLayout) View.inflate(mContext,
//                R.layout.layout_roll_view, null);
		// 用于将轮播图添加进去
		top_news_viewpager = (LinearLayout) root.findViewById(R.id.top_sliding_viewpager1);
		dots_ll = (LinearLayout) root.findViewById(R.id.dots_ll_ongoing1);
		BBCListView = (ListViewNoScroll) root.findViewById(R.id.bbc_list);
		voaSListView = (ListViewNoScroll) root.findViewById(R.id.voac_list);
		voaNListView = (ListViewNoScroll) root.findViewById(R.id.voan_list);
		headNewsListView = (ListViewNoScroll) root.findViewById(R.id.head_news_list);
		songListView = (ListViewNoScroll) root.findViewById(R.id.song_list);
		titleDec = (TextView) root.findViewById(R.id.top_news_title);
		voacMore = root.findViewById(R.id.voac_more);
		bbcMore = root.findViewById(R.id.bbc_more);
		songMore = root.findViewById(R.id.song_more);
		headNewsMore = root.findViewById(R.id.head_news_more);
		voanMore = root.findViewById(R.id.voan_more);
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMM");
		Date date = new Date();
		dt = dateFormater.format(date);

		voacMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, SimpleNews.class);
				intent.putExtra("type", mContext.getString(com.iyuba.lib.R.string.voa_speical));
				startActivity(intent);
			}
		});

		bbcMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, SimpleNews.class);
				intent.putExtra("type", mContext.getString(com.iyuba.lib.R.string.voa_bbc6));
				startActivity(intent);
			}
		});

		songMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, SimpleNews.class);
				intent.putExtra("type", mContext.getString(com.iyuba.lib.R.string.voa_music));
				startActivity(intent);
			}
		});

		headNewsMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, HeadlineActivity.class);
				startActivity(intent);
			}
		});

		voanMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, SimpleNews.class);
				intent.putExtra("type", mContext.getString(com.iyuba.lib.R.string.voa_cs));
				startActivity(intent);
			}
		});

		voaSListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				newsArrayList.clear();
				newsArrayList.addAll(voaSList);
				String lesson = "VOA慢速英语";
				String shareUrl = "http://voa.iyuba.com/audioitem_special_";
				CommonNewsDataManager.Instace().voasTemp = newsArrayList;
				CommonNewsDataManager.Instace().position = position;
				CommonNewsDataManager.Instace().lesson = lesson;
				CommonNewsDataManager.Instace().url = shareUrl;
				intent.setClass(mContext, AudioPlayer.class);
				intent.putExtra("source", 0);
				startActivity(intent);
			}
		});

		BBCListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				newsArrayList.clear();
				newsArrayList.addAll(BBCList);
				String lesson = "BBC英语";
				String shareUrl = "http://bbc.iyuba.com/BBCItem_1_";
				CommonNewsDataManager.Instace().voasTemp = newsArrayList;
				CommonNewsDataManager.Instace().position = position;
				CommonNewsDataManager.Instace().lesson = lesson;
				CommonNewsDataManager.Instace().url = shareUrl;
				intent.setClass(mContext, AudioPlayer.class);
				intent.putExtra("source", 1);
				startActivity(intent);
			}
		});

		songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				newsArrayList.clear();
				newsArrayList.addAll(songList);
				String lesson = "听歌学英语";
				String shareUrl = "http://music.iyuba.com/play.jsp?SongId=";
				CommonNewsDataManager.Instace().voasTemp = newsArrayList;
				CommonNewsDataManager.Instace().position = position;
				CommonNewsDataManager.Instace().lesson = lesson;
				CommonNewsDataManager.Instace().url = shareUrl;
				intent.setClass(mContext, AudioPlayer.class);
				intent.putExtra("source", 2);
				startActivity(intent);
			}
		});

		headNewsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				articles.clear();
				for (int i = 0; i < headNewsList.size(); i++) {
					Article at = new Article();
					at.setCreatTime(headNewsList.get(i).time);
					at.setDescCn(headNewsList.get(i).content);
					at.setNewsId(headNewsList.get(i).id);
					at.setPic(headNewsList.get(i).picUrl);
					at.setReadCount(Integer.parseInt(headNewsList.get(i).readTimes));
					at.setTitle(headNewsList.get(i).titleEn);
					at.setTitle_cn(headNewsList.get(i).title);
					at.setSound(headNewsList.get(i).musicUrl);
					at.setSource(headNewsList.get(i).source);
					at.setCategory(Integer.valueOf(headNewsList.get(i).category));
					at.setFlag(Integer.parseInt(headNewsList.get(i).flag));
					at.setWordCount(Integer.parseInt(headNewsList.get(i).wordCount));
					at.setTopicId(Integer.parseInt(headNewsList.get(i).topicId));
					at.setHardWeight(Double.parseDouble(headNewsList.get(i).hardWeight));
					articles.add(at);
				}
				HeadlineTheme mTheme = getActivity().getIntent().getParcelableExtra(HeadlineTheme.TAG);
				if (mTheme == null)
					mTheme = HeadlineTheme.DEFAULT_THEME;
				Intent intent = new Intent(mContext, ReadActivity.class);
				Article item = articles.get(position);
				intent.putExtra("article", item);
				intent.putExtra(HeadlineTheme.TAG, mTheme);
				startActivity(intent);
			}
		});

		voaNListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				newsArrayList.clear();
				newsArrayList.addAll(voaNList);
				String lesson = "VOA常速英语";
				String shareUrl = "http://voa.iyuba.com/audioitem_stardard_";
				CommonNewsDataManager.Instace().voasTemp = newsArrayList;
				CommonNewsDataManager.Instace().position = position;
				CommonNewsDataManager.Instace().lesson = lesson;
				CommonNewsDataManager.Instace().url = shareUrl;
				intent.setClass(mContext, AudioPlayer.class);
				intent.putExtra("source", 0);
				startActivity(intent);
			}
		});

		RuntimeManager.setApplication(getActivity().getApplication());
		RuntimeManager.setApplicationContext(getActivity().getApplicationContext());
		RuntimeManager.setDisplayMetrics(getActivity());
		initSlideShowViewPicData();
		new GetBBCNews().start();
		new GetVOACNews().start();
		new GetVOANNews().start();
		new GetSongs().start();
		new GetHeadNews().start();
	}

	/**
	 * 初始化相关Data
	 */
	private void initSlideShowViewPicData() {
		// 一步任务获取图片
		Log.e("准备发送一次轮播图片的请求：", "准备发送！！！");
		new GetSlidePicListTask().execute("");
	}

	/**
	 * 异步任务,获取数据
	 */
	class GetSlidePicListTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				// 这里一般调用服务端接口获取一组轮播图片，下面是从百度找的几个图片

				ClientSession.Instace().asynGetResponse(
						// 获取所有课程轮播图片的列表
						new SlideShowCourseListRequest(reqPackDesc),
						new IResponseReceiver() {
							@Override
							public void onResponse(BaseHttpResponse response,
												   BaseHttpRequest request, int rspCookie) {
								SlideShowCourseListResponse res = (SlideShowCourseListResponse) response;
								if (res.result.equals("1")) {
									if (res.ssCourseList.size() > 0) {
										ssCourseList.clear();
										ssCourseList.addAll(res.ssCourseList);
										imageUrls.clear();
										decList.clear();
										for (int i = 0; i < res.ssCourseList
												.size(); i++) {
											if (!imageUrls
													.contains(PIC_BASE_URL
															+ res.ssCourseList
															.get(i).pic)) {
												imageUrls.add(PIC_BASE_URL
														+ res.ssCourseList
														.get(i).pic);
											}
											decList.add(res.ssCourseList.get(i).name + ":" + res.ssCourseList.get(i).desc1);
										}
									}
									//添加底部的点、为轮播图添加点击处理事件
									slideHandler.sendEmptyMessage(0);
								}
							}
						},
						new IErrorReceiver() {

							@Override
							public void onError(ErrorResponse errorResponse, BaseHttpRequest request,
												int rspCookie) {
								// TODO Auto-generated method stub
								if (imageUrls.size() == 0) {
									Log.e("执行轮播图片请求异常：", "异常中！！！");
									imageUrls.clear();
//                                    imageUrls.add(PIC_BASE_URL + "upload/1430274693593.png");
//                                    imageUrls.add(PIC_BASE_URL + "upload/1459324400736.png");
//                                    imageUrls.add(PIC_BASE_URL + "upload/1459417381077.png");
//
//                                    ssCourseList.clear();
//                                    ssCourseList.add(new SlideShowCourse(26, 8600, "托福听力", 7,
//                                            "upload/1430274693593.png", "听得好才能记得好"));
//                                    ssCourseList.add(new SlideShowCourse(803, 0, "雅思听力导学", 61,
//                                            "upload/1459324400736.png", "雅思听力导学"));
//                                    ssCourseList.add(new SlideShowCourse(804, 0, "N1语法", 61,
//                                            "upload/1459417381077.png", "雅思口语入门"));

									//添加底部的点、为轮播图添加点击处理事件
									slideHandler.sendEmptyMessage(0);
								}
							}
						}, null);

				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}

	private void initDot() {
		// 滚动的个数应该和图片的个数相等
		// 清空点所在集合
		dot_list.clear();
		dots_ll.removeAllViews();
		for (int i = 0; i < imageUrls.size(); i++) {
			View view = new View(mContext);
			if (i == 0) {
				// 红色
				view.setBackgroundResource(com.iyuba.lib.R.drawable.dot_focus);
			} else {
				view.setBackgroundResource(com.iyuba.lib.R.drawable.dot_blur);
			}
			// 指定点的大小
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					7, 7);
			// 指定点的间距
			layoutParams.setMargins(5, 0, 5, 0);
			// 添加到线性布局中
			dots_ll.addView(view, layoutParams);
			// 添加到集合中去
			dot_list.add(view);
		}
	}

	Handler slideHandler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
				case 0:
					// 初始划滚动点
					initDot();
					// 创建轮播图
					if (ssCourseList != null && ssCourseList.size() != 0) {
						RollViewPager rollViewPager = new RollViewPager(mContext,
								dot_list, new RollViewPager.OnViewClickListener() {
							// 用于处理点击图片的逻辑
							public void viewClick(SlideShowCourse ssCourse) {
								Intent intent = new Intent();
								CommonNews cn = new CommonNews();
								switch (ssCourse.name) {
									case "VOA慢速英语":
										newsArrayList.clear();
										cn.picUrl = "http://static.iyuba.com/images/voa/" + ssCourse.id + ".jpg";
										cn.title = ssCourse.desc1;
										cn.id = ssCourse.id;
										cn.musicUrl = "/" + dt + "/" + ssCourse.id + ".mp3";
										Log.e("musicUrl",cn.musicUrl);
										newsArrayList.add(cn);
										String lesson = "VOA慢速英语";
										String shareUrl = "http://voa.iyuba.com/audioitem_special_";
										CommonNewsDataManager.Instace().voasTemp = newsArrayList;
										CommonNewsDataManager.Instace().position = 0;
										CommonNewsDataManager.Instace().lesson = lesson;
										CommonNewsDataManager.Instace().url = shareUrl;
										intent.setClass(mContext, AudioPlayer.class);
										intent.putExtra("source", 0);
										startActivity(intent);
										break;
									case "BBC英语":
										newsArrayList.clear();
										cn.picUrl = "http://static.iyuba.com/images/minutes/" + ssCourse.id + ".jpg";
										cn.title = ssCourse.desc1;
										cn.id = ssCourse.id;
										cn.musicUrl = ssCourse.id + ".mp3";
										newsArrayList.add(cn);
										lesson = "BBC英语";
										shareUrl = "http://bbc.iyuba.com/BBCItem_1_";
										CommonNewsDataManager.Instace().voasTemp = newsArrayList;
										CommonNewsDataManager.Instace().position = 0;
										CommonNewsDataManager.Instace().lesson = lesson;
										CommonNewsDataManager.Instace().url = shareUrl;
										intent.setClass(mContext, AudioPlayer.class);
										intent.putExtra("source", 1);
										startActivity(intent);
										break;
									case "听歌学英语":
										newsArrayList.clear();
										cn.picUrl = ssCourse.id + ".jpg";
										cn.title = ssCourse.desc1;
										cn.id = ssCourse.id;
										cn.musicUrl = ssCourse.id + "S.mp3";
										newsArrayList.add(cn);
										lesson = "听歌学英语";
										shareUrl = "http://music.iyuba.com/play.jsp?SongId=";
										CommonNewsDataManager.Instace().voasTemp = newsArrayList;
										CommonNewsDataManager.Instace().position = 0;
										CommonNewsDataManager.Instace().lesson = lesson;
										CommonNewsDataManager.Instace().url = shareUrl;
										intent.setClass(mContext, AudioPlayer.class);
										intent.putExtra("source", 2);
										startActivity(intent);
										break;
									case "英语头条":
										break;
									case "VOA常速英语":
										newsArrayList.clear();
										cn.picUrl = "http://static.iyuba.com/images/voa/" + ssCourse.id + ".jpg";
										cn.title = ssCourse.desc1;
										cn.id = ssCourse.id;
										cn.musicUrl = "/" + dt + "/" + ssCourse.id + ".mp3";
										Log.e("musicUrl",cn.musicUrl);
										newsArrayList.add(cn);
										lesson = "VOA常速英语";
										shareUrl = "http://voa.iyuba.com/audioitem_stardard_";
										CommonNewsDataManager.Instace().voasTemp = newsArrayList;
										CommonNewsDataManager.Instace().position = 0;
										CommonNewsDataManager.Instace().lesson = lesson;
										CommonNewsDataManager.Instace().url = shareUrl;
										intent.setClass(mContext, AudioPlayer.class);
										intent.putExtra("source", 0);
										startActivity(intent);
										break;
									default:
										break;
								}
								// 之前是position-1，现在因为添加了ListView的Header，所以改成了position-2
//                                curPackId = ssCourse.id;
//                                curPackPrice = ssCourse.price;
//                                MobManager.Instance().packid = curPackId;
//                                MobManager.Instance().ownerid = ssCourse.ownerid;
//                                MobManager.Instance().appId = Constant.APPID;
//                                MobManager.Instance().desc = ssCourse.desc1;
//                                MobManager.Instance().curPackPrice = curPackPrice;
//                                intent.putExtra("packname", ssCourse.name);
//                                intent.setClass(mContext,
//                                        MobileClassActivity.class);
//                                if(curPackId != 0){
//                                    startActivity(intent);
//                                }

							}
						});
						// 将图片地址添加到轮播图中
						rollViewPager.initSlideShowCourseList(ssCourseList);
						rollViewPager.initImgUrl(imageUrls);
						rollViewPager.initTitle(decList, titleDec);
						rollViewPager.startRoll();
						top_news_viewpager.removeAllViews();
						top_news_viewpager.addView(rollViewPager);
					} else if (imageUrls != null && imageUrls.size() != 0) {
						RollViewPager rollViewPager = new RollViewPager(mContext,
								dot_list, new RollViewPager.OnViewClickListener() {
							// 用于处理点击图片的逻辑
							public void viewClick(SlideShowCourse ssCourse) {

							}
						});
						// 将图片地址添加到轮播图中
						rollViewPager.initImgUrl(imageUrls);
						rollViewPager.startRoll();
						top_news_viewpager.removeAllViews();
						top_news_viewpager.addView(rollViewPager);
					}
					break;
				case 1:
					NewsListAdapter nlaB = new NewsListAdapter(mContext, BBCList);
					BBCListView.setAdapter(nlaB);
					break;
				case 2:
					NewsListAdapter nlaVc = new NewsListAdapter(mContext, voaSList);
					voaSListView.setAdapter(nlaVc);
					break;
				case 3:
					NewsListAdapter nlaVn = new NewsListAdapter(mContext, voaNList);
					voaNListView.setAdapter(nlaVn);
					break;
				case 4:
					NewsListAdapter nlaSo = new NewsListAdapter(mContext, songList);
					songListView.setAdapter(nlaSo);
					break;
				case 5:
					NewsListAdapter nlaHd = new NewsListAdapter(mContext, headNewsList);
					headNewsListView.setAdapter(nlaHd);
					break;
				default:
					break;
			}
		}

	};

	class GetBBCNews extends Thread {
		public void run() {
			ClientSession.Instace().asynGetResponse(
					// 获取所有课程轮播图片的列表
					new BBCNewsRequest(itemNum),
					new IResponseReceiver() {
						@Override
						public void onResponse(BaseHttpResponse response,
											   BaseHttpRequest request, int rspCookie) {
							BBCNewsResponse res = (BBCNewsResponse) response;
							if (!res.mList.isEmpty()) {
								BBCList.addAll(res.mList);
								slideHandler.sendEmptyMessage(1);

							}
						}
					},
					new IErrorReceiver() {

						@Override
						public void onError(ErrorResponse errorResponse, BaseHttpRequest request,
											int rspCookie) {
							// TODO Auto-generated method stub
						}
					}, null);
		}
	}

	class GetVOACNews extends Thread {
		public void run() {
			ClientSession.Instace().asynGetResponse(
					// 获取所有课程轮播图片的列表
					new VOACNewsRequest(itemNum),
					new IResponseReceiver() {
						@Override
						public void onResponse(BaseHttpResponse response,
											   BaseHttpRequest request, int rspCookie) {
							VOACNewsResponse res = (VOACNewsResponse) response;
							if (!res.mList.isEmpty()) {
//                                String time = res.mList.get(0).date.substring(0, res.mList.get(0).date.indexOf(" "));
//                                voaSList.add(res.mList.get(0));
//                                for (int i = 1; i < res.mList.size(); i++) {
//                                    if (time.equals(res.mList.get(i).date.substring(0, res.mList.get(i).date.indexOf(" "))))
//                                        voaSList.add(res.mList.get(i));
//                                }
								voaSList.addAll(res.mList);
								slideHandler.sendEmptyMessage(2);

							}
						}
					},
					new IErrorReceiver() {

						@Override
						public void onError(ErrorResponse errorResponse, BaseHttpRequest request,
											int rspCookie) {
							// TODO Auto-generated method stub
						}
					}, null);
		}
	}

	class GetVOANNews extends Thread {
		public void run() {
			ClientSession.Instace().asynGetResponse(
					// 获取所有课程轮播图片的列表
					new VOANNewsRequest(itemNum),
					new IResponseReceiver() {
						@Override
						public void onResponse(BaseHttpResponse response,
											   BaseHttpRequest request, int rspCookie) {
							VOANNewsResponse res = (VOANNewsResponse) response;
							if (!res.mList.isEmpty()) {
//                                String time = res.mList.get(0).date.substring(0, res.mList.get(0).date.indexOf(" "));
//                                voaNList.add(res.mList.get(0));
//                                for (int i = 1; i < res.mList.size(); i++) {
//                                    if (time.equals(res.mList.get(i).date.substring(0, res.mList.get(i).date.indexOf(" "))))
//                                        voaNList.add(res.mList.get(i));
//                                }
								voaNList.addAll(res.mList);
								slideHandler.sendEmptyMessage(3);

							}
						}
					},
					new IErrorReceiver() {

						@Override
						public void onError(ErrorResponse errorResponse, BaseHttpRequest request,
											int rspCookie) {
							// TODO Auto-generated method stub
						}
					}, null);
		}
	}


	class GetSongs extends Thread {
		public void run() {
			ClientSession.Instace().asynGetResponse(
					// 获取所有课程轮播图片的列表
					new SongsRequest(itemNum),
					new IResponseReceiver() {
						@Override
						public void onResponse(BaseHttpResponse response,
											   BaseHttpRequest request, int rspCookie) {
							SongsResponse res = (SongsResponse) response;
							if (!res.mList.isEmpty()) {
//                                String time = res.mList.get(0).date.substring(0, res.mList.get(0).date.indexOf(" "));
//                                voaNList.add(res.mList.get(0));
//                                for(int i = 1;i<res.mList.size();i++){
//                                    if(time.equals(res.mList.get(i).date.substring(0, res.mList.get(i).date.indexOf(" "))))
//                                        voaNList.add(res.mList.get(i));
//                                }
								songList.addAll(res.mList);
								slideHandler.sendEmptyMessage(4);

							}
						}
					},
					new IErrorReceiver() {

						@Override
						public void onError(ErrorResponse errorResponse, BaseHttpRequest request,
											int rspCookie) {
							// TODO Auto-generated method stub
						}
					}, null);
		}
	}

	class GetHeadNews extends Thread {
		public void run() {
			ClientSession.Instace().asynGetResponse(
					// 获取所有课程轮播图片的列表
					new HeadNewsRequest(itemNum),
					new IResponseReceiver() {
						@Override
						public void onResponse(BaseHttpResponse response,
											   BaseHttpRequest request, int rspCookie) {
							HeadNewsResponse res = (HeadNewsResponse) response;
							if (!res.mList.isEmpty()) {
//                                String time = res.mList.get(0).date.substring(0, res.mList.get(0).date.indexOf(" "));
//                                voaNList.add(res.mList.get(0));
//                                for(int i = 1;i<res.mList.size();i++){
//                                    if(time.equals(res.mList.get(i).date.substring(0, res.mList.get(i).date.indexOf(" "))))
//                                        voaNList.add(res.mList.get(i));
//                                }
								headNewsList.addAll(res.mList);
								slideHandler.sendEmptyMessage(5);

							}
						}
					},
					new IErrorReceiver() {

						@Override
						public void onError(ErrorResponse errorResponse, BaseHttpRequest request,
											int rspCookie) {
							// TODO Auto-generated method stub
						}
					}, null);
		}
	}
}
