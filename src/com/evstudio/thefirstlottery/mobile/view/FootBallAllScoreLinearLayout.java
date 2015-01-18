package com.evstudio.thefirstlottery.mobile.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.pojo.FootballInfoMix;

import java.util.List;

/**
 * Created by zyn on 15/1/14.
 */
public class FootBallAllScoreLinearLayout extends LinearLayout {
    View frameView;
    TextView tvAll1,tvAll2,tvAll3,tvAll4,tvAll5,tvAll6,tvAll7,tvAll8,tvAll9,tvAll10,tvAll11,tvAll12,tvAll13,tvAll14,tvAll15,tvAll16,tvAll17,tvAll18,tvAll19,tvAll20,tvAll21,tvAll22,tvAll23,tvAll24,tvAll25,tvAll26,tvAll27,tvAll28,tvAll29,tvAll30,tvAll31;
    TextView[] mTextViews = {tvAll1,tvAll2,tvAll3,tvAll4,tvAll5,tvAll6,tvAll7,tvAll8,tvAll9,tvAll10,tvAll11,tvAll12,tvAll13,tvAll14,tvAll15,tvAll16,tvAll17,tvAll18,tvAll19,tvAll20,tvAll21,tvAll22,tvAll23,tvAll24,tvAll25,tvAll26,tvAll27,tvAll28,tvAll29,tvAll30,tvAll31};
    int[] mResIds = {R.id.tvAll1,R.id.tvAll2,R.id.tvAll3,R.id.tvAll4,R.id.tvAll5,R.id.tvAll6,R.id.tvAll7,R.id.tvAll8,R.id.tvAll9,R.id.tvAll10,R.id.tvAll11,R.id.tvAll12,R.id.tvAll13,R.id.tvAll14,R.id.tvAll15,R.id.tvAll16,R.id.tvAll17,R.id.tvAll18,R.id.tvAll19,R.id.tvAll20,R.id.tvAll21,R.id.tvAll22,R.id.tvAll23,R.id.tvAll24,R.id.tvAll25,R.id.tvAll26,R.id.tvAll27,R.id.tvAll28,R.id.tvAll29,R.id.tvAll30,R.id.tvAll31};
    String[] mTexts = {"1:0\n","2:0\n","2:1\n","3:0\n","3:1\n","3:2\n","4:0\n","4:1\n","4:2\n","5:0\n","5:1\n","5:2\n","胜其他\n","0:0\n","1:1\n","2:2\n","3:3\n","平其他\n","0:1\n","0:2\n","1:2\n","0:3\n","1:3\n","2:3\n","0:4\n","1:4\n","2:4\n","0:5\n","1:5\n","2:5\n","负其他\n",};
    public FootballInfoMix infoMix;

    public FootBallAllScoreLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }

    public FootBallAllScoreLinearLayout(Context context) {
        super(context);
        initializeView(context);
    }

    private void initializeView(Context context){
        frameView = LayoutInflater.from(context)
                .inflate(R.layout.football_all_linearlayout, this);

    }

    public void initializeData(List<String> dataList){
        for (int i=0;i<mTextViews.length;i++){
            mTextViews[i] = (TextView) frameView.findViewById(mResIds[i]);
            mTextViews[i].setText(mTexts[i]+dataList.get(i));
            mTextViews[i].setTag(i + 15);
            mTextViews[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(v);
                }
            });
            if( infoMix.selected[i + 6] == 1)
                setSelected(mTextViews[i]);

        }
    }

    public void setSelected( View v ){
        Resources resources = this.getResources();
        Drawable drawable;
        TextView textView = (TextView) v;
        int index = (Integer) textView.getTag();
        if( v.isSelected() ){
            drawable = resources.getDrawable(R.color.white);
            v.setSelected(false);
            infoMix.selected[index] = 0;
        }else{
            drawable = resources.getDrawable(R.color.blue);
            v.setSelected(true);
            infoMix.selected[index] = 1;
        }
        v.setBackgroundDrawable(drawable);
    }

}
