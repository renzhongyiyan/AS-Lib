package com.iyuba.core.discover.activity.test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.iyuba.core.common.base.BasisActivity;
import com.iyuba.core.common.base.CrashApplication;
import com.iyuba.core.discover.adapter.SimpleTestAdapter;
import com.iyuba.lib.R;

/**
 * 四六级主界面
 * 
 * @author chentong
 * @version 1.0
 * @para "type" 四级还是六级
 */
public class SimpleTest extends BasisActivity {
	private Context mContext;
	private SimpleTestAdapter simpleTestAdapter;
	private Button backBtn, downloadBtn;
	private ListView newsList;
	private TextView title;
	private String curTestType;
	private String getTestUrl;
	private String downloadAppUrl;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_news);
		mContext = this;
		CrashApplication.getInstance().addActivity(this);
		curTestType = this.getIntent().getExtras().getString("type");
		initData();
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
		simpleTestAdapter = new SimpleTestAdapter(mContext, type);
		newsList = (ListView) findViewById(R.id.listview);
		newsList.setAdapter(simpleTestAdapter);
		newsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, SimpleTestSub.class);
				intent.putExtra("testUrl", getTestUrl);
				intent.putExtra("item", simpleTestAdapter.getItem(arg2));
				intent.putExtra("type", type);
				startActivity(intent);
			}
		});
		title = (TextView) findViewById(R.id.title);
		title.setText(curTestType);
	}

	/**
	 * 
	 */
	private void initData() {
		// TODO Auto-generated method stub
		downloadAppUrl = "market://details?id=";
		if (curTestType.equals(mContext.getString(R.string.cet_4))) {
			getTestUrl = "http://apps.iyuba.com/newcet/cetTest.jsp?level=4&year=";
			downloadAppUrl += "com.iyuba.cet4";
			type = 0;
		} else if (curTestType.equals(mContext.getString(R.string.cet_6))) {
			getTestUrl = "http://apps.iyuba.com/newcet/cetTest.jsp?level=6&year=";
			downloadAppUrl += "com.iyuba.cet6";
			type = 1;
		} else if (curTestType.equals(mContext.getString(R.string.toeic))) {
			getTestUrl = "http://apps.iyuba.com/voa/titleApi.jsp?maxid=0&pageNum=20&pages=1&category=csvoa&type=json";
			downloadAppUrl += "com.iyuba.toeiclistening";
			type = 2;
		} else if (curTestType.equals(mContext.getString(R.string.toelf))) {
			getTestUrl = "http://apps.iyuba.com/minutes/titleApi.jsp?type=android&format=json&pages=1&parentID=2&pageNum=20&maxid=0";
			downloadAppUrl += "com.iyuba.toelflistening";
			type = 3;
		} else if (curTestType.equals(mContext.getString(R.string.jlpt1))) {
			getTestUrl = "http://apps.iyuba.com/minutes/titleApi.jsp?type=android&format=json&pages=1&parentID=1&pageNum=20&maxid=0";
			downloadAppUrl += "com.iyuba.JLPT1Listening";
			type = 4;
		} else if (curTestType.equals(mContext.getString(R.string.jlpt2))) {
			getTestUrl = "http://apps.iyuba.com/voa/titleApi.jsp?maxid=0&pageNum=20&pages=1&type=json&parentID=200";
			downloadAppUrl += "com.iyuba.JLPT2Listening";
			type = 5;
		} else if (curTestType.equals(mContext.getString(R.string.jlpt3))) {
			getTestUrl = "http://apps.iyuba.com/minutes/titleApi.jsp?type=android&format=json&pages=1&parentID=3&pageNum=20&maxid=0";
			downloadAppUrl += "com.iyuba.JLPT3Listening";
			type = 6;
		}
	}
}
