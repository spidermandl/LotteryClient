package com.evstudio.thefirstlottery.mobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.SampleAdapter;
import com.evstudio.thefirstlottery.mobile.common.Constants;
import com.tandong.sa.json.Gson;
import com.tandong.sa.json.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ericren on 14-10-25.
 */
public class Sh11x5DrawZs extends Fragment implements View.OnClickListener {
    private LayoutInflater mInflater;
    private View contextView;
    private String data;
    private ArrayList<String[]> dataList;
    private ListView lvDraw;
    public SampleAdapter adapter;

    public TextView tv_periods,tv_number1,tv_number2,tv_number3,tv_number4,tv_number5,tv_number6,tv_number7,tv_number8,tv_number9,tv_number10,tv_number11;
    public TextView[] widgets = {tv_number1,tv_number2,tv_number3,tv_number4,tv_number5,tv_number6,tv_number7,tv_number8,tv_number9,tv_number10,tv_number11};
    public int[] resIds = {R.id.tv_number1,R.id.tv_number2,R.id.tv_number3,R.id.tv_number4,R.id.tv_number5,R.id.tv_number6,R.id.tv_number7,R.id.tv_number8,R.id.tv_number9,R.id.tv_number10,R.id.tv_number11};
//    public int[] selectCircle = {0,0,0,0,0,0,0,0,0,0,0}; // 0未选 1选中
    private CancelHistoryCallback callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
        mInflater = inflater;
        contextView = inflater.inflate(R.layout.fragment_sh11x5_list, container, false);
        Bundle bundle = getArguments();

        data = bundle.getString("data");
        if (null != data && !"".equals(data)) {
            Gson gson = new Gson();
            dataList = gson.fromJson(data,
                    new TypeToken<ArrayList<String[]>>() {
                    }.getType()
            );

            Collections.reverse(dataList);
        }
        lvDraw = (ListView) contextView.findViewById(R.id.fmLvSh11x5HistoryList);
        adapter = new SampleAdapter(inflater.getContext(), R.id.fmLvSh11x5HistoryList);
        lvDraw.setAdapter(adapter);

        for (int i = 0; i < dataList.size(); i++)
            adapter.add(dataList.get(i));

        adapter.notifyDataSetChanged();

        initializeView();

        return contextView;
    }

    public void ListNumberUpdate(String responseData){
        data = responseData;
        if (null != data && !"".equals(data)) {
            Gson gson = new Gson();
            dataList = gson.fromJson(data,
                    new TypeToken<ArrayList<String[]>>() {
                    }.getType()
            );
            adapter.clear();
            for (int i = 0; i < dataList.size(); i++)
                adapter.add(dataList.get(i));

            Collections.reverse(dataList);
        }

        adapter.notifyDataSetChanged();
    }

    public void initializeView(){
        tv_periods = (TextView) contextView.findViewById(R.id.tv_periods);
        tv_periods.setOnClickListener(this);
        for (int i = 0; i < widgets.length; i++) {
            widgets[i] = (TextView) contextView.findViewById(resIds[i]);
            widgets[i].setTag(i);
            widgets[i].setOnClickListener(this);

            if (Constants.SH11X5SelectNums[i] == 1){
                widgets[i].setSelected(true);
                widgets[i].setBackgroundResource(R.drawable.ball_red);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_periods:
                if (callback!= null){
                    callback.cancelHistory();
                }
                break;

            case R.id.tv_number1:
                setSelectCircle(0, (TextView) view);
                break;

            case R.id.tv_number2:
                setSelectCircle(1, (TextView) view);
                break;

            case R.id.tv_number3:
                setSelectCircle(2, (TextView) view);
                break;

            case R.id.tv_number4:
                setSelectCircle(3, (TextView) view);
                break;

            case R.id.tv_number5:
                setSelectCircle(4, (TextView) view);
                break;

            case R.id.tv_number6:
                setSelectCircle(5, (TextView) view);
                break;

            case R.id.tv_number7:
                setSelectCircle(6, (TextView) view);
                break;

            case R.id.tv_number8:
                setSelectCircle(7, (TextView) view);
                break;

            case R.id.tv_number9:
                setSelectCircle(8, (TextView) view);
                break;

            case R.id.tv_number10:
                setSelectCircle(9, (TextView) view);
                break;

            case R.id.tv_number11:
                setSelectCircle(10, (TextView) view);
                break;
        }
    }

    public void setSelectCircle(int index,TextView tv){
        if (Constants.SH11X5SelectNums[index] == 0){
            tv.setBackgroundResource(R.drawable.ball_red);
            Constants.SH11X5SelectNums[index] = 1;
        }else {
            tv.setBackgroundResource(0);
            Constants.SH11X5SelectNums[index] = 0;
        }
    }

    /** 投注回调接口 */
    public interface CancelHistoryCallback{
        void cancelHistory();
    }
    
    public void setcancelHistoryCallback(CancelHistoryCallback callback){
        this.callback = callback;
    }

}
