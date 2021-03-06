package wale_tech.tryon.product;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.login.Login_Act;
import wale_tech.tryon.pattern.PatternAction;
import wale_tech.tryon.publicObject.ObjectCoupon;
import wale_tech.tryon.publicSet.IntentSet;
import wale_tech.tryon.user.coupon.CouponAction;
import wale_tech.tryon.user.favourite.FavAction;
import wale_tech.tryon.R;
import wale_tech.tryon.ScanAction;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.http.HttpAction;
import wale_tech.tryon.http.HttpHandler;
import wale_tech.tryon.http.HttpResult;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicObject.ObjectShoe;
import wale_tech.tryon.publicObject.ObjectShoeImage;

/**
 * Created by KeY on 2016/6/30.
 */
public class ProductAction extends BaseAction {

    private FavAction favAction;
    private CouponAction couponAction;

//    private boolean isGetCoupon;

//    public boolean isGetCoupon() {
//        return isGetCoupon;
//    }

    public ProductAction(Context context) {
        super(context);
        favAction = new FavAction(context);
        couponAction = new CouponAction(context);
//        isGetCoupon = false;
    }

    public void getShoeDetails(String sku_code, String path) {
        if (!checkNet()) {
            return;
        }

//        if (!sharedAction.getLoginStatus()) {
//            Intent login_int = new Intent(context, Login_Act.class);
//            login_int.putExtra(IntentSet.KEY_SKU_CODE, sku_code);
//            login_int.putExtra(IntentSet.KEY_TRIGGER_PATH, path);
//            login_int.setAction("GetSkuCodeAction");
//            context.startActivity(login_int);
//            ((Base_Act) context).finish();
//            return;
//        }

        String username = "Visitor";
        if (sharedAction.getLoginStatus()) {
            username = sharedAction.getUsername();
        }

        Log.i("Result", "un is : " + username + " sku " + sku_code + " path " + path);

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_SKU_CODE, HttpSet.KEY_TRIGGER_PATH};
        String[] value = {username, sku_code, path};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_SHOE_DETAILS);
        action.setTag(HttpTag.PRODUCT_GET_SHOE_DETAILS);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));

        action.interaction();
    }

    public void onFavouriteOperate(int operation_code, String sku_code) {
        favAction.operate(operation_code, sku_code);
    }

    public void onColorPick(String brand, String product_name) {
        String[] key = {HttpSet.KEY_BRAND, HttpSet.KEY_PRODUCT_NAME};
        String[] value = {brand, product_name};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_SHOE_COLOR);
        action.setTag(HttpTag.PRODUCT_GET_SHOE_COLOR);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));

        action.interaction();
    }

    public void checkCouponAward(String sku_code) {
        couponAction.checkCouponAward(sku_code);
    }

    public void getCouponAward(String sku_code) {
        couponAction.getCouponAward(sku_code);
    }

//
//    public void checkCouponOwn(String sku_code) {
//        if (!checkNet()) {
//            return;
//        }
//
//        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_SKU_CODE};
//        String[] value = {sharedAction.getUsername(), sku_code};
//
//        HttpAction action = new HttpAction(context);
//        action.setUrl(HttpSet.URL_COUPON_AWARD);
//        action.setTag(HttpTag.COUPON_CHECK_COUPON);
//        action.setMap(key, value);
////        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
//        action.setHandler(new HttpHandler(context));
//
//        action.interaction();
//    }

//    public void onGetCoupon(String sku_code) {
//        if (!checkNet()) {
//            return;
//        }
//
//        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_SKU_CODE};
//        String[] value = {sharedAction.getUsername(), sku_code};
//
//        HttpAction action = new HttpAction(context);
//        action.setUrl(HttpSet.URL_COUPON_AWARD);
//        action.setTag(HttpTag.COUPON_AWARD_COUPON);
//        action.setMap(key, value);
//        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
//        action.setHandler(new HttpHandler(context));
//
//        action.interaction();
//    }

    public ObjectShoe handleDetailsResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        ObjectShoe shoe = new ObjectShoe();

        for (int i = 0; i < obj.length(); i++) {
            shoe.value_set[i] = obj.getString(ObjectShoe.key_set[i]);
        }

        return shoe;
    }

    public ArrayList<ObjectShoeImage> handleColorPickResponse(String result) throws JSONException {
        ArrayList<ObjectShoeImage> shoeImageList = new ArrayList<>();

        JSONArray array = new JSONArray(result);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            ObjectShoeImage shoeImage = new ObjectShoeImage();

            shoeImage.setColor(obj.getString(HttpResult.SHOE_COLOR));
            shoeImage.setImagePath(obj.getString(HttpResult.IMAGE_PATH));

            shoeImageList.add(shoeImage);
        }

        return shoeImageList;
    }

    public void handleFavouriteResponse(String result) throws JSONException {
        favAction.handleResponse(result);
//        showSnack(new JSONObject(result).getString(HttpResult.RESULT));
    }

    public boolean handleCouponCheckResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        return obj.getBoolean(HttpResult.RESULT);
    }

    public void handleCouponAwardResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        ObjectCoupon coupon = new ObjectCoupon();

//        isGetCoupon = true;

        for (int i = 0; i < obj.length(); i++) {
            coupon.value_set[i] = obj.getString(ObjectCoupon.key_set[i]);
        }

        new AlertDialog.Builder(context)
                .setMessage(context.getString(R.string.product_action_congratulation_start) + coupon.getName() + context.getString(R.string.product_action_congratulation_end))
                .setPositiveButton(context.getString(R.string.base_dialog_btn_okay), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
}
