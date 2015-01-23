package com.evstudio.thefirstlottery.mobile.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.activity.*;
import com.tandong.sa.tools.AssistTool;

import java.util.ArrayList;

/**
 * Created by ericren on 14-10-8.
 * 胡莹莹注释
 * 热门彩票适配器
 */
public class PlayItemAdapter extends BaseAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context c;
    private AssistTool assistTool;

    public PlayItemAdapter(Context context, ArrayList<String> strings) {
        this.c = context;
        this.list = strings;
        assistTool = new AssistTool(c);
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
        boolean isSelected;
        boolean isActived;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return list.size();
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
        return list.get(position);
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(c, R.layout.play_menu, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tvPlayItem);
            holder.imageView = (ImageView) convertView.findViewById(R.id.ivPlayItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String s = list.get(position);
        holder.textView.setText(s);

        Drawable drawableHome = null;

        if (s.contains("上海11选5")) {
            drawableHome = c.getResources().getDrawable(R.drawable.app_icon_new_01);
        } else if (s.contains("大乐透")) {
            drawableHome = c.getResources().getDrawable(R.drawable.app_icon_new_03);
        } else if (s.contains("竞彩足球")) {
            drawableHome = c.getResources().getDrawable(R.drawable.app_icon_new_04);

        } else if (s.contains("竞彩篮球")) {
            drawableHome = c.getResources().getDrawable(R.drawable.app_icon_new_02);

        } else if (s.contains("排列三排列五")) {
            drawableHome = c.getResources().getDrawable(R.drawable.app_icon_new_05);

        } else if (s.contains("足球单场")) {
            drawableHome = c.getResources().getDrawable(R.drawable.n_icon_zqdc_gray);

        } else if (s.contains("快乐扑克")) {
            drawableHome = c.getResources().getDrawable(R.drawable.n_icon_klpk_gray);

        } else if (s.contains("更多彩种")) {
            drawableHome = c.getResources().getDrawable(R.drawable.app_icon_new_06);

        }

        holder.imageView.setImageDrawable(drawableHome);
        if (s.contains("上海11选5")) {

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    assistTool.gotoActivity(Sh11x5BetActivity.class, false );
                    assistTool.gotoActivity(SH1X5Activity.class,false);
                }
            });
        }else if (s.contains("大乐透")) {

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assistTool.gotoActivity(NewDltActivity.class, false );
                }
            });
        }else if (s.contains("竞彩足球")) {

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    assistTool.gotoActivity(ActivityFootBallSfp.class, false );
                }
            });
        }else if (s.contains("竞彩篮球")) {

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    AssistTool assistTool = new AssistTool(c);
//                    assistTool.gotoActivity(ActivityBasketBallSfp.class, false );
                }
            });
        }

        return convertView;
    }
}
