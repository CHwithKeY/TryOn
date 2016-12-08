package wale_tech.tryon;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import org.json.JSONException;

import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.history.History_Act;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.login.LoginAction;
import wale_tech.tryon.login.Login_Act;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.result.Result_Act;
import wale_tech.tryon.update.UpdateAction;
import wale_tech.tryon.user.User_Act;

public class MainActivity extends Base_Act implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ScanAction scanAction;

    // hey , this is from ck

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        varInit();

        setupSearchItem();

        setupScanImgBtn();

        setupHistoryImgBtn();

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

    private void setupHistoryImgBtn() {
        final FloatingActionButton history_fab = (FloatingActionButton) findViewById(R.id.main_history_fab);
        history_fab.setOnClickListener(this);
    }

    private void setupUserFab() {
        final FloatingActionButton user_fab = (FloatingActionButton) findViewById(R.id.main_user_fab);
        user_fab.setOnClickListener(this);
    }

    private void setupNetSwitch() {
        final SwitchCompat net_sw = (SwitchCompat) findViewById(R.id.main_net_sw);
        net_sw.setOnCheckedChangeListener(this);
    }

    private UpdateAction updateAction;

    private void checkUpdate() {
        updateAction = new UpdateAction(this);
        updateAction.checkVersion(false);
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

    }

    @Override
    public void onPermissionRefused(int permission_code) {

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

            case R.id.main_history_fab:
                toHistory();
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
        scanAction.scan();
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

    private void toHistory() {
        Intent history_int = new Intent(this, History_Act.class);
        startActivity(history_int);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            showSnack(R.id.main_col, "切换至专用网络");
            HttpSet.setBaseUrl(HttpSet.DEDICATED_URL);

            sharedAction.setNet(1);
        } else {
            showSnack(R.id.main_col, "切换至普通网络");
            HttpSet.setBaseUrl(HttpSet.NORMAL_URL);

            sharedAction.setNet(0);
        }
    }
}
