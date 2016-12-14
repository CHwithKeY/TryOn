package wale_tech.tryon.user.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.update.Update_Act;

public class Setting_Act extends Base_Act implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);

        varInit();

        setupToolbar();

        setupUpdateText();

        setupAuthorizeText();

        setupRefreshTimeText();

        setupGetRefreshTimeText();

        setupCheckNetText();

    }

    @Override
    public void varInit() {

    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupUpdateText() {
        final TextView update_tv = (TextView) findViewById(R.id.setting_update_tv);
        update_tv.setOnClickListener(this);
    }

    private void setupAuthorizeText() {
        final TextView authorize_tv = (TextView) findViewById(R.id.setting_authorize_tv);
        authorize_tv.setOnClickListener(this);
    }

    private void setupRefreshTimeText() {
        final TextView refresh_time_tv = (TextView) findViewById(R.id.setting_refresh_time_tv);
        refresh_time_tv.setOnClickListener(this);
    }

    private void setupGetRefreshTimeText() {
        final TextView get_time_tv = (TextView) findViewById(R.id.setting_get_refresh_time_tv);
        get_time_tv.setOnClickListener(this);
    }

    private void setupCheckNetText() {
        final TextView check_net_tv = (TextView) findViewById(R.id.setting_check_net_tv);
        check_net_tv.setOnClickListener(this);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {

    }

    @Override
    public void onNullResponse(String tag) throws JSONException {

    }

    @Override
    public void onPermissionAccepted(int permission_code) {

    }

    @Override
    public void onPermissionRefused(int permission_code) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_update_tv:
                Intent update_int = new Intent(this, Update_Act.class);
                startActivity(update_int);
                break;

            case R.id.setting_authorize_tv:
                Intent authorize_int = new Intent(this, Authorize_Act.class);
                startActivity(authorize_int);
                break;

            case R.id.setting_refresh_time_tv:
                setRefreshTimeDialog();
                break;

            case R.id.setting_get_refresh_time_tv:
                onShowRefreshTime();
                break;

            case R.id.setting_check_net_tv:
                onCheckNet();
                break;

            default:
                break;
        }
    }

    private void setRefreshTimeDialog() {
        final View time_edit_view = View.inflate(this, R.layout.dialog_edit_refresh_time_view, null);

        new AlertDialog.Builder(this)
                .setTitle("设置时间")
                .setView(time_edit_view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText time_et = (EditText) time_edit_view.findViewById(R.id.dialog_ert_time_et);
                        int time = Integer.parseInt(time_et.getText().toString());
                        if (time < 1000) {
                            showSnack(0, "不能设置时间小于1秒");
                        } else {
                            sharedAction.setRefreshTime(time);
                            showSnack(0, "设置成功");
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void onShowRefreshTime() {
        new AlertDialog.Builder(this)
                .setMessage("刷新时间为：" + sharedAction.getRefreshTime())
                .show();
    }

    private void onCheckNet() {
        String net;
        if (sharedAction.getNet() == 0) {
            net = HttpSet.NORMAL_URL;
        } else {
            net = HttpSet.DEDICATED_URL;
        }

        new AlertDialog.Builder(this)
                .setMessage("当前网络为：" + net)
                .show();
    }
}
