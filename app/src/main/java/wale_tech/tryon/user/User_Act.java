package wale_tech.tryon.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;

import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.user.cart.Cart_Act;
import wale_tech.tryon.history.History_Act;
import wale_tech.tryon.user.order.Order_Act;
import wale_tech.tryon.user.coupon.Coupon_Act;
import wale_tech.tryon.user.favourite.Favourite_Act;
import wale_tech.tryon.user.setting.Setting_Act;

public class User_Act extends Base_Act implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_layout);

        varInit();

        setupToolbar();

        setupUserHead();

        setupItemBtn();

    }

    @Override
    public void varInit() {

    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupUserHead() {
        final TextView username_tv = (TextView) findViewById(R.id.user_username_tv);
        username_tv.setText(sharedAction.getUsername());

        final TextView level_tv = (TextView) findViewById(R.id.user_level_tv);
        final TextView mark_tv = (TextView) findViewById(R.id.user_mark_tv);
    }

    private void setupItemBtn() {
        final ImageButton coupon_imgbtn = (ImageButton) findViewById(R.id.user_coupon_imgbtn);
        final ImageButton history_imgbtn = (ImageButton) findViewById(R.id.user_history_imgbtn);
        final ImageButton cart_imgbtn = (ImageButton) findViewById(R.id.user_cart_imgbtn);

        final Button favourite_btn = (Button) findViewById(R.id.user_favourite_btn);
        final Button order_btn = (Button) findViewById(R.id.user_order_btn);
        final Button address_btn = (Button) findViewById(R.id.user_address_btn);
        final Button advice_btn = (Button) findViewById(R.id.user_advice_btn);
        final Button setting_btn = (Button) findViewById(R.id.user_setting_btn);

        ImageButton[] item_imgbtn = {coupon_imgbtn, history_imgbtn, cart_imgbtn};
        Button[] item_btn = {favourite_btn, order_btn, address_btn, advice_btn, setting_btn};
        Class[] item_cls = {
                Coupon_Act.class, History_Act.class, Cart_Act.class,
                Favourite_Act.class, Order_Act.class, Address_Act.class, Advice_Act.class, Setting_Act.class};

        for (int i = 0; i < item_imgbtn.length; i++) {
            item_imgbtn[i].setTag(item_cls[i]);
            item_imgbtn[i].setOnClickListener(this);
        }

        for (int i = 0; i < item_btn.length; i++) {
            item_btn[i].setTag(item_cls[item_imgbtn.length + i]);
            item_btn[i].setOnClickListener(this);
        }

        final Button logout_btn = (Button) findViewById(R.id.user_logout_btn);
        logout_btn.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_logout_btn:
                onLogout();
                break;

            default:
                Intent intent = new Intent();
                intent.setClass(this, (Class<?>) v.getTag());
                startActivity(intent);
                break;
        }
    }


    private void onLogout() {
        new AlertDialog.Builder(this)
                .setTitle("退出登录")
                .setMessage("确定退出登录吗？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sharedAction.clearLoginStatus();
                        finish();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
