package wale_tech.tryon.user.setting;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;

import org.json.JSONException;

import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.publicSet.PermissionSet;

public class Authorize_Act extends Base_Act implements CompoundButton.OnCheckedChangeListener {

    //    private SwitchCompat config_sw;
    private SwitchCompat camera_sw;
    //    private SwitchCompat location_sw;
//    private SwitchCompat read_state_sw;
    private SwitchCompat storage_sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize_layout);

        varInit();

        setupToolbar();

        setupCameraSwitch();

//        setupLocationSwitch();

//        setupReadStateSwitch();

        setupStorageSwitch();
    }

    @Override
    public void varInit() {
        camera_sw = (SwitchCompat) findViewById(R.id.auth_camera_sw);
//        location_sw = (SwitchCompat) findViewById(R.id.auth_location_sw);
//        read_state_sw = (SwitchCompat) findViewById(R.id.auth_read_state_sw);
        storage_sw = (SwitchCompat) findViewById(R.id.auth_storage_sw);
    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupCameraSwitch() {
        if (PermissionAction.check(this, Manifest.permission.CAMERA)) {
            camera_sw.setChecked(true);
        } else {
            camera_sw.setChecked(false);
        }
        camera_sw.setOnCheckedChangeListener(this);
    }

//    private void setupLocationSwitch() {
//        if (PermissionAction.check(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
//            location_sw.setChecked(true);
//        } else {
//            location_sw.setChecked(false);
//        }
//        location_sw.setOnCheckedChangeListener(this);
//    }
//
//    private void setupReadStateSwitch() {
//        if (PermissionAction.check(this, Manifest.permission.READ_PHONE_STATE)) {
//            read_state_sw.setChecked(true);
//        } else {
//            read_state_sw.setChecked(false);
//        }
//
//        read_state_sw.setOnCheckedChangeListener(this);
//    }

    private void setupStorageSwitch() {
        if (PermissionAction.check(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            storage_sw.setChecked(true);
        } else {
            storage_sw.setChecked(false);
        }

        storage_sw.setOnCheckedChangeListener(this);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {

    }

    @Override
    public void onNullResponse(String tag) throws JSONException {

    }

    @Override
    public void onPermissionAccepted(int permission_code) {
        switch (permission_code) {
            case PermissionSet.CAMERA:
                camera_sw.setChecked(true);
                break;

//            case PermissionSet.ACCESS_COARSE_LOCATION:
//                location_sw.setChecked(true);
//                break;
//
//            case PermissionSet.READ_PHONE_STATE:
//                read_state_sw.setChecked(true);
//                break;

            case PermissionSet.WRITE_EXTERNAL_STORAGE:
                storage_sw.setChecked(true);
                break;

            default:
                break;
        }
    }

    @Override
    public void onPermissionRefused(int permission_code) {
        switch (permission_code) {
            case PermissionSet.CAMERA:
                camera_sw.setChecked(false);
                break;

//            case PermissionSet.ACCESS_COARSE_LOCATION:
//                location_sw.setChecked(false);
//                break;
//
//            case PermissionSet.READ_PHONE_STATE:
//                read_state_sw.setChecked(false);
//                break;

            case PermissionSet.WRITE_EXTERNAL_STORAGE:
                storage_sw.setChecked(false);
                break;

            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            switch (buttonView.getId()) {
                case R.id.auth_camera_sw:
                    PermissionAction.request(this, Manifest.permission.CAMERA, PermissionSet.CAMERA);
                    break;

//                case R.id.auth_location_sw:
//                    PermissionAction.request(this, Manifest.permission.ACCESS_COARSE_LOCATION, PermissionSet.ACCESS_COARSE_LOCATION);
//                    break;
//
//                case R.id.auth_read_state_sw:
//                    PermissionAction.request(this, Manifest.permission.READ_PHONE_STATE, PermissionSet.READ_PHONE_STATE);
//                    break;

                case R.id.auth_storage_sw:
                    PermissionAction.request(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionSet.WRITE_EXTERNAL_STORAGE);
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionAction.handle(this, requestCode, grantResults);
    }
}
