package com.iyuba.core.teacher.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.roundview.RoundTextView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.iyuba.configation.Constant;
import com.iyuba.core.common.activity.Login;
import com.iyuba.core.common.activity.Web;
import com.iyuba.core.common.listener.ProtocolResponse;
import com.iyuba.core.common.manager.AccountManager;
import com.iyuba.core.common.manager.QuestionManager;
import com.iyuba.core.common.network.ClientSession;
import com.iyuba.core.common.network.IErrorReceiver;
import com.iyuba.core.common.network.IResponseReceiver;
import com.iyuba.core.common.protocol.BaseHttpRequest;
import com.iyuba.core.common.protocol.BaseHttpResponse;
import com.iyuba.core.common.protocol.ErrorResponse;
import com.iyuba.core.common.thread.GitHubImageLoader;
import com.iyuba.core.common.util.ExeProtocol;
import com.iyuba.core.common.util.TimestampConverter;
import com.iyuba.core.common.widget.dialog.CustomDialog;
import com.iyuba.core.common.widget.dialog.CustomToast;
import com.iyuba.core.iyulive.util.MD5;
import com.iyuba.core.iyumooc.teacher.API.CheckQuesPayedApi;
import com.iyuba.core.iyumooc.teacher.API.GetAgreeListApi;
import com.iyuba.core.iyumooc.teacher.API.GetAnswerListApi;
import com.iyuba.core.iyumooc.teacher.API.GetCommentListApi;
import com.iyuba.core.iyumooc.teacher.API.PayForQuestionApi;
import com.iyuba.core.iyumooc.teacher.adapter.AnswerRVAdapter;
import com.iyuba.core.iyumooc.teacher.adapter.CommentRVAdapter;
import com.iyuba.core.iyumooc.teacher.adapter.PraiseRVAdapter;
import com.iyuba.core.iyumooc.teacher.bean.AgreeListBean;
import com.iyuba.core.iyumooc.teacher.bean.AnswerListBean;
import com.iyuba.core.iyumooc.teacher.bean.CommentListBean;
import com.iyuba.core.iyumooc.teacher.bean.PayQuestionListBean;
import com.iyuba.core.iyumooc.teacher.bean.PayResultBean;
import com.iyuba.core.iyumooc.teacher.bean.QuesPayedRecordBean;
import com.iyuba.core.microclass.protocol.PayCourseAmountRequest;
import com.iyuba.core.microclass.protocol.PayCourseAmountResponse;
import com.iyuba.core.microclass.sqlite.mode.CourseContent;
import com.iyuba.core.teacher.protocol.AnswerQuesRequest;
import com.iyuba.core.teacher.protocol.AnswerQuesResponse;
import com.iyuba.lib.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.SimpleXmlConverterFactory;

import static com.iyuba.lib.R.id.button_express;

/**
 * 作者：renzhy on 17/7/8 11:11
 * 邮箱：renzhongyigoo@gmail.com
 */
public class PayQuesDetailActivity extends Activity implements ObservableScrollViewCallbacks {
    private static final String TAG = "PayQuesDetailActivity";

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    private static final String AGREE_REQUEST_TYPE = "questionid";
    private static final String FORMAT_JSON = "json";
    private static final String COMMENT_AUTHOR_TYPE = "2";
    private static final String CHECK_QUESTION_PAYED_ID = "1000";

    private Context mContext;
    private CustomDialog waitDialog;

    private LinearLayout llBack;
//    private SwipeRefreshLayout mSwipeRefreshLayout;

    //"悬赏"类型问题组件
    public ImageView userImage;
    public TextView userName;
    public TextView quesPrice;
    public TextView quesContent;
    public ImageView quesImage;
    public TextView quesAppType;
    public TextView quesAbilityType;
    public TextView commentNum;
    public TextView agreeNum;
    public TextView leftTime;
    public Button quickAnswer;
    private ObservableScrollView observableScrollView;
    //点赞部分组件
    private PraiseRVAdapter praiseAdapter;
    private RecyclerView mPraiseRecyclerView;
    private TextView tvPraiseNum;
    //教师回答部分组件
    private AnswerRVAdapter answerAdapter;
    private RecyclerView mAnswerRecyclerView;
    private TextView tvAnswerNum;
    //评论部分组件
    private CommentRVAdapter commentAdapter;
    private RecyclerView mCommentRecyclerView;
    private TextView tvCommentNum;

