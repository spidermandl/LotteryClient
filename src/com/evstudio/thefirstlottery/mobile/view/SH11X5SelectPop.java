package com.evstudio.thefirstlottery.mobile.view;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.evstudio.thefirstlottery.mobile.R;

/**
 * Created by zyn on 15/1/9.
 */
public class SH11X5SelectPop implements View.OnClickListener {
    public static SH11X5SelectPop instance;
    public View view;

    private LinearLayout llPop1, llPop2, llPop3, llPop4, llPop5, llPop6, llPop7, llPop8, llPop9,llPop10,llPop11,llPop12;
    private ImageView ivPop1, ivPop2, ivPop3, ivPop4, ivPop5, ivPop6, ivPop7, ivPop8, ivPop9,ivPop10,ivPop11,ivPop12;

    private LinearLayout[] lPops = {llPop1, llPop2, llPop3, llPop4, llPop5, llPop6, llPop7, llPop8, llPop9,llPop10,llPop11,llPop12};
    private ImageView[] iPops = {ivPop1, ivPop2, ivPop3, ivPop4, ivPop5, ivPop6, ivPop7, ivPop8, ivPop9,ivPop10,ivPop11,ivPop12};

    private int[] lResIds = {R.id.llPop1, R.id.llPop2, R.id.llPop3, R.id.llPop4, R.id.llPop5, R.id.llPop6, R.id.llPop7, R.id.llPop8, R.id.llPop9,R.id.llPop10,R.id.llPop11,R.id.llPop12};
    private int[] iResIds = {R.id.ivPop1, R.id.ivPop2, R.id.ivPop3,R.id.ivPop4, R.id.ivPop5, R.id.ivPop6, R.id.ivPop7, R.id.ivPop8, R.id.ivPop9,R.id.ivPop10,R.id.ivPop11,R.id.ivPop12};

    private String[] mSelectTexts = {"任选一", "任选二", "任选三","任选四", "任选五",
            "任选六", "任选七", "任选八", "组选前二", "组选前三", "直选前二", "直选前三"};

    private SH11X5UpdateCallback callback;

    private int selectIndex = 0; // 当前选择玩法

    public SH11X5SelectPop(View popupWindow){
        view = popupWindow;
    }

    public static SH11X5SelectPop newInstance(View popupWindow){
        if (instance == null){
            instance = new SH11X5SelectPop(popupWindow);
        }
        return instance;
    }

    public static boolean checkInstance(){
        if (instance == null){
            return false;
        }
        return true;
    }

    /** 初始化 */
    public void initialize(){

        for (int i=0;i<lResIds.length;i++){
            lPops[i] = (LinearLayout) view.findViewById(lResIds[i]);
            iPops[i] = (ImageView) view.findViewById(iResIds[i]);
            lPops[i].setOnClickListener(this);
        }

        Log.d("#####################################   ",selectIndex +"");

        setSelected(selectIndex);
    }

    @Override
    public void onClick(View view) {
        for (int i = 0; i < lResIds.length; i++) {
            if (view.getId() == lResIds[i]){
                selectIndex = i;
                setSelected(selectIndex);
                callback.update(i);
                return;
            }
        }

    }

    /** 更新玩法选中状态 */
    private void setSelected(int index){
        for (int i = 0; i < lResIds.length; i++){

            if (index == i){
                iPops[i].setVisibility(View.VISIBLE);

            }else {
                iPops[i].setVisibility(View.GONE);
            }
        }
    }

    /** 玩法切换更新回调 */
    public interface SH11X5UpdateCallback{
        void update(int selectIndex);
    }
    
    public void setSH11X5UpdateCallback(SH11X5UpdateCallback callback){
        this.callback = callback;
    }

    /** 获取当前玩法 */
    public int getSelectIndex(){
        return selectIndex;
    }

    /** 设置当前玩法 */
    public void setSelectIndex(int index){
        this.selectIndex = index;
    }

    /** 释放单例 */
    public static void release(){
        if (instance !=null){
            instance = null;
        }
    }
}
