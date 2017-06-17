package com.iyuba.core.me.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.iyuba.core.common.manager.AccountManager;
import com.iyuba.core.common.thread.GitHubImageLoader;
import com.iyuba.core.common.widget.circularimageview.CircularImageView;
import com.iyuba.core.common.widget.dialog.CustomDialog;
import com.iyuba.core.me.adapter.LearnRankListAdapter;
import com.iyuba.core.me.adapter.RankListAdapter;
import com.iyuba.core.me.adapter.TestRankListAdapter;
import com.iyuba.core.me.protocol.GetLearnRankInfoRequest;
import com.iyuba.core.me.protocol.GetLearnRankInfoResponse;
import com.iyuba.core.me.protocol.GetRankInfoRequest;
import com.iyuba.core.me.protocol.GetRankInfoResponse;
import com.iyuba.core.me.protocol.GetTestRankInfoRequest;
import com.iyuba.core.me.protocol.GetTestRankInfoResponse;
import com.iyuba.core.me.sqlite.mode.LearnRankUser;
import com.iyuba.core.me.sqlite.mode.RankUser;
import com.iyuba.core.me.sqlite.mode.TestRankUser;
import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.toolbox.ExeProtocol;
import com.iyuba.http.toolbox.ProtocolResponse;
import com.iyuba.lib.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.y;

/**
 * Created by Ivot on 2016/12/29.
 */

public class TestRankActivity extends Activity {
    private Context mContext;
    private String uid;
    private String type;
    private String total;
    private String start;

    private String result = "";
    private String message = "";
    private String myTotalTest = "";
    private String myImgSrc = "";
    private String myId = "";
    private String myRanking = "";
    private String myTotalRight = "";
    private String myName = "";
    private List<TestRankUser> rankUsers = new ArrayList<>();
    private TestRankUser champion;

    private TestRankListAdapter rankListAdapter;

    private Pattern p;
    private Matcher m;

    private CustomDialog waitDialog;

    private LayoutInflater inflater;
    private View listFooter;
    private boolean scorllable = true;

    TextView note;
    CircularImageView userImage;
    TextView userImageText;
    TextView userName;
    TextView myUsername;

    TextView rankChampionInfo;
    TextView myRankInfo;

    ListView rankListView;
    TextView title;
//    ImageButton titleButton;
    ImageButton backButton;

