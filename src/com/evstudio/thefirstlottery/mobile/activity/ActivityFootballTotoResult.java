package com.evstudio.thefirstlottery.mobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.common.MyToast;
import com.evstudio.thefirstlottery.mobile.pojo.FootballGameInfo;
import com.evstudio.thefirstlottery.mobile.pojo.FootballTotoInfo;
import com.tandong.sa.eventbus.EventBus;
import com.tandong.sa.json.Gson;
import com.tandong.sa.loopj.JsonHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.tools.AssistTool;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 14/11/20.
 */
public class ActivityFootballTotoResult extends SherlockActivity {

    private ActionBar actionBar;

    private WebView wvTotoBuyContent;
    private EditText etBeishu;
    private String sellNumber;
    private String endTime;

    private ArrayList<FootballTotoInfo> arrayList = new ArrayList<FootballTotoInfo>();
    private Button btSubmit;
    private TextView tvTip;

    private ProgressDialog progressDialog;
    private AssistTool assistTool;
    private int[] numbers = new int[14];
    private float[] fMax = new float[14];
    private int ticketCount = 1;
    private float maxBonus = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_football_toto_result);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        wvTotoBuyContent = (WebView) findViewById(R.id.wvTotoBuyContent);
        etBeishu = (EditText) findViewById(R.id.etFtTotoBs);
        btSubmit = (Button) findViewById(R.id.btnFtToto14ResultSubmit);
        tvTip = (TextView)findViewById(R.id.tvToto14Tip);

        assistTool = new AssistTool(this);
        //得到传过来的Intent对象
        Intent intent = getIntent();
        //获得Intent中的Extra
        arrayList = (ArrayList<FootballTotoInfo>) intent.getSerializableExtra("games");
        sellNumber = intent.getStringExtra("sellNumber");
        endTime = intent.getStringExtra("endTime");
        String buyContent = "<!DOCTYPE html>\n" +
                "<html lang=\"zh\">\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\" />\n" +
                "<title>任选9场</title>\n" +
                "<style type=\"text/css\">\n" +
                ".matchTitle{\n" +
                "text-align: center;\n" +
                "width:100%;\n" +
                "}\n" +
                "table{\n" +
                "  width:100%;\n" +
                "  border-collapse:collapse;\n" +
                "}\n" +
                "table, th, td\n" +
                "  {\n" +
                "  border: 1px solid blue;\n" +
                "  }\n" +
                "  th{\n" +
                "  padding: 3px 7px; \n" +
                "  border: 1px solid #f60; \n" +
                "  border-width: 2px 1px 1px; \n" +
                "  background: #ffffe1; \n" +
                "  }\n" +
                "  td{\n" +
                "    text-align: center;\n" +
                "  }\n" +
                "  .selected{\n" +
                "    background: #ffffe1; \n" +
                "  }\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"matchTitle\">\n" +
                "<h3>任选九场&nbsp;&nbsp;<small>第" + sellNumber + "期,停售时间:" + endTime + "</small></h3>\n" +
                "</div>\n" +
                "<table>\n" +
                "<thead>\n" +
                "<tr> <th width=\"5%\">序号</th><th>对阵双方</th><th width=\"15%\">胜</th><th width=\"15%\">平</th><th width=\"15%\">负</th> </tr>\n" +
                "</thead>\n" +
                "<tbody>\n";
        for (int i = 0; i < arrayList.size(); i++) {
            FootballTotoInfo totoInfo = arrayList.get(i);
            int iCountOneGame = 0;
            fMax[i] = 0;
            buyContent +=
                    "<tr> <td>" + totoInfo.gameNumberOfDay +
                            "</td><td class=\"matchName\">" + totoInfo.teams[0] +
                            "VS" + totoInfo.teams[1] +
                            "</td><td ";
            if (totoInfo.selected[0] == 1) {
                buyContent += "class=\"selected\"";
                iCountOneGame += 1;
                fMax[i] = fMax[i] < Float.parseFloat(totoInfo.teamOdds[0]) ? Float.parseFloat(totoInfo.teamOdds[0]) : fMax[i];
            }
            buyContent += ">" + totoInfo.teamOdds[0] + "</td><td ";
            if (totoInfo.selected[1] == 1) {
                buyContent += "class=\"selected\"";
                iCountOneGame += 1;
                fMax[i] = fMax[i] < Float.parseFloat(totoInfo.teamOdds[1]) ? Float.parseFloat(totoInfo.teamOdds[1]) : fMax[i];
            }
            buyContent += ">" + totoInfo.teamOdds[1] + "</td><td ";
            if (totoInfo.selected[2] == 1) {
                buyContent += "class=\"selected\"";
                iCountOneGame += 1;
                fMax[i] = fMax[i] < Float.parseFloat(totoInfo.teamOdds[2]) ? Float.parseFloat(totoInfo.teamOdds[2]) : fMax[i];
            }

            buyContent += ">" + totoInfo.teamOdds[2] + "</td></tr>";
            if (iCountOneGame > 0) {
                numbers[i] = iCountOneGame;
            }
        }


        int iCom = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > 0) {
                ticketCount *= numbers[i];
                iCom ++;
            }
            maxBonus *= fMax[i];
        }
        //计算票数
        if( iCom > 9 ){
            for( int i = 10; i <= iCom; i ++ ){
                ticketCount *= i;
            }
        }

        buyContent += "</tbody>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>\n";

        wvTotoBuyContent.loadDataWithBaseURL("", buyContent, "text/html", "utf-8", "");

        setTotoTip();

        etBeishu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != s && !"".equals(String.valueOf(s)))
                    setTotoTip();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!assistTool.getPreferenceBoolean("isLogon")) {
                    MyToast.instance.showToast(ActivityFootballTotoResult.this,
                            LayoutInflater.from(ActivityFootballTotoResult.this), "您尚未登录，请先登录！");
                    assistTool.gotoActivity(LoginActivity.class, false);
                    return;
                }

                int cash = Integer.parseInt(assistTool.getPreferenceString("cash"));
                if (cash < 2 * Integer.parseInt(String.valueOf(etBeishu.getText())) * ticketCount) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityFootballTotoResult.this).setTitle("提示")
                            .setMessage("您的余额不足，当前余额为" + String.valueOf(cash) + "，请充值！")
                            .create();
                    alertDialog.show();
                    return;
                }

                progressDialog = new ProgressDialog(ActivityFootballTotoResult.this);
                progressDialog.setTitle("正在提交");
                progressDialog.setMessage("正在提交购买彩票，请稍候......");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();
                Gson gson = new Gson();
                String ftinfo = gson.toJson(arrayList);
                String playType = "08";//足球任选9场

                RequestParams requestParams = new RequestParams();
                requestParams.put("mobileuid", assistTool.getPreferenceString("userid"));
                requestParams.put("bettype", playType);
                requestParams.put("betcontent", ftinfo);
                requestParams.put("betcount", etBeishu.getText().toString());
                requestParams.put("periods", sellNumber);//足球任选9场

                System.out.println("play type:" + playType);
                System.out.println("bettype:" + ftinfo);
                System.out.println("betcontent:" + etBeishu.getText().toString());
                System.out.println("periods:" + "05");

                HttpRestClient.post("mobile/doBet", requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        progressDialog.dismiss();
                        int cash = Integer.parseInt(assistTool.getPreferenceString("cash"));
                        cash = cash - 2 * Integer.parseInt(String.valueOf(etBeishu.getText().toString())) * ticketCount;
                        assistTool.savePreferenceString("cash", String.valueOf(cash));

                        MyToast.instance.showToast(ActivityFootballTotoResult.this,
                                LayoutInflater.from(ActivityFootballTotoResult.this), "投注成功，感谢您的投注！");

                        EventBus.getDefault().post("betSuccess");

                        super.onSuccess(statusCode, headers, response);

                        ActivityFootballTotoResult.this.finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        progressDialog.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(ActivityFootballTotoResult.this).setTitle("提示")
                                .setMessage("投注失败，请检查您的网络！")
                                .create();
                        alertDialog.show();
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private List<String> combine(String[] a, int num) {
        List<String> list = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        String[] b = new String[a.length];
        for (int i = 0; i < b.length; i++) {
            if (i < num) {
                b[i] = "1";
            } else
                b[i] = "0";
        }

        int point = 0;
        int nextPoint = 0;
        int count = 0;
        int sum = 0;
        String temp = "1";
        while (true) {
            // 判断是否全部移位完毕
            for (int i = b.length - 1; i >= b.length - num; i--) {
                if (b[i].equals("1"))
                    sum += 1;
            }
            // 根据移位生成数据
            for (int i = 0; i < b.length; i++) {
                if (b[i].equals("1")) {
                    point = i;
                    sb.append(a[point]);
                    sb.append(" ");
                    count++;
                    if (count == num)
                        break;
                }
            }
            // 往返回值列表添加数据
            list.add(sb.toString());

            // 当数组的最后num位全部为1 退出
            if (sum == num) {
                break;
            }
            sum = 0;

            // 修改从左往右第一个10变成01
            for (int i = 0; i < b.length - 1; i++) {
                if (b[i].equals("1") && b[i + 1].equals("0")) {
                    point = i;
                    nextPoint = i + 1;
                    b[point] = "0";
                    b[nextPoint] = "1";
                    break;
                }
            }
            // 将 i-point个元素的1往前移动 0往后移动
            for (int i = 0; i < point - 1; i++)
                for (int j = i; j < point - 1; j++) {
                    if (b[i].equals("0")) {
                        temp = b[i];
                        b[i] = b[j + 1];
                        b[j + 1] = temp;
                    }
                }
            // 清空 StringBuffer
            sb.setLength(0);
            count = 0;
        }
        //
        System.out.println("数据长度 " + list.size());
        return list;
    }

    private void setTotoTip() {
        DecimalFormat fnum = new DecimalFormat("##0.00");
        int i = 1;
        tvTip.setText("您选择了" + String.valueOf(ticketCount) + "张票，投注金额为" +
                String.valueOf(2 * ticketCount * Integer.parseInt(etBeishu.getText().toString())) +
                "元");
    }
}