package com.evstudio.thefirstlottery.mobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.BetItemAdapter;
import com.evstudio.thefirstlottery.mobile.adapter.Sh11x5BetFragmentAdapter;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.common.MyToast;
import com.evstudio.thefirstlottery.mobile.core.Sh11x5BetItemFragment;
import com.tandong.sa.loopj.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericren on 14-9-11.
 */
public class Sh11x5BetActivity extends SherlockFragmentActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

    private ActionBar mActionBar;
    private ViewPager mPager;

    private List<Sh11x5BetItemFragment> mFragmentList;
    private String[] mTabTitles = {"任选一", "任选二", "任选三","任选四", "任选五", "任选六", "任选七", "任选八", "组选前二", "组选前三", "直选前二", "直选前三"};
    private String[] playCmd = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "11", "10", "12"};
    private int[] selectNumbers = {1,2,3,4,5,6,7,8,2,3,2,3};

    private ArrayList<String> lstItem;
    private BetItemAdapter itemAdapter;

    private ProgressDialog progressDialog;

    private String responseData;

    private Sh11x5BetFragmentAdapter adapter;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(this.getLocalClassName(), "Get It" + String.valueOf(item.getItemId()));
                this.finish();
                break;
            case 0:
                Intent intent = new Intent();
                intent.setClass(Sh11x5BetActivity.this, Sh11x5History.class);
                startActivityForResult(intent, 200);
                break;
            default:
                Log.d(this.getLocalClassName(), String.valueOf(item.getItemId()));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_main);

        mFragmentList = new ArrayList<Sh11x5BetItemFragment>();

        /** Getting a reference to ViewPager from the layout */
        mPager = (ViewPager) findViewById(R.id.vpFirst);

        adapter = new Sh11x5BetFragmentAdapter(getSupportFragmentManager(), mFragmentList);

        mPager.setAdapter(adapter);

        mPager.setOnPageChangeListener(this);

        /** Getting a reference to action bar of this activity */
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        /** Set tab navigation mode */
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mActionBar.setDisplayShowTitleEnabled(true);

        mActionBar.setLogo(R.drawable.app_icon_new_01);

        for (int i = 0; i < mTabTitles.length; i++) {
            ActionBar.Tab tab = mActionBar.newTab();
            tab.setText(mTabTitles[i]);
            tab.setTabListener(this);
            mActionBar.addTab(tab, i);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在载入");
        progressDialog.setMessage("正在载入数据，请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        HttpRestClient.post("mobile/getSh11x5Recent?recent=15", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Coming:" + response.toString());
                responseData = response.toString();
                progressDialog.dismiss();
                //将Fragment加入到List中，并将Tab的title传递给Fragment
                for (int i = 0; i < mTabTitles.length; i++) {
                    Sh11x5BetItemFragment fragment = new Sh11x5BetItemFragment();
                    Bundle args = new Bundle();
                    args.putString("title", mTabTitles[i]);
                    args.putString("arg", playCmd[i]);
                    args.putInt( "numbers", selectNumbers[i] );
                    args.putString("data", responseData);
                    fragment.setArguments(args);

                    mFragmentList.add(fragment);
                }
                adapter.notifyDataSetChanged();
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                MyToast.instance.showToast(Sh11x5BetActivity.this, getLayoutInflater(), "无法连接服务器，请检查网络！");
                progressDialog.dismiss();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        /**
         * After ADT 22 the PagerAdapter has gotten very strict about calling notifyDataSetChanged() before calling getCount().
         * It evidently keeps track of what it thinks the count should be and if this is not the same as what getCount() returns it throws this exception.
         * So the solution is simply to call notifyDataSetChanged() on the adapter every time the size of the data changes.
         *
         *In my case I have a background task that is creating an array of data.
         * After the array is filled I call notifyDataSetChanged() in onPostExecute().
         * When I had a path in onPostExecute() that did not call notifyDataSetChanged() I was getting the exception.
         */

    }


    /**
     * Called when a tab enters the selected state.
     *
     * @param tab The tab that was selected
     * @param ft  A {@link android.support.v4.app.FragmentTransaction} for queuing fragment operations to execute
     *            during a tab switch. The previous tab's unselect and this tab's select will be
     *            executed in a single transaction. This FragmentTransaction does not support
     */
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mPager.setCurrentItem(tab.getPosition());
    }

    /**
     * Called when a tab exits the selected state.
     *
     * @param tab The tab that was unselected
     * @param ft  A {@link android.support.v4.app.FragmentTransaction} for queuing fragment operations to execute
     *            during a tab switch. This tab's unselect and the newly selected tab's select
     *            will be executed in a single transaction. This FragmentTransaction does not
     */
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    /**
     * Called when a tab that is already selected is chosen again by the user.
     * Some applications may use this action to return to the top level of a category.
     *
     * @param tab The tab that was reselected.
     * @param ft  A {@link android.support.v4.app.FragmentTransaction} for queuing fragment operations to execute
     *            once this method returns. This FragmentTransaction does not support
     */
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        mActionBar.setSelectedNavigationItem(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("购彩助手").setIcon(R.drawable.app_icon_new_08).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }
}