    //底部回复框组件
    private View llReplyModeSelect;
    private View llReplyContent;
    private RoundTextView rtvAnswer;
    private RoundTextView rtvComment;

    private EditText etComment;
    private RoundTextView rtvSubmit;

    private String uid;
    private String username;
    private int qid;
    private String theQid;
    private String quesVip;
    private String mPraiseTotal;
    private String mAnswerTotal;
    private String mCommentTotal;
    private boolean mAnswerPayed = false;
    private String UserAmount;
    private int curCourseCost;

    private boolean isAgreeListLast = false;
    private int agreeListPageNum = 1;
    private int agreeListPageCount = 4;

    private static String shareQuestionTitleUrl;
    private static String shareCourseTitleImageUrl;

    private AnswerListBean.QuestionBean questionBean = new AnswerListBean.QuestionBean();
    private ArrayList<AgreeListBean.AgreeDataBean> agreeList = new ArrayList<>();
    private ArrayList<AnswerListBean.AnswersBean> answerList = new ArrayList<>();
    private ArrayList<CommentListBean.AnswersBean> commentList = new ArrayList<>();

    public HashMap<String ,String> abilityTypeCatalog=new HashMap<>();
    public HashMap<String ,String> appTypeCatalog=new HashMap<>();


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            int jiFen = 0;
            super.handleMessage(msg);
            jiFen = msg.arg1;
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    tvPraiseNum.setText(mPraiseTotal);
                    break;
                case 2:
                    waitDialog.show();
                    break;
                case 3:
                    waitDialog.dismiss();
                    break;
                case 4:
//                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 5:
                    CustomToast.showToast(mContext, "已经是最新数据了哦~");
//                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 6:
                    CustomToast.showToast(mContext, "暂时没有数据,下拉刷新数据！");
//                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 7:
                    CustomToast.showToast(mContext, "已是最后一页");
                    break;
                case 8:
                    CustomToast.showToast(mContext, "删除成功!");
                    break;
                case 9:
                    CustomToast.showToast(mContext, "请检查网络连接！");
                    break;
                case 10:
                    tvCommentNum.setText(mContext.getString(R.string.ques_detail_comment_num,mCommentTotal));
                    break;
                case 11:
                    tvAnswerNum.setText(mContext.getString(R.string.ques_detail_answer_num,mAnswerTotal));
                    break;
                case 12:
                    initQuestion();
                case 13:
                    Toast.makeText(mContext, "提交成功+"+jiFen+"积分！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    Handler handlerBuy = new Handler() {
        int curNeedCost;
        int userAmount;

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 0:
                    UserAmount = AccountManager.Instace(mContext).userInfo.iyubi;
                    //余额可用于支付
                    if (Integer.parseInt(UserAmount) >= curCourseCost) {
                        handlerBuy.sendEmptyMessage(2);
                    } else if (Integer.parseInt(UserAmount) < curCourseCost) {
                        Message msg2 = new Message();
                        msg2.arg1 = curCourseCost;
                        msg2.arg2 = Integer.parseInt(UserAmount);
                        msg2.what = 3;
                        handlerBuy.sendMessage(msg2);
                    } else {
                        handlerBuy.sendEmptyMessage(7);
                    }
                    break;
                case 1:
                    break;
                case 2:
                    payForQuestion();
                    break;
                case 3:
                    curNeedCost = msg.arg1;
                    userAmount = msg.arg2;
                    Dialog dialog1 = new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("提示")
                            .setMessage("当前购买需要支付" + curNeedCost + "爱语币,您的余额为" + userAmount + ",不足以支付，是否充值？")
                            .setPositiveButton("充值",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int whichButton) {
                                            Intent intent = new Intent();
                                            intent.setClass(mContext, Web.class);
                                            intent.putExtra(
                                                    "url",
                                                    "http://app.iyuba.com/wap/index.jsp?uid="
                                                            + AccountManager
                                                            .Instace(mContext).userId);
                                            intent.putExtra("title", "购买爱语币");
                                            mContext.startActivity(intent);
                                        }
                                    }).setNeutralButton("取消", null).create();
                    dialog1.show();// 如果要显示对话框，一定要加上这句
                    break;
                case 4:
                    Toast.makeText(mContext, "支付成功!", Toast.LENGTH_SHORT).show();
                    mAnswerPayed = true;
                    break;
                case 5:
                    Toast.makeText(mContext, "您已购买本课程", Toast.LENGTH_SHORT).show();
                case 6:
                    break;
                case 7:
                    Toast.makeText(mContext, "购买错误，请稍后再试！", Toast.LENGTH_SHORT).show();
                    mAnswerPayed = false;
                case 8:
                    answerAdapter.setAnswerPayed(mAnswerPayed);
                    break;
                case 9:
                    break;
                default:
                    break;
            }
        }
    };

    Handler handlerComment = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch ( msg.what ){
                case 0:
                    break;
                case 1:
                    CustomToast.showToast(mContext, R.string.send_success);
                    rtvSubmit.setEnabled(true);
                    etComment.setText("");
                    break;
                case 2:
                    CustomToast.showToast(mContext, R.string.send_fail);
                    rtvSubmit.setEnabled(true);
                    break;
                case 3:
                    etComment.setText("");
                    break;
                case 4:
                    String text = (String) msg.obj;
                    etComment.setText(text);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_ques_detail);
        mContext = this;
        initData();
        initWidget();
        findViews();
        setListeners();
        setViews();

        checkQuestionPayed();
        getAgreeList();
        getAnswerList();
        getCommentList();
    }

    public void initWidget(){
        waitDialog = new CustomDialog(mContext);
        praiseAdapter = new PraiseRVAdapter(mContext);
        answerAdapter = new AnswerRVAdapter(mContext);
    }

    public void findViews(){
        //标题栏和问题内容组件
        llBack = (LinearLayout) findViewById(R.id.ll_iv_back);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_ques_detail);
        userImage = (ImageView) findViewById(R.id.iv_user_image);
        userName = (TextView) findViewById(R.id.tv_user_name);
        quesPrice = (TextView) findViewById(R.id.tv_question_price);
        quesContent = (TextView) findViewById(R.id.tv_question_content);
        quesImage = (ImageView) findViewById(R.id.ques_image);
        quesAppType = (TextView) findViewById(R.id.tv_ques_app_type);
        quesAbilityType = (TextView) findViewById(R.id.tv_ques_ability_type);
        commentNum = (TextView) findViewById(R.id.comment_num);
        agreeNum = (TextView) findViewById(R.id.agree_num);
        leftTime = (TextView) findViewById(R.id.tv_left_time);
        quickAnswer = (Button) findViewById(R.id.btn_quick_answer);
        observableScrollView = (ObservableScrollView) findViewById(R.id.observable_sv_ques_detail);
        //点赞布局组件
        mPraiseRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_praise_user);
        tvPraiseNum = (TextView) findViewById(R.id.tv_praise_num);

        //教师回答布局组件
        mAnswerRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_detail_answer);
        tvAnswerNum = (TextView) findViewById(R.id.tv_answer_num);

        //评论布局组件
        mCommentRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_detail_comment);
        tvCommentNum = (TextView) findViewById(R.id.tv_comment_num);

        //回复组件
        llReplyModeSelect = findViewById(R.id.ll_reply_mode_to_user);
        llReplyContent = findViewById(R.id.ll_reply_content_to_user);
        rtvAnswer = (RoundTextView) findViewById(R.id.rtv_answer);
        rtvComment = (RoundTextView) findViewById(R.id.rtv_comment);
        etComment = (EditText) findViewById(R.id.et_reply_input);
        rtvSubmit = (RoundTextView) findViewById(R.id.rtv_submit);
    }

    public void setViews(){

        observableScrollView.setScrollViewCallbacks(this);
        observableScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScrollChanged(observableScrollView.getScrollY(), true, true);
            }
        });

        //点赞列表
        // 确定每个item的排列方式
        LinearLayoutManager praiseLayoutManager = new LinearLayoutManager(mContext) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                // 这里要复写一下，因为默认宽高都是wrap_content
                // 这个不复写，你点击的背景色就只充满你的内容
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        praiseLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mPraiseRecyclerView.setLayoutManager(praiseLayoutManager);
        mPraiseRecyclerView.setHasFixedSize(true);
        praiseAdapter = new PraiseRVAdapter(mContext);
        praiseAdapter.clearList();
        mPraiseRecyclerView.setAdapter(praiseAdapter);

        //回答列表
        // 确定每个item的排列方式
        LinearLayoutManager answerLayoutManager = new LinearLayoutManager(mContext) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                // 这里要复写一下，因为默认宽高都是wrap_content
                // 这个不复写，你点击的背景色就只充满你的内容
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        answerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mAnswerRecyclerView.setLayoutManager(answerLayoutManager);
        mAnswerRecyclerView.setHasFixedSize(true);
        answerAdapter = new AnswerRVAdapter(mContext);
        answerAdapter.setOnAnswerCoverClickListener(onAnswerCoverClickListener);
        answerAdapter.clearList();
        mAnswerRecyclerView.setAdapter(answerAdapter);

        //评论列表
        // 确定每个item的排列方式
        LinearLayoutManager commentLayoutManager = new LinearLayoutManager(mContext) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                // 这里要复写一下，因为默认宽高都是wrap_content
                // 这个不复写，你点击的背景色就只充满你的内容
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        commentLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mCommentRecyclerView.setLayoutManager(commentLayoutManager);
        mCommentRecyclerView.setHasFixedSize(true);
        commentAdapter = new CommentRVAdapter(mContext);
        commentAdapter.clearList();
        mCommentRecyclerView.setAdapter(commentAdapter);

        rtvAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llReplyModeSelect.setVisibility(View.GONE);
                llReplyContent.setVisibility(View.VISIBLE);
            }
        });

        rtvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llReplyModeSelect.setVisibility(View.GONE);
                llReplyContent.setVisibility(View.VISIBLE);
            }
        });

        rtvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccountManager.Instace(mContext).checkUserLogin()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            rtvComment.getWindowToken(), 0);
                    String expressionInput = etComment.getText()
                            .toString();
                    if (expressionInput.toString().trim().equals("")) {
                        CustomToast.showToast(mContext, "请输入评论！");
                        etComment.setText("");
                    } else {
                        if (expressionInput.toString().trim().length() > 100) {
                            CustomToast.showToast(mContext,
                                    "最多只能输入100字，已超啦!");
                            return;
                        }
                        answerQuestion(expressionInput.toString().toString(), 2);
                        etComment.setText("");
                        rtvSubmit.setEnabled(false);
                    }
                }else {
                    Intent intent = new Intent();
                    intent.setClass(mContext, Login.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void setListeners(){
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void initData() {
        if (AccountManager.Instace(mContext).userId == null) {
            uid = "0";
        }
        uid = AccountManager.Instace(mContext).userId;
        username = AccountManager.Instace(mContext).userName;
        Intent intent = getIntent();
        theQid = intent.getStringExtra("qid");
        quesVip = intent.getStringExtra("vip");
        if (theQid == null)
            theQid = "1";
        qid = Integer.parseInt(theQid);

        setAbilityTypeCatalog();
        setAppTypeCatalog();

        shareQuestionTitleUrl = "http://m.iyuba.com/teacher/qdetail.jsp?qid="+qid;
        shareCourseTitleImageUrl = "http://app.iyuba.com/android/images/iyuba/iyuba.png";
    }

    public void initQuestion() {

        GitHubImageLoader.Instace(mContext)
                .setCirclePic(questionBean.getUid(), userImage);
        userName.setText(questionBean.getUsername());

        quesPrice.setText(questionBean.getPrice());
        quesContent.setText(questionBean.getQuestion());

        if (questionBean.getImg().equals("")) {
            quesImage.setVisibility(View.GONE);
        } else {
            quesImage.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage("http://www.iyuba.com/question/" + questionBean.getImg(),
                    quesImage);

        }

        if (questionBean.getCreatetime() == null || "null".equals(questionBean.getCreatetime())) {
            questionBean.setCreatetime("");
        }

        if (questionBean.getLocation() == null || "null".equals(questionBean.getLocation())) {
            questionBean.setLocation("");
        }
        questionBean.setCreatetime(questionBean.getCreatetime().substring(0,19));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long time = sdf.parse(questionBean.getCreatetime()).getTime();
            leftTime.setText(formatTime(time));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        quesAppType.setText(appTypeCatalog.get(questionBean.getCategory2()));
        quesAbilityType.setText(abilityTypeCatalog.get(questionBean.getCategory1()));

        commentNum.setText(questionBean.getCommentcount());
        agreeNum.setText(questionBean.getAgreecount());
    }

    private Handler binderPraiseAdapterHandler = new Handler();
    private Runnable binderPraiseAdapterRunnable = new Runnable() {
        public void run() {
            praiseAdapter.clearList();
            praiseAdapter.addList(agreeList);
            praiseAdapter.notifyDataSetChanged();
            handler.sendEmptyMessage(3);
            handler.sendEmptyMessage(1);
        }
    };

    private Handler binderAnswerAdapterHandler = new Handler();
    private Runnable binderAnswerAdapterRunnable = new Runnable() {
        public void run() {
            answerAdapter.clearList();
            answerAdapter.addList(answerList);
            answerAdapter.notifyDataSetChanged();
            handler.sendEmptyMessage(3);
            handler.sendEmptyMessage(11);
        }
    };

    private Handler binderCommentAdapterHandler = new Handler();
    private Runnable binderCommentAdapterRunnable = new Runnable() {
        public void run() {
            commentAdapter.clearList();
            commentAdapter.addList(commentList);
            commentAdapter.notifyDataSetChanged();
            handler.sendEmptyMessage(3);
            handler.sendEmptyMessage(10);
        }
    };

    AnswerRVAdapter.OnAnswerCoverClickListener onAnswerCoverClickListener = new AnswerRVAdapter.OnAnswerCoverClickListener() {
        @Override
        public void onItemClick(View view) {

            Dialog dialog = new AlertDialog.Builder(mContext)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("温馨提示")
                    .setMessage(
                            "该问题是付费问题，您只用支付提问花费的10%(即爱语币:"+ curCourseCost +"个)" +
                                    "就可以查看该问题所有老师答案哦")
                    .setPositiveButton("去支付",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    handlerBuy.sendEmptyMessage(0);
                                }
                            }).setNeutralButton("取消", null).create();
            dialog.show();// 如果要显示对话框，一定要加上这句
        }
    };

    public void checkQuestionPayed(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
                .baseUrl("http://app.iyuba.com/")
                .client(client.build())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        CheckQuesPayedApi apiStores = retrofit.create(CheckQuesPayedApi.class);
        Call<QuesPayedRecordBean> call = apiStores.checkQuestionPayed(
                AccountManager.Instace(mContext).userId,
                Constant.APPID,
                CHECK_QUESTION_PAYED_ID,
                theQid,
                MD5.getMD5ofStr(Constant.APPID + AccountManager.Instace(mContext).userId + CHECK_QUESTION_PAYED_ID
                    + theQid + "apiGetPayQuestionRecord" + TimestampConverter.convertTimestamp()));
        call.enqueue(new Callback<QuesPayedRecordBean>() {
            @Override
            public void onResponse(Response<QuesPayedRecordBean> response) {

                if(response.body().result != null && !response.body().result.equals("")){
                    if(response.body().result.equals("1")){
                        mAnswerPayed = true;
                    }else {
                        mAnswerPayed = false;
                    }
                    answerAdapter.setAnswerPayed(mAnswerPayed);
                }else {
                    mAnswerPayed = false;
                    answerAdapter.setAnswerPayed(mAnswerPayed);
                }

            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null) {
                }
            }
        });

    }

    public void payForQuestion(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
                .baseUrl("http://app.iyuba.com/")
                .client(client.build())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        PayForQuestionApi apiStores = retrofit.create(PayForQuestionApi.class);
        Call<PayResultBean> call = apiStores.payForQuestion(
                AccountManager.Instace(mContext).userId,
                Constant.APPID,
                curCourseCost+"",
                CHECK_QUESTION_PAYED_ID,
                theQid,
                MD5.getMD5ofStr(curCourseCost+Constant.APPID + AccountManager.Instace(mContext).userId + CHECK_QUESTION_PAYED_ID
                        + theQid + "payQuestionApi" + TimestampConverter.convertTimestamp()));
        call.enqueue(new Callback<PayResultBean>() {
            @Override
            public void onResponse(Response<PayResultBean> response) {

                if(response.body().result != null && !response.body().result.equals("")){
                    if(response.body().result.equals("1")){
                        mAnswerPayed = true;
                        handlerBuy.sendEmptyMessage(4);
                        handlerBuy.sendEmptyMessage(8);
                    }else {
                        mAnswerPayed = false;
                        handlerBuy.sendEmptyMessage(7);
                    }
                    answerAdapter.setAnswerPayed(mAnswerPayed);


                }else {
                    mAnswerPayed = false;
                    answerAdapter.setAnswerPayed(mAnswerPayed);
                    handlerBuy.sendEmptyMessage(7);
                }

            }
            @Override
            public void onFailure(Throwable t) {
                if (t != null) {

                }
            }
        });

    }

    public void getAgreeList(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
                .baseUrl("http://www.iyuba.com/")
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetAgreeListApi apiStores = retrofit.create(GetAgreeListApi.class);
        Call<AgreeListBean> call = apiStores.getAgreeList(AGREE_REQUEST_TYPE,theQid,FORMAT_JSON,agreeListPageNum,agreeListPageCount);
        call.enqueue(new Callback<AgreeListBean>() {
            @Override
            public void onResponse(Response<AgreeListBean> response) {
                if(response.body().getData() != null && response.body().getData().size() != 0){

                    agreeList.clear();
                    agreeList.addAll(response.body().getData());
                    binderPraiseAdapterHandler.post(binderPraiseAdapterRunnable);
                    agreeListPageNum++;
                    handler.sendEmptyMessage(3);
                    handler.sendEmptyMessage(4);
                    if(response.body().getData().size() < 4){
                        isAgreeListLast = true;
                    }else {
                        isAgreeListLast = false;
                    }
                    mPraiseTotal = response.body().getTotal();
                }else {
                    mPraiseTotal = "0";
                    handler.sendEmptyMessage(4);
                    handler.sendEmptyMessage(3);
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null) {
                }
            }
        });
    }

    public void getAnswerList(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
                .baseUrl("http://www.iyuba.com/")
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetAnswerListApi apiStores = retrofit.create(GetAnswerListApi.class);
        Call<AnswerListBean> call = apiStores.getAnswerList(FORMAT_JSON,theQid);
        call.enqueue(new Callback<AnswerListBean>() {
            @Override
            public void onResponse(Response<AnswerListBean> response) {
                if(response.body().getAnswers() != null && response.body().getAnswers().size() != 0){

                    questionBean = response.body().getQuestion();
                    QuestionManager.getInstance().questionBean = questionBean;
                    curCourseCost = Integer.parseInt(questionBean.getPrice()) / 10;

                    answerList.clear();
                    answerList.addAll(response.body().getAnswers());

                    binderAnswerAdapterHandler.post(binderAnswerAdapterRunnable);
                    handler.sendEmptyMessage(3);
                    handler.sendEmptyMessage(4);
                    handler.sendEmptyMessage(12);

                    mAnswerTotal = response.body().getTotal();
                }else {
                    mAnswerTotal = "0";
                    handler.sendEmptyMessage(4);
                    handler.sendEmptyMessage(3);
                    handler.sendEmptyMessage(11);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null) {
                }
            }
        });
    }

    public void getCommentList(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
                .baseUrl("http://www.iyuba.com/")
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetCommentListApi apiStores = retrofit.create(GetCommentListApi.class);
        Call<CommentListBean> call = apiStores.getCommentList(FORMAT_JSON,COMMENT_AUTHOR_TYPE,theQid);
        call.enqueue(new Callback<CommentListBean>() {
            @Override
            public void onResponse(Response<CommentListBean> response) {
                if(response.body().getAnswers() != null && response.body().getAnswers().size() != 0){

                    commentList.clear();
                    commentList.addAll(response.body().getAnswers());

                    binderCommentAdapterHandler.post(binderCommentAdapterRunnable);
                    handler.sendEmptyMessage(3);
                    handler.sendEmptyMessage(4);

                    mCommentTotal = response.body().getTotal();
                }else {
                    mCommentTotal = "0";
                    handler.sendEmptyMessage(4);
                    handler.sendEmptyMessage(3);
                    handler.sendEmptyMessage(10);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null) {
                }
            }
        });
    }

    public void answerQuestion(final String answer, final int userType) {

        username = AccountManager.Instace(mContext).userName;

        ClientSession.Instace().asynGetResponse(
                new AnswerQuesRequest(AccountManager.Instace(mContext).userId
                        + "", AccountManager.Instace(mContext).userName,
                        userType, qid, answer),
                new IResponseReceiver() {
                    @Override
                    public void onResponse(BaseHttpResponse response, BaseHttpRequest request, int rspCookie) {
                        AnswerQuesResponse tr = (AnswerQuesResponse) response;
                        if ("1".equals(tr.result) || "0".equals(tr.result)) {
                            if(tr.jiFen != null && Integer.parseInt(tr.jiFen) > 0){
                                Message msg = new Message();
                                msg.what = 13;
                                msg.arg1 = Integer.parseInt(tr.jiFen);
                                handler.sendMessage(msg);
                            }else{
                                handlerComment.sendEmptyMessage(1);
                            }
                            if (userType == 1) {
//                                new GetChatDataTask().execute();
                                getAnswerList();
                            } else {
//                                new GetCommentDataTask().execute();
                                getCommentList();
                            }
                        } else {
                            handlerComment.sendEmptyMessage(2);
                            Message msg = handlerComment.obtainMessage();
                            msg.obj = answer;
                            msg.what = 4;
                            handlerComment.sendMessage(msg);
                        }
                    }
                },
                new IErrorReceiver() {
                    @Override
                    public void onError(ErrorResponse errorResponse, BaseHttpRequest request, int rspCookie) {
                        if(errorResponse != null){
                            handlerComment.sendEmptyMessage(2);
                            Message msg = handlerComment.obtainMessage();
                            msg.obj = answer;
                            msg.what = 4;
                            handlerComment.sendMessage(msg);
                        }
                    }
                });
    }

    private String formatTime(long time) {
        Date date = new Date(time);
        Date date2 = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar.setTime(date);
        calendar2.setTime(date2);

        if (calendar2.get(Calendar.DAY_OF_YEAR)
                - calendar.get(Calendar.DAY_OF_YEAR) > 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(date);
        } else if (System.currentTimeMillis() - time > 60 * 60 * 1000) {
            return (System.currentTimeMillis() - time) / (60 * 60 * 1000)
                    + "小时之前";
        } else if (System.currentTimeMillis() - time > 60 * 1000) {
            return (System.currentTimeMillis() - time) / (60 * 1000) + "分钟之前";
        } else if (System.currentTimeMillis() - time > 60) {

            return (System.currentTimeMillis() - time) / (1000) + "秒之前";

        } else if (System.currentTimeMillis() - time == 0) {
            return "1秒之前";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(date);
        }
    }

    public  void setAbilityTypeCatalog(){
        abilityTypeCatalog=new HashMap<String, String>();
        abilityTypeCatalog.put("0", "其他");
        abilityTypeCatalog.put("1", "口语");
        abilityTypeCatalog.put("2", "听力");
        abilityTypeCatalog.put("3", "阅读");
        abilityTypeCatalog.put("4", "写作");
        abilityTypeCatalog.put("5", "翻译");
        abilityTypeCatalog.put("6", "单词");
        abilityTypeCatalog.put("7", "语法");
        abilityTypeCatalog.put("8", "其他");
    }

    public  void setAppTypeCatalog(){
        appTypeCatalog=new HashMap<>();
        appTypeCatalog.put("0", "其他");
        appTypeCatalog.put("101", "VOA");
        appTypeCatalog.put("102", "BBC");
        appTypeCatalog.put("103", "听歌");
        appTypeCatalog.put("104", "CET4");
        appTypeCatalog.put("105", "CET6");
        appTypeCatalog.put("106", "托福");
        appTypeCatalog.put("107", "N1");
        appTypeCatalog.put("108", "N2");
        appTypeCatalog.put("109", "N3");
        appTypeCatalog.put("110", "微课");
        appTypeCatalog.put("111", "雅思");
        appTypeCatalog.put("112", "初中");
        appTypeCatalog.put("113", "高中");
        appTypeCatalog.put("114", "考研");
        appTypeCatalog.put("115", "新概念");
        appTypeCatalog.put("116", "走遍美国");
        appTypeCatalog.put("117", "英语头条");
    }
}
