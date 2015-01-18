package com.evstudio.thefirstlottery.mobile.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.pojo.FootballGameInfo;
import com.evstudio.thefirstlottery.mobile.pojo.FootballInfoMix;
import com.tandong.sa.eventbus.EventBus;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by eric on 14/10/20.
 */
public class AdapterListViewFtHh extends BaseAdapter {
    public ArrayList<FootballInfoMix> arrayList = new ArrayList<FootballInfoMix>();
    private HashMap<Integer, View> map = new HashMap<Integer, View>();
    private final LayoutInflater mLayoutInflater;
    private Context context;
    private Handler mHandler;
    public HashMap<Integer, FootballInfoMix> selectedItems = new HashMap<Integer, FootballInfoMix>();

    public AdapterListViewFtHh(Context ctx, ArrayList<FootballInfoMix> list, Handler handler) {
        arrayList = list;
        context = ctx;
        mLayoutInflater = LayoutInflater.from(context);
        mHandler = handler;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        FootballInfoMix infoMix = (FootballInfoMix) arrayList.get(position);

        if (map.get(position) == null) {
            convertView = mLayoutInflater.inflate(R.layout.listview_football_hh, parent, false);
            vh = new ViewHolder();

            vh.imGameNumber = (TextView) convertView.findViewById(R.id.imGameNumber);
            vh.imMatchName = (TextView) convertView.findViewById(R.id.imMatchName);
            vh.imHomeTeam = (TextView) convertView.findViewById(R.id.imHomeTeam);
            vh.imAwayTeam = (TextView) convertView.findViewById(R.id.imAwayTeam);
            vh.imOdd = (TextView) convertView.findViewById(R.id.imOdd);

            vh.imOriPl[0] = (TextView) convertView.findViewById(R.id.imOriPl3);
            vh.imOriPl[1] = (TextView) convertView.findViewById(R.id.imOirPl1);
            vh.imOriPl[2] = (TextView) convertView.findViewById(R.id.imOriPl0);
            vh.imOddPl[0] = (TextView) convertView.findViewById(R.id.imOddPl3);
            vh.imOddPl[1] = (TextView) convertView.findViewById(R.id.imOddPl1);
            vh.imOddPl[2] = (TextView) convertView.findViewById(R.id.imOddPl0);

            vh.imHalfWin[0] = (TextView) convertView.findViewById(R.id.imHw0);
            vh.imHalfWin[1] = (TextView) convertView.findViewById(R.id.imHw1);
            vh.imHalfWin[2] = (TextView) convertView.findViewById(R.id.imHw2);
            vh.imHalfWin[3] = (TextView) convertView.findViewById(R.id.imHw3);
            vh.imHalfWin[4] = (TextView) convertView.findViewById(R.id.imHw4);
            vh.imHalfWin[5] = (TextView) convertView.findViewById(R.id.imHw5);
            vh.imHalfWin[6] = (TextView) convertView.findViewById(R.id.imHw6);
            vh.imHalfWin[7] = (TextView) convertView.findViewById(R.id.imHw7);
            vh.imHalfWin[8] = (TextView) convertView.findViewById(R.id.imHw8);

            vh.imGoal[0] = (TextView) convertView.findViewById(R.id.imJq0);
            vh.imGoal[1] = (TextView) convertView.findViewById(R.id.imJq1);
            vh.imGoal[2] = (TextView) convertView.findViewById(R.id.imJq2);
            vh.imGoal[3] = (TextView) convertView.findViewById(R.id.imJq3);
            vh.imGoal[4] = (TextView) convertView.findViewById(R.id.imJq4);
            vh.imGoal[5] = (TextView) convertView.findViewById(R.id.imJq5);
            vh.imGoal[6] = (TextView) convertView.findViewById(R.id.imJq6);
            vh.imGoal[7] = (TextView) convertView.findViewById(R.id.imJq7);

            vh.imScores[0] = (TextView) convertView.findViewById(R.id.imBf0);
            vh.imScores[1] = (TextView) convertView.findViewById(R.id.imBf1);
            vh.imScores[2] = (TextView) convertView.findViewById(R.id.imBf2);
            vh.imScores[3] = (TextView) convertView.findViewById(R.id.imBf3);
            vh.imScores[4] = (TextView) convertView.findViewById(R.id.imBf4);
            vh.imScores[5] = (TextView) convertView.findViewById(R.id.imBf5);
            vh.imScores[6] = (TextView) convertView.findViewById(R.id.imBf6);
            vh.imScores[7] = (TextView) convertView.findViewById(R.id.imBf7);
            vh.imScores[8] = (TextView) convertView.findViewById(R.id.imBf8);
            vh.imScores[9] = (TextView) convertView.findViewById(R.id.imBf9);
            vh.imScores[10] = (TextView) convertView.findViewById(R.id.imBf10);
            vh.imScores[11] = (TextView) convertView.findViewById(R.id.imBf11);
            vh.imScores[12] = (TextView) convertView.findViewById(R.id.imBf12);
            vh.imScores[13] = (TextView) convertView.findViewById(R.id.imBf13);
            vh.imScores[14] = (TextView) convertView.findViewById(R.id.imBf14);
            vh.imScores[15] = (TextView) convertView.findViewById(R.id.imBf15);
            vh.imScores[16] = (TextView) convertView.findViewById(R.id.imBf16);
            vh.imScores[17] = (TextView) convertView.findViewById(R.id.imBf17);
            vh.imScores[18] = (TextView) convertView.findViewById(R.id.imBf18);
            vh.imScores[19] = (TextView) convertView.findViewById(R.id.imBf19);
            vh.imScores[20] = (TextView) convertView.findViewById(R.id.imBf20);
            vh.imScores[21] = (TextView) convertView.findViewById(R.id.imBf21);
            vh.imScores[22] = (TextView) convertView.findViewById(R.id.imBf22);
            vh.imScores[23] = (TextView) convertView.findViewById(R.id.imBf23);
            vh.imScores[24] = (TextView) convertView.findViewById(R.id.imBf24);
            vh.imScores[25] = (TextView) convertView.findViewById(R.id.imBf25);
            vh.imScores[26] = (TextView) convertView.findViewById(R.id.imBf26);
            vh.imScores[27] = (TextView) convertView.findViewById(R.id.imBf27);
            vh.imScores[28] = (TextView) convertView.findViewById(R.id.imBf28);
            vh.imScores[29] = (TextView) convertView.findViewById(R.id.imBf29);
            vh.imScores[30] = (TextView) convertView.findViewById(R.id.imBf30);

            convertView.setTag(vh);
        } else {
            convertView = (View) map.get(position);
            vh = (ViewHolder) convertView.getTag();
        }

        vh.imGameNumber.setText(infoMix.gameNumber);
        vh.imMatchName.setText(infoMix.matchName);
        vh.imMatchName.setBackgroundColor(android.graphics.Color.parseColor(infoMix.matchColor));
        vh.imHomeTeam.setText("[主]" + infoMix.homeTeam);
        vh.imAwayTeam.setText("[客]" + infoMix.awayTeam);
        vh.imOdd.setText("让" + infoMix.odd);

        for (int i = 0; i < vh.imOriPl.length; i++) {
            vh.imOriPl[i].setText(infoMix.oriPl[i]);
            vh.imOriPl[i].setTag(new Integer(i));
            vh.imOriPl[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FootballInfoMix infoM = (FootballInfoMix) getItem(position);
                    Resources resources = context.getResources();
                    Drawable drawable;
                    TextView textView = (TextView) v;
                    int index = (Integer) textView.getTag();
                    if (v.isSelected()) {
                        v.setSelected(false);
                        drawable = resources.getDrawable(R.drawable.footselectitembg);
                        infoM.selected[0 + index] = 0;
                        if (!infoM.isSelected()) {
                            selectedItems.remove(position);
                        }
                    } else {
                        v.setSelected(true);
                        drawable = resources.getDrawable(R.color.blue);
                        infoM.selected[0 + index] = 1;
                        selectedItems.put(position, infoM);
                    }
                    v.setBackgroundDrawable(drawable);
                    EventBus.getDefault().post("hhSelected");
                }
            });
        }

        for (int i = 0; i < vh.imOddPl.length; i++) {
            vh.imOddPl[i].setText(infoMix.oddPl[i]);
            vh.imOddPl[i].setTag(new Integer(i));
            vh.imOddPl[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FootballInfoMix infoM = (FootballInfoMix) getItem(position);
                    Resources resources = context.getResources();
                    Drawable drawable;
                    TextView textView = (TextView) v;
                    int index = (Integer) textView.getTag();
                    if (v.isSelected()) {
                        v.setSelected(false);
                        drawable = resources.getDrawable(R.drawable.footselectitembg);
                        infoM.selected[3 + index] = 0;
                        if (!infoM.isSelected()) {
                            selectedItems.remove(position);
                        }
                    } else {
                        v.setSelected(true);
                        drawable = resources.getDrawable(R.color.blue);
                        infoM.selected[3 + index] = 1;
                        selectedItems.put(position, infoM);
                    }
                    v.setBackgroundDrawable(drawable);
                    EventBus.getDefault().post("hhSelected");
                }
            });
        }

        for (int i = 0; i < vh.imHalfWin.length; i++) {
            vh.imHalfWin[i].setText(infoMix.halfWin[i]);
            vh.imHalfWin[i].setTag(new Integer(i));
            vh.imHalfWin[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FootballInfoMix infoM = (FootballInfoMix) getItem(position);
                    Resources resources = context.getResources();
                    Drawable drawable;
                    TextView textView = (TextView) v;
                    int index = (Integer) textView.getTag();
                    if (v.isSelected()) {
                        v.setSelected(false);
                        drawable = resources.getDrawable(R.drawable.footselectitembg);
                        infoM.selected[6 + index] = 0;
                        if (!infoM.isSelected()) {
                            selectedItems.remove(position);
                        }
                    } else {
                        v.setSelected(true);
                        drawable = resources.getDrawable(R.color.blue);
                        infoM.selected[6 + index] = 1;
                        selectedItems.put(position, infoM);
                    }
                    v.setBackgroundDrawable(drawable);
                    EventBus.getDefault().post("hhSelected");
                }
            });
        }

        for (int i = 0; i < vh.imGoal.length; i++) {
            vh.imGoal[i].setText(infoMix.goals[i]);
            vh.imGoal[i].setTag(new Integer(i));
            vh.imGoal[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FootballInfoMix infoM = (FootballInfoMix) getItem(position);
                    Resources resources = context.getResources();
                    Drawable drawable;
                    TextView textView = (TextView) v;
                    int index = (Integer) textView.getTag();
                    if (v.isSelected()) {
                        v.setSelected(false);
                        drawable = resources.getDrawable(R.drawable.footselectitembg);
                        infoM.selected[15 + index] = 0;
                        if (!infoM.isSelected()) {
                            selectedItems.remove(position);
                        }
                    } else {
                        v.setSelected(true);
                        drawable = resources.getDrawable(R.color.blue);
                        infoM.selected[15 + index] = 1;
                        selectedItems.put(position, infoM);
                    }
                    v.setBackgroundDrawable(drawable);
                    EventBus.getDefault().post("hhSelected");
                }
            });
        }

        for (int i = 0; i < vh.imScores.length; i++) {
            vh.imScores[i].setText(infoMix.scores[i]);
            vh.imScores[i].setTag(new Integer(i));
            vh.imScores[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FootballInfoMix infoM = (FootballInfoMix) getItem(position);
                    Resources resources = context.getResources();
                    Drawable drawable;
                    TextView textView = (TextView) v;
                    int index = (Integer) textView.getTag();
                    if (v.isSelected()) {
                        v.setSelected(false);
                        drawable = resources.getDrawable(R.drawable.footselectitembg);
                        infoM.selected[23 + index] = 0;
                        if (!infoM.isSelected()) {
                            selectedItems.remove(position);
                        }
                    } else {
                        v.setSelected(true);
                        drawable = resources.getDrawable(R.color.blue);
                        infoM.selected[23 + index] = 1;
                        selectedItems.put(position, infoM);
                    }
                    v.setBackgroundDrawable(drawable);
                    EventBus.getDefault().post("hhSelected");
                }
            });
        }


        return convertView;
    }

    class ViewHolder {
        TextView imGameNumber;
        TextView imMatchName;
        TextView imHomeTeam;
        TextView imAwayTeam;
        TextView imOdd;
        TextView[] imOddPl = new TextView[3];
        TextView[] imOriPl = new TextView[3];
        TextView[] imScores = new TextView[31];
        TextView[] imGoal = new TextView[8];
        TextView[] imHalfWin = new TextView[9];
    }

}
