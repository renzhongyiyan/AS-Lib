package com.iyuba.core.microclass.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;

import com.iyuba.configation.Constant;
import com.iyuba.core.common.activity.Login;
import com.iyuba.core.common.base.BasisActivity;
import com.iyuba.core.common.listener.ProtocolResponse;
import com.iyuba.core.common.manager.AccountManager;
import com.iyuba.core.common.manager.MobManager;
import com.iyuba.core.common.network.ClientSession;
import com.iyuba.core.common.network.IResponseReceiver;
import com.iyuba.core.common.protocol.BaseHttpRequest;
import com.iyuba.core.common.protocol.BaseHttpResponse;
import com.iyuba.core.common.util.Base64Coder;
import com.iyuba.core.common.util.ExeProtocol;
import com.iyuba.core.common.util.GetDeviceInfo;
import com.iyuba.core.common.util.ReadBitmap;
import com.iyuba.core.common.widget.BackPlayer;
import com.iyuba.core.common.widget.Player;
import com.iyuba.core.common.widget.dialog.CustomToast;
import com.iyuba.core.microclass.protocol.AddCreditsRequest;
import com.iyuba.core.microclass.protocol.AddCreditsResponse;
import com.iyuba.core.microclass.protocol.UploadStudyRecordRequest;
import com.iyuba.core.microclass.protocol.UploadStudyRecordResponse;
import com.iyuba.core.microclass.protocol.ViewCountTitleRequest;
import com.iyuba.core.microclass.protocol.ViewCountTitleResponse;
import com.iyuba.core.microclass.sqlite.mode.MbText;
import com.iyuba.core.microclass.sqlite.mode.StudyRecordInfo;
import com.iyuba.core.microclass.sqlite.op.MobClassResOp;
import com.iyuba.core.microclass.sqlite.op.StudyRecordOp;
import com.iyuba.lib.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MobClassBase extends BasisActivity {
	public final static String MOB_CLASS_COURSE_IMAGE = "http://static3.iyuba.com/resource/";
	private Context mContext;

	private ArrayList<MbText> mbList = new ArrayList<MbText>();

	private String OwnerId;
	private String PackId;
	private String Title;
	private String TitleId;
//	private String url;
	
	private int curPosition;
	private int curPackId;
	private int titleId;
	
	private MobClassResOp mobClassResOp;
	private StudyRecordOp studyRecordOp;
	
	private int curImage = 0;//记录当前播放的图片ID
	private int preImage = 0;
	
	private String playDir = "";
	private String currImageName = "";
	
	private Bitmap bp;
	
	private LinearLayout playContrlBar;
	
	// widget
	private SeekBar seekBar = null;
	private BackPlayer vv;
	private Player player;
	private ProgressDialog waitting;
	private boolean isPaused;
	private String imagePath = "";

	private String imageDir = "";

	private Boolean playerShow = true;
	// 显示图片界面
	private ImageView coursePictures;
	private Timer mTimer = new Timer();

	private RelativeLayout rlPlayBar;
	private RelativeLayout rlPlayTimeAllScreen;
	private Button ibPrePic;
	private RelativeLayout rlMobClassBaseTitle;
	private Button ibNextPic;
	private Button btnBack;
	private Button btnChange;
	private Button btnPlay;
	private ImageView ivCourseShare;
	
	private TextView tvCurPicNum;
	private TextView tvAllPicNum;
	private TextView tvMobClassTitle;
	private TextView tvMobclassCurTime;
	private TextView tvMobclassAllTime;

	private TextView tvMobclassCurTimeAllScreen;
	private TextView tvMobclassAllTimeAllScreen;
	
	private ImageView ivMicrossBaseReplay;
	
	private StudyRecordInfo studyRecordInfo;
	private GetDeviceInfo getDeviceInfo;
	private String PackName;
	private String LessonId;
	private String TestNumber;
	private String Lesson;
	
	private int hasVideo;
	private int IsAllDownload;
	private int IsAudioDownload;
	private int IsVideoDownload;
	
	private String BeginTime;
	private String EndTime;
	private String updateTime;
	
	private static String shareCourseTitleUrl;
	private String shareCourseTitleImageUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置为无标题样式
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.lib_microclass_mobclass_base);
		mContext = this;
		
		configImageLoader();
		
		vv = new BackPlayer(mContext);
		player = new Player(mContext, null);
		mobClassResOp = new MobClassResOp(mContext);
		studyRecordOp = new StudyRecordOp(mContext);
