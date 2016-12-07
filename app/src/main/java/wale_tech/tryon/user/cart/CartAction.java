package wale_tech.tryon.user.cart;

import android.content.Context;

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
import wale_tech.tryon.publicObject.ObjectShoe;

/**
 * Created by KeY on 2016/6/30.
 */
public class CartAction extends BaseAction {

    public final static int OPERATION_ADD = 1;
    //    public final static int OPERATION_DELETE = 2;
    public final static int OPERATION_DELETE_BATCH = 3;

    public CartAction(Context context) {
        super(context);
    }

    public int getCart(int request) {
        if (!checkNet()) {
            return ACTION_NET_DOWN;
        }

        checkRequest(request);

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_LAST_ID};
        String[] value = {sharedAction.getUsername(), "" + sharedAction.getLastId()};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_CART);
        action.setTag(HttpTag.CART_GET_CART);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();

        return ACTION_DONE;
    }

    public void operate(int operation_code, String sku_code) {
        if (!checkNet()) {
            return;
        }
        sharedAction.clearLastIdInfo();

        String[] key = {HttpSet.KEY_OPERATION_CODE, HttpSet.KEY_USERNAME, HttpSet.KEY_SKU_CODE};
        String[] value = {"" + operation_code, sharedAction.getUsername(), sku_code};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_CART);

        switch (operation_code) {
            case OPERATION_ADD:
                action.setTag(HttpTag.FAVOURITE_ADD_FAVOURITE);
                action.setDialog(context.getString(R.string.base_add_progress_title), context.getString(R.string.base_add_progress_msg));
                break;

//            case OPERATION_DELETE:
//                action.setTag(HttpTag.CART_DELETE_CART);
//                action.setDialog(context.getString(R.string.base_delete_progress_title), context.getString(R.string.base_delete_progress_msg));
//                break;

            case OPERATION_DELETE_BATCH:
                action.setTag(HttpTag.CART_DELETE_BATCH_CART);
                action.setDialog(context.getString(R.string.base_delete_progress_title), context.getString(R.string.base_delete_progress_msg));
                break;

            default:
                break;
        }

        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public void updateCount(String sku_code, int new_count) {
        if (!checkNet()) {
            return;
        }

        sharedAction.clearLastIdInfo();

        updateInteraction(sku_code, new_count);
    }

    public void updateProduct() {

    }

    private void updateInteraction(String sku_code, int new_count) {
        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_SKU_CODE, HttpSet.KEY_NEW_COUNT};
        String[] value = {sharedAction.getUsername(), sku_code, "" + new_count};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_UPDATE_CART);
        action.setTag(HttpTag.CART_UPDATE_CART);
        action.setDialog(context.getString(R.string.base_refresh_progress_title), context.getString(R.string.base_refresh_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public ArrayList<ObjectShoe> handleListResponse(String result) throws JSONException {
        JSONArray array = new JSONArray(result);
        ArrayList<ObjectShoe> shoeList = new ArrayList<>();

        if (array.length() == 0) {
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

        String last_id = shoeList.get(shoeList.size() - 1).getLastId();
        sharedAction.setLastId(Integer.parseInt(last_id));

        return shoeList;
    }

    public void handleResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        showSnack(obj.getString(HttpResult.RESULT));
//        toast.showToast(obj.getString(HttpResult.RESULT));
    }

}
