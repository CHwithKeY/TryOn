package wale_tech.tryon.trigger;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.Base_Serv;
import wale_tech.tryon.http.HttpAction;
import wale_tech.tryon.http.HttpHandler;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicObject.ObjectShoe;

/**
 * Created by lenovo on 2016/11/22.
 */

public class TriggerAction extends BaseAction {

    private Base_Serv base_serv;

    public TriggerAction(Context context) {
        super(context);
    }

    public TriggerAction(Context context, Base_Serv base_serv) {
        super(context);
        this.base_serv = base_serv;
    }

    public int getList(int request, String trigger_result, String trigger_path, String trigger_work_space, boolean isShowDialog) {
        if (!checkNet()) {
            return ACTION_NET_DOWN;
        }

        checkRequest(request);

        if (sharedAction.getUsername().isEmpty()) {
            return ACTION_LACK;
        }

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_TRIGGER_RESULT, HttpSet.KEY_TRIGGER_PATH, HttpSet.KEY_TRIGGER_WORK_SPACE};
        String[] value = {sharedAction.getUsername(), trigger_result, trigger_path, trigger_work_space};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_TRIGGER);
        action.setTag(HttpTag.TRIGGER_GET_TRIGGER);
        action.setMap(key, value);

        if (isShowDialog) {
            action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        }

        if (base_serv == null) {
            action.setHandler(new HttpHandler(context));
        } else {
            action.setHandler(new HttpHandler(context, base_serv));
        }

        action.interaction();
        return ACTION_DONE;
    }

    public ArrayList<ObjectShoe> handleListResponse(String result) throws JSONException {
        JSONArray array = new JSONArray(result);
        ArrayList<ObjectShoe> shoeList = new ArrayList<>();

        if (array.length() == 0) {
            if (base_serv == null) {
                showSnack(context.getString(R.string.base_snack_no_more_item));
                return shoeList;
            } else {
                return shoeList;

            }
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

}
