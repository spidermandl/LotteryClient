package com.evstudio.thefirstlottery.mobile.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.actionbarsherlock.app.SherlockFragment;
import com.evstudio.thefirstlottery.mobile.R;

/**
 * Created by ericren on 14-9-19.
 */
public class BuyHistoryFragment extends SherlockFragment implements AdapterView.OnItemClickListener, View.OnClickListener  {
    private LayoutInflater mLayoutInflater;
    private View contextView;
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p/>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        contextView = inflater.inflate(R.layout.layout_buyhistory_adapter, container, false );

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
