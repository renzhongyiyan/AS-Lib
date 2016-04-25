package com.iyuba.core.me.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.core.me.holder.BaseViewHolder;
import com.iyuba.lib.R;
/**
 * @Description:gridview的Adapter
 */
public class MyGridAdapter extends BaseAdapter {
	private Context mContext;
	public String[] img_text = { "无广告", "尊贵标识", "调节语速", "高速下载", "查看解析", "一对一服务",
			"无限下载", "全部应用", "送书", };
	public int[] imgs = { R.drawable.tequan1, R.drawable.tequan2,
			R.drawable.tequan3, R.drawable.tequan4,
			R.drawable.tequan5, R.drawable.tequan6,
			R.drawable.tequan7, R.drawable.tequan8,
			R.drawable.tequan9};
	public MyGridAdapter(Context mContext) {
		super();
		this.mContext = mContext;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return img_text.length;
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.grid_item, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
		ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
		iv.setBackgroundResource(imgs[position]);

		tv.setText(img_text[position]);
		return convertView;
	}
}
