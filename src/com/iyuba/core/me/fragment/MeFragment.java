/*
 * 文件名 
 * 包含类名列表
 * 版本信息，版本号
 * 创建日期
 * 版权声明
 */
package com.iyuba.core.me.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.configation.ConfigManager;
import com.iyuba.core.common.activity.Login;
import com.iyuba.core.common.activity.Web;
import com.iyuba.core.common.listener.ProtocolResponse;
import com.iyuba.core.common.manager.AccountManager;
import com.iyuba.core.common.manager.SocialDataManager;
import com.iyuba.core.common.network.ClientSession;
import com.iyuba.core.common.network.IResponseReceiver;
import com.iyuba.core.common.protocol.BaseHttpRequest;
import com.iyuba.core.common.protocol.BaseHttpResponse;
import com.iyuba.core.common.protocol.base.GradeRequest;
import com.iyuba.core.common.protocol.base.GradeResponse;
import com.iyuba.core.common.protocol.message.RequestBasicUserInfo;
import com.iyuba.core.common.protocol.message.RequestNewInfo;
import com.iyuba.core.common.protocol.message.RequestUserDetailInfo;
import com.iyuba.core.common.protocol.message.ResponseBasicUserInfo;
import com.iyuba.core.common.protocol.message.ResponseNewInfo;
import com.iyuba.core.common.protocol.message.ResponseUserDetailInfo;
import com.iyuba.core.common.setting.SettingConfig;
import com.iyuba.core.common.sqlite.db.Emotion;
import com.iyuba.core.common.sqlite.mode.UserInfo;
import com.iyuba.core.common.thread.GitHubImageLoader;
import com.iyuba.core.common.util.CheckGrade;
import com.iyuba.core.common.util.ExeProtocol;
import com.iyuba.core.common.util.Expression;
import com.iyuba.core.common.widget.dialog.CustomToast;
import com.iyuba.core.me.activity.AttentionCenter;
import com.iyuba.core.me.activity.FansCenter;
import com.iyuba.core.me.activity.InfoFullFillActivity;
import com.iyuba.core.me.activity.IntelLearningActivity;
import com.iyuba.core.me.activity.LearnResultActivity;
import com.iyuba.core.me.activity.MessageCenter;
import com.iyuba.core.me.activity.NewVipCenterActivity;
import com.iyuba.core.me.activity.NoticeCenter;
import com.iyuba.core.me.activity.PersonalHome;
import com.iyuba.core.me.activity.PersonalSignatureList;
import com.iyuba.core.me.activity.TestResultActivity;
import com.iyuba.core.me.activity.WordResultActivity;
import com.iyuba.core.teacher.activity.SettingActivity;
import com.iyuba.core.teacher.activity.QuesListActivity;
import com.iyuba.core.teacher.activity.QuestionNotice;
import com.iyuba.core.teacher.activity.TheQuesListActivity;
import com.iyuba.lib.R;

