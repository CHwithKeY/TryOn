package wale_tech.tryon.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.product.Product_Act;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicSet.IntentSet;
import wale_tech.tryon.trigger.TriggerList_Act;
import wale_tech.tryon.user.User_Act;

public class Login_Act extends Base_Act implements View.OnClickListener {

    private TextInputLayout usn_input;
    private TextInputLayout psd_input;
    private TextInputLayout psd_confirm_input;

    private LoginAction loginAction;
    private RegisterAction registerAction;

    private boolean isLoginMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);

        varInit();

        setupToolbar();

        setupSwitchImgBtn();

        setupIconImgBtn();

        setupInputField();

        setupLoginOrRegisterBtn();
    }

    @Override
    public void varInit() {

        isLoginMode = true;

        usn_input = (TextInputLayout) findViewById(R.id.login_usn_input);
        psd_input = (TextInputLayout) findViewById(R.id.login_psd_input);
        psd_confirm_input = (TextInputLayout) findViewById(R.id.login_psd_confirm_input);

        loginAction = new LoginAction(this);
        registerAction = new RegisterAction(this);
    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupInputField() {
        final RelativeLayout login_rl = (RelativeLayout) findViewById(R.id.login_rl);
        login_rl.setOnClickListener(this);

        usn_input.setFocusable(true);
        usn_input.requestFocus();
        usn_input.setHint(getString(R.string.login_usn_iet_hint));
        psd_input.setHint(getString(R.string.login_psd_iet_hint));
        psd_confirm_input.setHint(getString(R.string.login_psd_confirm_iet_hint));
    }

    private void setupSwitchImgBtn() {
        final ImageButton switch_imgbtn = (ImageButton) findViewById(R.id.login_switch_imgbtn);
        switch_imgbtn.setOnClickListener(this);
    }

    private void setupIconImgBtn() {
        final ImageButton alipay_imgbtn = (ImageButton) findViewById(R.id.login_icon_alipay_imgbtn);
        alipay_imgbtn.setOnClickListener(this);

        final ImageButton wechat_imgbtn = (ImageButton) findViewById(R.id.login_icon_wechat_imgbtn);
        wechat_imgbtn.setOnClickListener(this);

        final ImageButton microblog_imgbtn = (ImageButton) findViewById(R.id.login_icon_microblog_imgbtn);
        microblog_imgbtn.setOnClickListener(this);

    }

    private void setupLoginOrRegisterBtn() {
        final Button login_btn = (Button) findViewById(R.id.login_login_btn);
        login_btn.setOnClickListener(this);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        boolean loginSuccess = false;

        switch (tag) {
            case HttpTag.LOGIN_LOGIN:
                loginSuccess = loginAction.handleResponse(result);
                break;

            case HttpTag.REGISTER_REGISTER:
                loginSuccess = registerAction.handleResponse(result);
                break;
        }

        // wow

        if (loginSuccess) {
            Intent user_int = new Intent();

            if (getIntent() != null && getIntent().getAction() != null && getIntent().getAction().equals("GetSkuCodeAction")) {
//                String sku_code = getIntent().getStringExtra(IntentSet.KEY_SKU_CODE);
//                String path = getIntent().getStringExtra(IntentSet.KEY_TRIGGER_PATH);
//
//                user_int.setClass(this, TriggerList_Act.class);
//                user_int.putExtra(IntentSet.KEY_SKU_CODE, sku_code);
//                user_int.putExtra(IntentSet.KEY_TRIGGER_PATH, path);
                user_int.setClass(this, User_Act.class);
            } else {
                user_int.setClass(this, User_Act.class);
            }

            startActivity(user_int);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_icon_alipay_imgbtn:
            case R.id.login_icon_wechat_imgbtn:
            case R.id.login_icon_microblog_imgbtn:
                showSnack(0, getString(R.string.base_snack_function_closed));
                break;

            case R.id.login_rl:
                Methods.collapseIME(this);
                break;

            case R.id.login_switch_imgbtn:
                switchPage();
                break;

            case R.id.login_login_btn:
                onLoginOrRegister();
                break;
        }
    }

    private void switchPage() {

        final LinearLayout reg_layout = (LinearLayout) findViewById(R.id.login_reg_layout);

        final ImageButton switch_imgbtn = (ImageButton) findViewById(R.id.login_switch_imgbtn);
        final TextView switch_tv = (TextView) findViewById(R.id.login_switch_tv);

        final Button login_btn = (Button) findViewById(R.id.login_login_btn);

        if (isLoginMode) {
            // Register Layout
            switch_imgbtn.setImageResource(R.drawable.image_login_triangle_login);
            switch_tv.setText(getString(R.string.login_triangle_login_tv));

            login_btn.setBackgroundResource(R.drawable.dw_color_special_square_btn);
            login_btn.setText(getString(R.string.login_register_btn));

            reg_layout.setVisibility(View.VISIBLE);
        } else {
            // Login Layout
            switch_imgbtn.setImageResource(R.drawable.image_login_triangle_register);
            switch_tv.setText(getString(R.string.login_triangle_register_tv));

            login_btn.setBackgroundResource(R.drawable.dw_color_assist_square_btn);
            login_btn.setText(getString(R.string.login_login_btn));

            reg_layout.setVisibility(View.INVISIBLE);
        }

        isLoginMode = !isLoginMode;
    }

    private void onLoginOrRegister() {
        final TextInputEditText usn_iet = (TextInputEditText) findViewById(R.id.login_usn_iet);
        final TextInputEditText psd_iet = (TextInputEditText) findViewById(R.id.login_psd_iet);
        final TextInputEditText psd_confirm_iet = (TextInputEditText) findViewById(R.id.login_psd_confirm_iet);

        String usn_str = usn_iet.getText().toString();
        String psd_str = psd_iet.getText().toString();
        String psd_confirm_str = psd_confirm_iet.getText().toString();

        if (isLoginMode) {
            loginAction.login(usn_str, psd_str);
        } else {
            final RadioButton male_rb = (RadioButton) findViewById(R.id.login_male_rb);
            final RadioButton female_rb = (RadioButton) findViewById(R.id.login_female_rb);

            String sex_str = "";
            if (male_rb.isChecked()) {
                sex_str = male_rb.getText().toString();
            } else if (female_rb.isChecked()) {
                sex_str = female_rb.getText().toString();
            } else {
                showSnack(0, getString(R.string.login_snack_sex_isNull));
                return;
            }

            registerAction.register(usn_str, psd_str, psd_confirm_str, sex_str);
        }
    }

}
