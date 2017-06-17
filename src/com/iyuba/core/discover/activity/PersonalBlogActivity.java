/**
 * 
 */
package com.iyuba.core.discover.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.iyuba.configation.Constant;
import com.iyuba.core.common.manager.DataManager;
import com.iyuba.core.common.util.OnPlayStateChangedListener;
import com.iyuba.core.common.util.Player;
import com.iyuba.core.common.util.TextLength;
import com.iyuba.core.common.util.VideoPlayer;
import com.iyuba.core.common.widget.dialog.CustomDialog;
import com.iyuba.lib.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author yao 显示日志内容
 */
public class PersonalBlogActivity extends THSActivity{

	private Button backButton, blogComments,share;
	private String message = "",subject="",deadline="";
	private TextView blogTitle, blogSubject,blogTime;
	private TextView test;
	int i = 0;
	private CustomDialog waitingDialog;
	// ------------------------单词释义相关控件
	private TextView keyText, pronText, defText;
	private Button addWordsBtn;
	private ProgressBar loadingBar;
	Spanned sp;
	// 播放
	private LinearLayout playerLayout;
	private ImageButton pause;
	private TextView durationTextView = null, playedTextView = null;
	private SurfaceView vv = null;
	private SeekBar seekBar = null;
	private boolean isPaused = true;
	private static int screenWidth = 0, screenHeight = 0;
	private ImageView image_stop;
	private Player m = null;// 播放音频
	// 获取VIP信息
	private static String vip = "0";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blog);
		waitingDialog = waitingDialog();
		message = getIntent().getStringExtra("message");
		subject = getIntent().getStringExtra("subject");
		deadline = getIntent().getStringExtra("deadline");
		test = (TextView) findViewById(R.id.test);
		
		initTranslationPanel();
		initWidget();
		
		handler.sendEmptyMessage(2);
		Thread thread = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				sp = Html.fromHtml(message, new Html.ImageGetter() {
					@Override
					public Drawable getDrawable(String source) {
						InputStream is = null;
						try {
							is = (InputStream) new URL(source).getContent();
							Drawable d = Drawable.createFromStream(is, "src");
							d.setBounds(0, 0, 600,300);
							is.close();
							return d;
						} catch (Exception e) {
							return null;
						}
					}
				}, null);
				handler.sendEmptyMessage(1);
			}
		};
		thread.start();

	}

	private void initTranslationPanel() {
		// TODO Auto-generated method stub
		keyText = (TextView) findViewById(R.id.word_key);
		pronText = (TextView) findViewById(R.id.word_pron);
		defText = (TextView) findViewById(R.id.word_def);
		
		loadingBar = (ProgressBar) findViewById(R.id.progressBar_get_Interperatatior);
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		backButton = (Button) findViewById(R.id.blog_back_btn);

		blogTitle = (TextView) findViewById(R.id.blog_title);
		blogSubject = (TextView) findViewById(R.id.blog_subject);
		blogTime = (TextView) findViewById(R.id.blog_time);

		blogComments = (Button) findViewById(R.id.blogcomments);
		playerLayout = (LinearLayout) findViewById(R.id.playerLayout);
		pause = (ImageButton) findViewById(R.id.video_play);
		durationTextView = (TextView) findViewById(R.id.textView_alltime);
		playedTextView = (TextView) findViewById(R.id.textView_currenttime);
		vv = (SurfaceView) findViewById(R.id.videoView_small);
		LayoutParams lp = vv.getLayoutParams();
		lp.height = 200;
		lp.width = screenWidth - 100;
		vv.setLayoutParams(lp);
		seekBar = (SeekBar) findViewById(R.id.seekBar_player);
		image_stop = (ImageView) findViewById(R.id.image_stop);

		blogComments.setVisibility(View.INVISIBLE);
		blogTitle.setText("日志");
		if(subject.contains("</b>")){
			blogSubject.setText(subject.substring(3,subject.indexOf("</b>")));
		}else{
			blogSubject.setText(subject);
		}


		blogTime.setText(DateFormat.format(
				"yyyy-MM-dd kk:mm:ss",
				Long.parseLong(deadline) * 1000));

		playerLayout.setVisibility(View.GONE);
		vv.setVisibility(View.GONE);
		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PersonalBlogActivity.this.finish();
			}
		});
		blogComments.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, BlogCommentsActivity.class);
				mContext.startActivity(intent);
			}
		});

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.e("onPause","onPause");
		super.onPause();
	}


	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				test.setText(sp);
				handler.sendEmptyMessage(3);
				
				break;
			case 2:
				waitingDialog.show();
				break;
			case 3:
				waitingDialog.dismiss();
				break;
			
			default:
				break;
			}
		}

	};

	public CustomDialog waitingDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.wetting_layout, null);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
		CustomDialog cDialog = customBuilder.setContentView(layout).create();	
		return cDialog;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initWidget();
	}

	}