/**
 * 类名
 * 
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class MeFragment extends Fragment {
	private View noLogin, login; // 登录提示面板
	private Button loginBtn, logout;
	private Context mContext;
	private ImageView photo;
	private TextView name, state, fans, attention, notification, listem_time,
			integral, position, lv;
	private View person;
//	private View local_panel;
	private View stateView, messageView, vipView, back;
//	private View local, favor, read;
	private View attentionView, fansView, notificationView, integralView,
			discover_rqlist, discover_qnotice, discover_myq, discover_mysub,
			discover_iyubaset,intel_learn_goal, intel_userinfo, intel_result,
			intel_word_result,intel_test_result;
	private UserInfo userInfo;
	private View root;
//	private boolean showLocal;
	
	private ResponseUserDetailInfo userDetailInfo;
	private boolean bInfoFullFill = false;
	private boolean bLearnTarget = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		
//		CustomToast.showToast(mContext, "MeFragment---------->onCreate()");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.me, container, false);
		noLogin = root.findViewById(R.id.noLogin);
		login = root.findViewById(R.id.login);
		logout = (Button) root.findViewById(R.id.logout);
		back = root.findViewById(R.id.button_back);
		back.setVisibility(View.GONE);
//		local_panel = root.findViewById(R.id.local_panel);
//		if (!showLocal) {
//			local_panel.setVisibility(View.INVISIBLE);
//		}
		return root;
	}

	@Override
	public void setArguments(Bundle args) {
		super.setArguments(args);
//		showLocal = args.getBoolean("showLocal");
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (getUserVisibleHint() && isVisibleToUser) {
			checkLogin();
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// MobclickAgent.onResume(mContext);
		viewChange();
		verifyUsrInfoAndTarget();
		
	}

	@Override
	public void onPause() {
		super.onPause();
		// MobclickAgent.onPause(mContext);
	}

	private void viewChange() {
//		initLocal();
		if (checkLogin()) {
			userInfo = AccountManager.Instace(mContext).userInfo;
			ClientSession.Instace()
					.asynGetResponse(
							new RequestNewInfo(
									AccountManager.Instace(mContext).userId),
							new IResponseReceiver() {
								@Override
								public void onResponse(
										BaseHttpResponse response,
										BaseHttpRequest request, int rspCookie) {
									// TODO Auto-generated method stub
									ResponseNewInfo rs = (ResponseNewInfo) response;
									if (rs.letter > 0) {
										handler.sendEmptyMessage(2);
									} else {
										handler.sendEmptyMessage(5);
									}
								}
							});
			loadData();
		}
	}
	
	private void verifyUsrInfoAndTarget(){
		ExeProtocol.exe(new RequestUserDetailInfo(AccountManager.Instace(mContext).userId),
				new ProtocolResponse() {

					@Override
					public void finish(BaseHttpResponse bhr) {
						// TODO Auto-generated method stub
						ResponseUserDetailInfo responseUserDetailInfo = (ResponseUserDetailInfo) bhr;
						if (responseUserDetailInfo.result.equals("211")) {
							userDetailInfo = responseUserDetailInfo;
							if(!userDetailInfo.gender.equals("")
									&& !userDetailInfo.birthday.equals("")
									&& !userDetailInfo.resideLocation.equals("")
									&& !userDetailInfo.occupation.equals("")
									&& !userDetailInfo.education.equals("")
									&& !userDetailInfo.graduateschool.equals("")){
								bInfoFullFill = true;
							}else{
								bInfoFullFill = false;
							}
							
							if(!userDetailInfo.editUserInfo.getPlevel().equals("")&&
									!userDetailInfo.editUserInfo.getPtalklevel().equals("")&&
									!userDetailInfo.editUserInfo.getPreadlevel().equals("")&&
									!userDetailInfo.editUserInfo.getGlevel().equals("")&&
									!userDetailInfo.editUserInfo.getGtalklevel().equals("")&&
									!userDetailInfo.editUserInfo.getGreadlevel().equals("")&&
									Integer.valueOf(userDetailInfo.editUserInfo.getPlevel()) > 0
									&&Integer.valueOf(userDetailInfo.editUserInfo.getPtalklevel()) > 0
									&&Integer.valueOf(userDetailInfo.editUserInfo.getPreadlevel()) > 0
									&&Integer.valueOf(userDetailInfo.editUserInfo.getGlevel()) > 0
									&&Integer.valueOf(userDetailInfo.editUserInfo.getGtalklevel()) > 0
									&&Integer.valueOf(userDetailInfo.editUserInfo.getGreadlevel()) > 0)
								bLearnTarget = true;
							}else{
								bLearnTarget = false;
							}
						}

					@Override
					public void error() {
						// TODO Auto-generated method stub
						
					}	

				});
	}

	private boolean checkLogin() {
		if (!AccountManager.Instace(mContext).checkUserLogin()) {
			noLogin.setVisibility(View.VISIBLE);
			login.setVisibility(View.GONE);
			loginBtn = (Button) root.findViewById(R.id.button_to_login);
			loginBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(mContext, Login.class);
					startActivity(intent);
				}
			});
			
			logout.setVisibility(View.GONE);
			
			discover_iyubaset = root.findViewById(R.id.ll_layout_setting);
			discover_iyubaset.setVisibility(View.VISIBLE);
			discover_iyubaset.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent;
					intent = new Intent();
					intent.setClass(mContext, SettingActivity.class);
					startActivity(intent);
				}
			});
			
			return false;
		} else {
			noLogin.setVisibility(View.GONE);
			login.setVisibility(View.VISIBLE);
			logout.setVisibility(View.VISIBLE);
			logout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AlertDialog dialog = new AlertDialog.Builder(mContext)
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle(
									getResources().getString(
											R.string.alert_title))
							.setMessage(
									getResources().getString(
											R.string.logout_alert))
							.setPositiveButton(
									getResources().getString(
											R.string.alert_btn_ok),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											// TODO Auto-generated method stub
											handler.sendEmptyMessage(4);
										}
									})
							.setNeutralButton(
									getResources().getString(
											R.string.alert_btn_cancel),
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									}).create();
					dialog.show();
				}
			});
			
			discover_iyubaset = root.findViewById(R.id.ll_layout_setting);
			discover_iyubaset.setVisibility(View.VISIBLE);
			discover_iyubaset.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent;
					intent = new Intent();
					intent.setClass(mContext, SettingActivity.class);
					startActivity(intent);
				}
			});
			return true;
		}
	}

	/**
	 * 
	 */
	private void loadData() {
		// TODO Auto-generated method stub
		final String id = AccountManager.Instace(mContext).userId;
		init();
		ExeProtocol.exe(new RequestBasicUserInfo(id, id),
				new ProtocolResponse() {

					@Override
					public void finish(BaseHttpResponse bhr) {
						// TODO Auto-generated method stub
						ResponseBasicUserInfo response = (ResponseBasicUserInfo) bhr;
						userInfo = response.userInfo;
						AccountManager.Instace(mContext).userInfo = userInfo;
						handler.sendEmptyMessage(3);
						Looper.prepare();
						ExeProtocol.exe(new GradeRequest(id),
								new ProtocolResponse() {

									@Override
									public void finish(BaseHttpResponse bhr) {
										// TODO Auto-generated method stub
										GradeResponse response = (GradeResponse) bhr;
										userInfo.studytime = Integer
												.parseInt(response.totalTime);
										userInfo.position = response.positionByTime;
										handler.sendEmptyMessage(3);
									}

									@Override
									public void error() {
										// TODO Auto-generated method stub
										handler.sendEmptyMessage(0);
									}
								});
						Looper.loop();
					}

					@Override
					public void error() {
						// TODO Auto-generated method stub
					}
				});
	}

	/**
	 * 
	 */
	private void init() {
		// TODO Auto-generated method stub
		person = root.findViewById(R.id.personalhome);
		photo = (ImageView) root.findViewById(R.id.me_pic);
		name = (TextView) root.findViewById(R.id.me_name);
		state = (TextView) root.findViewById(R.id.me_state);
		attention = (TextView) root.findViewById(R.id.me_attention);
		listem_time = (TextView) root.findViewById(R.id.me_listem_time);
		position = (TextView) root.findViewById(R.id.me_position);
		lv = (TextView) root.findViewById(R.id.lv);
		fans = (TextView) root.findViewById(R.id.me_fans);
		notification = (TextView) root.findViewById(R.id.me_notification);

		integral = (TextView) root.findViewById(R.id.Integral_notification);
		stateView = root.findViewById(R.id.me_state_change);
		vipView = root.findViewById(R.id.me_vip);
		messageView = root.findViewById(R.id.me_message);
		attentionView = root.findViewById(R.id.attention_area);
		fansView = root.findViewById(R.id.fans_area);

		discover_rqlist = root.findViewById(R.id.discover_rqlist);
		discover_qnotice = root.findViewById(R.id.discover_qnotice);
		discover_myq = root.findViewById(R.id.discover_myq);
		discover_mysub = root.findViewById(R.id.discover_mysub);
		discover_iyubaset = root.findViewById(R.id.ll_layout_setting);
		
		intel_userinfo = root.findViewById(R.id.intel_userinfo);
		intel_learn_goal = root.findViewById(R.id.intel_goal);
		intel_result = root.findViewById(R.id.intel_result);
		intel_word_result = root.findViewById(R.id.intel_word_result);
		intel_test_result = root.findViewById(R.id.intel_test_result);
		
		if (AccountManager.Instace(mContext).isteacher.equals("1")) {
			discover_myq.setVisibility(View.GONE);
			discover_mysub.setVisibility(View.VISIBLE);
		} else {
			discover_myq.setVisibility(View.VISIBLE);
			discover_mysub.setVisibility(View.GONE);
		}

		notificationView = root.findViewById(R.id.notification_area);
		integralView = root.findViewById(R.id.Integral);
		setViewClick();
		if (userInfo != null) {
			setTextViewContent();
		}
	}

	/**
	 * 
	 */
	private void setViewClick() {
		// TODO Auto-generated method stub
		person.setOnClickListener(ocl);
		stateView.setOnClickListener(ocl);
		vipView.setOnClickListener(ocl);
		messageView.setOnClickListener(ocl);
		attentionView.setOnClickListener(ocl);
		fansView.setOnClickListener(ocl);
		stateView.setOnClickListener(ocl);
		notificationView.setOnClickListener(ocl);
		integralView.setOnClickListener(ocl);
		discover_rqlist.setOnClickListener(ocl);
		discover_qnotice.setOnClickListener(ocl);
		discover_myq.setOnClickListener(ocl);
		discover_mysub.setOnClickListener(ocl);
		discover_iyubaset.setOnClickListener(ocl);
		intel_userinfo.setOnClickListener(ocl);
		intel_learn_goal.setOnClickListener(ocl);
		intel_result.setOnClickListener(ocl);
		intel_word_result.setOnClickListener(ocl);
		intel_test_result.setOnClickListener(ocl);

	}

	/**
	 * 
	 */
	private void setTextViewContent() {
		// TODO Auto-generated method stub
		GitHubImageLoader.Instace(mContext).setCirclePic(
				AccountManager.Instace(mContext).userId, photo);
//		name.setText(AccountManager.Instace(mContext).userName);
		name.setText(userInfo.username);
		if (ConfigManager.Instance().loadInt("isvip") == 1) {
			Drawable img = mContext.getResources().getDrawable(R.drawable.vip);
			img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
			name.setCompoundDrawables(null, null, img, null);
		} else {
			Drawable img = mContext.getResources().getDrawable(
					R.drawable.no_vip);
			img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
			name.setCompoundDrawables(null, null, img, null);
		}
		if (userInfo.text == null) {
			state.setText(R.string.social_default_state);
		} else {

			String zhengze = "image[0-9]{2}|image[0-9]";
			Emotion emotion = new Emotion();
			userInfo.text = emotion.replace(userInfo.text);
			SpannableString spannableString = Expression.getExpressionString(
					mContext, userInfo.text, zhengze);
			state.setText(spannableString);
		}
		attention.setText(userInfo.following);
		fans.setText(userInfo.follower);
		listem_time.setText(exeStudyTime());
		position.setText(exePosition());
		lv.setText(exeIyuLevel());
		notification.setText(userInfo.notification);
		integral.setText(userInfo.icoins);
	}

	private String exeStudyTime() {
		StringBuffer sb = new StringBuffer(
				mContext.getString(R.string.me_study_time));
		int time = userInfo.studytime;
		int minus = time % 60;
		int minute = time / 60 % 60;
		int hour = time / 60 / 60;
		if (hour > 0) {
			sb.append(hour).append(mContext.getString(R.string.me_hour));
		} else if (minute > 0) {
			sb.append(minute).append(mContext.getString(R.string.me_minute));
		} else {
			sb.append(minus).append(mContext.getString(R.string.me_minus));
		}
		return sb.toString();
	}

	private String exePosition() {
		StringBuffer sb = new StringBuffer(
				mContext.getString(R.string.me_study_position));
		int position = Integer.parseInt(userInfo.position);

		if (position < 10000) {
			sb.append(position);
		} else {
			sb.append(position / 10000 * 10000).append("+");
		}
		return sb.toString();
	}

	private String exeIyuLevel() {
		StringBuffer sb = new StringBuffer("");
		sb.append(mContext.getString(R.string.me_lv));
		sb.append(CheckGrade.Check(userInfo.icoins));
		sb.append(" ");
		sb.append(CheckGrade.CheckLevelName(userInfo.icoins));
		// sb.append("   ");
		// sb.append(mContext.getString(R.string.me_score));
		// sb.append(userInfo.icoins);
		return sb.toString();
	}

	private OnClickListener ocl = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent;
			int id = arg0.getId();
			String packageName = mContext.getPackageName();
			if (id == R.id.personalhome) {
				intent = new Intent(mContext, PersonalHome.class);
				SocialDataManager.Instance().userid = AccountManager
						.Instace(mContext).userId;
				startActivity(intent);
			} else if (id == R.id.me_state_change) {
//				intent = new Intent(mContext, WriteState.class);
				intent = new Intent(mContext, PersonalSignatureList.class);
				startActivity(intent);
			} else if (id == R.id.me_vip) {
//				intent = new Intent(mContext, VipCenter.class);
				intent = new Intent(mContext, NewVipCenterActivity.class);
				intent.putExtra("userid", AccountManager.Instace(mContext).userId);
				intent.putExtra("username",AccountManager.Instace(mContext).userName);
				intent.putExtra("iyubi",AccountManager.Instace(mContext).userInfo.iyubi);
				startActivity(intent);
			} else if (id == R.id.me_message) {
				intent = new Intent(mContext, MessageCenter.class);
				startActivity(intent);
			} 
