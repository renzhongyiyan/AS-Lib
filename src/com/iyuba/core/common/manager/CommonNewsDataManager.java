package com.iyuba.core.common.manager;

import java.util.ArrayList;
import java.util.List;

import com.iyuba.core.common.sqlite.mode.CommonNews;
import com.iyuba.core.common.sqlite.mode.CommonNewsDetail;
import com.iyuba.core.common.widget.subtitle.Subtitle;
import com.iyuba.core.common.widget.subtitle.SubtitleSum;

/**
 * 简版新闻内容管理
 * 
 * @author chentong
 * @version 1.0
 */
public class CommonNewsDataManager {

	private static CommonNewsDataManager instance;

	private CommonNewsDataManager() {
	}

	public static synchronized CommonNewsDataManager Instace() {
		if (instance == null) {
			instance = new CommonNewsDataManager();
		}
		return instance;
	}

	public String url;
	public String lesson;
	public CommonNews voaTemp;
	public int position;
	public ArrayList<CommonNews> voasTemp = new ArrayList<CommonNews>();
	public List<CommonNewsDetail> voaDetailsTemp = new ArrayList<CommonNewsDetail>();

	public SubtitleSum subtitleSum;

	public void setSubtitleSum(CommonNews voa) {
		subtitleSum = new SubtitleSum();
		subtitleSum.voaid = voa.id;
		if (subtitleSum.subtitles == null) {
			subtitleSum.subtitles = new ArrayList<Subtitle>();
			subtitleSum.subtitles.clear();
		}
		setDetail();
	}

	public void setSubtitleSum() {
		subtitleSum = new SubtitleSum();
		if (subtitleSum.subtitles == null) {
			subtitleSum.subtitles = new ArrayList<Subtitle>();
			subtitleSum.subtitles.clear();
		}
		setDetail();
	}

	private void setDetail() {
		if (voaDetailsTemp != null) {
			int size = voaDetailsTemp.size();
			Subtitle st;
			for (int i = 0; i < size; i++) {
				st = new Subtitle();
				st.content = voaDetailsTemp.get(i).sentence + "\n"
						+ voaDetailsTemp.get(i).sentence_cn;
				st.english = voaDetailsTemp.get(i).sentence;
				st.pointInTime = voaDetailsTemp.get(i).startTime;
				subtitleSum.subtitles.add(st);
			}
		}
	}
}
