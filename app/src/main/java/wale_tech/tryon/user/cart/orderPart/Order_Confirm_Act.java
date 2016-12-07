package wale_tech.tryon.user.cart.orderPart;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicAdapter.BaseShoeRycAdapter;
import wale_tech.tryon.publicObject.ObjectCoupon;
import wale_tech.tryon.publicObject.ObjectShoe;
import wale_tech.tryon.publicSet.IntentSet;
import wale_tech.tryon.user.cart.Cart_Act;
import wale_tech.tryon.user.order.OrderItemRycAdapter;

public class Order_Confirm_Act extends Base_Act implements View.OnClickListener {

    private OrderConfirmAction orderConfirmAction;
    private ArrayList<ObjectShoe> shoeList;

    private String coupon_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm_layout);

        varInit();

        setupToolbar();

    }

    @Override
    public void varInit() {
        String sku_code_series = getIntent().getStringExtra(IntentSet.KEY_SKU_CODE);

        shoeList = new ArrayList<>();
        orderConfirmAction = new OrderConfirmAction(this);
        orderConfirmAction.getOrderConfirmShoe(sku_code_series);

        setupSelectCouponBtn();
        setupPayBtn();
    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupRecyclerView() {
        BaseShoeRycAdapter adapter = new OrderItemRycAdapter(shoeList) {
            @Override
            public void onLoadMore() {

            }
        };

        final RecyclerView order_confirm_rv = (RecyclerView) findViewById(R.id.order_confirm_rv);
        order_confirm_rv.setLayoutManager(new LinearLayoutManager(this));
        order_confirm_rv.setAdapter(adapter);
    }

    private void onCalculateTotalPrice() {
        float discount_before_price = 0;
        for (int i = 0; i < shoeList.size(); i++) {
            discount_before_price += Float.parseFloat(shoeList.get(i).getPrice());
        }
        setupTotalPriceText(discount_before_price, discount_before_price);
    }

    private void setupTotalPriceText(float discount_before_price, float discount_after_price) {
        final TextView discount_before_tv = (TextView) findViewById(R.id.order_confirm_discount_before_tv);
        discount_before_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        String discount_before_str = "优惠前总金额：" + discount_before_price;
        discount_before_tv.setText(discount_before_str);

        final TextView discount_after_tv = (TextView) findViewById(R.id.order_confirm_discount_after_tv);
        String discount_after_str = "优惠后总金额：" + discount_after_price;
        discount_after_tv.setText(discount_after_str);
    }

    private void setupSelectCouponBtn() {
        final Button select_coupon_btn = (Button) findViewById(R.id.order_confirm_coupon_select_btn);
        select_coupon_btn.setOnClickListener(this);
    }

    private void setupPayBtn() {
        final Button pay_btn = (Button) findViewById(R.id.order_confirm_pay_btn);
        pay_btn.setOnClickListener(this);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.ORDER_CONFIRM_GET_ITEM:
                shoeList = orderConfirmAction.handleOrderConfirmResponse(result);

                setupRecyclerView();
                onCalculateTotalPrice();
                break;

            case HttpTag.COUPON_COUPON:
                ArrayList<ObjectCoupon> couponList = orderConfirmAction.handleEnableCouponResponse(result);
                onCreateCouponSelectDialog(couponList);
                break;

            case HttpTag.ORDER_CONFIRM_CREATE_ORDER:
                orderConfirmAction.handleCreateOrderResponse(result);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.activity_order_confirm_layout, new Order_Success_Frag());
                transaction.commit();
                return;

            default:
                break;
        }
    }

    private void onCreateCouponSelectDialog(final ArrayList<ObjectCoupon> couponList) {
        ArrayList<String> couponInfoList = new ArrayList<>();
        for (int i = 0; i < couponList.size(); i++) {
            ObjectCoupon coupon = couponList.get(i);
            float discount = Float.parseFloat(coupon.getDiscount()) / 10;
            couponInfoList.add(coupon.getName() + "-" + discount + "折");
        }

        couponInfoList.add("不选择");

        final String[] couponInfo_arr = couponInfoList.toArray(new String[couponInfoList.size()]);

        new AlertDialog.Builder(this)
                .setSingleChoiceItems(couponInfo_arr, couponInfoList.size() - 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == couponList.size()) {
                            coupon_number = "";
                        } else {
                            coupon_number = couponList.get(i).getNumber();
                        }
                        setupCouponSelectText(couponInfo_arr[i]);
                    }
                })
                .show();
    }

    private void setupCouponSelectText(String coupon_info) {
        final TextView coupon_select_tv = (TextView) findViewById(R.id.order_confirm_coupon_select_tv);

        final TextView discount_before_tv = (TextView) findViewById(R.id.order_confirm_discount_before_tv);
        String discount_before_str = discount_before_tv.getText().toString();

        if (!coupon_info.equals("不选择")) {
            // parse discount
            String[] coupon_info_arr = coupon_info.split("-");
            int len = coupon_info_arr[1].length();
            float discount = Float.parseFloat(coupon_info_arr[1].substring(0, len - 1));

            coupon_info = "优惠券：" + coupon_info;
            coupon_select_tv.setText(coupon_info);

            float discount_before_price = Float.parseFloat(discount_before_str.substring(7, discount_before_str.length()));
            float discount_after_price = discount_before_price * discount / 10;

            setupTotalPriceText(discount_before_price, discount_after_price);
        } else {
            coupon_info = "优惠券：" + coupon_info;
            coupon_select_tv.setText(coupon_info);

            float discount_before_price = Float.parseFloat(discount_before_str.substring(7, discount_before_str.length()));
            setupTotalPriceText(discount_before_price, discount_before_price);
        }

        Log.i("Result", "coupon number is : " + coupon_number);
    }

    @Override
    public void onNullResponse(String tag) throws JSONException {
        switch (tag) {
            case HttpTag.ORDER_CONFIRM_GET_ITEM:
                showNetDownPage(R.id.activity_order_confirm_layout);
                break;

            default:
                break;
        }
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
            case R.id.order_confirm_coupon_select_btn:
                orderConfirmAction.getEnableCoupon();
                break;

            case R.id.order_confirm_pay_btn:
                onMakeOrder();
                break;

            default:
                break;
        }
    }

    private void onMakeOrder() {
        final TextView discount_after_tv = (TextView) findViewById(R.id.order_confirm_discount_after_tv);
        String order_total_price = discount_after_tv.getText().toString();
        order_total_price = "" + Float.parseFloat(order_total_price.substring(7, order_total_price.length()));

        StringBuilder sku_code_builder = new StringBuilder("");
        StringBuilder count_builder = new StringBuilder("");
        StringBuilder total_price_builder = new StringBuilder("");

        for (int i = 0; i < shoeList.size(); i++) {
            ObjectShoe shoe = shoeList.get(i);

            sku_code_builder.append(shoe.getSkuCode()).append("-");
            count_builder.append(shoe.getCount()).append("-");
            total_price_builder.append(shoe.getPrice()).append("-");
        }

        sku_code_builder.replace(sku_code_builder.length() - 1, sku_code_builder.length(), "");
        count_builder.replace(count_builder.length() - 1, count_builder.length(), "");
        total_price_builder.replace(total_price_builder.length() - 1, total_price_builder.length(), "");

        orderConfirmAction.onCreateOrder(coupon_number, sku_code_builder.toString(), count_builder.toString(), total_price_builder.toString(), order_total_price);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.KEYCODE_BACK) {
            Cart_Act.cart_act.finish();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
