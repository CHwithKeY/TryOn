package wale_tech.tryon.update;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;

import wale_tech.tryon.PermissionAction;
import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicSet.PermissionSet;

public class Update_Act extends Base_Act implements View.OnClickListener {

    private UpdateAction updateAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_layout);

        varInit();

        setupToolbar();

        setupVersionNameText();

        setupCheckUpdateText();

    }

    @Override
    public void varInit() {
        updateAction = new UpdateAction(this);
    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupVersionNameText() {
        final TextView version_tv = (TextView) findViewById(R.id.update_version_tv);
        String version_str = version_tv.getText() + Methods.getVersionName(this);
        version_tv.setText(version_str);
    }

    private void setupCheckUpdateText() {
        final TextView update_tv = (TextView) findViewById(R.id.update_check_tv);
        update_tv.setOnClickListener(this);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        updateAction.handleResponse(result, true);
    }

    @Override
    public void onNullResponse(String tag) throws JSONException {

    }

    @Override
    public void onPermissionAccepted(int permission_code) {
        updateAction.checkVersion(true);
    }

    @Override
    public void onPermissionRefused(int permission_code) {
        showSnack(0, getString(R.string.auth_toast_permission_update_authorized));
    }


    @Override
    public void onClick(View v) {
        if (PermissionAction.checkAutoRequest(Update_Act.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionSet.WRITE_EXTERNAL_STORAGE)) {
            updateAction.checkVersion(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionAction.handle(this, requestCode, grantResults);
    }
}
