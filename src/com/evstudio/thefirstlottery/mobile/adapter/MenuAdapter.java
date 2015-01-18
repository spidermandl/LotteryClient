package com.evstudio.thefirstlottery.mobile.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.evstudio.thefirstlottery.mobile.R;

import java.util.ArrayList;

public class MenuAdapter extends BaseAdapter {

	private Context c;
	public static ArrayList<String> ss;

	public MenuAdapter(Context context, ArrayList<String> str) {
		this.c = context;
		this.ss = str;
	}

	@Override
	public int getCount() {
		return ss.size();
	}

	@Override
	public Object getItem(int arg0) {
		return ss.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(c, R.layout.item_menu, null);
			holder = new ViewHolder();
			holder.tv_menu = (TextView) convertView.findViewById(R.id.tv_menu);
			holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String s = ss.get(position);
		holder.tv_menu.setText(s);

        
		if (s.contains("首页")) {
			Drawable drawableHome = c.getResources().getDrawable(R.drawable.menu_ico_gcjl);
			drawableHome.setBounds(0, 0, drawableHome.getMinimumWidth(), drawableHome.getMinimumHeight());
			holder.tv_menu.setCompoundDrawables(drawableHome, null, null, null);
		} else if (s.contains("个人资料")) {
			Drawable drawableHome = c.getResources().getDrawable(R.drawable.menu_ico_sjfw);
			drawableHome.setBounds(0, 0, drawableHome.getMinimumWidth(), drawableHome.getMinimumHeight());

			holder.tv_menu.setCompoundDrawables(drawableHome, null, null, null);
		} else if (s.contains("购彩记录")) {
			Drawable drawableHome = c.getResources().getDrawable(R.drawable.menu_ico_zhjl);
			drawableHome.setBounds(0, 0, drawableHome.getMinimumWidth(), drawableHome.getMinimumHeight());
			holder.tv_menu.setCompoundDrawables(drawableHome, null, null, null);
		} else if (s.contains("中奖记录")) {
			Drawable drawableHome = c.getResources().getDrawable(R.drawable.menu_ico_zhmx);
			drawableHome.setBounds(0, 0, drawableHome.getMinimumWidth(), drawableHome.getMinimumHeight());
			holder.tv_menu.setCompoundDrawables(drawableHome, null, null, null);
		} else if (s.contains("财务中心")) {
			Drawable drawableHome = c.getResources().getDrawable(R.drawable.menu_ico_bdyhk);
			drawableHome.setBounds(0, 0, drawableHome.getMinimumWidth(), drawableHome.getMinimumHeight());
			holder.tv_menu.setCompoundDrawables(drawableHome, null, null, null);
		} else if (s.contains("我的优惠")) {
			Drawable drawableHome = c.getResources().getDrawable(R.drawable.menu_ico_xxhz);
			drawableHome.setBounds(0, 0, drawableHome.getMinimumWidth(), drawableHome.getMinimumHeight());
			holder.tv_menu.setCompoundDrawables(drawableHome, null, null, null);
		} else if (s.contains("关于我们")) {
			Drawable drawableHome = c.getResources().getDrawable(R.drawable.menu_ico_sjfw);
			drawableHome.setBounds(0, 0, drawableHome.getMinimumWidth(), drawableHome.getMinimumHeight());
			holder.tv_menu.setCompoundDrawables(drawableHome, null, null, null);
		}

//		if (s.contains("购彩记录")) {
//			holder.tv_number.setVisibility(View.VISIBLE);
//		} else {
//			holder.tv_number.setVisibility(View.INVISIBLE);
//		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_menu;
		TextView tv_number;
	}
}
