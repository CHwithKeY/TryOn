package wale_tech.tryon;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;

import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.history.HistoryImage_Act;
import wale_tech.tryon.history.History_Act;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.login.LoginAction;
import wale_tech.tryon.login.Login_Act;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicSet.PermissionSet;
import wale_tech.tryon.result.Result_Act;
import wale_tech.tryon.update.UpdateAction;
import wale_tech.tryon.user.User_Act;
import wale_tech.tryon.user.setting.PermissionAction;

public class MainActivity extends Base_Act implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ScanAction scanAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        varInit();

        setupSearchItem();

        setupScanImgBtn();

        setupHistoryRecordFab();

        setupHistoryImageFab();

        setupUserFab();

        setupNetSwitch();

        checkUpdate();
    }

    @Override
    public void varInit() {
        scanAction = new ScanAction(this);

        LoginAction loginAction = new LoginAction(this);
        loginAction.record();
    }

    @Override
    protected void setupToolbar() {

    }

    private void setupSearchItem() {
        final View search_item = findViewById(R.id.main_search_item);
        search_item.setOnClickListener(this);
    }

    private void setupScanImgBtn() {
        final ImageButton scan_imgbtn = (ImageButton) findViewById(R.id.main_scan_imgbtn);
        scan_imgbtn.setOnClickListener(this);
    }

    private void setupHistoryRecordFab() {
        final FloatingActionButton history_record_fab = (FloatingActionButton) findViewById(R.id.main_history_record_fab);
        history_record_fab.setOnClickListener(this);
    }

    private void setupHistoryImageFab() {
        final FloatingActionButton history_image_fab = (FloatingActionButton) findViewById(R.id.main_history_image_fab);
        history_image_fab.setOnClickListener(this);
    }

    private void setupUserFab() {
        final FloatingActionButton user_fab = (FloatingActionButton) findViewById(R.id.main_user_fab);
        user_fab.setOnClickListener(this);
    }

    private void setupNetSwitch() {
        final SwitchCompat net_sw = (SwitchCompat) findViewById(R.id.main_net_sw);
        if (sharedAction.getNet() == HttpSet.NORMAL_NET) {
            net_sw.setChecked(false);
        } else {
            net_sw.setChecked(true);
        }
        net_sw.setOnCheckedChangeListener(this);
    }

    private UpdateAction updateAction;

    private void checkUpdate() {
        if (PermissionAction.checkAutoRequest(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionSet.WRITE_EXTERNAL_STORAGE)) {
            updateAction = new UpdateAction(this);
            updateAction.checkVersion(false);
        }
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.UPDATE_CHECK_VERSION:
                updateAction.handleResponse(result, false);
                break;

            default:
                break;
        }
    }

    @Override
    public void onNullResponse(String tag) throws JSONException {

    }

    @Override
    public void onPermissionAccepted(int permission_code) {
        switch (permission_code) {
            case PermissionSet.CAMERA:
                scanAction.scan();
                break;

            default:
                break;
        }
    }

    @Override
    public void onPermissionRefused(int permission_code) {
        switch (permission_code) {
            case PermissionSet.CAMERA:
                showSnack(R.id.main_col, getString(R.string.auth_toast_permission_camera_authorized));
                break;

            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionAction.handle(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == ScanAction.REQUEST_MAIN) {
                String scan_result = data.getStringExtra("scan_result");
                scanAction.handle(scan_result);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_search_item:
                toSearch();
                break;

            case R.id.main_scan_imgbtn:
                toScan();
                break;

            case R.id.main_history_record_fab:
                toHistoryRecord();
                break;

            case R.id.main_history_image_fab:
                toHistoryImage();
                break;

            case R.id.main_user_fab:
                toUser();
                break;

            default:
                break;
        }
    }

    private void toSearch() {
        Intent search_int = new Intent(this, Result_Act.class);
        startActivity(search_int);
    }

    private void toScan() {
        if (PermissionAction.checkAutoRequest(this, Manifest.permission.CAMERA, PermissionSet.CAMERA)) {
            scanAction.scan();
        }
    }

    private void toUser() {
        if (sharedAction.getLoginStatus()) {
            Intent user_int = new Intent(this, User_Act.class);
            startActivity(user_int);
            return;
        }

        Intent login_int = new Intent(this, Login_Act.class);
        startActivity(login_int);
    }

    private void toHistoryRecord() {
        Intent history_record_int = new Intent(this, History_Act.class);
        startActivity(history_record_int);
    }

    private void toHistoryImage() {
        Log.i("Result", "click history image");
        Intent history_image_int = new Intent(this, HistoryImage_Act.class);
        startActivity(history_image_int);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            showSnack(R.id.main_col, getString(R.string.main_snack_switch_dedicated_net));
            HttpSet.setBaseUrl(HttpSet.DEDICATED_URL);

            sharedAction.setNet(HttpSet.DEDICATED_NET);
        } else {
            showSnack(R.id.main_col, getString(R.string.main_snack_switch_normal_net));
            HttpSet.setBaseUrl(HttpSet.NORMAL_URL);

            sharedAction.setNet(HttpSet.NORMAL_NET);
        }
    }
}
