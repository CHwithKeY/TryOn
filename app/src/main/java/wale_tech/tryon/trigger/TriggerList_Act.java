package wale_tech.tryon.trigger;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicAdapter.BaseRycAdapter;
import wale_tech.tryon.publicClass.DataParse;
import wale_tech.tryon.publicObject.ObjectShoe;
import wale_tech.tryon.publicObject.ObjectTrigger;
import wale_tech.tryon.publicSet.BundleSet;

public class TriggerList_Act extends Base_Act {

    private TriggerAction triggerAction;
    private RecyclerView trigger_rv;
    private ObjectTrigger trigger;

    private ServiceConnection conn;
    private Messenger serv_msger;
    private RefreshHandler handler;

    private ArrayList<ObjectShoe> shoeList;

    private boolean refreshing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger_list_layout);

        trigger = new DataParse().parseTriggerIntent(this, getIntent());
        if (trigger == null) {
            showSnack(0, "无法解析该数据");
            return;
        }

        varInit();

        setupToolbar();

        setupServiceConnect();
    }

    public ObjectTrigger getTrigger() {
        return trigger;
    }

    @Override
    public void varInit() {
        refreshing = false;

        shoeList = new ArrayList<>();

        triggerAction = new TriggerAction(this);
        triggerAction.getList(BaseAction.REQUEST_DEFAULT, trigger.getResult(), trigger.getPath(), trigger.getWorkSpace(), true, false);
    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupServiceConnect() {
        handler = new RefreshHandler();

        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                serv_msger = new Messenger(iBinder);

                Message msg = Message.obtain();
                msg.what = TriggerService.MSG_MONITOR;
                msg.replyTo = new Messenger(handler);

                Bundle bundle = new Bundle();
                bundle.putString(BundleSet.KEY_TRIGGER_RESULT, trigger.getResult());
                bundle.putString(BundleSet.KEY_TRIGGER_PATH, trigger.getPath());
                bundle.putString(BundleSet.KEY_TRIGGER_WORK_SPACE, trigger.getWorkSpace());

                msg.setData(bundle);

                try {
                    serv_msger.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
    }

    private void setupRecyclerView(/*ArrayList<ObjectShoe> shoeList*/) {
        BaseRycAdapter adapter = new TriggerShoeRycAdapter(shoeList) {
            @Override
            public void onLoadMore() {
            }
        };

        LinearLayoutManager manager = new LinearLayoutManager(this);
        trigger_rv = (RecyclerView) findViewById(R.id.trigger_rv);
        trigger_rv.setLayoutManager(manager);
        trigger_rv.setAdapter(adapter);
    }

    private void launchService() {
        Intent get_list_int = new Intent(this, TriggerService.class);
        bindService(get_list_int, conn, BIND_AUTO_CREATE);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.TRIGGER_GET_TRIGGER:
                onHandleTrigger(result);
                break;

            default:
                break;
        }
    }

    private void onHandleTrigger(String result) throws JSONException {
        ArrayList<ObjectShoe> shoeList = triggerAction.handleListResponse(result);

        switch (triggerAction.getRequest()) {
            case BaseAction.REQUEST_DEFAULT:
                handleDefaultList(shoeList);
                break;

            case BaseAction.REQUEST_REFRESH:
                handleRefreshList(shoeList);
                break;

            default:
                break;
        }

        onStopRefresh();
        triggerAction.cancelProgressBar();
    }

    private void handleDefaultList(ArrayList<ObjectShoe> shoeList) {
        this.shoeList = shoeList;
        setupRecyclerView();
        if (trigger.getResult().startsWith("ws")) {
            launchService();
        }
    }

    private void handleRefreshList(ArrayList<ObjectShoe> shoeList) {
        TriggerShoeRycAdapter adapter = (TriggerShoeRycAdapter) trigger_rv.getAdapter();
        adapter.setDataList(shoeList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNullResponse(String tag) throws JSONException {
        switch (tag) {
            case HttpTag.TRIGGER_GET_TRIGGER:
                showNetDownPage(R.id.activity_trigger_list_layout);
                onStopRefresh();
                triggerAction.cancelProgressBar();
                break;

            default:
                break;
        }
    }

    @Override
    public void onPermissionAccepted(int permission_code) {

    }

    @Override
    public void onPermissionRefused(int permission_code) {

    }

    private class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (!refreshing) {
                if (msg.what == TriggerService.MSG_REFRESH) {
                    triggerAction.getList(BaseAction.REQUEST_REFRESH, trigger.getResult(), trigger.getPath(), trigger.getWorkSpace(), false, true);
                    refreshing = true;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serv_msger != null) {
            unbindService(conn);
            serv_msger = null;
        }
    }

    private void onStopRefresh() {
        refreshing = false;
    }
}
