package wale_tech.tryon.user.cart.orderPart;

import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.http.HttpAction;
import wale_tech.tryon.http.HttpHandler;
import wale_tech.tryon.http.HttpResult;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicObject.ObjectCoupon;
import wale_tech.tryon.publicObject.ObjectShoe;
import wale_tech.tryon.user.coupon.CouponAction;

/**
 * Created by lenovo on 2016/11/17.
 */

public class OrderConfirmAction extends BaseAction {

    private CouponAction couponAction;

    public OrderConfirmAction(Context context) {
        super(context);
        couponAction = new CouponAction(context);
    }

    public int getOrderConfirmShoe(String sku_code_series) {
        if (!checkNet()) {
            return ACTION_NET_DOWN;
        }

        String[] key = {HttpSet.KEY_SKU_CODE};
        String[] value = {sku_code_series};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_CONFIRM_ITEM);
        action.setTag(HttpTag.ORDER_CONFIRM_GET_ITEM);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();

        return ACTION_DONE;
    }

    public int getEnableCoupon() {
        if (!checkNet()) {
            return ACTION_NET_DOWN;
        }
        couponAction.getCoupon(CouponAction.COUPON_ENABLE);

        return ACTION_DONE;
    }

    // 这里的 sku_code_series 是多个 sku_code 通过"-"连接在一起
    public void onCreateOrder(String coupon_number, String sku_code_series, String count_series, String total_price_series, String order_total_price) {
        String[] key = {
                HttpSet.KEY_USERNAME, HttpSet.KEY_COUPON_NUMBER, HttpSet.KEY_SKU_CODE,
                HttpSet.KEY_COUNT, HttpSet.KEY_PRICE, HttpSet.KEY_TOTAL_PRICE};
        String[] value = {
                sharedAction.getUsername(), coupon_number, sku_code_series,
                count_series, total_price_series, order_total_price};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_CREATE_ORDER);
        action.setTag(HttpTag.ORDER_CONFIRM_CREATE_ORDER);
        action.setDialog(context.getString(R.string.base_add_progress_title), context.getString(R.string.base_add_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public ArrayList<ObjectShoe> handleOrderConfirmResponse(String result) throws JSONException {
        JSONArray array = new JSONArray(result);
        ArrayList<ObjectShoe> shoeList = new ArrayList<>();

        if (array.length() == 0) {
            showSnack(context.getString(R.string.base_snack_no_more_item));
            return shoeList;
        }

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            ObjectShoe shoe = new ObjectShoe();

            for (int k = 0; k < obj.length(); k++) {
                shoe.value_set[k] = obj.getString(ObjectShoe.key_set[k]);
            }
            shoeList.add(shoe);
        }
        return shoeList;
    }

    public ArrayList<ObjectCoupon> handleEnableCouponResponse(String result) throws JSONException {
        return couponAction.handleResponse(result);
    }

    public void handleCreateOrderResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        showSnack(obj.getString(HttpResult.RESULT));
    }


}