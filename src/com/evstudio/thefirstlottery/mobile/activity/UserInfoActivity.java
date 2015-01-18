package com.evstudio.thefirstlottery.mobile.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.evstudio.thefirstlottery.mobile.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.tandong.sa.tools.AssistTool;

import br.com.dina.ui.model.BasicItem;
import br.com.dina.ui.widget.UITableView;

/**
 * Created by ericren on 14-8-28.
 */
public class UserInfoActivity extends SherlockActivity {
    private ActionBar actionBar;
    private TextView tvCash;
    private AssistTool assistTool;
    private ImageView barcode;
    private UITableView uiTableView;
    private UITableView uiUserBarCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_userinfo);
        assistTool = new AssistTool(this);

        actionBar = getSupportActionBar();

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String cash = assistTool.getPreferenceString("cash");

        uiTableView = (UITableView) findViewById(R.id.userInfoTableView);
        createList();
        uiTableView.commit();

        uiUserBarCode = (UITableView) findViewById(R.id.userBrCode);
        createList2();
        uiUserBarCode.commit();

        barcode = (ImageView) findViewById(R.id.imgBarcode);

        String url = "http://www.thefirstlottery.com/v/qrverify?id=" + assistTool.getPreferenceString("userid");

        try {
            barcode.setImageBitmap(Create2DCode(url));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void createList() {
        CustomClickListener listener = new CustomClickListener();
        uiTableView.setClickListener(listener);

        uiTableView.addBasicItem("现金账号余额：" + assistTool.getPreferenceString("cash") + "元");
        uiTableView.addBasicItem("彩金账号余额：0元");
        uiTableView.addBasicItem("您传播的人数：0人");
        uiTableView.addBasicItem("您的传播奖励：0元");
        uiTableView.addBasicItem("修改密码");
    }

    private void createList2() {
        BrCodeClickListener brListener = new BrCodeClickListener();
//        uiUserBarCode.setClickListener(brListener);
        BasicItem i1 = new BasicItem("我的二维码");
        i1.setDrawable(0);
        i1.setClickable(false);
        uiUserBarCode.addBasicItem(i1);
    }

    private class CustomClickListener implements UITableView.ClickListener {
        @Override
        public void onClick(int index) {
            switch (index){
                case 4://修改密码
                    assistTool.gotoActivity(ChangePasswordActivity.class,false);
                    break;
            }
        }
    }

    private class BrCodeClickListener implements UITableView.ClickListener {
        @Override
        public void onClick(int index) {
//            Toast.makeText(UserInfoActivity.this, "item clicked: " + index, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case 0:
                assistTool.gotoActivity(PersonalInfoActivity.class,false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
//        return super.onOptionsItemSelected(item);
    }

    public Bitmap Create2DCode(String str) throws WriterException {
        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 400, 400);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        //二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("编辑").
                setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
                        MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }
}