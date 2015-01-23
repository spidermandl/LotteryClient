package com.evstudio.thefirstlottery.mobile.core;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.activity.DltBetListActivity;
import com.evstudio.thefirstlottery.mobile.activity.LoginActivity;
import com.evstudio.thefirstlottery.mobile.activity.SH11X5PushActivity;
import com.evstudio.thefirstlottery.mobile.adapter.BetItemAdapter;
import com.evstudio.thefirstlottery.mobile.adapter.SampleAdapter;
import com.evstudio.thefirstlottery.mobile.common.Constants;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.common.MyToast;
import com.evstudio.thefirstlottery.mobile.common.Util;
import com.evstudio.thefirstlottery.mobile.pojo.SH11X5PushBean;
import com.evstudio.thefirstlottery.mobile.pojo.Sh11x5Next;
import com.tandong.sa.eventbus.EventBus;
import com.tandong.sa.json.Gson;
import com.tandong.sa.json.reflect.TypeToken;
import com.tandong.sa.loopj.JsonHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.tools.AssistTool;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ericren on 14-9-12.
 * 胡莹莹注释
 * 上海11选5彩票投注的fragment
 */
public class Sh11x5BetItemFragment extends SherlockFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ArrayList<String> lstItem;
    public BetItemAdapter itemAdapter;
    private SampleAdapter adapter;
    private ProgressDialog progressDialog;
    private String playCmd;
    private GridView gridView;
    private View contextView;
    private AssistTool assistTool;
    private EditText sh11x5BetEtBei;
    private LayoutInflater layoutInflater;
    private int selectNumber;

    private String responseData;

    private String thisTitle;
    private String betContent;

    private CountDownTextView tvCountDown;

    private ListView listView;
    
    private ArrayList<String[]> dataList;

    private int combination;

    Handler handler = new Handler();

    /** 当前选择的号码 */
    public List<String> selectDataList = new ArrayList<String>();

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contextView = inflater.inflate(R.layout.fragment_sh11x5bet, container, false);

        assistTool = new AssistTool(contextView.getContext());
        Bundle mBundle = getArguments();
        thisTitle = mBundle.getString("title");
        playCmd = mBundle.getString("arg");
        selectNumber = mBundle.getInt("numbers");
        //将11选5的11个号码数据传到BetItemAdapter中并显示在gridView上
        gridView = (GridView) contextView.findViewById(R.id.sh11x5BetGridView);
        lstItem = new ArrayList<String>();
        for (int i = 1; i < 12; i++)
            lstItem.add(String.valueOf(i));

        itemAdapter = new BetItemAdapter(container.getContext(), lstItem);
        gridView.setAdapter(itemAdapter);
        gridView.setOnItemClickListener(this);

        BootstrapButton btnBet = (BootstrapButton) contextView.findViewById(R.id.sh11x5BetBtnSubmit);

        btnBet.setOnClickListener(this);
        sh11x5BetEtBei = (EditText) contextView.findViewById(R.id.sh11x5BetEtBei);
        //设置显示彩票销售时间信息
        tvCountDown = (CountDownTextView) contextView.findViewById(R.id.tv_bet_countdown);
        long countdown = Long.parseLong(Sh11x5Next.nextTime) * 1000 - System.currentTimeMillis() - 120 * 1000;
        countdown = countdown < 0 ? 0 : countdown;
        tvCountDown.setText(Util.getCountDown(countdown));

        responseData = mBundle.getString("data");
        //获得为显示彩民自己账号购买彩票选中的号码信息的listView
        listView = (ListView) contextView.findViewById(R.id.listDraw);

//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        HorizontalScrollView layout = (HorizontalScrollView) contextView.findViewById(R.id.betRelativeLayout);
//        params.height = layout.getHeight(); //需要设置的listview的高度，你可以设置成一个定值，也可以设置成其他容器的高度，如果是其他容器高度，那么不要在oncreate中执行，需要做延时处理，否则高度为0
//        listView.setLayoutParams(params);

        layoutInflater = inflater;
        adapter = new SampleAdapter(container.getContext(), R.id.panel_content);
        listView.setAdapter(adapter);

        if (null != responseData && !"".equals(responseData)) {
            Gson gson = new Gson();
            dataList = gson.fromJson(responseData,
                    new TypeToken<ArrayList<String[]>>() {
                    }.getType()
            );

            Collections.reverse(dataList);
            if (null != dataList && dataList.size() > 0) {
                for (int i = 7; i < dataList.size(); i++)
                    Sh11x5BetItemFragment.this.adapter.add(dataList.get(i));
            }
        }
