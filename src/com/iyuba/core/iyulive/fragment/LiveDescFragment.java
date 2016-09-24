package com.iyuba.core.iyulive.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.core.common.activity.Web;
import com.iyuba.core.iyulive.listener.ILiveDescListener;
import com.iyuba.core.iyulive.manager.ConstantManager;
import com.iyuba.lib.R;

import static com.youdao.sdk.nativeads.YouDaoInterstitialActivity.getActivity;


/**
 * 作者：renzhy on 16/7/21 19:34
 * 邮箱：renzhongyigoo@gmail.com
 */
public class LiveDescFragment extends Fragment implements ILiveDescListener,View.OnClickListener {

	TextView tvContentDesc;
	TextView tvTeacherDesc;
	TextView tvSuitablePeople;
	View mViewNormalProblem;
	View mViewQQConsulation;

	private Context mContext;
	private String mContentDesc = null;
	private String mTeacherDesc = null;
	private String mSuitablePeople = null;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_course_desc,container,false);
		initWidget(view);
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void initWidget(View view){
		tvContentDesc = (TextView) view.findViewById(R.id.tv_live_content_desc);
		tvTeacherDesc = (TextView) view.findViewById(R.id.tv_live_teacher_desc);
		tvSuitablePeople = (TextView) view.findViewById(R.id.tv_live_suitable_people);
		mViewNormalProblem = view.findViewById(R.id.ll_course_normal_problem);
		mViewQQConsulation = view.findViewById(R.id.ll_course_qq_consulation);

		if(mContentDesc != null){
			tvContentDesc.setText(mContentDesc);
		}
		if(mTeacherDesc != null){
			tvTeacherDesc.setText(mTeacherDesc);
		}
		if(mSuitablePeople != null){
			tvSuitablePeople.setText(mSuitablePeople);
		}
		mViewNormalProblem.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, Web.class);
				intent.putExtra(
						"url",
						"http://www.iyuba.com/cq.jsp");
				intent.putExtra("title", "常见问题");
				startActivity(intent);
			}
		});
		mViewQQConsulation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = "mqqwpa://im/chat?chat_type=wpa&uin="+ ConstantManager.QQ_CONSULT+"&version=1";
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				} catch (ActivityNotFoundException e) {
					// TODO Auto-generated catch block
					Toast.makeText(mContext, "您的设备尚未安装QQ客户端", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});
	}

	public void setContentDesc(String mContentDesc) {
		this.mContentDesc = mContentDesc;
	}

	public void setTeacherDesc(String mTeacherDesc) {
		this.mTeacherDesc = mTeacherDesc;
	}

	public void setSuitablePeople(String mSuitablePeople) {
		this.mSuitablePeople = mSuitablePeople;
	}

	@Override
	public void onFragmentDescUpdate(String contentDesc, String teacherDesc, String suitablePeople) {
		if(contentDesc != null && tvContentDesc != null){
			tvContentDesc.setText(contentDesc);
		}
		if(teacherDesc != null && tvTeacherDesc != null){
			tvTeacherDesc.setText(teacherDesc);
		}
		if(suitablePeople != null && tvSuitablePeople != null){
			tvSuitablePeople.setText(suitablePeople);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
//			case R.id.ll_course_normal_problem:
//
//				break;
//			case R.id.ll_course_qq_consulation:
//
//				break;
		}
	}
}
