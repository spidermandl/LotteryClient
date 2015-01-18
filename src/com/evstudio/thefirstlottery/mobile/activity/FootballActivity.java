package com.evstudio.thefirstlottery.mobile.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.FootballAdapter;
import com.evstudio.thefirstlottery.mobile.adapter.FootballMatchAdapter;
import com.evstudio.thefirstlottery.mobile.fragments.FragmentFootball01;
import com.evstudio.thefirstlottery.mobile.fragments.FragmentFootball02;
import com.evstudio.thefirstlottery.mobile.pojo.FootballGameInfo;
import com.tandong.sa.bv.BelowView;
import com.tandong.sa.json.Gson;
import com.tandong.sa.json.GsonBuilder;
import com.tandong.sa.json.JsonElement;
import com.tandong.sa.json.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericren on 14-10-31.
 */
public class FootballActivity extends SherlockFragmentActivity implements ViewPager.OnPageChangeListener, ActionBar.OnNavigationListener {
    private ActionBar actionBar;
    private ViewPager mPager;

    private List<Fragment> mFragmentList;

    private ArrayList<FootballGameInfo> arrayList = new ArrayList<FootballGameInfo>();

    private TextView[] textView = new TextView[10];

    private BelowView blv;

    private String[] playTitle = {"胜平负/让球胜平负",
            "半全场",
            "总进球数",
            "比分",
            "混合过关",
            "任选9场",
            "14场胜负",
            "进球彩",
            "单场"};

    private String[] playCmd = {"01",
            "02",
            "03",
            "04",
            "05",
            "06",
            "07",
            "08",
            "09"
    };

    private TextView tvSelect;

    private ProgressDialog progressDialog;

    private ListView lvFootballMatch;

    private FootballMatchAdapter adapter;

    private FootballAdapter footballAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_football_main);

        actionBar = getSupportActionBar();
        actionBar.setTitle("竞彩足球");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Context context = getSupportActionBar().getThemedContext();

        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context,
                R.array.footballPlayFunc,
                R.layout.sherlock_spinner_item);
        list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        getSupportActionBar().setListNavigationCallbacks(list, this );

        mPager = (ViewPager) findViewById(R.id.vpFootball);
        mFragmentList = new ArrayList<Fragment>();

        footballAdapter = new FootballAdapter(getSupportFragmentManager(), mFragmentList);

        FragmentFootball01 fragment01 = new FragmentFootball01();
        Bundle bundle = new Bundle();
        bundle.putString( "title", playTitle[0] );
        fragment01.setArguments(bundle);
        mFragmentList.add(fragment01);

        fragment01 = new FragmentFootball01();
        bundle = new Bundle();
        bundle.putString( "title", playTitle[0] );
        fragment01.setArguments(bundle);
        mFragmentList.add(fragment01);

        fragment01 = new FragmentFootball01();
        bundle = new Bundle();
        bundle.putString( "title", playTitle[0] );
        mFragmentList.add(fragment01);

        fragment01 = new FragmentFootball01();
        bundle = new Bundle();
        bundle.putString( "title", playTitle[0] );
        mFragmentList.add(fragment01);

        fragment01 = new FragmentFootball01();
        bundle = new Bundle();
        bundle.putString( "title", playTitle[0] );
        mFragmentList.add(fragment01);


        fragment01 = new FragmentFootball01();
        bundle = new Bundle();
        bundle.putString( "title", playTitle[0] );
        mFragmentList.add(fragment01);

        fragment01 = new FragmentFootball01();
        bundle = new Bundle();
        bundle.putString( "title", playTitle[0] );
        mFragmentList.add(fragment01);

        FragmentFootball02 fragment02 = new FragmentFootball02();

        bundle = new Bundle();
        bundle.putString( "title", playTitle[1] );
        fragment02.setArguments(bundle);
//        mFragmentList.add(fragment02);
//        FragmentFootball03 fragment03 = new FragmentFootball03();
//        mFragmentList.add(fragment03);
//        FragmentFootball04 fragment04 = new FragmentFootball04();
//        mFragmentList.add(fragment04);

//        adapter.notifyDataSetChanged();
        mPager.setAdapter(footballAdapter);

    }
    class DropDownListenser implements ActionBar.OnNavigationListener {
        // 得到和SpinnerAdapter里一致的字符数组
        /* 当选择下拉菜单项的时候，将Activity中的内容置换为对应的Fragment */
        public boolean onNavigationItemSelected(int itemPosition, long itemId) {
            // 生成自定的Fragment
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            // 将Activity中的内容替换成对应选择的Fragment
            transaction.replace(android.R.id.content, mFragmentList.get(itemPosition), playTitle[itemPosition]);
            transaction.commit();
            return true;

        }
    }
//    @Override
//    public boolean onCreatePanelMenu(int featureId, Menu menu) {
//        return super.onCreatePanelMenu(featureId, menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called whenever a navigation item in your action bar
     * is selected.
     *
     * @param itemPosition Position of the item clicked.
     * @param itemId       ID of the item clicked.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        this.mPager.setCurrentItem( itemPosition );
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add("玩法选择").setTitle("玩法选择").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
//                MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//        return true;
//    }

    public String jsonFormatter(String uglyJSONString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        getSupportActionBar().setSelectedNavigationItem(i);
    }

    @Override
        public void onPageScrollStateChanged(int i) {

    }
}