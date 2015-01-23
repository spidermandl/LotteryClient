package com.evstudio.thefirstlottery.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.evstudio.thefirstlottery.mobile.R;

/**
 * Created by ericren on 14-9-19.
 * 胡莹莹注释
 * 彩民投注的彩票历史记录适配器
 */
public class BuyHistoryAdapter extends ArrayAdapter<String[]> {
    private static String TAG = "BUYHISTORY";
    private LayoutInflater layoutInflater;
    private Context thisContext;

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     */
    public BuyHistoryAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.thisContext = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return 0;
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
        ViewHolder vh;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_buyhistory_adapter, parent, false);
            vh = new ViewHolder();
            vh.tvHisBetContent = (TextView) convertView.findViewById(R.id.tv_his_betcontent);
            vh.tvHisType = (TextView) convertView.findViewById(R.id.tv_his_type);
            vh.tvHisBetType = (TextView) convertView.findViewById(R.id.tv_his_bettype);
            vh.tvHisPeriods = (TextView) convertView.findViewById(R.id.tv_his_periods);
            vh.tvHisBetCount = (TextView) convertView.findViewById(R.id.tv_his_betcount);
            vh.tvHisState = (TextView) convertView.findViewById(R.id.tv_his_state);
            vh.ivPrize = (ImageView) convertView.findViewById(R.id.ivPrize);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        String[] strs = getItem(position);

        vh.tvHisType.setText("上海11选5");
        vh.tvHisPeriods.setText(strs[0]);
        String betType = "";
        switch (Integer.parseInt(strs[1])) {
            case 3:
                betType = "任选三";
                break;
            case 5:
                betType = "任选五";
                break;
            case 7:
                betType = "任选七";
                break;
            case 8:
                betType = "任选八";
                break;
            default:
                break;
        }
        vh.tvHisBetType.setText(betType);
        vh.tvHisBetContent.setText(strs[2]);
        vh.tvHisBetCount.setText(strs[3]);
        String betState = "未中奖";
        boolean isPrize = false;//是否中奖；
        if (null != strs[5] && !"".equals(strs[5])) {
            if ("1".equals(strs[5]))
                betState = "出票中";
            else if ("2".equals(strs[5])) {
                if (null == strs[4] || "null".equals(strs[4]) || "".equals(strs[4]))
                    betState = "投注成功";
                else if( "0".equals(strs[4])){
                    betState = "未中奖";
                }
                else if( "1".equals(strs[4])){
                    betState = "已中奖，中奖金额" + strs[6];
                    isPrize = true;
                }
            }else if ("3".equals(strs[5])) {
                betState = "出票未成功";
            }
        }
        vh.tvHisState.setText(betState);
        vh.ivPrize.setVisibility(isPrize ? View.VISIBLE : View.GONE);

        return convertView;

    }

    class ViewHolder {
        TextView tvHisType;
        TextView tvHisBetType;
        TextView tvHisPeriods;
        TextView tvHisBetContent;
        TextView tvHisBetCount;
        TextView tvHisState;
        ImageView ivPrize;
    }
}
