package wale_tech.tryon.history;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
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
import wale_tech.tryon.publicObject.ObjectHistoryImage;
import wale_tech.tryon.publicObject.ObjectShoe;
import wale_tech.tryon.publicObject.ObjectUtil;

/**
 * Created by KeY on 2016/11/2.
 */

public class HistoryAction extends BaseAction {

    private Base_Frag fragment;

    public HistoryAction(Context context) {
        super(context);
    }

    public HistoryAction(Context context, Base_Frag fragment) {
        super(context);
        this.fragment = fragment;
    }

    public int getHistoryRecord(int request) {
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
        action.setTag(HttpTag.HISTORY_GET_RECORD);
        if (fragment == null) {
            action.setHandler(new HttpHandler(context));
        } else {
            action.setHandler(new HttpHandler(context, fragment));
        }
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.interaction();
        return ACTION_DONE;
    }

    public int getHistoryImage(int request) {
        if (!checkNet()) {
            return ACTION_NET_DOWN;
        }

        if (!checkLoginStatus()) {
            ((AppCompatActivity) context).finish();
            return ACTION_LACK;
        }

        checkRequest(request);

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_LAST_ID};
        String[] value = {sharedAction.getUsername(), "" + sharedAction.getLastId()};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_HISTORY_IMAGE);
        action.setTag(HttpTag.HISTORY_GET_IMAGE);
        if (fragment == null) {
            action.setHandler(new HttpHandler(context));
        } else {
            action.setHandler(new HttpHandler(context, fragment));
        }
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_msg));
        action.interaction();
        return ACTION_DONE;
    }

    public ArrayList<ObjectShoe> handleRecordResponse(String result) throws JSONException {
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

    public ArrayList<String> handleImageResponse(String result) throws JSONException {
        ArrayList<String> imageList = new ArrayList<>();
        JSONArray array = new JSONArray(result);

        if (array.length() == 0) {
            showSnack(context.getString(R.string.base_snack_no_more_item));
            return imageList;
        }

        for (int i = 0; i < array.length(); i++) {
            imageList.add(array.getString(i));
        }

//        for (int i = 0; i < array.length(); i++) {
//            ObjectHistoryImage historyImage = ObjectUtil.wrapHistoryImage(array.getJSONObject(i));
//            imageList.add(historyImage);
//        }
//
//        sharedAction.setLastId(Integer.parseInt(imageList.get(imageList.size() - 1).getLastId()));

        return imageList;
    }
}
