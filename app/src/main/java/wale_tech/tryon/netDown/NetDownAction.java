package wale_tech.tryon.netDown;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.Base_Frag;
import wale_tech.tryon.http.HttpAction;
import wale_tech.tryon.http.HttpHandler;
import wale_tech.tryon.http.HttpResult;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.http.HttpTag;

/**
 * Created by KeY on 2016/11/4.
 */

public class NetDownAction extends BaseAction {

    private Base_Frag base_frag;

    public NetDownAction(Context context, Base_Frag base_frag) {
        super(context);
        this.base_frag = base_frag;
    }

    public void ping() {
        if (!checkNet()) {
            return;
        }

        String[] key = {""};
        String[] value = {""};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_PING);
        action.setMap(key, value);
        action.setTag(HttpTag.NET_DOWN_PING);
        action.setHandler(new HttpHandler(context, base_frag));
        action.setDialog(context.getString(R.string.base_refresh_progress_title), context.getString(R.string.base_refresh_progress_msg));
        action.interaction();
    }

    public boolean handleResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        String response = obj.getString(HttpResult.RESULT);
        Log.i("Result", "response  is : " + response);
        return response.equals("Link");
    }
}
