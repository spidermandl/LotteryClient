package com.evstudio.thefirstlottery.mobile.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.FootballMatchAdapter;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.pojo.FootballGameInfo;
import com.tandong.sa.json.stream.JsonReader;
import com.tandong.sa.loopj.AsyncHttpResponseHandler;
import org.apache.http.Header;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by ericren on 14-11-12.
 */
public class FragmentFootball01 extends SherlockFragment {
    private ListView listView;
    private ProgressDialog progressDialog;
    private LayoutInflater layoutInflater;
    private View contextView;
    private ArrayList<FootballGameInfo> arrayList = new ArrayList<FootballGameInfo>();
    private FootballMatchAdapter adapter;

    private BootstrapButton btnClear;
    private BootstrapButton btnSubmit;

    private TextView tipText;

    private int selectedMatch = 0;

    private Handler handler = new Handler(){
        /**
         * Subclasses must implement this to receive messages.
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:

                    break;
                case 1:
                    selectedMatch ++;
                    tipText.setText("已经选择" + String.valueOf(selectedMatch) + "场比赛。");
                    break;
                case -1:
                    selectedMatch --;
                    tipText.setText("已经选择" + String.valueOf(selectedMatch) + "场比赛。");
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layoutInflater = inflater;
        contextView = inflater.inflate(R.layout.layout_fragment_football01, container, false);

        btnClear = (BootstrapButton)contextView.findViewById(R.id.btnPutClear);
        btnSubmit = (BootstrapButton)contextView.findViewById(R.id.btnPutSubmit);
        tipText = (TextView)contextView.findViewById(R.id.tvFootTips);

        listView = (ListView) contextView.findViewById(R.id.lvFootballMatch);
        adapter = new FootballMatchAdapter(inflater.getContext(), arrayList, handler);
        listView.setAdapter(adapter);


        progressDialog = new ProgressDialog(inflater.getContext());
        progressDialog.setTitle("正在载入");
        progressDialog.setMessage("正在载入数据，请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        HttpRestClient.getDirect("http://i.sporttery.cn/odds_calculator/get_odds?" +
                "i_format=json&" +
                "i_callback=getData&" +
                "poolcode[]=hhad&" +
                "poolcode[]=had&", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String strResponse = new String(bytes);
                strResponse = strResponse.substring(8);
                strResponse = strResponse.substring(0, strResponse.length() - 2);
//                System.out.println("response:" + jsonFormatter(strResponse));
                try {
                    JsonReader reader = new JsonReader(new StringReader(strResponse));
                    reader.beginObject();
                    if (reader.hasNext()) {
                        //data
                        String rootName = reader.nextName();
                        reader.beginObject();
                        while (reader.hasNext()) {
                            FootballGameInfo football = new FootballGameInfo();
                            String idName = reader.nextName();
                            football.id = idName;
                            reader.beginObject();
                            while (reader.hasNext()) {
                                //id
                                while (reader.hasNext()) {
                                    String tagName = reader.nextName();
                                    if (null != tagName) {
                                        if ("id".equals(tagName))
                                            football.id = reader.nextString();
                                        else if ("date".equals(tagName))
                                            football.gameDate = reader.nextString();
                                        else if ("num".equals(tagName))
                                            football.gameNumber = reader.nextString();
                                        else if ("time".equals(tagName))
                                            football.beginTime = reader.nextString();
                                        else if ("l_cn_abbr".equals(tagName))
                                            football.matchName = reader.nextString();
                                        else if ("h_cn_abbr".equals(tagName))
                                            football.hostTeam = reader.nextString();
                                        else if ("a_cn_abbr".equals(tagName))
                                            football.clientTeam = reader.nextString();
                                        else if ("l_background_color".equals(tagName)) {
                                            football.backgroundColor = reader.nextString();
                                        } else if ("weather".equals(tagName))
                                            football.weather = reader.nextString();
                                        else if ("hhad".equals(tagName)) {
                                            reader.beginObject();
                                            while (reader.hasNext()) {
                                                String childTagName = reader.nextName();
                                                if ("h".equals(childTagName))
                                                    football.oddsAssignHost = Float.parseFloat(reader.nextString());
                                                else if ("a".equals(childTagName))
                                                    football.oddsAssignClient = Float.parseFloat(reader.nextString());
                                                else if ("d".equals(childTagName))
                                                    football.oddsAssignDraw = Float.parseFloat(reader.nextString());
                                                else if ("fixedodds".equals(childTagName)) {
                                                    football.assignor = reader.nextString();
                                                } else
                                                    System.out.println(reader.nextString());
                                            }
                                            reader.endObject();
                                        } else if ("had".equals(tagName)) {
                                            reader.beginObject();
                                            while (reader.hasNext()) {
                                                String childTagName = reader.nextName();
                                                if ("h".equals(childTagName))
                                                    football.oddsHost = Float.parseFloat(reader.nextString());
                                                else if ("a".equals(childTagName))
                                                    football.oddsClient = Float.parseFloat(reader.nextString());
                                                else if ("d".equals(childTagName))
                                                    football.oddsDraw = Float.parseFloat(reader.nextString());
                                                else
                                                    System.out.println(reader.nextString());
                                            }
                                            reader.endObject();
                                        } else if ("match_info".equals(tagName)) {
                                            reader.beginArray();
                                            while (reader.hasNext()) {
                                                String childTagName = reader.nextName();
                                                System.out.println("childTag:" + childTagName + ",childValue:" + reader.nextString());
                                            }
                                            reader.endArray();
                                        } else {
                                            System.out.println(reader.nextString());
                                        }
                                    }
                                }
                            }
                            reader.endObject();
                            arrayList.add(football);
                        }
                        reader.endObject();
                    }
                    reader.endObject();

                    //TO do
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
//                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });

        return contextView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        parent.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//        parent.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
//    }

    public AdapterView.OnItemSelectedListener  onItemSelectedListener =
            new AdapterView.OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    //当此选中的item的子控件需要获得<strong>焦点</strong>时
                    parent.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                    //else parent.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    parent.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                }
            };
}