package wale_tech.tryon.user.cart.orderPart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;

import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.user.cart.Cart_Act;
import wale_tech.tryon.user.order.Order_Act;

public class Order_Success_Act extends Base_Act implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success_layout);

        varInit();

        setupToolbar();

        setupOrderCheckBtn();
    }

    @Override
    public void varInit() {
        showSnack(0, "下单成功");
    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupOrderCheckBtn() {
        final Button check_order_btn = (Button) findViewById(R.id.order_success_check_order_btn);
        check_order_btn.setOnClickListener(this);
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
            case R.id.order_success_check_order_btn:
                onCheckOrder();
                break;

            default:
                break;
        }
    }

    private void onCheckOrder() {
        Intent order_int = new Intent(this, Order_Act.class);
        startActivity(order_int);
        finish();
    }
}
