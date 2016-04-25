/**
 * 
 */
package com.iyuba.core.me.activity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.iyuba.core.common.base.BasisActivity;
import com.iyuba.core.common.base.CrashApplication;
import com.iyuba.core.common.manager.AccountManager;
import com.iyuba.core.common.thread.GitHubImageLoader;
import com.iyuba.core.common.util.FileOpera;
import com.iyuba.core.common.util.SaveImage;
import com.iyuba.core.common.util.TakePictureUtil;
import com.iyuba.core.common.widget.dialog.CustomDialog;
import com.iyuba.core.common.widget.dialog.CustomToast;
import com.iyuba.core.common.widget.dialog.WaittingDialog;
import com.iyuba.lib.R;

/**
 * 修改头像
 * 
 * @author ct
 * @version 1.0
 * @para "regist" 是否来自注册 是则下一步补充详细信息
 */
public class UpLoadImage extends BasisActivity {
	private ImageView image;
	private Button upLoad, photo, local, skip, back;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	private boolean fromRegist = false;
	private Context mContext;
	private CustomDialog waitingDialog;
	private boolean isSend = true;
	private boolean isSelectPhoto = false;
	
	private StringBuffer sbCutPath;
	private String strCutPath;
	
	public  String size;
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.upload_image);
		CrashApplication.getInstance().addActivity(this);
		mContext = this;
		fromRegist = getIntent().getBooleanExtra("regist", fromRegist);
		waitingDialog = WaittingDialog.showDialog(mContext);
		initWidget();
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		image = (ImageView) findViewById(R.id.userImage);
		back = (Button) findViewById(R.id.upload_back_btn);
		upLoad = (Button) findViewById(R.id.upLoad);
		photo = (Button) findViewById(R.id.photo);
		local = (Button) findViewById(R.id.local);
		skip = (Button) findViewById(R.id.skip);
		
		GitHubImageLoader.Instace(mContext).setCirclePic(AccountManager.Instace(mContext).userId,
				image);
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		photo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent;
				intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(TakePictureUtil.getPhotoFile(mContext)));
				startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
			}
		});
		local.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 手机相册中选择
				Intent intent;
				intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						"image/*");
				startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
			}
		});
		upLoad.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isSelectPhoto){
					if (!isSend) {
						isSend = !isSend;
						handler.sendEmptyMessage(0);
						CustomToast.showToast(mContext,
								R.string.uploadimage_uploading);
						new uploadThread().start();
					} else {
						CustomToast.showToast(mContext, R.string.submitting);
					}
				}else{
					CustomToast.showToast(mContext, "请先选择图片！");
				}
				
			}
		});
		skip.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stud
				if (fromRegist) {
					Intent intent = new Intent(mContext,
							EditUserInfoActivity.class);
					intent.putExtra("regist", fromRegist);
					startActivity(intent);
				} else {
					onBackPressed();
				}
			}
		});

	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO:
			if (resultCode == RESULT_CANCELED) {
				return;
			}
			startPhotoZoom(Uri.fromFile(new File(TakePictureUtil.photoPath)), 200);
			break;

		case PHOTO_REQUEST_GALLERY:
			if (resultCode == RESULT_CANCELED) {
				return;
			}
			if (data != null)
				startPhotoZoom1(data.getData(), 200);
			break;

		case PHOTO_REQUEST_CUT:
			if (resultCode == RESULT_CANCELED) {
				return;
			}
			if (data != null) {
				isSend = false;
				isSelectPhoto = true;
				GitHubImageLoader.Instace(mContext).clearCache();
				setPicToView(data);
				
				Bundle extras = data.getExtras();
				Bitmap photo = extras.getParcelable("data");

				if(TakePictureUtil.photoCutPath != null && !TakePictureUtil.photoCutPath.equals("")){
					SaveImage.saveImage(TakePictureUtil.photoCutPath, photo);
				}else if(TakePictureUtil.photoPath != null && !TakePictureUtil.photoPath.equals("")){
					sbCutPath = new StringBuffer(TakePictureUtil.photoPath);
					sbCutPath.insert(TakePictureUtil.photoPath.length() - 4, "cut");
					TakePictureUtil.photoCutPath = sbCutPath.toString();
					SaveImage.saveImage(TakePictureUtil.photoCutPath, photo);
				}else{
					return;
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);
		Log.e("startPhotoZoom",uri.toString());

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	private void startPhotoZoom1(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
		
		Bitmap photo = intent.getExtras().getParcelable("data");
		String[] proj = { MediaColumns.DATA };
		Cursor cursor = managedQuery(intent.getData(), proj, null, null, null);
		// 按我个人理解 这个是获得用户选择的图片的索引值
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		
		TakePictureUtil.photoPath = cursor.getString(column_index);
		Log.e("startPhotoZoom1",TakePictureUtil.photoPath);

	}

	// 将进行剪裁后的图片显示到UI界面上
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			Bitmap photo = bundle.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			//SaveImage.saveImage(tempFilePath, photo);
//			GitHubImageLoader.Instace(mContext).setCirclePic(AccountManager.Instace(mContext).userId,
//					image);
			image.setImageDrawable(drawable);
		}
	}
	
	
	private void showDialog(String mess, OnClickListener ocl) {
		new AlertDialog.Builder(UpLoadImage.this)
				.setTitle(R.string.alert_title).setMessage(mess)
				.setNegativeButton(R.string.alert_btn_ok, ocl).show();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int jiFen = 0;
			String picUrl = "";
			String fileUrl = "";
			super.handleMessage(msg);
			jiFen = msg.arg1;
			switch (msg.what) {
			case 0:
				waitingDialog.show();
				break;
			case 1:
				waitingDialog.dismiss();
				break;
			case 2:
				isSend = false;
				picUrl = "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid="
						+ AccountManager.Instace(mContext).userId
						+ "&size=middle";
				fileUrl = TakePictureUtil.photoCutPath;
				new FileOpera(mContext).deleteFile(fileUrl);
				GitHubImageLoader.Instace(mContext).clearCache();
				showDialog(
						getResources().getString(R.string.uploadimage_success),
						new OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (fromRegist) {
									Intent intent = new Intent(mContext,
											EditUserInfoActivity.class);
									intent.putExtra("regist", fromRegist);
									startActivity(intent);
								} else {
									onBackPressed();
								}
							}
						});
				break;
			case 3:
				isSend = false;
				showDialog(
						getResources().getString(
								R.string.uploadimage_failupload),
						new OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				waitingDialog.dismiss();
				break;
			case 4:
				isSend = false;
				picUrl = "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid="
						+ AccountManager.Instace(mContext).userId
						+ "&size=middle";
				fileUrl = TakePictureUtil.photoCutPath;
				new FileOpera(mContext).deleteFile(fileUrl);
				GitHubImageLoader.Instace(mContext).clearCache();
				showDialog(
						getResources().getString(R.string.uploadimage_success)+"+"+jiFen+"积分！",
						new OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (fromRegist) {
									Intent intent = new Intent(mContext,
											EditUserInfoActivity.class);
									intent.putExtra("regist", fromRegist);
									startActivity(intent);
								} else {
									onBackPressed();
								}
							}
						});
				break;
			default:
				break;
			}
		}
	};

	class uploadThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			uploadFile();
		}
	};
	
	String success;
	String failure;
	
	// 上传头像、文件到服务器上
		private void uploadFile() {
			String end = "\r\n";
			String twoHyphens = "--";
			String boundary = "*****";
			try {
				URL url =new URL("http://api.iyuba.com.cn/v2/avatar?uid=" 
						+ AccountManager.Instace(mContext).userId);
				
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				/* 允许Input、Output，不使用Cache */
				con.setDoInput(true);
				con.setDoOutput(true);
				con.setUseCaches(false);
				/* 设定传送的method=POST */
				con.setRequestMethod("POST");
				/* setRequestProperty */
				con.setRequestProperty("Connection", "Keep-Alive");
				con.setRequestProperty("Charset", "UTF-8");
				con.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				// con.setRequestProperty("iyu_describe",
				// URLEncoder.encode(mood_content.getText().toString(),"utf-8"));
				/* 设定DataOutputStream */
				DataOutputStream ds = new DataOutputStream(con.getOutputStream());
				File files = new File(TakePictureUtil.photoCutPath);
//				File files = new File(TakePictureUtil.photoPath);
				ds.writeBytes(twoHyphens + boundary + end);
				ds.writeBytes("Content-Disposition: form-data; " + "name=\""
						+ files + "\"" + ";filename=\""
						+ System.currentTimeMillis() + ".jpg\"" + end);
				ds.writeBytes(end);
				/* 取得文件的FileInputStream */
				
				Bitmap bmp = BitmapFactory.decodeFile(TakePictureUtil.photoCutPath);
//				Bitmap bmp = BitmapFactory.decodeFile(TakePictureUtil.photoPath);
				
//				Bitmap bmp = getImageZoomed(TakePictureUtil.photoPath);
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0
				Log.e("iyuba", stream.toByteArray().length / 1024
						+ "stream------------------------");
				
				String temp2 = TakePictureUtil.photoCutPath;
//				String temp2 = TakePictureUtil.photoPath;
				FileOutputStream os = new FileOutputStream(temp2);
				os.write(stream.toByteArray());
				os.close();
				
				FileInputStream fStream = new FileInputStream(temp2);
				/* 设定每次写入1024bytes */
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];
				int length = -1;
				/* 从文件读取数据到缓冲区 */
				while ((length = fStream.read(buffer)) != -1) { /* 将数据写入DataOutputStream中 */
					ds.write(buffer, 0, length);
				}
				ds.writeBytes(end);
				ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
				/* close streams */
				fStream.close();
				ds.flush();
				
				/* 取得Response内容 */
				InputStream is = con.getInputStream();
				int ch;
				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
				/* 将Response显示于Dialog */
				success = b.toString().trim();
				JSONObject jsonObject = new JSONObject(success.substring(
						success.indexOf("{"), success.lastIndexOf("}") + 1));
				System.out.println("cc=====" + jsonObject.getString("status"));
				if (jsonObject.getString("status").equals("0")) {// status 为0则修改成功
					try {
						if(jsonObject.getString("jiFen") != null
								&& Integer.parseInt(jsonObject.getString("jiFen")) > 0){
							Message msg = new Message();
							msg.what = 4;
							msg.arg1 = Integer.parseInt(jsonObject.getString("jiFen"));
							handler.sendMessage(msg);
						}else{
							handler.sendEmptyMessage(2);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finish();
				} else {
					handler.sendEmptyMessage(3);
				}
				/* 关闭DataOutputStream */
				ds.close();
			} catch (Exception e) {
				e.printStackTrace();
				failure = e.getMessage();
			}
		}

	@Override
	protected void onResume() {
		super.onResume();
		//MobclickAgent.onResume(this);
	}
}
