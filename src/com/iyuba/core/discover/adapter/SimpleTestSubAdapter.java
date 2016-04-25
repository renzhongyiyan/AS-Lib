
package com.iyuba.core.discover.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyuba.lib.R;

/**
 * 简版开考试列表适配器
 * 
 * @author 陈彤
 * @version 1.0
 */
public class SimpleTestSubAdapter extends BaseAdapter {
	private Context mContext;
	private String[] testContent;
	private boolean isCET;
	private int[] drawable = { R.drawable.section_a, R.drawable.section_b,
			R.drawable.section_c };

	public SimpleTestSubAdapter(Context context) {
		mContext = context;
		isCET = true;
		testContent = new String[] { "Section A", "Section B", "Section C" };
	}

	public SimpleTestSubAdapter(Context context, String[] content) {
		mContext = context;
		isCET = false;
		testContent = content;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return testContent.length;
	}

	@Override
	public String getItem(int arg0) {
		// TODO Auto-generated method stub
		return testContent[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final String content = testContent[position];
		ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.item_common_test, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.test_pic);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.test_title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (isCET) {
			viewHolder.imageView.setBackgroundResource(drawable[position]);
		} else {
			viewHolder.imageView.setBackgroundResource(drawable[2]);
		}
		viewHolder.title.setText(content);
		return convertView;
	}

	public class ViewHolder {
		TextView title;
		ImageView imageView;
	}
}
