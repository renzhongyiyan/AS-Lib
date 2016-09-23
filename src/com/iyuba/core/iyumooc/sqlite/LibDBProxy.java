package com.iyuba.core.iyumooc.sqlite;

import android.database.sqlite.SQLiteDatabase;

/**
 * 作者：renzhy on 16/6/27 11:33
 * 邮箱：renzhongyigoo@gmail.com
 */
public class LibDBProxy {
	public LibOpenHelper helper;
	public SQLiteDatabase sqLiteDatabase;

	public LibDBProxy(){
		helper = new LibOpenHelper();
	}

	public void openDB(){
		sqLiteDatabase = helper.getWritableDatabase();
	}

	public void closeDB(){
		if(sqLiteDatabase != null && sqLiteDatabase.isOpen()){
			sqLiteDatabase.close();
		}
	}
}
