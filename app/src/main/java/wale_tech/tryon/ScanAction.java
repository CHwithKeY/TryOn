package wale_tech.tryon;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zbar.lib.CaptureActivity;

import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.history.ZoomImage_Act;
import wale_tech.tryon.http.HttpAction;
import wale_tech.tryon.http.HttpHandler;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicSet.IntentSet;
import wale_tech.tryon.publicSet.PermissionSet;
import wale_tech.tryon.trigger.TriggerList_Act;
import wale_tech.tryon.trigger.TriggerSet;
import wale_tech.tryon.user.setting.PermissionAction;

/**
 * Created by KeY on 2016/10/27.
 */

public class ScanAction extends BaseAction {

    public final static int REQUEST_MAIN = 0;

    public ScanAction(Context context) {
        super(context);
    }

    public void scan() {
        if (!checkLoginStatus()) {
            return;
        }

        if (!checkNet()) {
            return;
        }

        Intent scan_int = new Intent(context, CaptureActivity.class);
        AppCompatActivity activity = (AppCompatActivity) context;
        activity.startActivityForResult(scan_int, REQUEST_MAIN);

//        String client_id = PushManager.getInstance().getClientid(context);
//        Log.i("Result", "client id is ; " + client_id);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void handle(String scan_result) {
        if (!checkNet()) {
            return;
        }

        Log.i("Result", "scan result is : " + scan_result);
        Log.i("Result", "handle scan Result");

        /*if (!scan_result.startsWith("sku") && !scan_result.startsWith("ws")) {
            showSnack("无法解析该数据");
            return;
        }*/

        String work_space = "";

        if (scan_result.startsWith("sku")) {
            work_space = TriggerSet.WORK_SPACE_PLATFORM;
        }

        if (scan_result.startsWith("ws")) {
            work_space = TriggerSet.WORK_SPACE_SEAT;
        }

        if (scan_result.startsWith("capture")) {
            String image_postfix = scan_result.replace("-", "/");
            Log.i("Result", "image postfix is : " + image_postfix);
            String image_path = HttpSet.BASE_URL + image_postfix;
            Intent capture_int = new Intent(context, ZoomImage_Act.class);
            capture_int.putExtra(IntentSet.KEY_IMAGE_PATH, image_path);
            context.startActivity(capture_int);

            bindHistoryImage(image_postfix);
            return;
        }

        if (work_space.isEmpty()) {
            showSnack("无法解析该数据");
            return;
        }

        Intent trigger_int = new Intent(context, TriggerList_Act.class);
        trigger_int.setAction(IntentSet.ACTION_SCAN);
        trigger_int.putExtra(IntentSet.KEY_TRIGGER_RESULT, scan_result);
        trigger_int.putExtra(IntentSet.KEY_TRIGGER_WORK_SPACE, work_space);
        context.startActivity(trigger_int);
    }

    private void bindHistoryImage(String image_postfix) {
        if (!checkNet()) {
            return;
        }

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_IMAGE_PATH};
        String[] value = {sharedAction.getUsername(), image_postfix};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_BIND_HISTORY_IMAGE);
        action.setTag(HttpTag.HISTORY_BIND_IMAGE);
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }
}
