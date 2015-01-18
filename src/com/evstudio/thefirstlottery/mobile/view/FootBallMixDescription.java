package com.evstudio.thefirstlottery.mobile.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.pojo.FootballInfoMix;
import com.tandong.sa.eventbus.EventBus;
import com.tandong.sa.json.Gson;
import com.tandong.sa.tools.AssistTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyn on 15/1/14.
 */
public class FootBallMixDescription extends Activity implements View.OnClickListener {
    private Button btnFinish;
    private LinearLayout frame;
    private FootballInfoMix infoMix;
    private int gameIndex;

    private AssistTool assistTool;

    /** 比赛双方 */
    private TextView tvHomeName ; // 主场队
    private TextView tvName ; // 客场队

    private TextView tvRangqiu;

    /** 胜平负 */
    private TextView tvWin,tvDraw,tvLose;

    /** 让球胜平负 */
    private TextView tvGiveWin,tvGiveDraw,tvGiveLose;

    /** 全场比分 */
    private FootBallAllScoreLinearLayout llAllScore;

    /** 总进球数量 */
    private TextView tvAllBall1,tvAllBall2,tvAllBall3,tvAllBall4,tvAllBall5,tvAllBall6,tvAllBall7,tvAllBall8;
    private TextView mTextViews[] = {tvAllBall1,tvAllBall2,tvAllBall3,tvAllBall4,tvAllBall5,tvAllBall6,tvAllBall7,tvAllBall8};
    private int mResIds[] = {R.id.tvAllBall1,R.id.tvAllBall2,R.id.tvAllBall3,R.id.tvAllBall4,R.id.tvAllBall5,R.id.tvAllBall6,R.id.tvAllBall7,R.id.tvAllBall8};

    /** 半全场 */
    private TextView tvAllHalf1,tvAllHalf2,tvAllHalf3,tvAllHalf4,tvAllHalf5,tvAllHalf6,tvAllHalf7,tvAllHalf8,tvAllHalf9;
    private TextView mHalfTextViews[] = {tvAllHalf1,tvAllHalf2,tvAllHalf3,tvAllHalf4,tvAllHalf5,tvAllHalf6,tvAllHalf7,tvAllHalf8,tvAllHalf9};
    private int mHalfResIds[] = {R.id.tvAllHalf1,R.id.tvAllHalf2,R.id.tvAllHalf3,R.id.tvAllHalf4,R.id.tvAllHalf5,R.id.tvAllHalf6,R.id.tvAllHalf7,R.id.tvAllHalf8,R.id.tvAllHalf9};

    private String halfTip[] = {"胜-胜\n","胜-平\n","胜-负\n","平-胜\n","平-平\n","平-负\n","负-胜\n","负-平\n","负-负\n"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.football_mix_description);

        assistTool = new AssistTool(this);

