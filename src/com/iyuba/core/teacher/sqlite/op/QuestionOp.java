package com.iyuba.core.teacher.sqlite.op;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.iyuba.core.common.sqlite.db.DatabaseService;
import com.iyuba.core.microclass.sqlite.mode.CoursePack;
import com.iyuba.core.teacher.sqlite.mode.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionOp extends DatabaseService {

	//Question表
	public static final String TABLE_NAME_QUESTION = "Question";

	public static final String QID = "qid";
	public static final String TYPE = "type";
	public static final String UID = "uid";
	public static final String USERNAME = "username";
	public static final String USERIMG = "userimg";
	public static final String QUESTION = "question";
	public static final String IMG = "img";
	public static final String AUDIO = "audio";
	public static final String COMMENTCOUNT = "commentcount";
	public static final String ANSCOUNT = "anscount";
	public static final String AGREE = "agree";
	public static final String AGAINEST = "againest";
	public static final String TIME = "time";
	public static final String LOCATION = "location";
	public static final String SOURCE = "source";
	public static final String CATEGORY1 = "category1";
	public static final String CATEGORY2 = "category2";
	public static final String QUESTIONDETAIL = "questiondetail";

	public QuestionOp(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public synchronized void insertQuestions(List<Question> questions){
		if (questions != null && questions.size() != 0) {
			String sqlString="insert or replace into " + TABLE_NAME_QUESTION + " (" + QID + ","
					+ TYPE + "," + UID + "," + USERNAME + ","
					+ USERIMG + ","+ QUESTION + "," + IMG + ","
					+ AUDIO + "," + COMMENTCOUNT+ ","
					+ ANSCOUNT + "," + AGREE + "," + AGAINEST + ","
					+ TIME + ","+ LOCATION + "," + SOURCE + ","
					+ CATEGORY1 + "," + CATEGORY2 + "," + QUESTIONDETAIL +") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			for (int i = 0; i < questions.size(); i++) {
				Question question = questions.get(i);
				Object[] objects=new Object[]{ question.qid,question.type,question.uid,question.username,
						question.userimg,question.question,question.img,question.audio,question.commentCount,
						question.ansCount,question.agree,question.againest,question.time,question.location,
						question.source,question.category1,question.category2,question.questiondetail};
				importDatabase.openDatabase().execSQL(sqlString,objects);
				
				closeDatabase(null);
			}
		}
	}	
	
	/**
	 * 删除  数据库里面的名师堂问题
	 * 
	 */
	
	public synchronized boolean deleteQuestionData(){
		String sqlString="delete from Question";
		try{
			importDatabase.openDatabase().execSQL(sqlString);
			closeDatabase(null);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false; 
		}
		
	}
	
	/**
	 *
	 * 查找所有的名师堂列表问题的信息
	 * @return
	 */
	public synchronized ArrayList<Question> findDataByAll() {
		ArrayList<Question> questions = new ArrayList<Question>();

		Cursor cursor = null;
		try {
			cursor = importDatabase.openDatabase().rawQuery(
					"select *" + " from " + TABLE_NAME_QUESTION
					, new String[] {});
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				Question question = new Question();
				question.qid = cursor.getInt(0);
				question.type = cursor.getString(1);
				question.uid = cursor.getString(2);
				question.username = cursor.getString(3);
				question.userimg = cursor.getString(4);
				question.question = cursor.getString(5);
				question.img = cursor.getString(6);
				question.audio = cursor.getString(7);
				question.commentCount = cursor.getInt(8);
				question.ansCount = cursor.getInt(9);
				question.agree = cursor.getInt(10);
				question.againest = cursor.getInt(11);
				question.time = cursor.getString(12);
				question.location = cursor.getString(13);
				question.source = cursor.getString(14);
				question.category1 = cursor.getString(15);
				question.category2 = cursor.getString(16);
				question.questiondetail = cursor.getString(17);
				questions.add(question);
			}
			cursor.close();
		}
		catch (Exception e) {
		}
		finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return questions;
	}


	/**
	 *
	 * 查找所有的名师堂列表问题的信息
	 * @return
	 */
	public synchronized ArrayList<Question> findDataLastTwenty() {
		ArrayList<Question> questions = new ArrayList<Question>();

		Cursor cursor = null;
		try {
			cursor = importDatabase.openDatabase().rawQuery(
					"select *" + " from " + TABLE_NAME_QUESTION + " LIMIT 20"
					, new String[] {});
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				Question question = new Question();
				question.qid = cursor.getInt(0);
				question.type = cursor.getString(1);
				question.uid = cursor.getString(2);
				question.username = cursor.getString(3);
				question.userimg = cursor.getString(4);
				question.question = cursor.getString(5);
				question.img = cursor.getString(6);
				question.audio = cursor.getString(7);
				question.commentCount = cursor.getInt(8);
				question.ansCount = cursor.getInt(9);
				question.agree = cursor.getInt(10);
				question.againest = cursor.getInt(11);
				question.time = cursor.getString(12);
				question.location = cursor.getString(13);
				question.source = cursor.getString(14);
				question.category1 = cursor.getString(15);
				question.category2 = cursor.getString(16);
				question.questiondetail = cursor.getString(17);
				questions.add(question);
			}
			cursor.close();
		}
		catch (Exception e) {
		}
		finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return questions;
	}
}
