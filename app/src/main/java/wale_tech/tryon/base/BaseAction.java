package wale_tech.tryon.base;

import android.content.Context;
import android.content.Intent;

import wale_tech.tryon.publicView.ColorSnackBar;
import wale_tech.tryon.login.Login_Act;
import wale_tech.tryon.R;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.sharedinfo.SharedAction;


/**
 * Created by KeY on 2016/6/3.
 */
public class BaseAction {

    public final static int REQUEST_DEFAULT = 0;
    public final static int REQUEST_LOAD_MORE = 1;
    public final static int REQUEST_FILTER = 2;
    public final static int REQUEST_REFRESH = 3;

    public final static int ACTION_NET_DOWN = 0;
    public final static int ACTION_LACK = 1;
    public final static int ACTION_DONE = 2;

    protected Context context;
    //    protected LineToast toast;
    protected ColorSnackBar snackBar;
    protected SharedAction sharedAction;

    public BaseAction(Context context) {
        this.context = context;

        varInit();
    }

    protected int request = REQUEST_DEFAULT;

    public int getRequest() {
        return request;
    }

    public void checkRequest(int request) {
        this.request = request;
        if (request != REQUEST_LOAD_MORE) {
            sharedAction.clearLastIdInfo();
        }
    }

    protected void varInit() {
        if (snackBar == null) {
            snackBar = new ColorSnackBar(context);
        }

        if (sharedAction == null) {
            sharedAction = new SharedAction(context);
        }
    }

    protected boolean checkNet() {
        varInit();
        if (!Methods.isNetworkAvailable(context)) {
            snackBar.show(context.getString(R.string.base_toast_net_down));
//            toast.showToast(context.getString(R.string.base_toast_net_down));
        }

        return Methods.isNetworkAvailable(context);
    }

    public boolean checkLoginStatus() {
        if (!sharedAction.getLoginStatus()) {
            Intent login_int = new Intent(context, Login_Act.class);
            context.startActivity(login_int);
//            toast.showToast(context.getString(R.string.base_toast_login_first));
            snackBar.show(context.getString(R.string.base_toast_login_first));
        }

        return sharedAction.getLoginStatus();
    }

    protected void showSnack(String snack_text) {
        if (snackBar == null) {
            snackBar = new ColorSnackBar(context);
        }

        snackBar.show(snack_text);
    }

}
