package com.evstudio.thefirstlottery.mobile.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.Sh11x5LvHistoryAdapter;
import com.tandong.sa.json.Gson;
import com.tandong.sa.json.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ericren on 14-10-25.
 */
public class Sh11x5DrawFragment extends SherlockFragment {
    private LayoutInflater mInflater;
    private View contextView;
    private String data;
    private ArrayList<String[]> dataList;
    private ListView lvDraw;
    private Sh11x5LvHistoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
        mInflater = inflater;
        contextView = inflater.inflate(R.layout.fragment_sh11x5_history, container, false);
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
        lvDraw = (ListView) contextView.findViewById(R.id.fmLvSh11x5History);
        adapter = new Sh11x5LvHistoryAdapter(inflater.getContext(), R.id.fmlSh11x5History);
        lvDraw.setAdapter(adapter);

        for (int i = 0; i < dataList.size(); i++)
            adapter.add(dataList.get(i));

        adapter.notifyDataSetChanged();

        return contextView;
    }
}