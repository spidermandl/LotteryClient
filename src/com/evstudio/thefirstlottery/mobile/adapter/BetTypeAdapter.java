package com.evstudio.thefirstlottery.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.core.BetItem;

import java.util.List;

/**
 * Created by ericren on 14-9-12.
 */
public class BetTypeAdapter extends BaseAdapter{

    private static final String TAG = "BetTypeAdapter";
    private Context mContext;
    private final LayoutInflater mLayoutInflater;
    public List<BetItem> betItems;
    private ViewHolder vh;

    static class ViewHolder{
        ImageView ivLogo;
        TextView tvTitle;
        TextView tvLastDraw;
        TextView tvNextTime;
    }

    public BetTypeAdapter( final Context context, List<BetItem> items ){
        super();
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        betItems = items;
    }
    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return betItems.size();
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
        return betItems.get(position);
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
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.layout_betitem, parent, false);
            vh = new ViewHolder();
            vh.ivLogo = (ImageView)convertView.findViewById(R.id.ivLogo );
            vh.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle );
            vh.tvLastDraw= (TextView)convertView.findViewById(R.id.tvLastDraw );
            vh.tvNextTime= (TextView)convertView.findViewById(R.id.tvNextTime );
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        BetItem item = (BetItem )getItem(position);
        vh.ivLogo.setImageResource(item.getiDrawable());
        vh.tvTitle.setText( item.getStrTilte() );
        vh.tvLastDraw.setText( item.getLastDraw() );
        vh.tvNextTime.setText( item.getNextTime() );
//        this.run();

        return convertView;
    }

}
