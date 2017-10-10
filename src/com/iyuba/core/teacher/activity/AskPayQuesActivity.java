package com.iyuba.core.teacher.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iyuba.core.common.activity.Login;
import com.iyuba.core.common.listener.OperateCallBack;
import com.iyuba.core.common.listener.ProtocolResponse;
import com.iyuba.core.common.listener.ResultIntCallBack;
import com.iyuba.core.common.manager.AccountManager;
import com.iyuba.core.common.protocol.BaseHttpResponse;
import com.iyuba.core.common.thread.UploadFile;
import com.iyuba.core.common.util.ExeProtocol;
import com.iyuba.core.common.util.NetWorkState;
import com.iyuba.core.common.util.TakePictureUtil;
import com.iyuba.core.common.util.TextAttr;
import com.iyuba.core.common.widget.ContextMenu;
import com.iyuba.core.common.widget.dialog.CustomDialog;
import com.iyuba.core.common.widget.dialog.CustomToast;
import com.iyuba.core.common.widget.dialog.WaittingDialog;
import com.iyuba.core.teacher.protocol.AskPayQuesRequest;
import com.iyuba.core.teacher.protocol.AskPayQuesResponse;
import com.iyuba.lib.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static com.iyuba.lib.R.id.question;

/**
 * 作者：renzhy on 17/6/25 17:27
 * 邮箱：renzhongyigoo@gmail.com
 */