//        adapter.notifyDataSetChanged();
        listView.setSelection(listView.getAdapter().getCount() - 1);

        EventBus.getDefault().register(this);

        return contextView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sh11x5BetBtnSubmit:
                // TODO 暂时注释 方便测试
                if (!assistTool.getPreferenceBoolean("isLogon")) {  //未登录
                    MyToast.instance.showToast(contextView.getContext(), layoutInflater, "您尚未登录，请先登录！");
                    assistTool.gotoActivity(LoginActivity.class, false);
                    break;
                }
                if (Long.parseLong(Sh11x5Next.nextTime) * 1000 - 120 * 1000 < System.currentTimeMillis()) {  //本期销售时间已过
                    AlertDialog alertDialog = new AlertDialog.Builder(contextView.getContext()).setTitle("提示")
                            .setMessage(Sh11x5Next.nextPeriods + " 本期销售已结束，请等待开奖！")
                            .create();
                    alertDialog.show();
                    return;
                }

                // TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                selectDataList.clear();
                for (int i = 0; i < itemAdapter.strResult.length; i++) {   //设置选中号码格式给selectDataList:两位数，不足左边补零
                    if (null != itemAdapter.strResult[i] && !"".equals(itemAdapter.strResult[i])) {
                        selectDataList.add(String.format("%02d", Integer.parseInt(itemAdapter.strResult[i])));
                    }
                }
                if (selectNumber > selectDataList.size()) {  //选择号码数量小于至少数
                    AlertDialog alertDialog = new AlertDialog.Builder(contextView.getContext()).setTitle("提示")
                            .setMessage("至少选择" + selectNumber + "个号码，请重新选择！")
                            .create();
                    alertDialog.show();
                    return;
                }

                StringBuffer strBuff = new StringBuffer();
                for (int i = 0; i < selectDataList.size(); i++) {   //遍历选中号码转成字符串，以","分割
                    strBuff.append(selectDataList.get(i));
                    if (i < selectDataList.size() - 1)
                        strBuff.append(",");
                }
                betContent = strBuff.toString();

//                Toast.makeText(getActivity(),betContent,Toast.LENGTH_LONG).show();

                // 跳转到下一个页面进行投注
                Intent pushIntent = new Intent(getActivity(), SH11X5PushActivity.class);

//                Intent intent = new Intent();
//                intent.putExtra("list", initDltBetData(betContent, money, 1));

                //赋值给投注信息实体类：玩法，选中号码，倍数，注数，投注金额
                SH11X5PushBean pushBean = new SH11X5PushBean();     
                pushBean.setBettype(playCmd);
                pushBean.setBetcontent(betContent);
                pushBean.setBetcount(sh11x5BetEtBei.getText() + "");
                
                String[] selectedNumber = pushBean.getBetcontent().split(",");
                int iplay = Integer.parseInt(pushBean.getBettype());
                int combi = selectedNumber.length;
                int finalnumber = 1;
                int needDiv = 1;
                for (int ik = 1; ik <= iplay; ik++) {
                    finalnumber *= combi;
                    combi--;
                    needDiv *= ik;
                }
                combi = finalnumber / needDiv;
                pushBean.setZhushu( combi );
                pushBean.setMoney( 2 * combi * Integer.parseInt(pushBean.getBetcount()));

                //将每次投注添加到已静态生成的投注信息实体类（SH11X5PushBean）类型的集合中
                Constants.pushBeanList.add(pushBean);

//                pushIntent.putExtra("data",pushBean);
                getActivity().startActivity(pushIntent);





