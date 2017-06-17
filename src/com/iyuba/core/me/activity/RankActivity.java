package com.iyuba.core.me.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
import com.iyuba.core.me.adapter.RankListAdapter;
import com.iyuba.core.me.protocol.GetRankInfoRequest;
import com.iyuba.core.me.protocol.GetRankInfoResponse;
import com.iyuba.core.me.sqlite.mode.RankUser;
import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.toolbox.ExeProtocol;
import com.iyuba.http.toolbox.ProtocolResponse;
import com.iyuba.lib.R;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ivot on 2016/12/29.
 */

public class RankActivity extends Activity {
    private Context mContext;
    private String uid;
    private String type;
    private String total;
    private String start;

    private String myWpm = "";
    private String myImgSrc = "";
    private String myId = "";
    private String myRanking = "";
    private String myCnt = "";
    private String result = "";
    private String message = "";
    private String myName = "";
    private String myWords = "";
    private List<RankUser> rankUsers = new ArrayList<>();
    private RankUser champion;

    private RankListAdapter rankListAdapter;

    private Pattern p;
    private Matcher m;

    private CustomDialog waitDialog;

    private LayoutInflater inflater;
    private View listFooter;
    private boolean scorllable = true;

    TextView today;
    TextView week;
    TextView month;
    TextView note;
    CircularImageView userImage;
    TextView userImageText;
    TextView userName;
    TextView myUsername;
    TextView userInfo;
    ListView rankListView;
    TextView title;
//    ImageButton titleButton;
    ImageButton backButton;

    public void findViews(){
        today = (TextView) findViewById(R.id.rank_today);
        week = (TextView) findViewById(R.id.rank_week);
        month = (TextView) findViewById(R.id.rank_month);
        note = (TextView) findViewById(R.id.rank_note);
        userImage = (CircularImageView) findViewById(R.id.rank_user_image);
        userImageText = (TextView) findViewById(R.id.rank_user_image_text);
        userName = (TextView) findViewById(R.id.rank_user_name);
        myUsername = (TextView) findViewById(R.id.username);
        userInfo = (TextView) findViewById(R.id.rank_info);
        rankListView = (ListView) findViewById(R.id.rank_list);
        title = (TextView) findViewById(R.id.titlebar_title);
//        titleButton = (ImageButton) findViewById(R.id.titlebar_overflow_button);
        backButton = (ImageButton) findViewById(R.id.titlebar_back_button);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank);

        mContext = this;
//        ButterKnife.bind(this);
        findViews();

        inflater = getLayoutInflater();

        waitDialog = new CustomDialog(mContext);
        waitDialog.setTitle("请稍后");

        title.setText("排行榜");
//        titleButton.setVisibility(View.GONE);

        today.setSelected(true);
        today.setTextColor(0xffffffff);

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

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!today.isSelected()) {
                    rankUsers.clear();

                    today.setSelected(true);
                    today.setTextColor(0xffffffff);

                    week.setSelected(false);
                    week.setTextColor(0xff239bf0);

                    month.setSelected(false);
                    month.setTextColor(0xff239bf0);

                    type = "D";

                    note.setText("今日数据本日24:00清零");

                    handler.sendEmptyMessage(0);
                }
            }
        });

        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!week.isSelected()) {
                    rankUsers.clear();

                    today.setSelected(false);
                    today.setTextColor(0xff239bf0);

                    week.setSelected(true);
                    week.setTextColor(0xffffffff);

                    month.setSelected(false);
                    month.setTextColor(0xff239bf0);

                    type = "W";

                    note.setText("本周数据周日24:00清零");

                    handler.sendEmptyMessage(0);
                }
            }
        });

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!month.isSelected()) {
                    rankUsers.clear();

                    today.setSelected(false);
                    today.setTextColor(0xff239bf0);

                    week.setSelected(false);
                    week.setTextColor(0xff239bf0);

                    month.setSelected(true);
                    month.setTextColor(0xffffffff);

                    type = "M";

                    note.setText("本月数据月末24:00清零");

                    handler.sendEmptyMessage(0);
                }
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
                    ExeProtocol.exe(new GetRankInfoRequest(uid, type, start, total), new ProtocolResponse() {
                        @Override
                        public void finish(BaseHttpResponse bhr) {
                            Log.e("RankActivity", "rankrank");
                            GetRankInfoResponse response = (GetRankInfoResponse) bhr;
                            myWpm = response.myWpm;
                            myImgSrc = response.myImgSrc;
                            myId = response.myId;
                            myRanking = response.myRanking;
                            myCnt = response.myCnt;
                            result = response.result;
                            message = response.message;
                            myName = response.myName;
                            myWords = response.myWords;
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
                        rankListAdapter = new RankListAdapter(mContext, rankUsers);
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
                            userInfo.setText("累计阅读" + myWords + "个单词,当前排名:" + myRanking);
                        } else {
                            userImageText.setBackgroundResource(R.drawable.rank_green);
                            userImageText.setText(firstChar);
                            myUsername.setText(myName);
                            userName.setText(champion.getName());
                            userInfo.setText("累计阅读" + myWords + "个单词,当前排名:" + myRanking);
                        }
                    } else {
                        userImageText.setVisibility(View.INVISIBLE);
                        userImage.setVisibility(View.VISIBLE);
                        GitHubImageLoader.Instace(mContext).setRawPic(champion.getImgSrc(), userImage,
                                R.drawable.noavatar_small);
                        myUsername.setText(myName);
                        userName.setText(champion.getName());
                        userInfo.setText("累计阅读" + myWords + "个单词,当前排名:" + myRanking);
                    }
                    break;
            }
        }
    };


}