public class AskPayQuesActivity extends Activity {
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final int QUESTION_REQUEST_TYPE = 4;
    public String size;
    int qAbilityType = -1;//问题的类型
    int qAppType = -1;
    String askuid = "";
    private Context mContext;
    private ContextMenu contextMenu;
    private LinearLayout llBack;
    //    private ImageView ivBack;
    private EditText quesDescText;
    private EditText quesRewardText;
    private ImageView photoPic;
    private Button btnAddPhoto;
    private TextView quesDescWordsNumTip;
    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = quesDescText.getSelectionStart();
            editEnd = quesDescText.getSelectionEnd();
            if (temp.length() > 140) {
                Toast.makeText(mContext, "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)
                        .show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                quesDescText.setText(s);
                quesDescText.setSelection(tempSelection);
            } else {
                int remainLen = 140 - temp.length();
                quesDescWordsNumTip.setText("剩余输入字数" + remainLen);
            }
        }
    };
    private LinearLayout ll_select_tag;
    private TextView tv_submit_ques;
    private boolean hasDiscPic = false;
    private CustomDialog cd;
    Handler handlerViewCtrl = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int jiFen = 0;
            super.handleMessage(msg);

            jiFen = msg.arg1;
            switch ( msg.what ) {
                case 0:
                    cd.dismiss();
                    break;
                case 1:
                    cd.show();
                    break;
                case 2:
                    CustomToast.showToast(mContext, "提交问题失败");
                    break;
                case 3:
                    CustomToast.showToast(mContext, "提交问题成功");
                    break;
                case 4:
                    CustomToast.showToast(mContext, "请输入您要提问的问题!");
                    break;
                case 5:
                    CustomToast.showToast(mContext, "悬赏金额超过当前余额!");
                    break;
                case 6:
                    CustomToast.showToast(mContext, "请输入悬赏金额，可以为0");
                    break;
                case 8:
                    CustomToast.showToast(mContext, "请选择问题标签");
                    break;
                case 9:
                    Toast.makeText(mContext, "提交问题成功+" + jiFen + "积分！", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };
    private String strQuestion;
    private String strReward;
    private double mIyubiAmount = 0;
    private int mReward;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_pay_ques);
        mContext = this;
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initWidget();
        findViews();
        setListeners();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initWidget() {
        Intent intent = getIntent();
        askuid = intent.getStringExtra("askuid");
        if (askuid == null) {
            askuid = "";
        }

        cd = WaittingDialog.showDialog(mContext);
    }

    public void findViews() {
        contextMenu = (ContextMenu) findViewById(R.id.context_menu);
        llBack = (LinearLayout) findViewById(R.id.ll_iv_back);
//        ivBack = (ImageView) findViewById(R.id.iv_back);
        ll_select_tag = (LinearLayout) findViewById(R.id.ll_select_tag);
        tv_submit_ques = (TextView) findViewById(R.id.title_submit);
        quesDescText = (EditText) findViewById(R.id.ques_text);
        quesRewardText = (EditText) findViewById(R.id.et_reward);
        quesDescWordsNumTip = (TextView) findViewById(R.id.ques_words_tip);
        photoPic = (ImageView) findViewById(R.id.iv_photo_pic);
        btnAddPhoto = (Button) findViewById(R.id.btn_add_photo);
    }

    public void setListeners() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        quesDescText.addTextChangedListener(mTextWatcher);
        ll_select_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(SelectPayQuesType.getIntent2Me(mContext), QUESTION_REQUEST_TYPE);
            }
        });

        tv_submit_ques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetWorkState.isConnectingToInternet()) {
                    handlerViewCtrl.sendEmptyMessage(2);
                } else {

                    //如果没登录则跳转登录
                    if (!AccountManager.Instace(mContext).checkUserLogin()) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, Login.class);
                        startActivity(intent);
                        return;
                    }
                    //如果选择了类型则可以提交，没有则提示没有选择类型
                    if (qAppType >= 0 && qAbilityType >= 0) {
                        askQuestion();
                    } else {
                        handlerViewCtrl.sendEmptyMessage(8);
                    }
                }
            }
        });

        photoPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContextMenu();
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContextMenu();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch ( requestCode ) {
            case PHOTO_REQUEST_TAKEPHOTO:
                startPhotoZoom(Uri.fromFile(new File(TakePictureUtil.photoPath)), 150);
                break;

            case PHOTO_REQUEST_GALLERY:
                if (data != null)
                    startPhotoZoom1(data.getData(), 150);
                break;

            case PHOTO_REQUEST_CUT:
                if (data != null) {
                    hasDiscPic = true;
                    setPicToView(data);
                }
                break;

            case QUESTION_REQUEST_TYPE:
                if (data != null) {
                    Bundle quesBundle = data.getExtras();
                    qAbilityType = quesBundle.getInt("qAbilityType", -1);
                    qAppType = quesBundle.getInt("qAppType", -1);
                }
                break;
        }

    }

    public void setContextMenu() {
        contextMenu.setText(mContext.getResources().getStringArray(
                R.array.choose_pic));
        contextMenu.setCallback(new ResultIntCallBack() {

            @Override
            public void setResult(int result) {
                Intent intent;
                switch ( result ) {
                    case 0:
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(TakePictureUtil.getPhotoFile(mContext)));

                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                        break;
                    case 1:
                        intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "image/*");
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                        break;
                    default:
                        break;
                }
                contextMenu.dismiss();
            }
        });
        contextMenu.show();
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
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(intent.getData(), proj, null, null, null);
        // 按我个人理解 这个是获得用户选择的图片的索引值
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        TakePictureUtil.photoPath = cursor.getString(column_index);
        Log.e("startPhotoZoom", TakePictureUtil.photoPath);

    }

    // 将进行剪裁后的图片显示到UI界面上
    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            //SaveImage.saveImage(tempFilePath, photo);
            photoPic.setBackgroundDrawable(drawable);
        }
    }

    public void askQuestion() {
        strQuestion = quesDescText.getText().toString();
        strReward = quesRewardText.getText().toString();

        if (strQuestion.trim().equals("") || strQuestion.trim().equals("")) {
            if (strQuestion.trim().equals("")) {
                //	question=TextAttr.encode(TextAttr.encode(TextAttr.encode(question)));
                handlerViewCtrl.sendEmptyMessage(4);
            }
            if (strReward.trim().equals("")) {
                handlerViewCtrl.sendEmptyMessage(6);
            }
        } else {
            mReward = Integer.parseInt(strReward);
            strQuestion = strQuestion.replaceAll("\'", "’");
            strQuestion = TextAttr.encode(TextAttr.encode(TextAttr.encode(strQuestion)));
            if (!hasDiscPic) {
                askQuestionWithoutFile();
            } else {
                askQuestionWithFile();
            }
        }
    }

    public void askQuestionWithoutFile() {
        new AskQuestionTask().execute();
    }

    public void askQuestionWithFile() {
        handlerViewCtrl.sendEmptyMessage(1);
        new UploadThread().start();
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 40, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 40;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private Bitmap getImageZoomed(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    class UploadThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                if (qAppType != 0) {
                    qAppType += 100;
                }
                String uri = "http://www.iyuba.com/question/askQuestion.jsp?"
                        + "&format=json" + "&uid=" + AccountManager.Instace(mContext).userId + "&username="
                        + AccountManager.Instace(mContext).userName + "&question=" + question + "&category1="
                        + qAbilityType + "&category2=" + qAppType;

                if (!askuid.equals("")) {
                    uri = uri + "&tuid=" + askuid;
                }
                Log.e("iyuba", uri);

                Bitmap bt = getImageZoomed(TakePictureUtil.photoPath);

//				Bitmap bt=BitmapFactory.decodeFile(TakePictureUtil.photoPath);
                File f = new File(TakePictureUtil.photoPath);
                FileInputStream fis = new FileInputStream(f);
                FileChannel fc = fis.getChannel();

//				Log.e("iyuba",fc.size()/1024+"------------"+100);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                int percent = 100;

                bt.compress(Bitmap.CompressFormat.JPEG, percent, stream);

                size = stream.size() / 1024 + "+++1++" + fc.size() / 1024;
                Log.e("iyuba", stream.size() / 1024 + "------------" + percent);

                //讲压缩后的文件保存在temp2下
//				String temp2=Constant.envir+"/temp2.jpg";
                String temp2 = TakePictureUtil.photoPath;
                FileOutputStream os = new FileOutputStream(temp2);
                os.write(stream.toByteArray());
                os.close();

                UploadFile.post(temp2, uri, new OperateCallBack() {

                    @Override
                    public void success(String message) {

                        handlerViewCtrl.sendEmptyMessage(0);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(message.substring(
                                    message.indexOf("{"), message.lastIndexOf("}") + 1));
                            if (jsonObject.getString("jiFen") != null
                                    && Integer.parseInt(jsonObject.getString("jiFen")) > 0) {
                                Message msg = new Message();
                                msg.what = 9;
                                msg.arg1 = Integer.parseInt(jsonObject.getString("jiFen"));
                                handlerViewCtrl.sendMessage(msg);
                            } else {
                                handlerViewCtrl.sendEmptyMessage(3);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void fail(String message) {
                        handlerViewCtrl.sendEmptyMessage(0);
                        handlerViewCtrl.sendEmptyMessage(2);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ;

    private class AskQuestionTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            handlerViewCtrl.sendEmptyMessage(1);
            if (qAppType != 0) {
                qAppType += 100;
            }
            ExeProtocol.exe(
                    new AskPayQuesRequest(AccountManager.Instace(mContext).userId + "", AccountManager.Instace(mContext).userName, strQuestion, qAbilityType, qAppType, askuid, mReward),
                    new ProtocolResponse() {

                        @Override
                        public void finish(BaseHttpResponse bhr) {
                            AskPayQuesResponse tr = (AskPayQuesResponse) bhr;
                            handlerViewCtrl.sendEmptyMessage(0);
                            if (!"0".equals(tr.result)) {
                                if (Integer.parseInt(tr.jiFen) > 0) {
                                    Message msg = new Message();
                                    msg.what = 9;
                                    msg.arg1 = Integer.parseInt(tr.jiFen);
                                    handlerViewCtrl.sendMessage(msg);
                                } else {
                                    handlerViewCtrl.sendEmptyMessage(3);
                                }
                            } else {
//                                handlerViewCtrl.sendEmptyMessage(2);
                                handlerViewCtrl.sendEmptyMessage(5);
                            }
                        }

                        @Override
                        public void error() {
                            handlerViewCtrl.sendEmptyMessage(0);
                            handlerViewCtrl.sendEmptyMessage(2);
                        }
                    });
            return null;
        }
    }
}
