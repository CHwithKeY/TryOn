package wale_tech.tryon.user.cart;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.BaseClickListener;
import wale_tech.tryon.publicView.ColorSnackBar;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicObject.ObjectShoe;
import wale_tech.tryon.publicSet.IntentSet;
import wale_tech.tryon.publicSet.MapSet;
import wale_tech.tryon.user.cart.orderPart.Order_Confirm_Act;

/**
 * Created by KeY on 2016/7/2.
 */
class ClickListener extends BaseClickListener {

    private CartAction cartAction;

    public ClickListener(Context context, BaseAction baseAction) {
        super(context, baseAction);
        cartAction = (CartAction) baseAction;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rv_pattern_fl:
                onHidePattern(v);
                break;

            case R.id.rv_make_order_tv:
                onCreateOrderConfirmDialog(v);
                break;

            default:
                break;
        }
    }

    private void onHidePattern(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    private void onCreateOrderConfirmDialog(final View view) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.cart_dialog_order_confirm_title))
                .setMessage(context.getString(R.string.cart_dialog_order_confirm_msg))
                .setPositiveButton(context.getString(R.string.base_dialog_btn_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onMakeOrder(view);
                    }
                })
                .setNegativeButton(context.getString(R.string.base_dialog_btn_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void onMakeOrder(View view) {
        HashMap<String, Object> map = Methods.cast(view.getTag());

        ArrayList<ObjectShoe> shoeList = Methods.cast(map.get(MapSet.KEY_SHOE_LIST));
        ArrayList<String> checkList = Methods.cast(map.get(MapSet.KEY_CHECK_LIST));

        if (checkList == null || checkList.size() == 0) {
            new ColorSnackBar(context).show(context.getString(R.string.base_snack_no_item_been_selected));
            return;
        }

        StringBuilder sku_code_builder = new StringBuilder("");
        for (int i = 0; i < checkList.size(); i++) {
            int position = Integer.parseInt(checkList.get(i));
            ObjectShoe shoe = shoeList.get(position);

            sku_code_builder.append(shoe.getSkuCode()).append("-");
        }
        sku_code_builder.replace(sku_code_builder.length() - 1, sku_code_builder.length(), "");

        Intent order_confirm_int = new Intent(context, Order_Confirm_Act.class);
        order_confirm_int.putExtra(IntentSet.KEY_SKU_CODE, sku_code_builder.toString());
        context.startActivity(order_confirm_int);
    }

}
