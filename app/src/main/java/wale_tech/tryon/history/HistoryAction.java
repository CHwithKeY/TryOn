package wale_tech.tryon.history;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

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
 * Created by KeY on 2016/11/2.
 */

public class HistoryAction extends BaseAction {

    public HistoryAction(Context context) {
        super(context);
    }

    public int getHistory(int request) {
        if (!checkNet()) {
            return ACTION_NET_DOWN;
        }

        if (!checkLoginStatus()) {
            ((AppCompatActivity) context).finish();
            return ACTION_LACK;
        }

//        this.request = request;
//        if (request != REQUEST_LOAD_MORE) {
//            sharedAction.clearLastIdInfo();
//        }
        checkRequest(request);

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_LAST_ID};
        String[] value = {sharedAction.getUsername(), "" + sharedAction.getLastId()};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_HISTORY);
        action.setTag(HttpTag.HISTORY_GET_HISTORY);
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.interaction();
        return ACTION_DONE;
    }

    public ArrayList<ObjectShoe> handleResponse(String result) throws JSONException {
        ArrayList<ObjectShoe> shoeList = new ArrayList<>();
        JSONArray array = new JSONArray(result);

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

        sharedAction.setLastId(Integer.parseInt(shoeList.get(shoeList.size() - 1).getLastId()));

        return shoeList;
    }
}
