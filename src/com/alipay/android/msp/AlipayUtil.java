package com.alipay.android.msp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.functions.MessageProcess;
import com.tandong.sa.loopj.JsonHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.tools.AssistTool;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * @author zyn
 * 
 */
public class AlipayUtil {

	private static final String TAG = "AlipayUtil";

	private static final int RQF_PAY = 1;

	// 上个页面传过来的值 需要上传给支付宝
	private String subject = "第一彩票支付宝测试";
	private String body = "很好很便宜哦哦哦";
	private String price = "0.01";
	private String out_trade_no = "141457682322pQeL"; // 订单号

	// 需要传给支付宝的回调接口
	private String notify_url = "http://www.thefirstlottery.com/tmserver/pay/aliPay";

	private Activity context;

	public static AlipayUtil instance;

    private AssistTool assistTool;
    private ProgressDialog progressDialog;
    private RequestParams requestParams;

	public AlipayUtil(Activity context) {
		this.context = context;        assistTool = new AssistTool(context);

    }

	/**
	 * 获取支付实例
	 * 
	 * @param context
	 * @return
	 */
	public static AlipayUtil getInstance(Activity context) {
		if (instance == null) {
			instance = new AlipayUtil(context);
		}
		return instance;
	}

	/**
	 * 设置支付信息
	 * 
	 * @param out_trade_no
	 *            订单号
	 * @param price
	 *            支付金额
	 * @param subject
	 *            支付店铺
	 * @param body
	 *            支付备注
	 * @param notify_url
	 *            支付宝回调url
	 */
	public AlipayUtil setOrderInformation(String out_trade_no, String price, String subject, String body,
			String notify_url) {
		this.out_trade_no = out_trade_no;
		this.price = price;
		this.subject = subject;
		this.body = body;
		this.notify_url = notify_url;

		return instance;
	}

	public void toAlipay() {
		try {
			Log.i("ExternalPartner", "onItemClick");
			String info = getNewOrderInfo();
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			Log.i("ExternalPartner", "start pay");
			// start the pay.
			Log.i(TAG, "info = " + info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(context, mHandler);

					// 设置为沙箱模式，不设置默认为线上环境
					// alipay.setSandBox(true);

					String result = alipay.pay(orderInfo);
					Log.d(TAG, "result = " + result);

					// 获取交易状态码，具体状态代码请参看文档
					String tradeStatus = "resultStatus={";
					int imemoStart = result.indexOf("resultStatus=");
					imemoStart += tradeStatus.length();
					int imemoEnd = result.indexOf("};memo=");
					tradeStatus = result.substring(imemoStart, imemoEnd);
					Log.d(TAG, "tradeStatus = " + tradeStatus);

					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = tradeStatus;
					mHandler.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String getNewOrderInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(out_trade_no);
		sb.append("\"&subject=\"");
		sb.append(subject);
		sb.append("\"&body=\"");
		sb.append(body);
		sb.append("\"&total_fee=\"");
		sb.append(price);
		sb.append("\"&notify_url=\"");
		sb.append(notify_url);

		// 网址需要做URL编码
		sb.append(URLEncoder.encode("http://notify.java.jpxx.org/index.jsp"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			String result = (String) msg.obj;

			switch (msg.what) {
			case RQF_PAY:
				if (result.equals("9000")) {
					// 支付成功
//					Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                    askSuccessed();
				} else {
					// 支付失败，进行跳转
					Log.d(TAG, "支付宝支付失败");
					Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		};
	};

    private void askSuccessed(){
        requestParams = new RequestParams();
        requestParams.put("userid", assistTool.getPreferenceString("userid"));
        requestParams.put("price", price);
        requestParams.put("goodName", "第一彩票点数充值");

        HttpRestClient.post("pay/aliPayOrder", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String result = response.getString("result");
                    if (result.equals("success")) {

                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                        assistTool.savePreferenceString("cash", response.getString("cash"));
                        assistTool.savePreferenceString("award", response.getString("award"));
                    } else {
                        Toast.makeText(context, "支付出现异常 请重新登陆 如果还未显示 请联系客服", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                            Log.d(this.getClass().getName(), responseString);
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                            Log.d(this.getClass().getName(), errorResponse.toString());
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }

}
