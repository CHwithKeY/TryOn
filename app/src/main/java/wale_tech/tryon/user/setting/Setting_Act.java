package wale_tech.tryon.user.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

        setupDedicatedNetManuallyText();

        setupSetDedicatedNetText();

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

    private void setupDedicatedNetManuallyText() {
        final TextView dedicated_net_manual_tv = (TextView) findViewById(R.id.setting_set_dedicated_net_manual_tv);
        dedicated_net_manual_tv.setOnClickListener(this);
    }

    private void setupSetDedicatedNetText() {
        final TextView dedicated_net_tv = (TextView) findViewById(R.id.setting_set_dedicated_net_tv);
        dedicated_net_tv.setOnClickListener(this);
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

            case R.id.setting_set_dedicated_net_manual_tv:
                onSetDedicatedNetManually();
                break;

            case R.id.setting_set_dedicated_net_tv:
                onSetDedicatedNet();
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
                .setTitle(getString(R.string.setting_dialog_set_refresh_time_title))
                .setView(time_edit_view)
                .setPositiveButton(getString(R.string.base_dialog_btn_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText time_et = (EditText) time_edit_view.findViewById(R.id.dialog_ert_time_et);
                        String time_str = time_et.getText().toString();
                        if (time_str.isEmpty()) {
                            showSnack(0, getString(R.string.setting_snack_refresh_time_cannot_zero));
                        } else {
                            int time = Integer.parseInt(time_et.getText().toString());
                            if (time < 1000) {
                                showSnack(0, getString(R.string.setting_snack_refresh_time_cannot_less_one));
                            } else {
                                sharedAction.setRefreshTime(time);
                                showSnack(0, getString(R.string.setting_snack_set_success));
                            }
                        }
                    }
                })
                .setNegativeButton(getString(R.string.base_dialog_btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void onShowRefreshTime() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.setting_dialog_check_refresh_time_msg) + sharedAction.getRefreshTime())
                .show();
    }

    private void onSetDedicatedNetManually() {
        final View dedicated_net_edit_view = View.inflate(this, R.layout.dialog_edit_dedicated_net_view, null);

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.setting_dialog_set_dedicated_net_manually_title))
                .setView(dedicated_net_edit_view)
                .setPositiveButton(getString(R.string.base_dialog_btn_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText net_et = (EditText) dedicated_net_edit_view.findViewById(R.id.dialog_ert_time_et);
                        String net_str = net_et.getText().toString();
                        if (net_str.isEmpty()) {
                            showSnack(0, getString(R.string.setting_snack_dedicated_net_cannot_null));
                        } else {
                            sharedAction.setDedicatedIP(net_str);
//                                sharedAction.setRefreshTime(time);
                            showSnack(0, getString(R.string.setting_snack_set_success));
                        }
                    }
                })
                .setNegativeButton(getString(R.string.base_dialog_btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void onSetDedicatedNet() {
        String[] net_list = {
                getString(R.string.setting_dialog_gzd_net),
                getString(R.string.setting_dialog_bs_net),
                getString(R.string.setting_dialog_other_net)
        };

        int net_default = 2;
        switch (HttpSet.DEDICATED_IP) {
            case HttpSet.GZD_DEDICATED_IP:
                net_default = 0;
                break;

            case HttpSet.BS_DEDICATED_IP:
                net_default = 1;
                break;

            default:
                net_default = 2;
                break;
        }

//        int net_default = HttpSet.DEDICATED_IP.equals(HttpSet.GZD_DEDICATED_IP) ? 0 : 1;

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.setting_dialog_choose_dedicated_net_title))
                .setSingleChoiceItems(net_list, net_default, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                HttpSet.setDedicatedIp(HttpSet.GZD_DEDICATED_IP);
                                break;

                            case 1:
                                HttpSet.setDedicatedIp(HttpSet.BS_DEDICATED_IP);
                                break;

                            case 2:
                                HttpSet.setDedicatedIp(sharedAction.getDedicatedIP());
                                break;
                        }
                        dialog.dismiss();
                        showSnack(0, getString(R.string.setting_snack_set_success));
                    }
                })
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
                .setMessage(getString(R.string.setting_dialog_check_net_msg) + net)
                .show();
    }
}
