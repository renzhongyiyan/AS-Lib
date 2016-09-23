package com.iyuba.core.iyumooc.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.iyuba.configation.RuntimeManager;
import com.iyuba.core.common.base.CrashApplication;

/**
 * 作者：renzhy on 16/6/27 10:21
 * 邮箱：renzhongyigoo@gmail.com
 */
public class LibOpenHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "lib_db";
	private static final int DB_VERION = 1;

	public LibOpenHelper(){
		super(RuntimeManager.getApplication().getApplicationContext(),DB_NAME,null,DB_VERION);
	}

	public static final String CREATE_COURSE_PACK_TYPE = "CREATE TABLE IF NOT exists CoursePackType (" +
			"id integer UNIQUE, " +
			"desc text, " +
			"condition text, " +
			"name text NOT NULL, " +
			"type integer NOT NULL, " +
			"destination text, " +
			"PRIMARY KEY (id,type) " +
			")";

	public static final String CREATE_SLIDE_PIC = "CREATE TABLE IF NOT exists SlidePic (" +
			"id text PRIMARY KEY, " +
			"price text, " +
			"name text, " +
			"pic text, " +
			"ownerid text, " +
			"desc1 text " +
			")";

	public static final String CREATE_COURSE_PACK = "CREATE TABLE IF NOT exists CoursePack (" +
			"id integer NOT NULL UNIQUE, " +
			"price double, " +
			"desc text, " +
			"name text, " +
			"ownerid integer, " +
			"pic text, " +
			"classNum integer, " +
			"realprice double, " +
			"viewCount integer " +
			")";

	private Context mContext;


	public LibOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_COURSE_PACK_TYPE);
		db.execSQL(CREATE_SLIDE_PIC);
		db.execSQL(CREATE_COURSE_PACK);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