//		mbList = (ArrayList<MbText>) getIntent().getSerializableExtra(
//				"mbTextList");
		OwnerId = MobManager.Instance().ownerid + "";
		PackId = MobManager.Instance().packid + "";

		Title = getIntent().getStringExtra("title");
		TitleId = getIntent().getStringExtra("titleid");
		curPosition = getIntent().getIntExtra("position", -1);
		Lesson = getIntent().getStringExtra("lesson");
		
		shareCourseTitleUrl = "http://class.iyuba.com/m.jsp?id="+TitleId;
		shareCourseTitleImageUrl = "http://static3.iyuba.com/resource/categoryIcon/"+PackId+".png";
		
		hasVideo = getIntent().getIntExtra("hasVideo",0);
		IsAllDownload = getIntent().getIntExtra("IsAllDownload",0);
		IsAudioDownload = getIntent().getIntExtra("IsAudioDownload",0);
		IsVideoDownload = getIntent().getIntExtra("IsVideoDownload",0);
		
		Log.e("MobClassBase中mbList size","取之前TitleId:"+TitleId);
		
		try {
			mbList = mobClassResOp.findSpecialCourseResourceData(TitleId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.e("MobClassBase中mbList size",mbList.size()+"");
		
		initStudyRecord();
		
		waitting = new ProgressDialog(mContext);
		
		findView();
		setView();
		initPlayer();
		
		controlVideo();

		playDir = Constant.envir + "res" + "/" + TitleId + "/"+TitleId+".m4a";
		
		imageDir = Constant.envir + "res" + "/" + TitleId + "/";
		
		
		Log.d("playDir:",playDir);
		Log.d("imageDir:",imageDir);
		
		Log.d("mbList's size:", mbList.size()+"");
		
//		player.playUrl(playDir);
		vv.setVideoPath(playDir);
		
		//增加浏览量
		handlerRequest.sendEmptyMessage(0);
		
		handleProgress.sendEmptyMessage(1);
		
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(vv.isPlaying()){
			vv.pause();
			
			EndTime = getDeviceInfo.getCurrentTime();
	        studyRecordInfo.EndTime = EndTime;
	        studyRecordInfo.Lesson = Lesson;
	        studyRecordInfo.EndFlg = "0";
	        studyRecordInfo.IsUpload = false;
	        studyRecordOp.saveStudyRecord(studyRecordInfo);
	        
	      //----------------学习记录，插入数据库一条就上传一条----------------------------------------------
	        Log.d("执行到的地方测试：","获取将要上传的学习记录！！！！！！！");
			
			Message msg = new Message();
			msg.what = 1;
			msg.obj = studyRecordInfo;
			studyHandler.sendMessageDelayed(msg, 1500);
			

			//----------------学习记录，插入数据库一条就上传一条----------------------------------------------
		
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		vv.start();
		
		BeginTime = getDeviceInfo.getCurrentTime();
		studyRecordInfo.BeginTime = BeginTime;
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		vv.stopPlayback();
	
	}
	
	public void initStudyRecord(){
		getDeviceInfo = new GetDeviceInfo(mContext);
		studyRecordInfo = new StudyRecordInfo();
		
		BeginTime = getDeviceInfo.getCurrentTime();
		studyRecordInfo.BeginTime = BeginTime;
		studyRecordInfo.LessonId = PackId;
		studyRecordInfo.TestNumber = TitleId;
		studyRecordInfo.appId = Constant.APPID;
		studyRecordInfo.appName = Constant.AppName;
		studyRecordInfo.IP = getDeviceInfo.getLocalIPAddress();
		studyRecordInfo.DeviceId = getDeviceInfo.getLocalMACAddress();
		studyRecordInfo.Device = getDeviceInfo.getLocalDeviceType();
		
		studyRecordInfo.updateTime = "   ";
		studyRecordInfo.EndFlg = " ";	
		
		if (AccountManager.Instace(mContext).checkUserLogin()){
			studyRecordInfo.uid = AccountManager.Instace(mContext).userId;
		}else{
			studyRecordInfo.uid = "0";
		}
	}


	private void findView() {
		// TODO Auto-generated method stub

		rlPlayBar = (RelativeLayout) this.findViewById(R.id.all_PlayBar);
		rlPlayTimeAllScreen = (RelativeLayout) this
				.findViewById(R.id.RL_mobclassBaseCurAllTimeAllScreen);
		ibPrePic = (Button) this.findViewById(R.id.audio_pre);
		rlMobClassBaseTitle = (RelativeLayout) this
				.findViewById(R.id.RL_course_title);
		ibNextPic = (Button) this.findViewById(R.id.audio_next);
		btnBack = (Button) this.findViewById(R.id.mobClassBaseBtnBack);
		
		btnChange = (Button) this.findViewById(R.id.mobClassBaseBtnChangeVideo);
		if(hasVideo == 0 || (IsVideoDownload == 0 && IsAllDownload == 0)){
			btnChange.setVisibility(View.INVISIBLE);
		}
		
		btnPlay = (Button) this.findViewById(R.id.audio_play);
		tvCurPicNum = (TextView) this.findViewById(R.id.tv_mobclassBaseCurPics);
		tvAllPicNum = (TextView) this.findViewById(R.id.tv_mobclassBaseAllPics);
		tvMobClassTitle = (TextView) this
				.findViewById(R.id.tv_mobclassBaseTitle);
		tvMobclassCurTime = (TextView) this.findViewById(R.id.textView_curtime);
		tvMobclassAllTime = (TextView) this.findViewById(R.id.textView_alltime);

		tvMobclassCurTimeAllScreen = (TextView) this
				.findViewById(R.id.tv_mobclassBaseCurTimeAllScreen);
		tvMobclassAllTimeAllScreen = (TextView) this
				.findViewById(R.id.tv_mobclassBaseAllTimeAllScreen);

		ivCourseShare = (ImageView) this.findViewById(R.id.iv_course_share);
		
		coursePictures = (ImageView) this.findViewById(R.id.course_pictures);
		coursePictures.setScaleType(ScaleType.FIT_XY);

		rlPlayTimeAllScreen.setVisibility(View.GONE);
		
		ivMicrossBaseReplay = (ImageView) findViewById(R.id.iv_microclass_base_replay);
	}

	private void setView() {

		mTimer.cancel();
		mTimer = new Timer();
		mTimer.schedule(mTimerTask, 0, 1000);

		// TODO Auto-generated method stub
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
		ivCourseShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AccountManager.Instace(mContext).checkUserLogin()) {
					
					String  addIntegralUrl ="http://api.iyuba.com/credits/updateScore.jsp?srid=49";
					String url =addIntegralUrl+
								 "&uid="+AccountManager.Instace(mContext).userId+
								 "&idindex="+TitleId+
								 "&mobile=1"+
								 "&flag="+"1234567890"+Base64Coder.getTime();
					Log.d("Share Coin:",url);
					
					showShare();
//					showSharePanel();
					
				} else {
					Intent intent;
					intent = new Intent();
					intent.setClass(mContext, Login.class);
					mContext.startActivity(intent);
				}
			}
		});
		
		btnChange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	        	Intent intent = new Intent();
				intent.putExtra("titleid", TitleId);
				intent.putExtra("title", Title);
				intent.putExtra("lesson", Lesson);
				
				intent.putExtra("hasVideo", hasVideo);
				intent.putExtra("IsAllDownload",IsAllDownload);
				intent.putExtra("IsAudioDownload",IsAudioDownload);
				intent.putExtra("IsVideoDownload",IsVideoDownload);
				
				intent.setClass(mContext, MobClassVideoBase.class);
				mContext.startActivity(intent);
				onBackPressed();
			}
		});
		
		ibPrePic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (curImage > 1) {// 不是第一张

					curImage -= 2;
					int preTime = mbList.get(curImage).seconds * 1000;

					vv.seekTo(preTime);
				} else {
					Toast.makeText(mContext, "这里是第一张", 1000).show();
				}
			}
		});
		ibNextPic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (curImage < mbList.size()) {// 不是最后一篇

					int nextTime = mbList.get(curImage).seconds * 1000;

					vv.seekTo(nextTime);
				} else {
					Toast.makeText(mContext, "已经是最后一张", 1000).show();
				}
			}
		});
		btnPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isPaused = !isPaused;
				if (isPaused) {
					btnPlay.setBackgroundResource(R.drawable.mob_play_button_pressed);
					
					vv.pause();
					
					EndTime = getDeviceInfo.getCurrentTime();
		            studyRecordInfo.EndTime = EndTime;
		            studyRecordInfo.Lesson = Lesson;
		            studyRecordInfo.EndFlg = "0";
		            studyRecordInfo.IsUpload = false;
		            studyRecordOp.saveStudyRecord(studyRecordInfo);
		            
		          //----------------学习记录，插入数据库一条就上传一条----------------------------------------------
		            Log.d("执行到的地方测试：","获取将要上传的学习记录！！！！！！！");
		    		
		    		Message msg = new Message();
		    		msg.what = 1;
		    		msg.obj = studyRecordInfo;
		    		studyHandler.sendMessageDelayed(msg, 1500);
		    		

		    		//----------------学习记录，插入数据库一条就上传一条----------------------------------------------
				
					
				} else {
					btnPlay.setBackgroundResource(R.drawable.mob_pause_button_pressed);
					vv.start();
					BeginTime = getDeviceInfo.getCurrentTime();
					studyRecordInfo.BeginTime = BeginTime;
				
					ivMicrossBaseReplay.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		ivMicrossBaseReplay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnPlay.setBackgroundResource(R.drawable.mob_pause_button_pressed);
				vv.start();
				
				BeginTime = getDeviceInfo.getCurrentTime();
				studyRecordInfo.BeginTime = BeginTime;
				
				ivMicrossBaseReplay.setVisibility(View.INVISIBLE);
			}
		});
	}

	/**
	 * 
	 */
	private void initPlayer() {
		// TODO Auto-generated method stub
		tvMobClassTitle.setText(Title);
		tvAllPicNum.setText("/" + mbList.size());
		seekBar = (SeekBar) findViewById(R.id.microclass_seekBar_player);
		seekBar.getParent().requestDisallowInterceptTouchEvent(true);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekbar, int progress,
					boolean fromUser) {
				if (fromUser) {
					vv.seekTo(progress);
					handler.sendEmptyMessage(0);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void setSeekbar() {
		int i = vv.getDuration();
		seekBar.setMax(i);
		i /= 1000;
		int minute = i / 60;
		int second = i % 60;
		minute %= 60;
		tvMobclassAllTime.setText(String.format("/%02d:%02d", minute, second));
		tvMobclassAllTimeAllScreen.setText(String.format("/%02d:%02d", minute,
				second));
	}

	private void controlVideo() {
		vv.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer arg0) {
				setSeekbar();
				vv.start();
				if (waitting != null && waitting.isShowing()) {
					handler.sendEmptyMessage(2);
				}
				videoHandler.sendEmptyMessage(0);
			}
		});
		vv.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				// TODO Auto-generated method stub
				Message msg = Message.obtain();
				msg.arg1 = percent;
				msg.what = 1;
				videoHandler.sendMessage(msg);
			}
		});
		vv.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				EndTime = getDeviceInfo.getCurrentTime();
				studyRecordInfo.Lesson = Lesson;
                studyRecordInfo.EndTime = EndTime;
                studyRecordInfo.EndFlg = "1";
                studyRecordInfo.IsUpload = false;
                studyRecordOp.saveStudyRecord(studyRecordInfo);
                //----------------学习记录，插入数据库一条就上传一条----------------------------------------------
	            Log.d("执行到的地方测试：","获取将要上传的学习记录！！！！！！！");
	    		
	    		Message msg = new Message();
	    		msg.what = 1;
	    		msg.obj = studyRecordInfo;
	    		studyHandler.sendMessageDelayed(msg, 1500);

	    		//----------------学习记录，插入数据库一条就上传一条----------------------------------------------
	               
	    		vv.seekTo(0);
	    		vv.pause();
	    		
	    		ivMicrossBaseReplay.setVisibility(View.VISIBLE);
	    		
	    		isPaused = true;
			}
		});
		
	}

	/**
	 * 
	 * 由目前播放到的时间在MbText时间点里面查找那张图片的位置
	 * 
	 * @param second目前播放到的时间
	 * @return
	 */
	public int getImage(int second) {
		int step = 0;
		if (mbList != null && mbList.size() != 0) {
			for (int i = 0; i < mbList.size(); i++) {
				if (second >= mbList.get(i).seconds) {
					step = i + 1;
				} else {
					break;
				}
			}
		}
		return step;
	}
	
	//**********************************************************************************
	//********************************无界面分享方式的测试用例********************************
	
	private void showSharePanel() {
		showShareCustom(true, QQ.NAME);
	}
	
	public class ShareContentCustomizeDemo implements ShareContentCustomizeCallback {

		public void onShare(Platform platform, ShareParams paramsToShare) {

			String text = Title;
			if ("QQ".equals(platform.getName())) {
				// 改写twitter分享内容中的text字段，否则会超长，
				// 因为twitter会将图片地址当作文本的一部分去计算长度
				text += "分享至QQ";
				paramsToShare.setText(text);
			}
		}

	}
	
	//快捷分享的文档：http://wiki.mob.com/Android_%E5%BF%AB%E6%8D%B7%E5%88%86%E4%BA%AB
	private void showShareCustom(boolean silent, String platform){
		final OnekeyShare oks = new OnekeyShare();
//		oks.setNotification(R.drawable.ic_launcher, this.getString(R.string.app_name));
		//不同平台的分享参数，请看文档
		//http://wiki.mob.com/Android_%E4%B8%8D%E5%90%8C%E5%B9%B3%E5%8F%B0%E5%88%86%E4%BA%AB%E5%86%85%E5%AE%B9%E7%9A%84%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E
		
		oks.setTitle(Title);
		oks.setTitleUrl(shareCourseTitleUrl);
		oks.setText("小伙伴们来听一下我们的爱语微课吧～");
		oks.setImageUrl(shareCourseTitleImageUrl);
		
		oks.setDialogMode();
		oks.disableSSOWhenAuthorize();
		if (platform != null) {
			oks.setPlatform(platform);
		}
		// 去自定义不同平台的字段内容
		// http://wiki.mob.com/Android_%E5%BF%AB%E6%8D%B7%E5%88%86%E4%BA%AB#.E4.B8.BA.E4.B8.8D.E5.90.8C.E5.B9.B3.E5.8F.B0.E5.AE.9A.E4.B9.89.E5.B7.AE.E5.88.AB.E5.8C.96.E5.88.86.E4.BA.AB.E5.86.85.E5.AE.B9
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
		oks.setCallback(new PlatformActionListener() {
			
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub
				Log.e("执行分享完毕的回调：", "分享完毕之后的回调！");
				Message msg = new Message();
				msg.what = 10;
				msg.obj = arg0.getName();
				
				handler.sendMessage(msg);
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		oks.show(this);
	}
	
	//********************************无界面分享方式的测试用例********************************
	//**********************************************************************************
	
	private void showShare() {
		
//		String  addIntegralUrl ="http://api.iyuba.com/credits/updateScore.jsp?srid=49";
//		String url =addIntegralUrl+
//				 "&uid="+AccountManager.USERID+
//				 "&idindex="+TitleId+
//				 "&mobile=1"+
//				 "&flag="+"1234567890"+Base64Coder.getTime();
//		Log.e("AddCoinRequest",url);		    
//		setAbsoluteURI(url);	
		
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//		 oks.setTitle("爱语微课");
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl(shareCourseTitleUrl);
		 // text是分享文本，所有平台都需要这个字段
//		 oks.setText("小伙伴们来听一下我们的爱语微课吧～");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 
//		 oks.setImageUrl(shareCourseTitleImageUrl);
		 
		 // url仅在微信（包括好友和朋友圈）中使用
		 Log.d("微信分享：",shareCourseTitleUrl);
//		 oks.setUrl(shareCourseTitleUrl);
//		 oks.setUrl("http://class.iyuba.com/m.jsp?id=2");
		 
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment(Constant.APPName);
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(Constant.APPName);
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl(shareCourseTitleUrl);
		 //图片的网络路径，新浪微博、人人、QQ空间和Linked-in
		 oks.setImageUrl(shareCourseTitleImageUrl);
		 
		 oks.setText("小伙伴们来听一下我们的爱语微课吧～");
		 oks.setTitle(Title);
		 oks.setUrl(shareCourseTitleUrl);
		 
		 oks.setCallback(new PlatformActionListener() {
			
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub
				
				Log.e("执行分享完毕的平台：", arg0.getName());
				
				Log.e("执行分享完毕的回调：", "分享完毕之后的回调！");
				
				Message msg = new Message();
				msg.what = 10;
				msg.obj = arg0.getName();
				
				handler.sendMessage(msg);
				
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		 
//		 oks.setText("测试分享文字 http://www.baidu.com");
//		 oks.setTitle("分享");
//		 oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
//		 oks.setUrl("http://www.baidu.com");
		 
		// 启动分享GUI
		 oks.show(this);
	}

	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			try {
				if (vv == null)
					return;
				if (vv.isPlaying() && seekBar.isPressed() == false) {
					handleProgress.sendEmptyMessage(0);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (vv != null && vv.isPlaying()) {
					int position = vv.getCurrentPosition();
	
					Log.d("CurrentPosition:", position + "");
	
					int duration = vv.getDuration();
	
					Log.d("Duration:", duration + "");
	
					if (duration > 0) {
	
					}
	
					int second = vv.getCurrentPosition() / 1000;
	
					curImage = getImage(second);
					
					Log.d("getCurImage 当前图片对应的序号：",curImage+"");
	
					imagePath = imageDir + mbList.get(curImage - 1).imageName
							+ ".jpg";
					
					File imageShow = new File(imagePath);
					
//					ImageLoader.getInstance().displayImage("file:\\"+imagePath,coursePictures);
					
					try {
						coursePictures.setImageBitmap(
								ReadBitmap.readBitmap(mContext,
										new FileInputStream(imageShow)));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
//					GitHubImageLoader.Instace(mContext).setPic(imagePath,
//							coursePictures, R.drawable.nearby_no_icon);
//					bp = BitmapFactory.decodeFile(imagePath);
//					coursePictures.setImageBitmap(bp);
					
					Log.e("imagePath00000:",imagePath);
	
				}
			break;
			case 1:
				//===================================================================
				imagePath = imageDir + mbList.get(curImage).imageName + ".jpg";
				
				File imageShow = new File(imagePath);
				
//				ImageLoader.getInstance().displayImage("file:\\"+imagePath,coursePictures);
				
				try {
					coursePictures.setImageBitmap(
							ReadBitmap.readBitmap(mContext,
									new FileInputStream(imageShow)));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				bp = BitmapFactory.decodeFile(imagePath);
//				
//				coursePictures.setImageBitmap(bp);
				
//				GitHubImageLoader.Instace(mContext).setPic(imagePath,
//						coursePictures, R.drawable.nearby_no_icon);
				
				Log.e("imagePath11111:",imagePath);
				//====================================================================
			break;
			}
		};
	};

	Handler videoHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				int i = vv.getCurrentPosition();
				seekBar.setProgress(i);
				i /= 1000;
				int minute = i / 60;
				int second = i % 60;
				minute %= 60;

				tvCurPicNum.setText("" + getImage(i));
				tvMobclassCurTime.setText(String.format("%02d:%02d", minute,
						second));
				tvMobclassCurTimeAllScreen.setText(String.format("%02d:%02d",
						minute, second));
				try {
					if (vv.isPlaying()) {
						handler.sendEmptyMessage(0);
						btnPlay.setBackgroundResource(R.drawable.mob_pause_button);
					} else {
						btnPlay.setBackgroundResource(R.drawable.mob_play_button);
						handler.sendEmptyMessage(5);
					}
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					sendEmptyMessageDelayed(0, 1000);
				}
				break;
			case 1:
				seekBar.setSecondaryProgress(msg.arg1 * seekBar.getMax() / 100);
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			if (playerShow == true) {
				rlMobClassBaseTitle.setVisibility(View.GONE);
				ibPrePic.setVisibility(View.GONE);
				ibNextPic.setVisibility(View.GONE);
				rlPlayBar.setVisibility(View.GONE);
				rlPlayTimeAllScreen.setVisibility(View.VISIBLE);

				playerShow = false;
			} else {
				rlMobClassBaseTitle.setVisibility(View.VISIBLE);
				ibPrePic.setVisibility(View.VISIBLE);
				ibNextPic.setVisibility(View.VISIBLE);
				rlPlayBar.setVisibility(View.VISIBLE);
				rlPlayTimeAllScreen.setVisibility(View.GONE);

				playerShow = true;
			}

			break;
		}
		return super.onTouchEvent(event);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			AddCreditsResponse addCoinResponse = null;
			String platformName = "";
			// TODO Auto-generated method stub
			if(msg.what == 8){
				addCoinResponse =(AddCreditsResponse) msg.obj;
			}else if(msg.what == 10){
				platformName = (String) msg.obj;
			}
			
			switch (msg.what) {
			case 0:
				// currentTextView.snyParagraph(currPara);
				break;
			case 1:
				waitting.show();
				break;
			case 2:
				waitting.dismiss();
				break;
			case 3:
				CustomToast.showToast(mContext,
						R.string.play_please_take_the_word);
				break;
			case 4:
				CustomToast.showToast(mContext, R.string.check_network);
				break;
			case 5:
				// currentTextView.unsnyParagraph();
				break;
			case 7:
				// currentTextView.setSubtitleSum(subtitleSum, 1);
				// currentTextView.setSyncho(syncho);
				// setLockButton();
				// if (BackgroundManager.bindService != null) {
				// if (BackgroundManager.bindService.getPlayer().isPlaying()) {
				// BackgroundManager.bindService.getPlayer().pause();
				// }
				// }
				// playVideo();
			case 8:
//				Toast.makeText(mContext,"平台分享回调，平台名称："+platformName,0).show();
				Toast.makeText(mContext,"分享成功加5分！您当前总积分:"+addCoinResponse.totalcredit, 0).show();
				break;
			case 9:
//				Toast.makeText(mContext,"平台分享回调，平台名称："+platformName,0).show();
				Toast.makeText(mContext,"您已分享过该课程，请换个课程！", 0).show();
				break;
			case 10:
				ClientSession.Instace().asynGetResponse(
						new AddCreditsRequest(
								AccountManager.Instace(mContext).userId,TitleId),
						new IResponseReceiver() {
							@Override
							public void onResponse(
									BaseHttpResponse response,
									BaseHttpRequest request, int rspCookie) {
								AddCreditsResponse addCoinResponse = (AddCreditsResponse) response;
								if (addCoinResponse.result == 200) {
									Message msg = Message.obtain();
									msg.obj = addCoinResponse;
									msg.what = 8;
									handler.sendMessage(msg);
								} else if (addCoinResponse.result == 201) {
									handler.sendEmptyMessage(9);
								}

							}
						});
				break;
			}
		}
	};
	
	Handler studyHandler = new Handler(){
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				final StudyRecordInfo studyRecordInfo = (StudyRecordInfo) msg.obj;
				ClientSession.Instace().asynGetResponse(
						new UploadStudyRecordRequest(studyRecordInfo),
						new IResponseReceiver() {

							@Override
							public void onResponse(
									BaseHttpResponse response,
									BaseHttpRequest request, int rspCookie) {
								// TODO Auto-generated method stub
								Log.d("UploadStudyRecordRequest response得到结果(单条上传记录)", "UploadStudyRecordRequest的内容");
								
								UploadStudyRecordResponse res = (UploadStudyRecordResponse)response;

								if (res.result.equals("1")) {
									Log.d("UploadStudyRecordResponse的结果为1", "结果为1");
									studyRecordOp.setIsUpload(studyRecordInfo.appId, studyRecordInfo.BeginTime);
								}
							}

						}, null, null);
				break;
			default:
				break;

			}
		}
	};
	
	Handler handlerRequest = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				try {
					ExeProtocol.exe(new ViewCountTitleRequest(PackId,TitleId), 
							new ProtocolResponse(){

							@Override
							public void finish(BaseHttpResponse bhr) {
								// TODO Auto-generated method stub
								Looper.prepare();

								ViewCountTitleResponse res = (ViewCountTitleResponse) bhr;

								if (res.ResultCode.equals("1")) {
									Toast.makeText(mContext, "获取Title浏览量正确！",1000);
								} else {
									Toast.makeText(mContext, "获取Title浏览量出错！",1000);
								}
								Looper.loop();
							}

							@Override
							public void error() {
								// TODO Auto-generated method stub
								
							}
							
						});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	 /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.nearby_no_icon) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.nearby_no_icon) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.nearby_no_icon) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                        // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

}
