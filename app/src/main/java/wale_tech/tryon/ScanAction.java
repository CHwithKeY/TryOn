package wale_tech.tryon;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zbar.lib.CaptureActivity;

import wale_tech.tryon.base.BaseAction;
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
}
