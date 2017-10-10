package com.iyuba.core.iyumooc.teacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iyuba.core.common.widget.flowtaglayout.OnInitSelectedPosition;
import com.iyuba.lib.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：renzhy on 17/7/3 15:52
 * 邮箱：renzhongyigoo@gmail.com
 */
public class TagAdapter<T> extends BaseAdapter implements OnInitSelectedPosition {

    private final Context mContext;
    private final List<T> mDataList;

    public TagAdapter(Context context) {
        this.mContext = context;
        mDataList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_ques_type_tab,null);

        TextView textView = (TextView) view.findViewById(R.id.tv_tag);
        T t = mDataList.get(i);

        if(t instanceof String){
            textView.setText((String) t);
        }
        return view;
    }

    @Override
    public boolean isSelectedPosition(int position) {
        if(position % 2 == 0){
            return true;
        }
        return false;
    }

    public void onlyAddAll(List<T> datas){
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<T> datas){
        mDataList.clear();
        onlyAddAll(datas);
    }
}