//                int cash = Integer.parseInt(assistTool.getPreferenceString("cash"));
//                combination = selectDataList.size();
//                int playnumber = selectNumber;
//                int finalnumber = 1;
////                for (int itmp = combination; itmp > playnumber; itmp--) {
////                    finalnumber *= itmp;
////                }
//                int needDiv = 1;
//                for( int ik =1; ik <= playnumber; ik ++ ){
//                    finalnumber *= combination;
//                    combination --;
//                    needDiv *= ik;
//                }
//
////                combination = combination * (combination - 1) * (combination - 2) / 6;
//                combination = finalnumber/needDiv;
//
//                if (cash < 2 * Integer.parseInt(sh11x5BetEtBei.getText().toString()) * combination) {
//                    AlertDialog alertDialog = new AlertDialog.Builder(contextView.getContext()).setTitle("提示")
//                            .setMessage("您的余额不足，无法投注！\r\n您的投注金额为：" +
//                                    String.valueOf(2 * combination * Integer.parseInt(sh11x5BetEtBei.getText().toString())) +
//                                    "\r\n您当前余额为：" + String.valueOf(cash) + "元")
//                            .create();
//                    alertDialog.show();
//                    return;
//                }
//
//                new AlertDialog.Builder(contextView.getContext())
//                        .setTitle("确认您的投注")
//                        .setMessage("您的投注内容是：\r\n上海11选5第" + Sh11x5Next.nextPeriods + "期\r\n" +
//                                "玩法：" + thisTitle + "\r\n" +
//                                "投注内容：" + betContent + "\r\n" +
//                                "投注倍数:" + sh11x5BetEtBei.getText() + "\r\n" +
//                                "投注金额：" + String.valueOf(2 * combination * Integer.parseInt(sh11x5BetEtBei.getText().toString())) + "元" + "\r\n" +
//                                "您的当前余额为：" + assistTool.getPreferenceString("cash") + "元")
//                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                progressDialog = new ProgressDialog(contextView.getContext());
//                                progressDialog.setTitle("投注");
//                                progressDialog.setMessage("正在投注，请稍候......");
//                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                                progressDialog.setCanceledOnTouchOutside(false);
//                                progressDialog.show();
//
//                                RequestParams requestParams = new RequestParams();
//                                requestParams.put("mobileuid", assistTool.getPreferenceString("userid"));
//                                requestParams.put("bettype", playCmd);
//                                requestParams.put("betcontent", betContent);
//                                requestParams.put("betcount", sh11x5BetEtBei.getText());
//                                requestParams.put("periods", Sh11x5Next.nextPeriods);
//                                HttpRestClient.post("mobile/doBet", requestParams, new JsonHttpResponseHandler() {
//                                    @Override
//                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                                        progressDialog.dismiss();
//                                        int cash = Integer.parseInt(assistTool.getPreferenceString("cash"));
//                                        cash = cash - 2 * Integer.parseInt(sh11x5BetEtBei.getText().toString()) * combination;
//                                        assistTool.savePreferenceString("cash", String.valueOf(cash));
//
//                                        MyToast.instance.showToast(contextView.getContext(), layoutInflater, "投注成功，感谢您的投注！");
//                                        EventBus.getDefault().post("sh11x5betted");
//                                        super.onSuccess(statusCode, headers, response);
//                                    }
//
//                                    @Override
//                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                                        progressDialog.dismiss();
//                                        AlertDialog alertDialog = new AlertDialog.Builder(contextView.getContext()).setTitle("提示")
//                                                .setMessage("投注失败，请检查您的网络！")
//                                                .create();
//                                        alertDialog.show();
//                                        super.onFailure(statusCode, headers, responseString, throwable);
//                                    }
//                                });
//                            }
//                        })
//                        .setNegativeButton("否", null)
//                        .show();

                break;
            default:
                break;
        }
    }

    public void onEventMainThread(String json) {// 接收事件内容地方（参数类型可以自己根据实际情况定义）
        if (null != json && !"".equals(json) && "sh11x5New".equals(json)) {
            String str = Sh11x5Next.lastPeriods + "," + Sh11x5Next.lastWinning;
            Sh11x5BetItemFragment.this.dataList.remove(0);
            Sh11x5BetItemFragment.this.dataList.add(str.split(","));

            Gson gson = new Gson();
            Sh11x5BetItemFragment.this.responseData = gson.toJson(Sh11x5BetItemFragment.this.dataList);

            Sh11x5BetItemFragment.this.adapter.remove(Sh11x5BetItemFragment.this.adapter.getItem(0));
            Sh11x5BetItemFragment.this.adapter.add(str.split(","));
            Sh11x5BetItemFragment.this.adapter.notifyDataSetChanged();
        }
    }

}
