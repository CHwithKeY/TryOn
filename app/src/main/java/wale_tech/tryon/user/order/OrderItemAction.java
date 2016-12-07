package wale_tech.tryon.user.order;

import android.content.Context;
import android.util.Log;

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
import wale_tech.tryon.publicObject.ObjectShoe;

/**
 * Created by KeY on 2016/7/26.
 */
public class OrderItemAction extends BaseAction {

    public OrderItemAction(Context context) {
        super(context);
    }

    public void getShoeList(String order_num) {
        String[] key = {HttpSet.KEY_ORDER_NUM};
        String[] value = {order_num};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_ORDER_ITEM);
        action.setTag(HttpTag.ORDER_GET_ORDER_ITEM);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public ArrayList<ObjectShoe> handleResponse(String result) throws JSONException {
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

//        String last_id = shoeList.get(shoeList.size() - 1).getLastId();
//        Log.i("Result", "last_id is : " + last_id);
//        sharedAction.setLastId(Integer.parseInt(last_id));

        return shoeList;
    }
}
