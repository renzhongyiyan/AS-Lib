package com.iyuba.core.me.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.iyuba.core.common.manager.AccountManager;
import com.iyuba.core.common.thread.GitHubImageLoader;
import com.iyuba.core.common.widget.circularimageview.CircularImageView;
import com.iyuba.core.common.widget.dialog.CustomDialog;
import com.iyuba.core.me.adapter.TestRankListAdapter;
import com.iyuba.core.me.protocol.GetTestRankInfoRequest;
import com.iyuba.core.me.protocol.GetTestRankInfoResponse;
import com.iyuba.core.me.sqlite.mode.TestRankUser;
import com.iyuba.http.BaseHttpResponse;
import com.iyuba.http.toolbox.ExeProtocol;
import com.iyuba.http.toolbox.ProtocolResponse;
import com.iyuba.lib.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ivot on 2016/12/29.
 */

public class TestRankFragment extends Fragment {
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

    private View rootView;
    private TestRankListAdapter rankListAdapter;

    private Pattern p;
    private Matcher m;

    private CustomDialog waitDialog;

    private View listFooter;
    private boolean scorllable = true;

    CircularImageView userImage;
    TextView userImageText;
    TextView userName;
    TextView myUsername;

    TextView tvRank;
    TextView tvTotalTest;
    TextView tvTotalRight;
    TextView tvRightRate;

    ListView rankListView;

    public void findViews(View root){
        userImage = (CircularImageView) root.findViewById(R.id.rank_user_image);
        userImageText = (TextView) root.findViewById(R.id.rank_user_image_text);
        userName = (TextView) root.findViewById(R.id.rank_user_name);
        myUsername = (TextView) root.findViewById(R.id.username);

        tvRank = (TextView) root.findViewById(R.id.tv_user_rank);
        tvTotalTest = (TextView) root.findViewById(R.id.tv_test_total_num);
        tvTotalRight = (TextView) root.findViewById(R.id.tv_test_right_num);
        tvRightRate = (TextView) root.findViewById(R.id.tv_test_right_rate);

        rankListView = (ListView) root.findViewById(R.id.test_rank_list);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_test_rank, container, false);
        findViews(rootView);

        waitDialog = new CustomDialog(mContext);
        waitDialog.setTitle("请稍后");

        uid = AccountManager.Instace(mContext).userId;
        type = "D";
        total = "10";
        start = "0";

        listFooter = inflater.inflate(R.layout.comment_footer, null);
        rankListView.addFooterView(listFooter);

        handler.sendEmptyMessage(0);

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
        return rootView;
    }

    public void updateTestRank(int timeType){
        handler.sendEmptyMessage(3);
        switch (timeType){
            case 0:
                type = "D";
                total = "10";
                start = "0";
                handler.sendEmptyMessage(0);
                break;
            case 1:
                type = "W";
                total = "10";
                start = "0";
                handler.sendEmptyMessage(0);
                break;
            case 2:
                type = "M";
                total = "10";
                start = "0";
                handler.sendEmptyMessage(0);
                break;
            default:
                break;
        }
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
                            Log.e("TestRankFragment", "rankrank");
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
                            tvRank.setText(myRanking);
                            tvTotalTest.setText(myTotalTest);
                            tvTotalRight.setText(myTotalRight);
                            if(Integer.parseInt(myTotalTest)==0){
                                tvRightRate.setText("0%");
                            }else{
                                tvRightRate.setText(Integer.parseInt(myTotalRight)*100/Integer.parseInt(myTotalTest)+"%");
                            }
                        } else {
                            userImageText.setBackgroundResource(R.drawable.rank_green);
                            userImageText.setText(firstChar);
                            myUsername.setText(myName);
                            userName.setText(champion.getName());
                            tvRank.setText(myRanking);
                            tvTotalTest.setText(myTotalTest);
                            tvTotalRight.setText(myTotalRight);
                            if(Integer.parseInt(myTotalTest)==0){
                                tvRightRate.setText("0%");
                            }else{
                                tvRightRate.setText(Integer.parseInt(myTotalRight)*100/Integer.parseInt(myTotalTest)+"%");
                            }
                        }
                    } else {
                        userImageText.setVisibility(View.INVISIBLE);
                        userImage.setVisibility(View.VISIBLE);
                        GitHubImageLoader.Instace(mContext).setRawPic(champion.getImgSrc(), userImage,
                                R.drawable.noavatar_small);
                        myUsername.setText(myName);
                        userName.setText(champion.getName());
                        tvRank.setText(myRanking);
                        tvTotalTest.setText(myTotalTest);
                        tvTotalRight.setText(myTotalRight);
                        if(Integer.parseInt(myTotalTest)==0){
                            tvRightRate.setText("0%");
                        }else{
                            tvRightRate.setText(Integer.parseInt(myTotalRight)*100/Integer.parseInt(myTotalTest)+"%");
                        }
                    }
                    break;
                case 3:
//                    rankListAdapter.clearList();
                    if(rankUsers != null){
                        rankUsers.clear();
                    }
                    break;
            }
        }
    };


}
