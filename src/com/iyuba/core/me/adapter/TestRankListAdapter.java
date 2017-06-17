package com.iyuba.core.me.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.core.common.thread.GitHubImageLoader;
import com.iyuba.core.me.sqlite.mode.LearnRankUser;
import com.iyuba.core.me.sqlite.mode.RankUser;
import com.iyuba.core.me.sqlite.mode.TestRankUser;
import com.iyuba.headnewslib.adapter.ViewHolder;
import com.iyuba.lib.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/1/4.
 */

public class TestRankListAdapter extends BaseAdapter {

    private Context mContext;
    private List<TestRankUser> rankUserList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Pattern p;
    private Matcher m;

    public TestRankListAdapter(Context mContext, List<TestRankUser> rankUserList) {
        this.mContext = mContext;
        this.rankUserList.addAll(rankUserList);
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return rankUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return rankUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.valueOf(rankUserList.get(position).getSort());
    }

    public void clearList(){
        rankUserList.clear();
        notifyDataSetChanged();
    }

    public void resetList(List<TestRankUser> list){
        rankUserList.clear();
        rankUserList.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(List<TestRankUser> list){
        rankUserList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.rank_test_info_item, null);
        }
        TestRankUser ru = rankUserList.get(position);
        Log.e("LearnRankListAdapter",String.valueOf(rankUserList.size()));
        String firstChar;

        ImageView rankLogoImage = ViewHolder.get(convertView, R.id.rank_logo_image);
        TextView rankLogoText = ViewHolder.get(convertView, R.id.rank_logo_text);
        ImageView userImage = ViewHolder.get(convertView, R.id.user_image);
        TextView userImageText = ViewHolder.get(convertView, R.id.user_image_text);
        TextView userName = ViewHolder.get(convertView, R.id.rank_user_name);
//        TextView userInfo = ViewHolder.get(convertView, R.id.rank_user_info);
        TextView userTotalTest =  ViewHolder.get(convertView, R.id.tv_test_total_num);
        TextView userTotalRight = ViewHolder.get(convertView, R.id.tv_test_right_num);
        TextView userRightRate = ViewHolder.get(convertView, R.id.tv_test_right_rate);

        firstChar = getFirstChar(ru.getName());
        switch (ru.getRanking()) {
            case "1":
                rankLogoText.setVisibility(View.INVISIBLE);
                rankLogoImage.setVisibility(View.VISIBLE);
                rankLogoImage.setImageResource(R.drawable.rank_gold);

                if (ru.getImgSrc().equals("http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg")) {
                    userImage.setVisibility(View.INVISIBLE);
                    userImageText.setVisibility(View.VISIBLE);
                    p = Pattern.compile("[a-zA-Z]");
                    m = p.matcher(firstChar);
                    if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                        userImageText.setBackgroundResource(R.drawable.rank_blue);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        userTotalTest.setText(ru.getTotalTest());
                        userTotalRight.setText(ru.getTotalRight());
                        if(Integer.parseInt(ru.getTotalTest())==0){
                            userRightRate.setText("0%");
                        }else{
                            userRightRate.setText(Integer.parseInt(ru.getTotalRight())*100/Integer.parseInt(ru.getTotalTest())+"%");
                        }
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        userTotalTest.setText(ru.getTotalTest());
                        userTotalRight.setText(ru.getTotalRight());
                        if(Integer.parseInt(ru.getTotalTest())==0){
                            userRightRate.setText("0%");
                        }else{
                            userRightRate.setText(Integer.parseInt(ru.getTotalRight())*100/Integer.parseInt(ru.getTotalTest())+"%");
                        }
                    }
                } else {
                    userImage.setVisibility(View.VISIBLE);
                    userImageText.setVisibility(View.INVISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName());
                    userTotalTest.setText(ru.getTotalTest());
                    userTotalRight.setText(ru.getTotalRight());
                    if(Integer.parseInt(ru.getTotalTest())==0){
                        userRightRate.setText("0%");
                    }else{
                        userRightRate.setText(Integer.parseInt(ru.getTotalRight())*100/Integer.parseInt(ru.getTotalTest())+"%");
                    }
                }
                break;
            case "2":
                rankLogoText.setVisibility(View.INVISIBLE);
                rankLogoImage.setVisibility(View.VISIBLE);
                rankLogoImage.setImageResource(R.drawable.rank_silvery);

