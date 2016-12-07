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

public class RegisterAction extends BaseAction {

    public RegisterAction(Context context) {
        super(context);
    }

    void register(String usn, String psd, String psd_confirm, String sex) {
        if (usn.isEmpty() || psd.isEmpty() || psd_confirm.isEmpty()) {
            snackBar.show(context.getString(R.string.login_snack_usn_psd_isNull));
            return;
        }

        if (!psd.equals(psd_confirm)) {
            snackBar.show(context.getString(R.string.login_snack_psd_isDifferent));
            return;
        }

        if (!checkNet()) {
            return;
        }

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_PASSWORD, HttpSet.KEY_CLIENT_ID, HttpSet.KEY_SEX};
        String[] value = {usn, psd, "", sex};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_REGISTER);
        action.setTag(HttpTag.REGISTER_REGISTER);
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.login_register_progress_title), context.getString(R.string.login_register_progress_msg));
        action.interaction();
    }

    boolean handleResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        String response = obj.getString(HttpResult.RESULT);
        snackBar.show(response);


        if (response.equals(HttpResult.REGISTER_SUCCESS)) {
            sharedAction.setLoginStatus(obj.getString(HttpResult.USERNAME));
            ((AppCompatActivity) context).finish();
            return true;
        }

        return false;
    }
}
