package wale_tech.tryon.user.coupon;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.Base_Frag;
import wale_tech.tryon.http.HttpAction;
import wale_tech.tryon.http.HttpHandler;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicObject.ObjectCoupon;

/**
 * Created by KeY on 2016/11/2.
 */

public class CouponAction extends BaseAction {

    private Base_Frag base_frag;

    public final static int COUPON_ENABLE = 0;
    public final static int COUPON_USED = 1;
    public final static int COUPON_UNABLE = 2;

    public final static int COUPON_AWARD_CHECK = 10;
    public final static int COUPON_AWARD_GET = 11;

    public CouponAction(Context context) {
        super(context);
    }

    public CouponAction(Context context, Base_Frag base_frag) {
        super(context);
        this.base_frag = base_frag;
    }

    public void checkCouponAward(String sku_code) {
        if (!checkNet()) {
            return;
        }

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_SKU_CODE, HttpSet.KEY_OPERATION_CODE};
        String[] value = {sharedAction.getUsername(), sku_code, "" + COUPON_AWARD_CHECK};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_COUPON_AWARD);
        action.setTag(HttpTag.COUPON_CHECK_COUPON);
        action.setMap(key, value);
//        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));

        action.interaction();
    }

    public void getCouponAward(String sku_code) {
        if (!checkNet()) {
            return;
        }

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_SKU_CODE, HttpSet.KEY_OPERATION_CODE};
        String[] value = {sharedAction.getUsername(), sku_code, "" + COUPON_AWARD_GET};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_COUPON_AWARD);
        action.setTag(HttpTag.COUPON_AWARD_COUPON);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));

        action.interaction();
    }

    public void getCoupon(int get_coupon_type) {
        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_GET_COUPON_TYPE};
        String[] value = {sharedAction.getUsername(), "" + get_coupon_type};

        Log.i("Result", "user name is : " + sharedAction.getUsername());

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_COUPON);
        action.setTag(HttpTag.COUPON_COUPON);
        if (base_frag != null) {
            action.setHandler(new HttpHandler(context, base_frag));
        } else {
            action.setHandler(new HttpHandler(context));
        }
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.interaction();
    }

    public ArrayList<ObjectCoupon> handleResponse(String result) throws JSONException {
        ArrayList<ObjectCoupon> couponList = new ArrayList<>();
        JSONArray array = new JSONArray(result);

        if (array.length() == 0) {
            showSnack(context.getString(R.string.base_snack_no_item));
            return couponList;
        }

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            ObjectCoupon coupon = new ObjectCoupon();

            for (int k = 0; k < obj.length(); k++) {
                coupon.value_set[k] = obj.getString(ObjectCoupon.key_set[k]);
            }
            couponList.add(coupon);
        }

        return couponList;
    }
}
