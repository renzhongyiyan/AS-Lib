package com.iyuba.core.iyumooc.teacher.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.iyuba.configation.ConfigManager;
import com.iyuba.core.common.activity.Login;
import com.iyuba.core.common.listener.ProtocolResponse;
import com.iyuba.core.common.listener.ResultIntCallBack;
import com.iyuba.core.common.manager.AccountManager;
import com.iyuba.core.common.manager.QuestionManager;
import com.iyuba.core.common.protocol.BaseHttpResponse;
import com.iyuba.core.common.util.ExeProtocol;
import com.iyuba.core.common.util.ExeRefreshTime;
import com.iyuba.core.common.util.NetWorkState;
import com.iyuba.core.common.widget.ContextMenu;
import com.iyuba.core.common.widget.DividerItemDecoration;
import com.iyuba.core.common.widget.dialog.CustomDialog;
import com.iyuba.core.common.widget.dialog.CustomToast;
import com.iyuba.core.common.widget.dialog.WaittingDialog;
import com.iyuba.core.iyumooc.teacher.API.PayQuestionListApi;
import com.iyuba.core.iyumooc.teacher.API.QuestionListApi;
import com.iyuba.core.iyumooc.teacher.adapter.PayQuestionListAdapter;
import com.iyuba.core.iyumooc.teacher.adapter.QuestionListAdapter;
import com.iyuba.core.iyumooc.teacher.bean.PayQuestionListBean;
import com.iyuba.core.iyumooc.teacher.bean.QuestionListBean;
import com.iyuba.core.teacher.activity.AskPayQuesActivity;
import com.iyuba.core.teacher.activity.PayQuesDetailActivity;
import com.iyuba.core.teacher.activity.QuesDetailActivity;
import com.iyuba.core.teacher.activity.QuezActivity;
import com.iyuba.core.teacher.protocol.DeleteAnswerQuesRequest;
import com.iyuba.core.teacher.protocol.DeleteAnswerQuesResponse;
import com.iyuba.core.teacher.sqlite.op.QuestionOp;
import com.iyuba.lib.R;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 作者：renzhy on 17/6/18 11:48
 * 邮箱：renzhongyigoo@gmail.com
 */
public class PayQListFragment extends Fragment {
    private static final String TAG = "PayQListFragment";
    private int adapterType;
    private Context mContext;

    private RecyclerView mRecyclerView;
//    private View mEmptyView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mIvAskButton;
    private ContextMenu contextMenu;
    private CustomDialog waitDialog;

    private PayQuestionListAdapter payQuesAdapter;
    private ArrayList<PayQuestionListBean.PayQuestionDataBean> payQuesList = new ArrayList<>();

    private int priceType = 0;
    public int pageNum = 1;
    private boolean isLast = false;
    private boolean mIsFirstTimeTouchBottom = true;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    payQuesAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    waitDialog.show();
                    break;
                case 3:
                    waitDialog.dismiss();
                    break;
                case 4:
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 5:
                    CustomToast.showToast(mContext, "已经是最新数据了哦~");
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 6:
                    CustomToast.showToast(mContext, "暂时没有数据,下拉刷新数据！");
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
                case 7:
                    CustomToast.showToast(mContext, "已是最后一页");
                    break;
                case 8:
                    CustomToast.showToast(mContext, "删除成功!");
                case 9:
                    CustomToast.showToast(mContext, "请检查网络连接！");
                    break;
            }
        }
    };

    private Handler binderAdapterDataHandler = new Handler();
    private Runnable binderAdapterDataRunnable = new Runnable() {
        public void run() {
            payQuesAdapter.clearList();
            payQuesAdapter.addList(payQuesList);
            payQuesAdapter.notifyDataSetChanged();
            handler.sendEmptyMessage(3);
        }
    };


    public static PayQListFragment newInstance(int adapterType){
        PayQListFragment newFragment = new PayQListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("adapterType",adapterType);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args != null){
            adapterType = args.getInt("adapterType");
            if(adapterType == 0){
                priceType = 1;
            }else {
                priceType = 0;
            }
        }

        mContext = getActivity();

        waitDialog = WaittingDialog.showDialog(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_qlist,container,false);

        initWidget(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void initWidget(View view){
        contextMenu = (ContextMenu)  view.findViewById(R.id.context_menu);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_ques_list);
//        mEmptyView = view.findViewById(R.id.empty_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mIvAskButton = (ImageView) view.findViewById(R.id.iv_edit_question);

        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new GetHeaderDataTask().execute();
            }
        });

        // 确定每个item的排列方式
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                // 这里要复写一下，因为默认宽高都是wrap_content
                // 这个不复写，你点击的背景色就只充满你的内容
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