//			else if (id == R.id.me_local) {
//				intent = new Intent();
//				intent.setComponent(new ComponentName(packageName, packageName
//						+ ".activity.LocalNews"));
//				intent.putExtra("localType", 0);
//				startActivity(intent);
//			} else if (id == R.id.me_love) {
//				intent = new Intent();
//				intent.setComponent(new ComponentName(packageName, packageName
//						+ ".activity.LocalNews"));
//				intent.putExtra("localType", 1);
//				startActivity(intent);
//			} else if (id == R.id.me_read) {
//				intent = new Intent();
//
//				intent.setComponent(new ComponentName(packageName, packageName
//						+ ".activity.LocalNews"));
//				intent.putExtra("localType", 2);
//				startActivity(intent);
//			} 
			else if (id == R.id.attention_area) {
				intent = new Intent(mContext, AttentionCenter.class);
				intent.putExtra("userid",
						AccountManager.Instace(mContext).userId);
				startActivity(intent);
			} else if (id == R.id.fans_area) {
				intent = new Intent(mContext, FansCenter.class);
				intent.putExtra("userid",
						AccountManager.Instace(mContext).userId);
				startActivity(intent);
			} else if (id == R.id.notification_area) {
				intent = new Intent(mContext, NoticeCenter.class);
				intent.putExtra("userid",
						AccountManager.Instace(mContext).userId);
				startActivity(intent);
			} else if (id == R.id.Integral) {
				intent = new Intent(mContext, Web.class);
				intent.putExtra("title", "积分明细");
				intent.putExtra("url",
						"http://api.iyuba.com/credits/useractionrecordmobileList1.jsp?uid="
								+ AccountManager.Instace(mContext).userId);
				startActivity(intent);
			} else if (id == R.id.discover_rqlist) {
				intent = new Intent();
				intent.setClass(mContext, QuesListActivity.class);
				startActivity(intent);
			} else if (id == R.id.discover_qnotice) {
				intent = new Intent();
				intent.setClass(mContext, QuestionNotice.class);
				startActivity(intent);
			} else if (id == R.id.discover_myq) {
				intent = new Intent();
				intent.setClass(mContext, TheQuesListActivity.class);
				intent.putExtra("utype", "4");
				startActivity(intent);
			} else if (id == R.id.discover_mysub) {
				intent = new Intent();
				intent.setClass(mContext, TheQuesListActivity.class);
				intent.putExtra("utype", "2");
				startActivity(intent);
			} else if (id == R.id.ll_layout_setting) {
				intent = new Intent();
				intent.setClass(mContext, SettingActivity.class);
				startActivity(intent);
			}else if (id == R.id.intel_userinfo) {
				intent = new Intent();
				intent.setClass(mContext, InfoFullFillActivity.class);
				startActivity(intent);
			}else if (id == R.id.intel_goal) {
				if(bInfoFullFill){
					intent = new Intent();
					intent.setClass(mContext, IntelLearningActivity.class);
					startActivity(intent);
				}else{
					handler.sendEmptyMessage(6);
				}
				
			}else if (id == R.id.intel_result) {
				if(bLearnTarget){
					intent = new Intent();
					intent.setClass(mContext, LearnResultActivity.class);
					startActivity(intent);
				}else{
					handler.sendEmptyMessage(7);
				}
				
			}else if(id == R.id.intel_word_result){
				if(bLearnTarget){
					intent = new Intent();
					intent.setClass(mContext, WordResultActivity.class);
					startActivity(intent);
				}else{
					handler.sendEmptyMessage(7);
				}
				
			}else if(id == R.id.intel_test_result){
				if(bLearnTarget){
					intent = new Intent();
					intent.setClass(mContext, TestResultActivity.class);
					startActivity(intent);
				}else{
					handler.sendEmptyMessage(7);
				}
			}
		}
	};
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				CustomToast.showToast(mContext, R.string.check_network);
				break;
			case 1:
				CustomToast.showToast(mContext, R.string.action_fail);
				break;
			case 2:
				root.findViewById(R.id.newletter).setVisibility(View.VISIBLE);
				break;
			case 3:
				setTextViewContent();
				break;
			case 4:
				AccountManager.Instace(mContext).loginOut();
				CustomToast.showToast(mContext, R.string.loginout_success);
				SettingConfig.Instance().setHighSpeed(false);
				onResume();
				break;
			case 5:
				root.findViewById(R.id.newletter).setVisibility(View.GONE);
				break;
			case 6:
				Dialog dialog = new AlertDialog.Builder(mContext)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("提示")
				.setMessage(
						"亲,请先完善个人信息~")
				.setPositiveButton("确定",null)
				.create();
				dialog.show();// 如果要显示对话框，一定要加上这句
				break;
			case 7:
				Dialog dialog2 = new AlertDialog.Builder(mContext)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("提示")
				.setMessage(
						"亲,请先补充您的学习目标~")
				.setPositiveButton("确定",null)
				.create();
				dialog2.show();// 如果要显示对话框，一定要加上这句
				break;
			}
		}
	};
}
