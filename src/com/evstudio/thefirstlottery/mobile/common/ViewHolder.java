package com.evstudio.thefirstlottery.mobile.common;

import android.util.SparseArray;
import android.view.View;

/**
 * @ClassName: ViewHolder
 * @Description:
 * @author zhouyong
 * @date 2014年7月29日 下午4:00:28
 * @version 1.0
 */
public class ViewHolder {

	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}

}
