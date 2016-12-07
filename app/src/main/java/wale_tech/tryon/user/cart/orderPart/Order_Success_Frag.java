package wale_tech.tryon.user.cart.orderPart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONException;

import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Frag;
import wale_tech.tryon.user.cart.Cart_Act;
import wale_tech.tryon.user.order.Order_Act;

/**
 * Created by lenovo on 2016/11/18.
 */

public class Order_Success_Frag extends Base_Frag implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
return inflater.inflate(R.layout.fragment_order_confirm_success_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupOrderCheckBtn(view);
    }

    private void setupOrderCheckBtn(View view) {
        final Button check_order_btn = (Button) view.findViewById(R.id.order_confirm_check_order_btn);
        check_order_btn.setOnClickListener(this);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {

    }

    @Override
    public void onNullResponse(String tag) throws JSONException {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.order_confirm_check_order_btn:
                onCheckOrder();
                break;

            default:
                break;
        }
    }

    private void onCheckOrder(){
        Intent order_int = new Intent(getContext(), Order_Act.class);
        getContext().startActivity(order_int);

        Cart_Act.cart_act.finish();
        getActivity().finish();
    }
}
