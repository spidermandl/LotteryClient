package com.evstudio.thefirstlottery.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.Constants;

import java.util.ArrayList;

/**
 * Created by ericren on 14-9-12.
 * 胡莹莹注释
 * 上海11选5中供彩民选择的11个号码适配器
 */
public class BetItemAdapter extends BaseAdapter {
    private ArrayList<String> list;
    public String[] strResult = new String[]{"","","","","","","","","","",""};
    private Context context;
    private final LayoutInflater mLayoutInflater;

    private final String TAG="BETITEM";

    public BetItemAdapter(Context ctx, ArrayList<String> list) {
        this.context = ctx;
        this.list = list;
        mLayoutInflater = LayoutInflater.from(ctx);
    }

//    public void updateHistoryBack(){
//        for (int i = 0; i < Constants.SH11X5SelectNums.length; i++) {
//            if (Constants.SH11X5SelectNums[i] == 1){
//                strResult[i] = (i+1)+"";
//            }
//        }
//        this.notifyDataSetChanged();
//    }

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
        if( null == convertView ){
            convertView = mLayoutInflater.inflate(R.layout.item_benumber, parent, false );
            vh = new ViewHolder();
            vh.textView = (TextView)convertView.findViewById(R.id.tvBetItem);

            convertView.setTag( vh );
        }else
          vh = (ViewHolder) convertView.getTag();

        String item = (String)getItem(position);
        //判断当前11个号码是否选中
        if (Constants.SH11X5SelectNums[position] == 1){  //选中
            vh.textView.setSelected(true);
            strResult[position] = (position+1)+"";
        }else {                                         //未选中
            vh.textView.setSelected(false);
            strResult[position] = "";
        }

        vh.textView.setText(item);
        vh.textView.setTag(position);
        vh.textView.setOnClickListener( new View.OnClickListener() {   //监听彩民选中的号码
            @Override
            public void onClick(View v) {
                final int index =  (Integer)v.getTag();

                v.setSelected( !v.isSelected() );
                String str = (String) ((TextView)v).getText();
                if( v.isSelected() ){
                    strResult[Integer.parseInt(str) - 1] = str;
                    Constants.SH11X5SelectNums[index] = 1;
                }else {
                    strResult[Integer.parseInt(str) - 1] = "";
                    Constants.SH11X5SelectNums[index] = 0;
                }
            }
        });

        return convertView;
    }

    static class ViewHolder{
        TextView textView;
    }
}
