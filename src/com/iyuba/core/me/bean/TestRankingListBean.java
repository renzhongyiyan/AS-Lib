package com.iyuba.core.me.bean;

import java.util.List;

/**
 * 作者：renzhy on 17/1/7 11:31
 * 邮箱：renzhongyigoo@gmail.com
 */
public class TestRankingListBean {

	/**
	 * message : Success
	 * result : 10
	 * totalTest : 1
	 * myranking : 489
	 * myid : 928
	 * data : [{"uid":3916121,"sort":1,"totalTest":280,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"黄玉芬","totalRight":157,"ranking":1},{"uid":3934749,"sort":2,"totalTest":266,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"tara8685","totalRight":194,"ranking":2},{"uid":3225513,"sort":3,"totalTest":176,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"可爱的公主梦","totalRight":39,"ranking":3},{"uid":3812312,"sort":4,"totalTest":172,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"不翼而飞","totalRight":153,"ranking":4},{"uid":3926375,"sort":5,"totalTest":122,"imgSrc":"http://static1.iyuba.com/uc_server/head/2017/0/3/15/37/30/34d532b8-4902-46e3-a252-9c7fba2f982f-m.jpg","name":"krystal11","totalRight":84,"ranking":5},{"uid":3789992,"sort":6,"totalTest":102,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"Yousuhong","totalRight":64,"ranking":6},{"uid":3323031,"sort":7,"totalTest":101,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"niya412528","totalRight":86,"ranking":7},{"uid":3794965,"sort":8,"totalTest":100,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"Sophie.Snownette","totalRight":35,"ranking":8},{"uid":3699113,"sort":9,"totalTest":100,"imgSrc":"http://static1.iyuba.com/uc_server/head/2016/10/5/22/56/16/5427bb42-373d-4d72-99fb-4eef33950be8-m.jpg","name":"人可Angel","totalRight":95,"ranking":9},{"uid":3656897,"sort":10,"totalTest":90,"imgSrc":"http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg","name":"epb1234","totalRight":52,"ranking":10}]
	 * myimgSrc : http://static1.iyuba.com/uc_server/head/2017/0/5/22/12/41/a19a6ce1-f40d-4d86-9e29-92ddd75d7bd2-m.jpg
	 * totalRight : 0
	 * myname : iyuba
	 */

	private String message;
	private int result;
	private int totalTest;
	private int myranking;
	private int myid;
	private String myimgSrc;
	private int totalRight;
	private String myname;
	/**
	 * uid : 3916121
	 * sort : 1
	 * totalTest : 280
	 * imgSrc : http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg
	 * name : 黄玉芬
	 * totalRight : 157
	 * ranking : 1
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

	public int getTotalTest() {
		return totalTest;
	}

	public void setTotalTest(int totalTest) {
		this.totalTest = totalTest;
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

	public String getMyimgSrc() {
		return myimgSrc;
	}

	public void setMyimgSrc(String myimgSrc) {
		this.myimgSrc = myimgSrc;
	}

	public int getTotalRight() {
		return totalRight;
	}

	public void setTotalRight(int totalRight) {
		this.totalRight = totalRight;
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
		private int totalTest;
		private String imgSrc;
		private String name;
		private int totalRight;
		private int ranking;

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

		public int getTotalTest() {
			return totalTest;
		}

		public void setTotalTest(int totalTest) {
			this.totalTest = totalTest;
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

		public int getTotalRight() {
			return totalRight;
		}

		public void setTotalRight(int totalRight) {
			this.totalRight = totalRight;
		}

		public int getRanking() {
			return ranking;
		}

		public void setRanking(int ranking) {
			this.ranking = ranking;
		}
	}
}
