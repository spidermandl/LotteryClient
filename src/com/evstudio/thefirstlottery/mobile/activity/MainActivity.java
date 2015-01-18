package com.evstudio.thefirstlottery.mobile.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.BetTypeAdapter;
import com.evstudio.thefirstlottery.mobile.adapter.MenuAdapter;
import com.evstudio.thefirstlottery.mobile.adapter.PlayItemAdapter;
import com.evstudio.thefirstlottery.mobile.common.MyToast;
import com.evstudio.thefirstlottery.mobile.common.Util;
import com.evstudio.thefirstlottery.mobile.core.BetItem;
import com.evstudio.thefirstlottery.mobile.fragments.BottomButton;
import com.evstudio.thefirstlottery.mobile.functions.UserFunctions;
import com.evstudio.thefirstlottery.mobile.pojo.Config;
import com.evstudio.thefirstlottery.mobile.pojo.Sh11x5Next;
import com.tandong.sa.eventbus.EventBus;
import com.tandong.sa.slideMenu.SlidingMenu;
import com.tandong.sa.tools.AssistTool;
import com.tandong.sa.view.SmartListView;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cn.hugo.android.scanner.CaptureActivity;

/**
 * Created by ericren on 14-9-7.
 */
public class MainActivity extends BaseBottomMenuActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static MainActivity activity;

    private AssistTool assistTool;

    private Bundle bundle;

    private ImageView imageView;
    private SlidingMenu menu;
    private SmartListView lv_menu;
    private ArrayList<String> menus;
    private MenuAdapter menuAdapter;
    private GridView gridView;

    private Handler handler = new Handler();
    private TextView sh11x5TextView;

    private BetTypeAdapter betTypeAdapter;

    private TextView tvBtnLogin;

    private TextView tvLastDraw;

    private TextView tvDlt;

    private BootstrapButton bbtnCharge;
    private BootstrapButton bbtnGetMoney;

    private ProgressDialog progressDialog;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_main);
//        activity = this;
//        assistTool = new AssistTool(this);
//        assistTool.userinfo = UserFunctions.userInfoTag;
//
//        bundle = savedInstanceState;
//        actionBar = getSupportActionBar();
//
//        actionBar.setLogo(R.drawable.top_btn_menu);
//        actionBar.setHomeButtonEnabled(true);
//
//        imageView = (ImageView) findViewById(R.id.ivJoinBar);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                assistTool.gotoActivity(CaptureActivity.class, false, bundle);
//            }
//        });
//        initMenu();
//        initCard();
//
//        EventBus.getDefault().register(this);
//    }

    /**
     * 子类实现后，在原来的onCreate方法中内容移到这里来操作。
     *
     * @param savedInstanceState
     */
    protected void onCreatOverride(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_main);
        activity = this;
        assistTool = new AssistTool(this);
        assistTool.userinfo = UserFunctions.userInfoTag;

        bundle = savedInstanceState;
