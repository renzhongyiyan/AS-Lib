package com.iyuba.core.me.bean;

import java.util.List;

/**
 * 作者：renzhy on 17/1/7 11:29
 * 邮箱：renzhongyigoo@gmail.com
 */
public class LearnRankingListBean {

	/**
	 * message : Success
	 * result : 10
	 * myranking : 49
	 * myid : 928
	 * data : [{"uid":3764607,"sort":1,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"syx_zju","totalWord":0,"ranking":1,"totalTime":12444},{"uid":3913531,"sort":2,"imgSrc":"http://static1.iyuba.com/uc_server/head/2016/11/28/9/54/41/169e321a-4447-4003-91c8-ff0cf522646f-m.jpg","name":"杜慧文(zy)","totalWord":0,"ranking":2,"totalTime":12378},{"uid":3763235,"sort":3,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"sangsang20","totalWord":0,"ranking":3,"totalTime":12346},{"uid":1401766,"sort":4,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"Carson405","totalWord":0,"ranking":4,"totalTime":12291},{"uid":3889040,"sort":5,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"尤格米钩","totalWord":0,"ranking":5,"totalTime":12236},{"uid":2710644,"sort":6,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"xxx@hhh@yyy","totalWord":0,"ranking":6,"totalTime":12153},{"uid":3722829,"sort":7,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"max1011","totalWord":0,"ranking":7,"totalTime":11775},{"uid":855176,"sort":8,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"无名小冰冰","totalWord":0,"ranking":8,"totalTime":11478},{"uid":3915832,"sort":9,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"叶薇ye","totalWord":0,"ranking":9,"totalTime":10829},{"uid":3618501,"sort":10,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"lmf65","totalWord":0,"ranking":10,"totalTime":10172}]
	 * totalWord : 0
	 * myimgSrc : http://static1.iyuba.com/uc_server/head/2017/0/5/22/12/41/a19a6ce1-f40d-4d86-9e29-92ddd75d7bd2-m.jpg
	 * totalTime : 6950
	 * myname : iyuba
	 */

	private String message;
	private int result;
	private int myranking;
	private int myid;
	private int totalWord;
	private String myimgSrc;
	private int totalTime;
	private String myname;
	/**
	 * uid : 3764607
	 * sort : 1
	 * imgSrc : http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg
	 * name : syx_zju
	 * totalWord : 0
	 * ranking : 1
	 * totalTime : 12444
	 */

	private List<DataBean> data;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getMyranking() {
		return myranking;
	}

	public void setMyranking(int myranking) {
		this.myranking = myranking;
	}

	public int getMyid() {
		return myid;
	}

	public void setMyid(int myid) {
		this.myid = myid;
	}

	public int getTotalWord() {
		return totalWord;
	}

	public void setTotalWord(int totalWord) {
		this.totalWord = totalWord;
	}

	public String getMyimgSrc() {
		return myimgSrc;
	}

	public void setMyimgSrc(String myimgSrc) {
		this.myimgSrc = myimgSrc;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	public String getMyname() {
		return myname;
	}

	public void setMyname(String myname) {
		this.myname = myname;
	}

	public List<DataBean> getData() {
		return data;
	}

	public void setData(List<DataBean> data) {
		this.data = data;
	}

	public static class DataBean {
		private int uid;
		private int sort;
		private String imgSrc;
		private String name;
		private int totalWord;
		private int ranking;
		private int totalTime;

		public int getUid() {
			return uid;
		}

		public void setUid(int uid) {
			this.uid = uid;
		}

		public int getSort() {
			return sort;
		}

		public void setSort(int sort) {
			this.sort = sort;
		}

		public String getImgSrc() {
			return imgSrc;
		}

		public void setImgSrc(String imgSrc) {
			this.imgSrc = imgSrc;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getTotalWord() {
			return totalWord;
		}

		public void setTotalWord(int totalWord) {
			this.totalWord = totalWord;
		}

		public int getRanking() {
			return ranking;
		}

		public void setRanking(int ranking) {
			this.ranking = ranking;
		}

		public int getTotalTime() {
			return totalTime;
		}

		public void setTotalTime(int totalTime) {
			this.totalTime = totalTime;
		}
	}
}
