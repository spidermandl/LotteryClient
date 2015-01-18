package com.evstudio.thefirstlottery.mobile.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.pojo.FootballGameInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ericren on 14-11-5.
 */
public class FootballMatchAdapter extends BaseAdapter implements View.OnClickListener{
    private static String TAG = "FootballMatchAdapter";
    private HashMap<Integer, View> map = new HashMap<Integer, View>();
    public ArrayList<FootballGameInfo> arrayList;
    private final LayoutInflater mLayoutInflater;
    private Context context;

    private Handler mHandler;

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        TextView t = (TextView) v;
        Boolean checked = (Boolean) t.getTag();
        Log.d(TAG,"checked is " + checked);
        if (!checked) {
            Log.d(TAG,"checked is getCheck " + checked);
            t.setTextColor(mLayoutInflater.getContext().getResources().getColor(R.color.white));
            t.setBackgroundColor(mLayoutInflater.getContext().getResources().getColor(R.color.blue));
            checked = true;
//            MyToast.instance.showToast(context, mLayoutInflater, "check = true");
        } else {
            Log.d(TAG,"checked is getCheck 2 " + checked);
            t.setTextColor(mLayoutInflater.getContext().getResources().getColor(R.color.black));
            t.setBackgroundResource(R.drawable.footselectitembg); //@drawable/footselectitembg
            checked = false;
//            MyToast.instance.showToast(context, mLayoutInflater, "this check = false");
        }
        t.setTag(checked);
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        TextView t = (TextView) view;
//        Boolean checked = (Boolean) t.getTag();
//        Log.d(TAG,"checked is " + checked);
//        if (!checked) {
//            Log.d(TAG,"checked is getCheck " + checked);
//            t.setTextColor(mLayoutInflater.getContext().getResources().getColor(R.color.white));
//            t.setBackgroundColor(mLayoutInflater.getContext().getResources().getColor(R.color.blue));
//            checked = true;
////            MyToast.instance.showToast(context, mLayoutInflater, "check = true");
//        } else {
//            Log.d(TAG,"checked is getCheck 2 " + checked);
//            t.setTextColor(mLayoutInflater.getContext().getResources().getColor(R.color.black));
//            t.setBackgroundResource(R.drawable.footselectitembg); //@drawable/footselectitembg
//            checked = false;
////            MyToast.instance.showToast(context, mLayoutInflater, "this check = false");
//        }
//        t.setTag(checked);
//        t.refreshDrawableState();
//    }

    class ViewHolder {
        TextView tvFbDate;
        TextView tvFbMatch;
        TextView tvFbTime;
        TextView tvFbWeather;
        TextView tvFbHteam;
        TextView tvFbHOdds;
        TextView tvFbDOdds;
        TextView tvFbLTeam;
        TextView tvFbLOdds;
        TextView tvFbHHOdds;
        TextView tvFbHDOdds;
        TextView tvFbHLOdds;
        FootballGameInfo fbGameInfo;
    }

    public FootballMatchAdapter(Context ctx, ArrayList<FootballGameInfo> list, Handler handler) {
//        super();
        this.context = ctx;
        this.mLayoutInflater = LayoutInflater.from(ctx);
        this.arrayList = list;
        mHandler = handler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return arrayList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, android.view.ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (null == map.get(position)) {
            convertView = mLayoutInflater.inflate(R.layout.item_football_detail, parent, false);
            vh = new ViewHolder();
            vh.tvFbDate = (TextView) convertView.findViewById(R.id.tvFbDate);
            vh.tvFbMatch = (TextView) convertView.findViewById(R.id.tvFbMatch);
            vh.tvFbTime = (TextView) convertView.findViewById(R.id.tvFbTime);
            vh.tvFbWeather = (TextView) convertView.findViewById(R.id.tvFbWeather);
            vh.tvFbHteam = (TextView) convertView.findViewById(R.id.tvFbHteam);
            vh.tvFbHOdds = (TextView) convertView.findViewById(R.id.tvFbHOdds);
            vh.tvFbDOdds = (TextView) convertView.findViewById(R.id.tvFbDOdds);
            vh.tvFbLTeam = (TextView) convertView.findViewById(R.id.tvFbLteam);
            vh.tvFbLOdds = (TextView) convertView.findViewById(R.id.tvFbLOdds);
            vh.tvFbHHOdds = (TextView) convertView.findViewById(R.id.tvFbHHOdds);
            vh.tvFbHDOdds = (TextView) convertView.findViewById(R.id.tvFbHDOdds);
            vh.tvFbHLOdds = (TextView) convertView.findViewById(R.id.tvFbHLOdds);
            FootballGameInfo footballGameInfo = (FootballGameInfo) this.getItem(position);
            vh.fbGameInfo = footballGameInfo;
            map.put( position, convertView );
            convertView.setTag(vh);
        } else {
            convertView = map.get(position);
            vh = (ViewHolder) convertView.getTag();
        }

        vh.tvFbDate.setText(vh.fbGameInfo.gameNumber);
        vh.tvFbMatch.setText(vh.fbGameInfo.matchName);
        vh.tvFbMatch.setBackgroundColor(android.graphics.Color.parseColor("#" + vh.fbGameInfo.backgroundColor));

        vh.tvFbTime.setText(vh.fbGameInfo.beginTime);
        vh.tvFbWeather.setText(vh.fbGameInfo.weather);
        vh.tvFbHteam.setText(vh.fbGameInfo.hostTeam);
        vh.tvFbLTeam.setText(vh.fbGameInfo.clientTeam);

        vh.tvFbHOdds.setText(String.valueOf(vh.fbGameInfo.oddsHost));
        vh.tvFbHOdds.setTag(false);

        vh.tvFbHOdds.setOnClickListener(this);


        vh.tvFbDOdds.setText(String.valueOf(vh.fbGameInfo.oddsDraw));
        vh.tvFbDOdds.setTag(false);
        vh.tvFbDOdds.setOnClickListener(this);

        vh.tvFbLOdds.setText(String.valueOf(vh.fbGameInfo.oddsClient));
        vh.tvFbLOdds.setTag(false);
        vh.tvFbLOdds.setOnClickListener(this);

        vh.tvFbHHOdds.setText("主 " + String.valueOf(vh.fbGameInfo.assignor)
                + " 胜 " + String.valueOf(vh.fbGameInfo.oddsAssignHost));
        vh.tvFbHHOdds.setTag(false);
        vh.tvFbHHOdds.setOnClickListener(this);

        vh.tvFbHDOdds.setText("平 " + String.valueOf(vh.fbGameInfo.oddsAssignDraw));
        vh.tvFbHDOdds.setTag(false);
        vh.tvFbHDOdds.setOnClickListener(this);

        vh.tvFbHLOdds.setText("负 " + String.valueOf(vh.fbGameInfo.oddsAssignClient));
        vh.tvFbHLOdds.setTag(false);
        vh.tvFbHLOdds.setOnClickListener(this);

        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