//        actionBar = getSupportActionBar();

        actionBar.setLogo(R.drawable.top_btn_menu);
        actionBar.setHomeButtonEnabled(true);

        Config.iActionBarHeight = actionBar.getHeight();

        imageView = (ImageView) findViewById(R.id.ivJoinBar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assistTool.gotoActivity(CaptureActivity.class, false, bundle);
            }
        });
        tvLastDraw = (TextView) findViewById(R.id.tvLastDraw);

        tvDlt = (TextView)findViewById(R.id.dltKaijiang);
        tvDlt.setText("第"+ assistTool.getPreferenceString("dltPeriods")+"期："+assistTool.getPreferenceString("dlt"));

        initMenu();
        initCard();
        initGrid();

        bbtnCharge = (BootstrapButton) findViewById(R.id.btnCharge);
        bbtnGetMoney = (BootstrapButton) findViewById(R.id.btnGetMoney);

        bbtnCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!assistTool.getPreferenceBoolean("isLogon")) {
                    Toast.makeText(MainActivity.this,"您尚未登录，请先登录！",Toast.LENGTH_SHORT).show();
                    assistTool.gotoActivity(LoginActivity.class, false);
                    return;
                }

                assistTool.gotoActivity(ActivityPayment.class,false);
            }
        });

        bbtnGetMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pd = ProgressDialog.show(MainActivity.this, "提现", "加载中，请稍后……");
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        pd.dismiss();
//                        MyToast.instance.showToast(MainActivity.this, getLayoutInflater(), "服务器支付鉴权失败，请稍后重试！");
//                    }
//                }, 3000);
            }
        });

        tvBtnLogin = (TextView) findViewById(R.id.tvBtnLogin);
        tvBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean bLogined = false;
                bLogined = assistTool.getPreferenceBoolean("isLogon");
                if (!bLogined) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 100);
                } else {
                    assistTool.gotoActivity(UserInfoActivity.class, false, bundle);
                }
            }
        });


        EventBus.getDefault().register(this);
    }

    public void initGrid() {
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("上海11选5");
        strings.add("大乐透");
        strings.add("竞彩足球");
        strings.add("竞彩篮球");
        strings.add("排列三排列五");
        strings.add("更多彩种");
        PlayItemAdapter playItemAdapter = new PlayItemAdapter(this, strings);
        gridView = (GridView) findViewById(R.id.gvMainMenu);
        gridView.setAdapter(playItemAdapter);
    }

    /**
     * 返回layout xml的ID
     * 原本在Activity的onCreate方法中调用的setContentView(R.layout.xxxxLayoutId); 现在从该方法返回。
     *
     * @return
     */
    @Override
    public int getContentViewLayoutResId() {
        return R.layout.layout_main;
    }

    /**
     * 创建底部菜单，需要子类实现，在此方法中，
     * 创建多个BottomButton对象并放置到List中返回即可。
     * 如果需要哪一个按钮当前被选中，则设置BottomButton的isCurrent属性为ture.
     * //	 * @param bottomButtons
     * //	 * @param bottomMenuLayout
     *
     * @return
     */
    @Override
    public List<BottomButton> getButtonList() {

        List<BottomButton> buttons = new ArrayList<BottomButton>();

        BottomButton bottomButton = new BottomButton();
        bottomButton.setBackgroundResource(R.drawable.btm_btn_cgdt_up);
        bottomButton.setText("首页");
        buttons.add(bottomButton);
        bottomButton = new BottomButton();
        bottomButton.setBackgroundResource(R.drawable.btm_btn_hmgd);
        bottomButton.setText("合买跟单");
        buttons.add(bottomButton);
        bottomButton = new BottomButton();
        bottomButton.setBackgroundResource(R.drawable.btm_btn_kjxx_up);
        bottomButton.setText("热门活动");
        bottomButton.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assistTool.gotoActivity(ActivityOwnGame.class, false, bundle);
            }
        });
        buttons.add(bottomButton);
        bottomButton = new BottomButton();
        bottomButton.setBackgroundResource(R.drawable.btm_btn_rmhd_up);
        bottomButton.setText("开奖信息");
        buttons.add(bottomButton);
        bottomButton = new BottomButton();
        bottomButton.setBackgroundResource(R.drawable.btm_btn_wdcp_up);
        bottomButton.setText("我的彩票");
        bottomButton.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sh11x5Next.historyTemp = "0";
                assistTool.gotoActivity(BuyHistoryActivity.class, false, bundle);
            }
        });
        buttons.add(bottomButton);
        return buttons;

    }

    public void initCard() {
        BetItem item = new BetItem();
        item.setiDrawable(R.drawable.app_icon_new_01);
        item.setLastDraw(assistTool.getPreferenceString("sh11x5lastperiods") + "期：" + assistTool.getPreferenceString("sh11x5lastwin"));
        Sh11x5Next.nextTime = assistTool.getPreferenceString("sh11x5nexttime");

        String nextTime = Sh11x5Next.nextTime;
        if (null != nextTime && !"".equals(nextTime))
            try {
                nextTime = Util.format.format(new Timestamp((new Long(nextTime)).longValue() * 1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
        else
            nextTime = "";

        item.setNextTime(nextTime);
        item.setStrTilte("上海11选5");
        tvLastDraw.setText(item.getLastDraw());
    }

    public void initMenu() {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        menu.setBehindWidth(360);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.layout_menu);
//        menu.setSecondaryMenu(R.layout.layout_menu);// 设置右侧菜单
//        menu.setSecondaryShadowDrawable(R.drawable.shadow);// 设置右侧菜单阴影的图片资源
        lv_menu = (SmartListView) menu.findViewById(R.id.lv_menu);
        menu.setOnOpenListener(ool);
        lv_menu.setOnItemClickListener(this);

        menus = new ArrayList<String>();
        menus.add("首页");
        menus.add("个人资料");
        menus.add("购彩记录");
        menus.add("中奖记录");
        menus.add("财务中心");
        menus.add("我的优惠");
        menus.add("关于我们");
    }

    SlidingMenu.OnOpenListener ool = new SlidingMenu.OnOpenListener() {

        @Override
        public void onOpen() {
            menuAdapter = new MenuAdapter(MainActivity.this, menus);
            lv_menu.setAdapter(menuAdapter);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            menu.add("").setIcon(R.drawable.top_btn_person).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
                    MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("onActivityResult~~");
        if (resultCode == RESULT_OK) {
            System.out.println("onActivityResult~~Result_OK");
            assistTool.savePreferenceBoolean("isLogon", true);
            invalidateOptionsMenu();
            tvBtnLogin.setText(assistTool.getPreferenceString("mobile") + "\r\n余额：" + assistTool.getPreferenceString("cash")
                    + "元，彩金："+assistTool.getPreferenceString("award")+"元");
        } else if (resultCode == RESULT_CANCELED) {
            System.out.println("onActivityResult~~Result_Cancel");
        }
        super.onActivityResult(requestCode, resultCode, data);
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

        String menu = this.menus.get(position);
        if (null == menu || "".equals(menu)) {
            assistTool.gotoActivity(Sh11x5BetActivity.class, false, bundle);
        } else if (menu.contains("首页")) {

            return;
        } else if (menu.contains("个人资料")) {
            assistTool.gotoActivity(UserInfoActivity.class, false, bundle);
            this.menu.toggle();
        } else if (menu.contains("购彩记录")) {
            this.menu.toggle();
            Sh11x5Next.historyTemp = "0";
            assistTool.gotoActivity(BuyHistoryActivity.class, false, bundle);
        } else if (menu.contains("中奖记录")) {
            this.menu.toggle();
            Sh11x5Next.historyTemp = "1";
            assistTool.gotoActivity(BuyHistoryActivity.class, false, bundle);
        } else if (menu.contains("财务中心")) {
            this.menu.toggle();
        } else if (menu.contains("我的优惠")) {
            this.menu.toggle();
            assistTool.gotoActivity(MyDiscountActivity.class, false, bundle);
        } else if (menu.contains("关于我们")) {
            this.menu.toggle();
        } else {
            assistTool.gotoActivity(Sh11x5BetActivity.class, false, bundle);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.cardListView:
//                assistTool.gotoActivity(Sh11x5BetActivity.class, false, bundle);
//                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                menu.toggle();
                break;
            case 0:
                if (assistTool.getPreferenceBoolean("isLogon")) {
                    assistTool.gotoActivity(UserInfoActivity.class, false, bundle);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 0);
//                    assistTool.gotoActivity(LoginActivity.class, false, bundle);
                }
                return true;
            case 1:
                assistTool.showToast("Click 1");
                return true;
            case 2:
                assistTool.showToast("Click 2");
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            dialog();
            return true;
        }
        return true;
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("确定要退出吗?");
        builder.setTitle("提示");
        builder.setPositiveButton("确认",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //AccoutList.this.finish();
                        //System.exit(1);
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public void onEventMainThread(String json) {// 接收事件内容地方（参数类型可以自己根据实际情况定义）
        if (null != json && !"".equals(json)) {
            if ("sh11x5New".equals(json)) {
                tvLastDraw.setText(assistTool.getPreferenceString("sh11x5lastperiods") + "期："
                        + assistTool.getPreferenceString("sh11x5lastwin"));
//                BetItem item = new BetItem();
//                item.setiDrawable(R.drawable.logo_sh11x5);
//            item.setLastDraw(assistTool.getPreferenceString("sh11x5lastperiods") + "期：" + assistTool.getPreferenceString("sh11x5lastwin"));
//            Sh11x5Next.nextTime = assistTool.getPreferenceString("sh11x5nexttime");

//                String nextTime = Sh11x5Next.nextTime;
//                if (null != nextTime && !"".equals(nextTime))
//                    try {
//                        nextTime = Util.format.format(new Timestamp((new Long(nextTime)).longValue() * 1000));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                else
//                    nextTime = "";
//
//                item.setNextTime(nextTime);
//                item.setStrTilte("上海11选5");
//            betTypeAdapter.betItems.set(0, item);
//            betTypeAdapter.notifyDataSetChanged();
            } else if ("sh11x5betted".equals(json)) {
                tvBtnLogin.setText(assistTool.getPreferenceString("mobile") + "\r\n余额：" +
                        assistTool.getPreferenceString("cash") + "元,彩金：" + assistTool.getPreferenceString("award") +"元");
            }else if ("betSuccess".equals(json)) {
                tvBtnLogin.setText(assistTool.getPreferenceString("mobile") + "\r\n余额：" +
                        assistTool.getPreferenceString("cash") + "元,彩金：" + assistTool.getPreferenceString("award") +"元");
            }else if("logined".equals(json)){
                System.out.println("onActivityResult~~Result_OK");
                assistTool.savePreferenceBoolean("isLogon", true);
                invalidateOptionsMenu();
                tvBtnLogin.setText(assistTool.getPreferenceString("mobile") + "\r\n余额：" + assistTool.getPreferenceString("cash")
                        + "元，彩金："+assistTool.getPreferenceString("award")+"元");
            }
        }
    }
}