        infoMix = (FootballInfoMix)getIntent().getExtras().getSerializable("gameinfo");
        gameIndex =( (Integer)getIntent().getExtras().getSerializable("gameIndex")).intValue();
        initializeView();
        initializeData();
    }

    /** 视图初始化 */
    private void initializeView(){
        btnFinish = (Button) findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(this);
        frame = (LinearLayout) findViewById(R.id.frame);
        WindowManager wm = (WindowManager)
                getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        frame.setLayoutParams(new LinearLayout.LayoutParams(width-36, height-100));


        tvHomeName = (TextView) findViewById(R.id.tvHomeName);
        tvName = (TextView) findViewById(R.id.tvName);

        tvRangqiu = (TextView)findViewById(R.id.tvRangqiu);

        tvWin = (TextView) findViewById(R.id.tvWin);
        tvDraw = (TextView) findViewById(R.id.tvDraw);
        tvLose = (TextView) findViewById(R.id.tvLose);


        tvGiveWin = (TextView) findViewById(R.id.tvGiveWin);
        tvGiveDraw = (TextView) findViewById(R.id.tvGiveDraw);
        tvGiveLose = (TextView) findViewById(R.id.tvGiveLose);

        llAllScore = (FootBallAllScoreLinearLayout) findViewById(R.id.llAllScore);
        llAllScore.infoMix = infoMix;

        for (int i=0;i<mTextViews.length;i++){
            mTextViews[i] = (TextView)findViewById(mResIds[i]);
        }
        for (int i=0;i<mHalfTextViews.length;i++){
            mHalfTextViews[i] = (TextView)findViewById(mHalfResIds[i]);
        }
    }

    /** 接口数据填充 */
    private void initializeData(){
        //  TODO 进行 /** 比赛双方 */ /** 胜平负 */ /** 让球胜平负 */ /** 总进球数量 */  /** 半全场 */ 的数据填充
        // TODO .................
        // TODO 直接填充 不写例子了
        tvHomeName.setText( "[主]" + infoMix.homeTeam);
        tvName.setText("[客]" + infoMix.awayTeam);

        tvWin.setText("胜" + infoMix.oriPl[0]);
        tvWin.setTag(0);
        tvWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(v);
            }
        });
        if( infoMix.selected[0] == 1)
            setSelected(tvWin);
        tvDraw.setText("平" + infoMix.oriPl[1]);
        tvDraw.setTag(1);
        tvDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(v);
            }
        });
        if( infoMix.selected[1] == 1)
            setSelected(tvDraw);

        tvLose.setText("负" + infoMix.oriPl[2]);
        tvLose.setTag(2);
        tvLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(v);
            }
        });
        if( infoMix.selected[2] == 1)
            setSelected(tvLose);

        tvRangqiu.setText("让球胜负平("+infoMix.odd+")");

        tvGiveWin.setText("胜" + infoMix.oddPl[0]);
        tvGiveWin.setTag(3);
        tvGiveWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(v);
            }
        });
        if( infoMix.selected[3] == 1)
            setSelected(tvGiveWin);

        tvGiveDraw.setText("平" + infoMix.oddPl[1]);
        tvGiveDraw.setTag(4);
        tvGiveDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(v);
            }
        });
        if( infoMix.selected[4] == 1)
            setSelected(tvGiveDraw);

        tvGiveLose.setText("负" + infoMix.oddPl[2]);
        tvGiveLose.setTag(5);
        tvGiveLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelected(v);
            }
        });
        if( infoMix.selected[5] == 1)
            setSelected(tvGiveLose);

        // TODO  /**全场比分 */ 数据填充
        // TODO ！！！ 请将全场比分的数据按照设计图上的全场比分从左到右顺序组成一个ArrayList<String> 然后执行initializeData(List<>)
        // TODO 方法传进去 下面是测试例子 ！！！
        List<String> dataList = new ArrayList<String>();
        for (int i=0;i<31;i++){
            dataList.add(infoMix.scores[i]+"");
        }
        llAllScore.initializeData(dataList);

        for( int i = 0; i < mTextViews.length; i ++ ){
            TextView tv = mTextViews[i];
            tv.setTag(i + 23);
            if( i < 6) {
                tv.setText(String.valueOf(i+1) + "\n" + infoMix.scores[i]);
            }else if( i == 6 ){
                tv.setText("7+\n" + infoMix.scores[i]);
            }
            tv.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(v);
                }
            });
            if( infoMix.selected[i + 23] == 1)
                setSelected(tv);
        }

        for( int i = 0; i < mHalfTextViews.length; i ++ ){
            TextView tv = mHalfTextViews[i];
            tv.setText( halfTip[i] + infoMix.halfWin[i]);
            tv.setTag(i + 6);
            tv.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(v);
                }
            });
            if( infoMix.selected[i + 6] == 1)
                setSelected(tv);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnFinish:
                if( infoMix.isSelected() ){
                    Gson gson = new Gson();
                    String value = gson.toJson(infoMix);
                    EventBus.getDefault().post("infoMix##" + String.valueOf(gameIndex) + "##"
                            + value);
                }
                finish();
                break;
            default:
                break;
        }
    }
}
