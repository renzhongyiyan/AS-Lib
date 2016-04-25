package com.iyuba.core.common.base;

/**
 * 程序崩溃后操作
 * 
 * @version 1.0
 * 
 * @author 陈彤
 *	修改日期    2014.3.29
 */
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.iyuba.configation.RuntimeManager;

public class CrashApplication extends Application {
	private static CrashApplication mInstance = null;
	private List<Activity> activityList = new LinkedList<Activity>();

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		RuntimeManager.setApplicationContext(getApplicationContext());
		RuntimeManager.setApplication(this);
		mInstance = this;
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}

	// 程序加入运行列表
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 程序退出
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static CrashApplication getInstance() {
		return mInstance;
	}
}