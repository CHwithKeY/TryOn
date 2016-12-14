package wale_tech.tryon.trigger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;

import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.Base_Serv;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicObject.ObjectShoe;
import wale_tech.tryon.publicSet.BundleSet;
import wale_tech.tryon.sharedinfo.SharedAction;

public class TriggerService extends Base_Serv {

    public static final int MSG_REFRESH = 0x11;
    public static final int MSG_MONITOR = 0x12;

    private MonitorHandler handler;
    private Messenger serv_msger;
    private Messenger act_msger;

    private TriggerAction triggerAction;
    private boolean getData;

    private HashSet<String> exSkuSet;
    private boolean fstInit;

    private Bundle bundle;

    private SharedAction sharedAction;

    public TriggerService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedAction = new SharedAction(this);

        getData = false;
        fstInit = true;

        triggerAction = new TriggerAction(this, this);
        handler = new MonitorHandler();
        serv_msger = new Messenger(handler);
        exSkuSet = new HashSet<>();
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.TRIGGER_GET_TRIGGER:
                ArrayList<ObjectShoe> shoeList = triggerAction.handleListResponse(result);
                onMonitorList(shoeList);
                break;

            default:
                break;
        }
    }

    private void onMonitorList(ArrayList<ObjectShoe> shoeList) {
        Log.i("Result", "list size is:" + shoeList.size());

        if (fstInit) {
            fstInit = false;
            for (int i = 0; i < shoeList.size(); i++) {
                String sku_code = shoeList.get(i).getSkuCode();
                exSkuSet.add(sku_code);
            }
        } else {
            int exSize = exSkuSet.size();

            HashSet<String> nowSkuSet = new HashSet<>();

            for (int i = 0; i < shoeList.size(); i++) {
                String sku_code = shoeList.get(i).getSkuCode();
                exSkuSet.add(sku_code);
                nowSkuSet.add(sku_code);
            }

            if (exSize <= nowSkuSet.size()) {
                if (exSize != exSkuSet.size()) {
                    sendRefreshMsg();
                }
            } else {
                sendRefreshMsg();
            }

            exSkuSet = nowSkuSet;

//            if (exSize != nowSkuSet.size() || exSize != exSkuSet.size()) {
//                exSkuSet = nowSkuSet;
//                sendRefreshMsg();
//            }
        }

        if (getData) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0x13);
                }
            }, sharedAction.getRefreshTime());
        }
    }

    private void sendRefreshMsg() {
        Message msg = Message.obtain();
        msg.what = MSG_REFRESH;
        msg.arg1 = 1;

        try {
            act_msger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNullResponse(String tag) throws JSONException {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return serv_msger.getBinder();
//        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private class MonitorHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_MONITOR) {
                bundle = msg.getData();
                getData = true;
                act_msger = msg.replyTo;
            }

            triggerAction.getList(BaseAction.REQUEST_REFRESH,
                    bundle.getString(BundleSet.KEY_TRIGGER_RESULT), bundle.getString(BundleSet.KEY_TRIGGER_PATH),
                    bundle.getString(BundleSet.KEY_TRIGGER_WORK_SPACE), false, false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getData = false;
    }
}
