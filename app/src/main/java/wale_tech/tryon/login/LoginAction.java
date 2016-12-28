package wale_tech.tryon.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import wale_tech.tryon.R;
import wale_tech.tryon.user.User_Act;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.http.HttpAction;
import wale_tech.tryon.http.HttpHandler;
import wale_tech.tryon.http.HttpResult;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.http.HttpTag;

/**
 * Created by KeY on 2016/10/22.
 */

public class LoginAction extends BaseAction {

    public LoginAction(Context context) {
        super(context);
    }

    public void record() {
        if (sharedAction.getLoginStatus()) {
            String[] key = {HttpSet.KEY_USERNAME};
            String[] value = {sharedAction.getUsername()};

            HttpAction action = new HttpAction(context);
            action.setUrl(HttpSet.URL_LOGIN_RECORD);
            action.setTag(HttpTag.LOGIN_RECORD);
            action.setHandler(new HttpHandler(context));
            action.setMap(key, value);
            action.interaction();
        }
    }

    void login(String usn, String psd) {
        if (usn.isEmpty() || psd.isEmpty()) {
            snackBar.show(context.getString(R.string.login_snack_usn_psd_isNull));
            return;
        }

        if (!checkNet()) {
            return;
        }

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_PASSWORD, HttpSet.KEY_CLIENT_ID};
        String[] value = {usn, psd, ""};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_LOGIN);
        action.setTag(HttpTag.LOGIN_LOGIN);
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.login_login_progress_title), context.getString(R.string.login_login_progress_msg));
        action.interaction();
    }

    boolean handleResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        String response = obj.getString(HttpResult.RESULT);

        if (response.equals(HttpResult.LOGIN_SUCCESS)) {
            showSnack(context.getString(R.string.login_snack_login_success));
            sharedAction.setLoginStatus(obj.getString(HttpResult.USERNAME));
            ((AppCompatActivity) context).finish();

            return true;
        } else {
            showSnack(response);
        }

        return false;
    }
}
