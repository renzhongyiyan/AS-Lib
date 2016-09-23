package com.iyuba.core.iyumooc.sqlite.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.iyuba.core.iyumooc.microclass.bean.CoursePackListBean;
import com.iyuba.core.iyumooc.microclass.bean.CourseTypeListBean;
import com.iyuba.core.iyumooc.sqlite.LibDBProxy;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：renzhy on 16/6/27 11:43
 * 邮箱：renzhongyigoo@gmail.com
 */
public class CoursePackTypeDao extends LibDBProxy {
	//CoursePackType表
	public static final String TABLE_NAME_COURSEPACKTYPE = "CoursePackType";
	public static final String ID = "id";
	public static final String DESC = "desc";
	public static final String CONDITION = "condition";
	public static final String NAME = "name";
	public static final String TYPE = "type";
	public static final String DESTINATION = "destination";

	private static CoursePackTypeDao instance;
	public static CoursePackTypeDao getInstance(){
		if(instance == null){
			synchronized (CoursePackTypeDao.class){
				if(instance == null){
					instance = new CoursePackTypeDao();
				}
			}
		}
		return instance;
	}

	public void insertCoursePackType(List<CourseTypeListBean.CourseTypeDataBean> courseTypes){
		openDB();
		if(courseTypes != null && courseTypes.size() > 0){
			for(CourseTypeListBean.CourseTypeDataBean courseType:courseTypes){
				ContentValues values = new ContentValues();
				values.put(ID,courseType.getId());
				values.put(DESC,courseType.getDesc());
				values.put(CONDITION,courseType.getCondition());
				values.put(NAME,courseType.getName());
				values.put(TYPE,courseType.getType());
				values.put(DESTINATION,courseType.getDestination());
				sqLiteDatabase.insert(TABLE_NAME_COURSEPACKTYPE,null,values);
			}
		}
		closeDB();
	}

	public void deleteCoursePackType(){
		openDB();
		sqLiteDatabase.delete(TABLE_NAME_COURSEPACKTYPE,null,null);
		closeDB();
	}

	public ArrayList<CourseTypeListBean.CourseTypeDataBean> findDataByAll(){
		openDB();
		ArrayList<CourseTypeListBean.CourseTypeDataBean> courseTypes = new ArrayList<>();
		Cursor cursor = sqLiteDatabase.query(TABLE_NAME_COURSEPACKTYPE,null,null,null,null,null,null);
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
			CourseTypeListBean.CourseTypeDataBean courseType = fillCursor(cursor);
			courseTypes.add(courseType);
		}
		closeDB();
		cursor.close();
		return courseTypes;
	}

	private CourseTypeListBean.CourseTypeDataBean fillCursor(Cursor cursor){
		CourseTypeListBean.CourseTypeDataBean courseType = new CourseTypeListBean.CourseTypeDataBean();
		courseType.setId(cursor.getInt(cursor.getColumnIndex(ID)));
		courseType.setDesc(cursor.getString(cursor.getColumnIndex(DESC)));
		courseType.setName(cursor.getString(cursor.getColumnIndex(NAME)));
		courseType.setType(cursor.getInt(cursor.getColumnIndex(TYPE)));
		courseType.setDestination(cursor.getString(cursor.getColumnIndex(DESTINATION)));
		return courseType;
	}

}
