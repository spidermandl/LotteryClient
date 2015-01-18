package com.evstudio.thefirstlottery.mobile.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.GlobalUtils;
import com.evstudio.thefirstlottery.mobile.common.Util;

import java.lang.reflect.Field;
import java.util.List;
//import cn.org.farseer.android.Demo;
//import cn.org.farseer.android.R;
//import cn.org.farseer.android.util.GlobalUtils;

/**
 * Author  : 简洋
 * Date    : 2011-9-22
 * <p/>
 * Note    :
 */
public class BottomMenuLayout extends LinearLayout {

    //实例化layout使用的类
    private LayoutInflater mInflater;

    //保存菜单按钮的集合，每一个集合元素代表一个按钮，包含了按钮所需要的信息：图片，文字，按键处理事件。
    private List<BottomButton> bottomButtons;

    //封装菜单按钮的布局。
    private View bottomMenuLayout;

    /**
     * 该值需要根据按钮背景图片来调整。
     */
    public static final int bottom_layoutHeight = 56;
    public static final int actionbar_layoutHeight = 56;

    public BottomMenuLayout(Context context) {
        super(context);
    }

    public BottomMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void processInitButton() {
        //初始化布局，将底部菜单layout加入到视图中。
        initLayout(this.getContext());

        //绑定每一个菜单按钮
        bindingButton();

        //重新计算整个布局的大小，使用整个屏幕的高度减去底部菜单的高度，
        //得出并设置中间页面部分的高度，就能够将菜单固定在底部。
        resizeLayout();
    }

    private void initLayout(Context context) {
        this.mInflater = LayoutInflater.from(context);
        bottomMenuLayout = mInflater.inflate(R.layout.bottom_menu_layout, null);
        addView(bottomMenuLayout);
    }

    private void resizeLayout() {
        View customView = getChildAt(0);
        android.view.ViewGroup.LayoutParams params = customView.getLayoutParams();
        int screenHeight = GlobalUtils.getInstance().getScreenHeight();

//        TypedArray actionbarSizeTypedArray = mInflater.getContext().obtainStyledAttributes(new int[]{
//                android.R.attr.actionBarSize
//        });

//        float h = actionbarSizeTypedArray.getDimension(0, 0);
//
//        actionbar_layoutHeight = (int) h;
        int lessHeight = screenHeight - Util.dip2px( mInflater.getContext(), bottom_layoutHeight )
                - Util.dip2px( mInflater.getContext(), actionbar_layoutHeight ) - getStatusBarHeight();
        params.height = lessHeight;
        customView.setLayoutParams(params);
    }

    private void bindingButton() {
        LinearLayout buttonGroup = (LinearLayout) findViewById(R.id.bottom_menu_button_group_id);

        //初始化底部菜单按钮。
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        if (this.bottomButtons != null && this.bottomButtons.size() > 0) {
            //根据按钮数量计算按钮所占的宽度
            //获取屏幕宽度
            int screenWidth = GlobalUtils.getInstance().getScreenWidth();
            layoutParams.width = screenWidth / (bottomButtons.size());

            //绑定每一个按钮
            for (int i = 0; i < bottomButtons.size(); i++) {
                BottomButton oneButton = bottomButtons.get(i);

                //获取底部菜单按钮布局片段。
                View buttonFrameInstance = mInflater.inflate(R.layout.bottom_menu_button_frame, null);
                TextView templateText = (TextView) buttonFrameInstance.findViewById(R.id.bottom_menu_template_text_id);
                templateText.setText(oneButton.getText());
                //templateText.setBackgroundResource(oneButton.getBackgroundResource());

                ImageView templateImg = (ImageView) buttonFrameInstance.findViewById(R.id.bottom_menu_template_img_id);
                templateImg.setImageResource(oneButton.getBackgroundResource());

                //如果不是第一个按钮，则需要增加分割线
                if (i != 0) {
                    ImageView fenge = new ImageView(buttonFrameInstance.getContext());
                    fenge.setImageResource(R.drawable.line);
                    fenge.setLayoutParams(params);
                    buttonGroup.addView(fenge);
                }

                //将按钮增加到菜单栏
                buttonGroup.addView(buttonFrameInstance);

                //设置监听事件。
                buttonFrameInstance.setOnClickListener(oneButton.getClickListener());

                //设置按钮背景的宽度和背景图片
                LinearLayout templateButtonLayout = (LinearLayout) buttonFrameInstance.findViewById(R.id.bottom_menu_template_button_id);
                templateButtonLayout.setLayoutParams(layoutParams);
//				if(oneButton.isCurrent())
//					templateButtonLayout.setBackgroundResource(R.drawable.tab_two_highlight);
//				else
//					templateButtonLayout.setBackgroundResource(R.drawable.tab_one_normal);
            }
        }

        //默认增加主菜单的按钮
//        View buttonFrameInstance = mInflater.inflate(R.layout.bottom_menu_button_frame, null);
//        TextView templateText = (TextView) buttonFrameInstance.findViewById(R.id.bottom_menu_template_text_id);
//        templateText.setText("主菜单");
//
//        ImageView templateImg = (ImageView) buttonFrameInstance.findViewById(R.id.bottom_menu_template_img_id);
//        templateImg.setImageResource(R.drawable.btm_btn_cgdt);

        //templateText.setBackgroundResource(R.drawable.home);

//        if (this.bottomButtons != null && this.bottomButtons.size() > 0) {
//            ImageView fenge = new ImageView(buttonFrameInstance.getContext());
//            fenge.setImageResource(R.drawable.line);
//            fenge.setLayoutParams(params);
//
//            buttonGroup.addView(fenge);
//        }
//        buttonGroup.addView(buttonFrameInstance);
//
//        LinearLayout templateButtonLayout = (LinearLayout) buttonFrameInstance.findViewById(R.id.bottom_menu_template_button_id);
//        templateButtonLayout.setLayoutParams(layoutParams);
////        templateButtonLayout.setBackgroundResource(R.drawable.tab_one_normal);
//        buttonFrameInstance.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                v.getContext().startActivity(new Intent(v.getContext(), MainActivity.class));
//            }
//        });
    }

    public void setButtonList(List<BottomButton> bottomButtons) {
        this.bottomButtons = bottomButtons;
    }

    private int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
//            Log.d(TAG, "get status bar height fail");
            e1.printStackTrace();
            return 75;
        }
    }


}