//        if(payQuesList.size() == 0){
//            mEmptyView.setVisibility(View.VISIBLE);
//            mSwipeRefreshLayout.setVisibility(View.GONE);
//        }else {
//            mEmptyView.setVisibility(View.GONE);
//            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
//        }
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        payQuesAdapter = new PayQuestionListAdapter(mContext,adapterType);
        payQuesAdapter.clearList();
        mRecyclerView.setAdapter(payQuesAdapter);
        mIvAskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //如果没登录则跳转登录
                if (!AccountManager.Instace(mContext).checkUserLogin()) {

                    Intent intent = new Intent();
                    intent.setClass(mContext, Login.class);
                    startActivity(intent);
                    return;
                }

                Intent intent = new Intent();
//                intent.setClass(mContext, QuezActivity.class);
                intent.setClass(mContext, AskPayQuesActivity.class);
                startActivity(intent);
            }
        });
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                mContext,R.drawable.recycler_rectangle, DividerItemDecoration.VERTICAL_LIST));

        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });

        mRecyclerView.addOnScrollListener(getOnBottomListener(layoutManager));

        payQuesAdapter.setOnItemClickListener(new PayQuestionListAdapter.OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                QuestionManager.getInstance().payQuestion = payQuesList.get(position);
                Intent intent = new Intent();
//                intent.setClass(mContext, QuesDetailActivity.class);
                intent.setClass(mContext, PayQuesDetailActivity.class);
                intent.putExtra("qid",payQuesList.get(position).getQuestionid()+"");
                intent.putExtra("vip",payQuesList.get(position).getVip()+"");
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                QuestionManager.getInstance().payQuestion = payQuesList.get(position);

                if(payQuesList.get(position).getUid().equals(AccountManager.Instace(mContext).userId)){

                    final int theqid=payQuesList.get(position).getQuestionid();
                    final int num=position;
                    contextMenu.setText(mContext.getResources().getStringArray(
                            R.array.choose_delete));
                    contextMenu.setCallback(new  ResultIntCallBack() {

                        @Override
                        public void setResult(int result) {
                            // TODO Auto-generated method stub
                            switch (result) {
                                case 0:
                                    delAlertDialog(theqid+"",num);
                                    break;
                                case 1:
                                    Intent intent = new Intent();
//                                    intent.setClass(mContext, QuesDetailActivity.class);
                                    intent.setClass(mContext, PayQuesDetailActivity.class);
                                    intent.putExtra("qid",theqid+"");
                                    startActivity(intent);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    contextMenu.show();
                }else{

                }
            }
        });

        //等待进度条
        handler.sendEmptyMessage(2);
        getHeaderData();
    }

    private void delAlertDialog(final String id, final int num) {
        AlertDialog alert = new AlertDialog.Builder(mContext).create();
        alert.setTitle(R.string.alert_title);
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setMessage(mContext.getString(R.string.alert_delete));
        alert.setButton(AlertDialog.BUTTON_POSITIVE,
                getResources().getString(R.string.alert_btn_ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        ExeProtocol.exe(new DeleteAnswerQuesRequest("0", id, AccountManager.Instace(mContext).userId), new ProtocolResponse() {

                            @Override
                            public void finish(BaseHttpResponse bhr) {
                                // TODO Auto-generated method stub
                                DeleteAnswerQuesResponse tr = (DeleteAnswerQuesResponse) bhr;
                                if (tr.result.equals("1")) {
                                    payQuesList.remove(num);
                                    binderAdapterDataHandler.post(binderAdapterDataRunnable);
                                    handler.sendEmptyMessage(8);

                                } else {
                                    payQuesList.remove(num);
                                    binderAdapterDataHandler.post(binderAdapterDataRunnable);
                                    handler.sendEmptyMessage(8);
                                }
                            }

                            @Override
                            public void error() {
                            }
                        });


                    }
                });
        alert.setButton(AlertDialog.BUTTON_NEGATIVE,
                getResources().getString(R.string.alert_btn_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                    }
                });
        alert.show();
    }

    private PullToRefreshBase.OnRefreshListener2<ListView> orfl = new PullToRefreshBase.OnRefreshListener2<ListView>() {

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            // TODO Auto-generated method stub

            if (NetWorkState.isConnectingToInternet()) {// 开始刷新

                new GetHeaderDataTask().execute();

            } else {// 刷新失败
            }

        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            // TODO Auto-generated method stub

            if (NetWorkState.isConnectingToInternet()) {// 开始刷新

                new GetFooterDataTask().execute();

            } else {// 刷新失败
                handler.sendEmptyMessage(3);
                handler.sendEmptyMessage(9);
                handler.sendEmptyMessage(4);
            }

        }
    };

    RecyclerView.OnScrollListener getOnBottomListener(final LinearLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {

            @Override public void onScrolled(RecyclerView rv, int dx, int dy) {
                boolean isBottom =
                        layoutManager.findFirstVisibleItemPosition() >=
                                payQuesAdapter.getItemCount() - 6;
                if (!mSwipeRefreshLayout.isRefreshing() && isBottom) {
                    if (!mIsFirstTimeTouchBottom) {
                        mSwipeRefreshLayout.setRefreshing(true);
                        new GetFooterDataTask().execute();
                    }
                    else {
                        mIsFirstTimeTouchBottom = false;
                    }
                }
            }
        };
    }

    public void getHeaderData() {

        int quesAppType = ConfigManager.Instance().loadInt("quesAppType");
        int quesAbilityType = ConfigManager.Instance().loadInt("quesAbilityType");
        int quesSort = ConfigManager.Instance().loadInt("quesSort");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
                .baseUrl("http://www.iyuba.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PayQuestionListApi apiStores = retrofit.create(PayQuestionListApi.class);
        Call<PayQuestionListBean> call = apiStores.getQuesList("json",3, quesAbilityType, quesAppType, 1, 20, quesSort,priceType);
        call.enqueue(new Callback<PayQuestionListBean>() {
            @Override
            public void onResponse(Response<PayQuestionListBean> response) {

                if (response.body().getData() != null && response.body().getData().size() != 0) {
                    payQuesList.clear();
                    payQuesList.addAll(response.body().getData());
                    binderAdapterDataHandler.post(binderAdapterDataRunnable);
                    pageNum = 2;
                    handler.sendEmptyMessage(3);
                    handler.sendEmptyMessage(4);
                    if (response.body().getData().size() < 20) isLast = true;
                    else isLast = false;
                } else {
                    handler.sendEmptyMessage(4);
                    handler.sendEmptyMessage(3);
                    handler.sendEmptyMessage(5);
                }

            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null) {
                }
            }
        });

    }

    public void getFooterData() {
        int quesAppType = ConfigManager.Instance().loadInt("quesAppType");
        int quesAbilityType = ConfigManager.Instance().loadInt("quesAbilityType");
        int quesSort = ConfigManager.Instance().loadInt("quesSort");

        if (isLast) {
            handler.sendEmptyMessage(4);
            handler.sendEmptyMessage(7);
            return;
        }

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
        PayQuestionListApi apiStores = retrofit.create(PayQuestionListApi.class);
        Call<PayQuestionListBean> call = apiStores.getQuesList("json", 3,quesAbilityType, quesAppType, pageNum, 20, quesSort,priceType);
        call.enqueue(new Callback<PayQuestionListBean>() {
            @Override
            public void onResponse(Response<PayQuestionListBean> response) {

                if (response.body().getData() != null && response.body().getData().size() != 0) {
                    payQuesList.addAll(response.body().getData());
                    pageNum++;
                    binderAdapterDataHandler.post(binderAdapterDataRunnable);

                    handler.sendEmptyMessage(3);
                    handler.sendEmptyMessage(4);
                    if (response.body().getData().size() < 20) isLast = true;
                    else isLast = false;
                } else {
                    handler.sendEmptyMessage(4);
                    handler.sendEmptyMessage(3);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (t != null) {
                }
            }
        });

    }

    private class GetHeaderDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            ExeRefreshTime.lastRefreshTime("NewPostListUpdateTime");
            getHeaderData();
            return null;
        }
    }

    private class GetFooterDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            ExeRefreshTime.lastRefreshTime("NewPostListUpdateTime");
            getFooterData();
            return null;
        }
    }
}
