package com.evstudio.thefirstlottery.mobile.common;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.evstudio.thefirstlottery.mobile.R;

/**
 * Created by ericren on 14-9-17.
 */
public class MyToast {
    public static MyToast instance = new MyToast();

    public void showToast( Context context, LayoutInflater layoutInflater, String message ){
        View toastView = layoutInflater.inflate(R.layout.layout_toast, null );
        TextView textView = (TextView) toastView.findViewById(R.id.message);
        textView.setText( message );

        Toast toast = new Toast( context );
        toast.setGravity(Gravity.BOTTOM, 0 , 10 );
        toast.setDuration( Toast.LENGTH_LONG );
        toast.setView( toastView );
        toast.show();
    }
}
