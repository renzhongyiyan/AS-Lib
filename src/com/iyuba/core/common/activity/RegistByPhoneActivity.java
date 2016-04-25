package com.iyuba.core.common.activity;

/**
 * 手机注册界面
 * 
 * @author czf
 * @version 1.0
 * 
 */
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.iyuba.configation.Constant;
import com.iyuba.core.common.base.BasisActivity;
import com.iyuba.core.common.base.CrashApplication;
import com.iyuba.core.common.listener.ProtocolResponse;
import com.iyuba.core.common.protocol.BaseHttpResponse;
import com.iyuba.core.common.protocol.message.RequestSubmitMessageCode;
import com.iyuba.core.common.protocol.message.ResponseSubmitMessageCode;
import com.iyuba.core.common.util.ExeProtocol;
import com.iyuba.core.common.util.SmsContent;
import com.iyuba.core.common.util.TelNumMatch;
import com.iyuba.core.common.widget.dialog.CustomDialog;
import com.iyuba.core.common.widget.dialog.CustomToast;
import com.iyuba.core.common.widget.dialog.WaittingDialog;
import com.iyuba.lib.R;

public class RegistByPhoneActivity extends BasisActivity {
	private Context mContext;
	private EditText phoneNum, messageCode;
	private Button getCodeButton;
	private TextView toEmailButton;
	private Button backBtn;
	private String phoneNumString = "", messageCodeString = "";
	private Timer timer;
	private TextView protocol;
	private EventHandler eh;
	private TimerTask timerTask;
	private SmsContent smsContent;
	private CustomDialog waittingDialog;
    private EditTextWatch  editTextWatch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		editTextWatch =new EditTextWatch();
		setContentView(R.layout.regist_layout_phone);
		CrashApplication.getInstance().addActivity(this);
		waittingDialog = WaittingDialog.showDialog(mContext);
		messageCode = (EditText) findViewById(R.id.regist_phone_code);
		messageCode.addTextChangedListener(editTextWatch);
		phoneNum = (EditText) findViewById(R.id.regist_phone_numb);
		phoneNum.addTextChangedListener(editTextWatch);
		getCodeButton = (Button) findViewById(R.id.regist_getcode);
		nextstep_unfocus = (Button) findViewById(R.id.nextstep_unfocus);
		nextstep_unfocus.setEnabled(false);
		nextstep_focus = (Button) findViewById(R.id.nextstep_focus);
		SMSSDK.initSDK(this, Constant.SMSAPPID, Constant.SMSAPPSECRET);
		eh = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handlerSms.sendMessage(msg);
			}
		};
		SMSSDK.registerEventHandler(eh);
		smsContent = new SmsContent(RegistByPhoneActivity.this, handler_verify);
		protocol = (TextView) findViewById(R.id.protocol);
		protocol.setText(Html
				.fromHtml("我已阅读并同意<a href=\"http://app.iyuba.com/ios/protocol.html\">使用条款和隐私政策</a>"));
		protocol.setMovementMethod(LinkMovementMethod.getInstance());
		backBtn = (Button) findViewById(R.id.button_back);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		toEmailButton = (TextView) findViewById(R.id.regist_email);
		toEmailButton.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		toEmailButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent();
				intent.setClass(mContext, RegistActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		});
		getCodeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				if (verificationNum()) {
					if(timer!=null){
						timer.cancel();
					}						
					handler_waitting.sendEmptyMessage(1);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0,
							InputMethodManager.HIDE_NOT_ALWAYS);
					phoneNumString = phoneNum.getText().toString();
					ExeProtocol.exe(new RequestSubmitMessageCode(
							phoneNumString), new ProtocolResponse() {

						@Override
						public void finish(BaseHttpResponse bhr) {
							// TODO Auto-generated method stub
							ResponseSubmitMessageCode res = (ResponseSubmitMessageCode) bhr;
							if (res != null) {
								if (res.result.equals("1")) {
									handler_verify.sendEmptyMessage(1);
									RegistByPhoneActivity.this
											.getContentResolver()
											.registerContentObserver(
													Uri.parse("content://sms/"),
													true, smsContent);
								} else if (res.result.equals("-1")) {
									handler_waitting.sendEmptyMessage(3);
								}
								handler_waitting.sendEmptyMessage(2);
							}

						}

						@Override
						public void error() {
							// TODO Auto-generated method stub
							handler_waitting.sendEmptyMessage(2);
						}
					});
				} else {
					CustomToast.showToast(mContext, "电话不能为空");
				}
				
			}
		});
		nextstep_focus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (verification()) {
					SMSSDK.submitVerificationCode("86", phoneNumString,
							messageCode.getText().toString());
				} else {
					CustomToast.showToast(mContext, "验证码不能为空");
				}

			}
		});
	}
    
	public class EditTextWatch implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if((s.toString().length()==4&&verificationNum())
					||(verificationNum()&&messageCode.getText().toString().length()==4)){
				if(timer!=null){
					timer.cancel();
				}
				nextstep_focus.setVisibility(View.VISIBLE);
				nextstep_focus.setEnabled(true);
			}else{
				nextstep_focus.setVisibility(View.GONE);
				nextstep_focus.setEnabled(false);
			}
			
		}
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SMSSDK.unregisterEventHandler(eh);
	}

	public boolean verification() {
		phoneNumString = phoneNum.getText().toString();
		messageCodeString = messageCode.getText().toString();
		if (phoneNumString.length() == 0) {
			phoneNum.setError("手机号不能为空");
			return false;
		}
		if (!checkPhoneNum(phoneNumString)) {
			phoneNum.setError("手机号输入错误");
			return false;
		}
		if (messageCodeString.length() == 0) {
			messageCode.setError("验证码不能为空");
			return false;
		}
		return true;
	}

	/**
	 * 验证
	 */
	public boolean verificationNum() {
		phoneNumString = phoneNum.getText().toString();
		messageCodeString = messageCode.getText().toString();
		if (phoneNumString.length() == 0) {
			phoneNum.setError("手机号不能为空");
			return false;
		}
		if (!checkPhoneNum(phoneNumString)) {
			phoneNum.setError("手机号输入错误");
			return false;
		}

		return true;
	}

	public boolean checkPhoneNum(String userId) {
		if (userId.length() < 2)
			return false;
		TelNumMatch match = new TelNumMatch(userId);
		int flag = match.matchNum();		
		/*不check 号码的正确性，只check 号码的长度*/
		/*if (flag == 1 || flag == 2 || flag == 3) {
			return true;
		} else {
			return false;
		}*/
		if(flag ==5){
			return false;
		}else {
			return true;
		}
	}

	Handler handlerSms = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			if (result == SMSSDK.RESULT_COMPLETE) {
				// 短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
					CustomToast.showToast(mContext, "验证成功");
					Intent intent = new Intent();
					intent.setClass(mContext, RegistSubmitActivity.class);
					intent.putExtra("phoneNumb", phoneNumString);
					startActivity(intent);
					finish();
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					CustomToast.showToast(mContext, "验证码已经发送，请等待接收");
				}
			} else {
				Log.e("RegistByPhoneActivity",""+result);
				//CustomToast.showToast(mContext, "验证失败，请输入正确的验证码！");
				getCodeButton.setText("获取验证码");
				getCodeButton.setEnabled(true);
			}
		}
	};

	Handler handler_time = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// Handler处理消息
			if (msg.what > 0) {
				getCodeButton.setText("重新发送(" + msg.what + "s)");
			} else {
				timer.cancel();
				getCodeButton.setEnabled(true);
				getCodeButton.setText("获取验证码");
			}
		}
	};

	Handler handler_waitting = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				waittingDialog.show();
				break;
			case 2:
				waittingDialog.dismiss();
			case 3:
				CustomToast.showToast(mContext,
						"手机号已注册，请换一个号码试试~", 2000);
				break;
			}
		}
	};

	Handler handler_verify = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// Handler处理消息
			if (msg.what == 0) {
				timer.cancel();
				/*
				 * getCodeButton.setText("下一步"); getCodeButton.setEnabled(true);
				 */
				String verifyCode = (String) msg.obj;
				messageCode.setText(verifyCode);
				nextstep_focus.setVisibility(View.VISIBLE);
				nextstep_focus.setEnabled(true);
			} else if (msg.what == 1) {
				SMSSDK.getVerificationCode("86", phoneNum.getText().toString());
				timer = new Timer();
				timerTask = new TimerTask() {
					int i = 60;

					@Override
					public void run() {
						Message msg = new Message();
						msg.what = i--;
						handler_time.sendMessage(msg);
					}
				};
				timer.schedule(timerTask, 1000, 1000);
				getCodeButton.setTextColor(Color.WHITE);
				/*getCodeButton.setEnabled(false);*/
			}
		}
	};
	private Button nextstep_unfocus;
	private Button nextstep_focus;
}