    public void findViews(){
        note = (TextView) findViewById(R.id.rank_note);
        userImage = (CircularImageView) findViewById(R.id.rank_user_image);
        userImageText = (TextView) findViewById(R.id.rank_user_image_text);
        userName = (TextView) findViewById(R.id.rank_user_name);
        myUsername = (TextView) findViewById(R.id.username);

        rankChampionInfo = (TextView) findViewById(R.id.rank_user_data_info);
        myRankInfo = (TextView) findViewById(R.id.rank_test_info);

        rankListView = (ListView) findViewById(R.id.test_rank_list);
        title = (TextView) findViewById(R.id.titlebar_title);
//        titleButton = (ImageButton) findViewById(R.id.titlebar_overflow_button);
        backButton = (ImageButton) findViewById(R.id.titlebar_back_button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_rank);

        mContext = this;
//        ButterKnife.bind(this);
        findViews();

        inflater = getLayoutInflater();

        waitDialog = new CustomDialog(mContext);
        waitDialog.setTitle("请稍后");

        title.setText("排行榜");
//        titleButton.setVisibility(View.GONE);

        uid = AccountManager.Instace(mContext).userId;
        type = "D";
        total = "10";
        start = "0";

        listFooter = inflater.inflate(R.layout.comment_footer, null);
        rankListView.addFooterView(listFooter);

        handler.sendEmptyMessage(0);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rankListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE: // 当不滚动时
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            // 当comment不为空且comment.size()不为0且没有完全加载
                            if (scorllable)
                                handler.sendEmptyMessage(0);
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private String getFirstChar(String name) {
        String subString;
        for (int i = 0; i < name.length(); i++) {
            subString = name.substring(i, i + 1);

            p = Pattern.compile("[0-9]*");
            m = p.matcher(subString);
            if (m.matches()) {
//                Toast.makeText(Main.this,"输入的是数字", Toast.LENGTH_SHORT).show();
                return subString;
            }

            p = Pattern.compile("[a-zA-Z]");
            m = p.matcher(subString);
            if (m.matches()) {
//                Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                return subString;
            }

            p = Pattern.compile("[\u4e00-\u9fa5]");
            m = p.matcher(subString);
            if (m.matches()) {
//                Toast.makeText(Main.this,"输入的是汉字", Toast.LENGTH_SHORT).show();
                return subString;
            }
        }

        return "A";
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    waitDialog.show();
                    if (rankUsers.size() == 0) {
                        start = "0";
                    } else {
                        start = String.valueOf(rankListAdapter.getCount());
                    }
                    ExeProtocol.exe(new GetTestRankInfoRequest(uid, type, start, total), new ProtocolResponse() {
                        @Override
                        public void finish(BaseHttpResponse bhr) {
                            Log.e("LearnRankActivity", "rankrank");
                            GetTestRankInfoResponse response = (GetTestRankInfoResponse) bhr;
                            myTotalTest = response.totalTest;
                            myImgSrc = response.myImgSrc;
                            myId = response.myId;
                            myRanking = response.myRanking;
                            myTotalRight = response.totalRight;
                            result = response.result;
                            message = response.message;
                            myName = response.myName;
                            rankUsers = response.rankUsers;
                            champion = rankUsers.get(0);

                            if (rankUsers.size() < 10) {
                                listFooter.setVisibility(View.GONE);
                                scorllable = false;
                            } else {
                                listFooter.setVisibility(View.VISIBLE);
                            }

                            handler.sendEmptyMessage(1);

                            if (champion.getRanking().equals("1"))
                                handler.sendEmptyMessage(2);
                        }

                        @Override
                        public void error() {

                        }
                    });
                    break;
                case 1:
                    if (rankListAdapter == null) {
                        rankListAdapter = new TestRankListAdapter(mContext, rankUsers);
                        rankListView.setAdapter(rankListAdapter);
                    } else if (champion.getRanking().equals("1")) {
                        rankListAdapter.resetList(rankUsers);
                    } else {
                        rankListAdapter.addList(rankUsers);
                    }
                    waitDialog.dismiss();
                    break;
                case 2:
                    String firstChar = getFirstChar(champion.getName());
                    if (champion.getImgSrc().equals("http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg")) {
                        userImage.setVisibility(View.INVISIBLE);
                        userImageText.setVisibility(View.VISIBLE);
                        p = Pattern.compile("[a-zA-Z]");
                        m = p.matcher(firstChar);
                        if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                            userImageText.setBackgroundResource(R.drawable.rank_blue);
                            userImageText.setText(firstChar);
                            myUsername.setText(myName);
                            userName.setText(champion.getName());
                            rankChampionInfo.setText("做题总数:" + champion.getTotalTest() + ",正确数:"+champion.getTotalRight());
                            myRankInfo.setText("做题总数:"+myTotalTest+",正确数:"+myTotalRight+",排名:"+myRanking);
                        } else {
                            userImageText.setBackgroundResource(R.drawable.rank_green);
                            userImageText.setText(firstChar);
                            myUsername.setText(myName);
                            userName.setText(champion.getName());
                            rankChampionInfo.setText("做题总数:" + champion.getTotalTest() + ",正确数:"+champion.getTotalRight());
                            myRankInfo.setText("做题总数:"+myTotalTest+",正确数:"+myTotalRight+",排名:"+myRanking);
                        }
                    } else {
                        userImageText.setVisibility(View.INVISIBLE);
                        userImage.setVisibility(View.VISIBLE);
                        GitHubImageLoader.Instace(mContext).setRawPic(champion.getImgSrc(), userImage,
                                R.drawable.noavatar_small);
                        myUsername.setText(myName);
                        userName.setText(champion.getName());
                        rankChampionInfo.setText("做题总数:" + champion.getTotalTest() + ",正确数:"+champion.getTotalRight());
                        myRankInfo.setText("做题总数:"+myTotalTest+",正确数:"+myTotalRight+",排名:"+myRanking);
                    }
                    break;
            }
        }
    };


}
