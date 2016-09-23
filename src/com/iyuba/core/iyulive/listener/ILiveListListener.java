package com.iyuba.core.iyulive.listener;


import com.iyuba.core.iyulive.bean.LiveContentBean;

import java.util.ArrayList;

/**
 * 作者：renzhy on 16/7/22 13:39
 * 邮箱：renzhongyigoo@gmail.com
 */
public interface ILiveListListener {
	void onFragmentListUpdate(ArrayList<LiveContentBean.LiveTitleBean.LiveDetailBean> liveDetailList);
}
