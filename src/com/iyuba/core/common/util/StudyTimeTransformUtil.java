package com.iyuba.core.common.util;

import android.util.Log;

import com.iyuba.core.common.sqlite.mode.FileInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

/****
 * 
 * 
 * 学习时长转换相关类
 * 
 * 
 * 
 */
public class StudyTimeTransformUtil {


	public static String getFormat(String totalTime) {

		int seconds = Integer.parseInt(totalTime);
		int hours = 0,minutes = 0;
		StringBuilder formatTime = new StringBuilder();

		if (seconds > 60) {
			minutes = seconds / 60;
			seconds = seconds % 60;
		}
		if (minutes > 60) {
			hours = minutes / 60;
			minutes = minutes % 60;
		}

		if(hours > 0){
			formatTime.append(hours+"小时");
		}
		if(minutes > 0){
			formatTime.append(minutes+"分");
		}
		if(seconds > 0){
			formatTime.append(seconds+"秒");
		}
		return formatTime.toString();
	}
}