                if (ru.getImgSrc().equals("http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg")) {
                    userImage.setVisibility(View.INVISIBLE);
                    userImageText.setVisibility(View.VISIBLE);
                    p = Pattern.compile("[a-zA-Z]");
                    m = p.matcher(firstChar);
                    if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                        userImageText.setBackgroundResource(R.drawable.rank_blue);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        userTotalTest.setText(ru.getTotalTest());
                        userTotalRight.setText(ru.getTotalRight());
                        if(Integer.parseInt(ru.getTotalTest())==0){
                            userRightRate.setText("0%");
                        }else{
                            userRightRate.setText(Integer.parseInt(ru.getTotalRight())*100/Integer.parseInt(ru.getTotalTest())+"%");
                        }
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        userTotalTest.setText(ru.getTotalTest());
                        userTotalRight.setText(ru.getTotalRight());
                        if(Integer.parseInt(ru.getTotalTest())==0){
                            userRightRate.setText("0%");
                        }else{
                            userRightRate.setText(Integer.parseInt(ru.getTotalRight())*100/Integer.parseInt(ru.getTotalTest())+"%");
                        }
                    }
                } else {
                    userImage.setVisibility(View.VISIBLE);
                    userImageText.setVisibility(View.INVISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName());
                    userTotalTest.setText(ru.getTotalTest());
                    userTotalRight.setText(ru.getTotalRight());
                    if(Integer.parseInt(ru.getTotalTest())==0){
                        userRightRate.setText("0%");
                    }else{
                        userRightRate.setText(Integer.parseInt(ru.getTotalRight())*100/Integer.parseInt(ru.getTotalTest())+"%");
                    }
                }
                break;
            case "3":
                rankLogoText.setVisibility(View.INVISIBLE);
                rankLogoImage.setVisibility(View.VISIBLE);
                rankLogoImage.setImageResource(R.drawable.rank_copper);

                if (ru.getImgSrc().equals("http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg")) {
                    userImage.setVisibility(View.INVISIBLE);
                    userImageText.setVisibility(View.VISIBLE);
                    p = Pattern.compile("[a-zA-Z]");
                    m = p.matcher(firstChar);
                    if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                        userImageText.setBackgroundResource(R.drawable.rank_blue);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        userTotalTest.setText(ru.getTotalTest());
                        userTotalRight.setText(ru.getTotalRight());
                        if(Integer.parseInt(ru.getTotalTest())==0){
                            userRightRate.setText("0%");
                        }else{
                            userRightRate.setText(Integer.parseInt(ru.getTotalRight())*100/Integer.parseInt(ru.getTotalTest())+"%");
                        }
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        userTotalTest.setText(ru.getTotalTest());
                        userTotalRight.setText(ru.getTotalRight());
                        if(Integer.parseInt(ru.getTotalTest())==0){
                            userRightRate.setText("0%");
                        }else{
                            userRightRate.setText(Integer.parseInt(ru.getTotalRight())*100/Integer.parseInt(ru.getTotalTest())+"%");
                        }
                    }
                } else {
                    userImageText.setVisibility(View.INVISIBLE);
                    userImage.setVisibility(View.VISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName());
                    userTotalTest.setText(ru.getTotalTest());
                    userTotalRight.setText(ru.getTotalRight());
                    if(Integer.parseInt(ru.getTotalTest())==0){
                        userRightRate.setText("0%");
                    }else{
                        userRightRate.setText(Integer.parseInt(ru.getTotalRight())*100/Integer.parseInt(ru.getTotalTest())+"%");
                    }
                }
                break;
            default:
                rankLogoImage.setVisibility(View.INVISIBLE);
                rankLogoText.setVisibility(View.VISIBLE);
                rankLogoText.setText(ru.getRanking());

                if (ru.getImgSrc().equals("http://static1.iyuba.com/uc_server/images/noavatar_middle.jpg")) {
                    userImage.setVisibility(View.INVISIBLE);
                    userImageText.setVisibility(View.VISIBLE);
                    p = Pattern.compile("[a-zA-Z]");
                    m = p.matcher(firstChar);
                    if (m.matches()) {
//                        Toast.makeText(Main.this,"输入的是字母", Toast.LENGTH_SHORT).show();
                        userImageText.setBackgroundResource(R.drawable.rank_blue);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        userTotalTest.setText(ru.getTotalTest());
                        userTotalRight.setText(ru.getTotalRight());
                        if(Integer.parseInt(ru.getTotalTest())==0){
                            userRightRate.setText("0%");
                        }else{
                            userRightRate.setText(Integer.parseInt(ru.getTotalRight())*100/Integer.parseInt(ru.getTotalTest())+"%");
                        }
                    } else {
                        userImageText.setBackgroundResource(R.drawable.rank_green);
                        userImageText.setText(firstChar);
                        userName.setText(ru.getName());
                        userTotalTest.setText(ru.getTotalTest());
                        userTotalRight.setText(ru.getTotalRight());
                        if(Integer.parseInt(ru.getTotalTest())==0){
                            userRightRate.setText("0%");
                        }else{
                            userRightRate.setText(Integer.parseInt(ru.getTotalRight())*100/Integer.parseInt(ru.getTotalTest())+"%");
                        }
                    }
                } else {
                    userImageText.setVisibility(View.INVISIBLE);
                    userImage.setVisibility(View.VISIBLE);
                    GitHubImageLoader.Instace(mContext).setRawPic(ru.getImgSrc(), userImage,
                            R.drawable.noavatar_small);
                    userName.setText(ru.getName());
                    userTotalTest.setText(ru.getTotalTest());
                    userTotalRight.setText(ru.getTotalRight());
                    if(Integer.parseInt(ru.getTotalTest())==0){
                        userRightRate.setText("0%");
                    }else{
                        userRightRate.setText(Integer.parseInt(ru.getTotalRight())*100/Integer.parseInt(ru.getTotalTest())+"%");
                    }
                }
                break;

        }
        return convertView;
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
}
