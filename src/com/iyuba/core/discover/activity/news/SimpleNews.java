/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.discover.activity.news;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.iyuba.core.common.base.BasisActivity;
import com.iyuba.core.common.base.CrashApplication;
import com.iyuba.core.common.listener.ProtocolResponse;
import com.iyuba.core.common.manager.CommonNewsDataManager;
import com.iyuba.core.common.protocol.BaseHttpResponse;
import com.iyuba.core.common.protocol.news.SimpleTitleRequest;
import com.iyuba.core.common.protocol.news.SimpleTitleResponse;
import com.iyuba.core.common.sqlite.mode.CommonNews;
import com.iyuba.core.common.util.ExeProtocol;
import com.iyuba.core.common.widget.dialog.CustomDialog;
import com.iyuba.core.common.widget.dialog.CustomToast;
import com.iyuba.core.common.widget.dialog.WaittingDialog;
import com.iyuba.core.discover.adapter.SimpleNewsAdapter;
import com.iyuba.lib.R;

/**
 * 简版新闻列表界面
 * 
 * @author chentong
 * @version 1.0
 * @para "type"新闻类别（VOA慢速、常速、BBC(3)、美语、视频、听歌）
 */
public class SimpleNews extends BasisActivity {
	private Context mContext;
	private SimpleNewsAdapter simpleNewsAdapter;
	private ArrayList<CommonNews> newsArrayList = new ArrayList<CommonNews>();
	private Button backBtn, downloadBtn;
	private ListView newsList;
	private TextView title;
	private String curNewsType;
	private String getTitleUrl;
	private String downloadAppUrl;
	private String shareUrl;
	private CustomDialog wettingDialog;
	private String lesson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_news);
		mContext = this;
		CrashApplication.getInstance().addActivity(this);
		curNewsType = this.getIntent().getExtras().getString("type");
		wettingDialog = WaittingDialog.showDialog(mContext);
		backBtn = (Button) findViewById(R.id.button_back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		downloadBtn = (Button) findViewById(R.id.app_download);
		downloadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Uri uri = Uri.parse(downloadAppUrl);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
					Dialog dialog = new AlertDialog.Builder(mContext)
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle(
									getResources().getString(
											R.string.alert_title))
							.setMessage(
									getResources().getString(
											R.string.alert_market_error))
							.setNeutralButton(
									getResources().getString(
											R.string.alert_btn_ok),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									}).create();
					dialog.show();
				}
			}
		});
		newsList = (ListView) findViewById(R.id.listview);
		newsList.setAdapter(simpleNewsAdapter);
		newsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				CommonNewsDataManager.Instace().voasTemp = newsArrayList;
				CommonNewsDataManager.Instace().position = arg2;
				CommonNewsDataManager.Instace().lesson = lesson;
				CommonNewsDataManager.Instace().url = shareUrl;
				if (curNewsType.equals(mContext.getString(R.string.voa_video))
						|| curNewsType.equals(mContext
								.getString(R.string.voa_ae))
						|| curNewsType.equals(mContext
								.getString(R.string.voa_ted))) {
					intent.setClass(mContext, VideoPlayer.class);
					startActivity(intent);
				} else {
					intent.setClass(mContext, AudioPlayer.class);
					if (curNewsType.equals(mContext
							.getString(R.string.voa_bbc6))
							|| curNewsType.equals(mContext
									.getString(R.string.voa_bbc))
							|| curNewsType.equals(mContext
									.getString(R.string.voa_bbcnews))) {
						intent.putExtra("source", 1);
					} else if (curNewsType.equals(mContext
							.getString(R.string.voa_music))) {
						intent.putExtra("source", 2);
					} else {
						intent.putExtra("source", 0);
					}

					startActivity(intent);
				}
			}
		});
		title = (TextView) findViewById(R.id.title);
		title.setText(curNewsType);
		initData();
	}

	/**
	 * 
	 */
	private void initData() {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(0);
		downloadAppUrl = "market://details?id=";
		if (curNewsType.equals(mContext.getString(R.string.voa_speical))) {
			getTitleUrl = "http://apps.iyuba.com/voa/titleApi.jsp?maxid=0&pageNum=20&pages=1&type=json";
			downloadAppUrl += "com.iyuba.voa";
			lesson = "VOA慢速英语";
			shareUrl = "http://voa.iyuba.com/audioitem_special_";
		} else if (curNewsType.equals(mContext.getString(R.string.voa_cs))) {
			getTitleUrl = "http://apps.iyuba.com/voa/titleApi.jsp?maxid=0&pageNum=20&pages=1&category=csvoa&type=json";
			downloadAppUrl += "com.iyuba.CSvoa";
			lesson = "VOA常速英语";
			shareUrl = "http://voa.iyuba.com/audioitem_stardard_";
		} else if (curNewsType.equals(mContext.getString(R.string.voa_video))) {
			getTitleUrl = "http://apps.iyuba.com/voa/titleApi.jsp?maxid=0&pageNum=20&pages=1&category=csvoa&type=json";
			downloadAppUrl += "com.iyuba.VoaVideo";
			lesson = "VOA英语视频";
			shareUrl = "http://voa.iyuba.com/videoitem_video_";
		} else if (curNewsType.equals(mContext.getString(R.string.voa_bbc))) {
			getTitleUrl = "http://apps.iyuba.com/minutes/titleApi.jsp?type=android&format=json&pages=1&parentID=2&pageNum=20&maxid=0";
			downloadAppUrl += "com.iyuba.bbcws";
			lesson = "BBC职场英语";
			shareUrl = "http://bbc.iyuba.com/BBCItem_2_";
		} else if (curNewsType.equals(mContext.getString(R.string.voa_bbc6))) {
			getTitleUrl = "http://apps.iyuba.com/minutes/titleApi.jsp?type=android&format=json&pages=1&parentID=1&pageNum=20&maxid=0";
			downloadAppUrl += "com.iyuba.bbc";
			lesson = "BBC六分钟英语";
			shareUrl = "http://bbc.iyuba.com/BBCItem_1_";
		} else if (curNewsType.equals(mContext.getString(R.string.voa_ae))) {
			getTitleUrl = "http://apps.iyuba.com/voa/titleApi.jsp?maxid=0&pageNum=20&pages=1&type=json&parentID=200";
			downloadAppUrl += "com.iyuba.AE";
			lesson = "美语怎么说";
			shareUrl = "http://voa.iyuba.com/videoitem_meiyu_";
		} else if (curNewsType.equals(mContext.getString(R.string.voa_bbcnews))) {
			getTitleUrl = "http://apps.iyuba.com/minutes/titleApi.jsp?type=android&format=json&pages=1&parentID=3&pageNum=20&maxid=0";
			downloadAppUrl += "com.iyuba.bbcinone";
			lesson = "BBC新闻";
			shareUrl = "http://bbc.iyuba.com/BBCItem_3_";
		} else if (curNewsType.equals(mContext.getString(R.string.voa_word))) {
			getTitleUrl = "http://apps.iyuba.com/voa/titleApi.jsp?maxid=0&pageNum=20&pages=1&type=json&parentID=10";
			downloadAppUrl += "com.iyuba.WordStory";
			lesson = "VOA单词故事";
			shareUrl = "http://voa.iyuba.com/audioitem_special_";
		} else if (curNewsType.equals(mContext.getString(R.string.voa_music))) {
			getTitleUrl = "http://apps.iyuba.com/afterclass/getSongList.jsp?maxId=0&pageNum=1&pageCounts=20&type=json";
			downloadAppUrl += "com.iyuba.music";
			lesson = "听歌学英语";
			shareUrl = "http://music.iyuba.com/play.jsp?SongId=";
		} else if (curNewsType.equals(mContext.getString(R.string.voa_ted))) {
			getTitleUrl = "http://apps.iyuba.com/voa/titleTed2.jsp?maxid=0&pageNum=20&pages=1&type=json";
			downloadAppUrl += "com.iyuba.TEDVideo";
			lesson = "TED英语演讲";
			shareUrl = "http://app.iyuba.com";
		}
		ExeProtocol.exe(new SimpleTitleRequest(getTitleUrl),
				new ProtocolResponse() {

					@Override
					public void finish(BaseHttpResponse bhr) {
						// TODO Auto-generated method stub
						Looper.prepare();
						newsArrayList = ((SimpleTitleResponse) bhr).voasTemps;
						if (curNewsType.equals(mContext
								.getString(R.string.voa_music))) {
							for (CommonNews commonNews : newsArrayList) {
								commonNews.picUrl = "http://static.iyuba.com/images/song/"
										+ commonNews.picUrl;
							}
						}
						handler.sendEmptyMessage(1);
						handler.sendEmptyMessage(3);
						Looper.loop();
					}

					@Override
					public void error() {
						// TODO Auto-generated method stub
						handler.sendEmptyMessage(1);
						handler.sendEmptyMessage(2);
					}
				});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				wettingDialog.show();
				break;
			case 1:
				wettingDialog.dismiss();
				break;
			case 2:
				CustomToast.showToast(mContext, R.string.check_network, 1000);
				break;
			case 3:
				simpleNewsAdapter = new SimpleNewsAdapter(mContext,
						newsArrayList);
				newsList.setAdapter(simpleNewsAdapter);
				break;
			}
		}
	};
}
