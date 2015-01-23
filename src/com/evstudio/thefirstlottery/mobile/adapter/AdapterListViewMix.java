package com.evstudio.thefirstlottery.mobile.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.evstudio.thefirstlottery.mobile.pojo.FootballInfoMix;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by eric on 15/1/3.
 * 
 */
public class AdapterListViewMix extends BaseAdapter {

    private ArrayList<FootballInfoMix> mArrayList = new ArrayList<FootballInfoMix>();
    private HashMap<Integer, View> map = new HashMap<Integer, View>();
    private final LayoutInflater mLayoutInflater;
    private Context mContext;
    private Handler mHandler;
    public HashMap<Integer, FootballInfoMix> selectedItems = new HashMap<Integer, FootballInfoMix>();


    public AdapterListViewMix(Context ctx, ArrayList<FootballInfoMix> list, Handler handler) {
        mArrayList = list;
        mContext = ctx;
        mLayoutInflater = LayoutInflater.from(mContext);
        mHandler = handler;
    }


    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    class ViewHolder{

    }
}
