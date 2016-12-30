package wale_tech.tryon.pattern;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.Base_Frag;
import wale_tech.tryon.user.cart.CartAction;
import wale_tech.tryon.http.HttpAction;
import wale_tech.tryon.http.HttpHandler;
import wale_tech.tryon.http.HttpResult;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicObject.ObjectShoe;

/**
 * Created by KeY on 2016/7/13.
 */
public class PatternAction extends BaseAction {
    private Base_Frag fragment;

    private String save_sku_code = "";
    private String save_color = "";
    private String save_size = "";

    public PatternAction(Context context, Base_Frag fragment) {
        super(context);
        this.fragment = fragment;
    }

    public String getSaveColor() {
        return save_color;
    }

    public String getSaveSize() {
        return save_size;
    }

    public void clearSaveParams() {
        save_sku_code = "";
        save_color = "";
        save_size = "";
    }

    public void getPattern(String brand, String product_name, boolean isStock) {
        if (!checkNet()) {
            return;
        }

        String[] key = {HttpSet.KEY_BRAND, HttpSet.KEY_PRODUCT_NAME, HttpSet.KEY_IS_STOCK};
        String[] value = {brand, product_name, "" + isStock};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_PATTERN);
        action.setTag(HttpTag.PATTERN_GET_PATTERN);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context, fragment));

        action.interaction();
    }

    public void getSizeMatch(String brand, String product_name, String color, boolean isStock) {
        if (!checkNet()) {
            return;
        }
        String[] key = {HttpSet.KEY_BRAND, HttpSet.KEY_PRODUCT_NAME, HttpSet.KEY_COLOR, HttpSet.KEY_IS_STOCK};
        String[] value = {brand, product_name, color, "" + isStock};

        save_color = color;
        save_size = "";

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_PATTERN_SIZE);
        action.setTag(HttpTag.PATTERN_GET_SIZE);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context, fragment));

        action.interaction();
    }

    public void getShoeStockAndPrice(String sku_code, String size) {
        if (!checkNet()) {
            return;
        }

        save_size = size;
        save_sku_code = sku_code;

        String[] key = {HttpSet.KEY_SKU_CODE, HttpSet.KEY_COLOR, HttpSet.KEY_SIZE};
        String[] value = {sku_code, save_color, size};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_PATTERN_STOCK_PRICE);
        action.setTag(HttpTag.PATTERN_STOCK_PRICE);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context, fragment));

        action.interaction();
    }

    public void onAddInCart() {
        if (!checkNet()) {
            return;
        }

        if (!checkLoginStatus()) {
            return;
        }

        if (save_color.equals("") || save_size.equals("")) {
            showSnack(context.getString(R.string.pattern_snack_select_color_size));
            return;
        }

        String[] key = {HttpSet.KEY_OPERATION_CODE, HttpSet.KEY_USERNAME, HttpSet.KEY_SKU_CODE};
        String[] value = {"" + CartAction.OPERATION_ADD, sharedAction.getUsername(), save_sku_code};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_CART);
        action.setTag(HttpTag.PATTERN_ADD_CART);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_add_progress_title), context.getString(R.string.base_add_progress_msg));
        if (fragment != null) {
            action.setHandler(new HttpHandler(context, fragment));
        } else {
            action.setHandler(new HttpHandler(context));
        }

        action.interaction();
    }

    public ArrayList<ObjectShoe> handlePatternResponse(String result) throws JSONException {
        Log.i("Result", "pattern result is : " + result);
        JSONArray array = new JSONArray(result);
        ArrayList<ObjectShoe> shoeList = new ArrayList<>();

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

    public ArrayList<String> handleSizeResponse(String result) throws JSONException {
        Log.i("Result", "size result is : " + result);
        JSONArray array = new JSONArray(result);
        ArrayList<String> sizeList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            sizeList.add(array.getString(i));
        }
        return sizeList;
    }

    public ObjectShoe handleStockPriceResponse(String result) throws JSONException {
        Log.i("Result", "get shoe detail in pattern is : " + result);

        JSONObject obj = new JSONObject(result);
        ObjectShoe shoe = new ObjectShoe();

        for (int k = 0; k < obj.length(); k++) {
            shoe.value_set[k] = obj.getString(ObjectShoe.key_set[k]);
        }

        return shoe;
    }
//
//    public void handleColorResponse(String result) throws JSONException {
//        Log.i("Result", "color result is : " + result);
//        JSONArray array = new JSONArray(result);
//    }

    public void handleAddCartResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        showSnack(obj.getString(HttpResult.RESULT));
    }
}
