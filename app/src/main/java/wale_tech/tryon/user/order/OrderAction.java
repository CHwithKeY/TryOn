package wale_tech.tryon.user.order;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.http.HttpAction;
import wale_tech.tryon.http.HttpHandler;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicObject.ObjectOrder;

/**
 * Created by KeY on 2016/7/25.
 */
public class OrderAction extends BaseAction {

    public OrderAction(Context context) {
        super(context);
    }

    public int getOrder(int request) {
        if (!checkNet()) {
            return ACTION_NET_DOWN;
        }

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_LAST_ID};
        String[] value = {sharedAction.getUsername(), sharedAction.getLastId() + ""};

        checkRequest(request);

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_ORDER);
        action.setTag(HttpTag.ORDER_GET_ORDER);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();

        return ACTION_DONE;
    }

    public ArrayList<ObjectOrder> handleResponse(String result) throws JSONException {
        JSONArray array = new JSONArray(result);
        ArrayList<ObjectOrder> orderList = new ArrayList<>();

        if (array.length() == 0) {
            showSnack(context.getString(R.string.base_snack_no_more_item));
            return orderList;
        }

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            ObjectOrder order = new ObjectOrder();

            for (int k = 0; k < obj.length(); k++) {
                order.value_set[k] = obj.getString(ObjectOrder.key_set[k]);
            }
            orderList.add(order);
        }

        sharedAction.setLastId(Integer.parseInt(orderList.get(orderList.size() - 1).getLastId()));

        return orderList;
    }
}
