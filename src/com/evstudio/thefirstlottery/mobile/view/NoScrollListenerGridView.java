/**
 * 
 */
package com.evstudio.thefirstlottery.mobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 
 * @author zhuoyong
 * @data ����ʱ�䣺2014-3-27 ����3:56:09
 */
public class NoScrollListenerGridView extends GridView {

	public NoScrollListenerGridView(Context context) {
		super(context);

	}

	public NoScrollListenerGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollListenerGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}  

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

}
