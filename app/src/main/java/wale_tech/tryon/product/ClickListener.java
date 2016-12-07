package wale_tech.tryon.product;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.base.Base_Frag;
import wale_tech.tryon.pattern.PatternAction;
import wale_tech.tryon.user.cart.Cart_Act;
import wale_tech.tryon.user.favourite.FavAction;
import wale_tech.tryon.PermissionAction;
import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.BaseClickListener;
import wale_tech.tryon.pattern.Pattern_Frag;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicObject.ObjectShoe;
import wale_tech.tryon.publicSet.MapSet;
import wale_tech.tryon.publicSet.PermissionSet;

/**
 * Created by KeY on 2016/6/30.
 */
class ClickListener extends BaseClickListener {

    private ProductAction productAction;

    public ClickListener(Context context, BaseAction baseAction) {
        super(context, baseAction);

        productAction = (ProductAction) baseAction;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_coupon_img:
                onGetCoupon();
                break;

            // ButtonBar
//            case R.id.product_scan_imgbtn:
//                onScan();
//                break;

            case R.id.product_fav_imgbtn:
                onFavouriteOperate(v);
                break;

            case R.id.product_cart_imgbtn:
                onEnterCart();
                break;

            case R.id.product_add_cart_btn:
                onAddCart();
                break;
        }
    }

    private void onGetCoupon() {
        Log.i("Result", "onGetCoupon");
        productAction.onGetCoupon();
    }


    private void onFavouriteOperate(View view) {
        if (!new BaseAction(context).checkLoginStatus()) {
            return;
        }

        HashMap<String, Object> map = Methods.cast(view.getTag());
        boolean isHaveFav = (boolean) map.get(MapSet.KEY_IS_HAVE_FAVOURITE);
        String sku_code = (String) map.get(MapSet.KEY_SKU_CODE);

        if (isHaveFav) {
            productAction.onFavouriteOperate(FavAction.OPERATION_DELETE, sku_code);
        } else {
            productAction.onFavouriteOperate(FavAction.OPERATION_ADD, sku_code);
        }
    }

    private void onEnterCart() {
        if (!new BaseAction(context).checkLoginStatus()) {
            return;
        }

        Intent cart_int = new Intent(context, Cart_Act.class);
        context.startActivity(cart_int);
    }

    private void onAddCart() {
        ((Product_Act) context).setCurrentFragment(1);

        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();

        for (int i = 0; i < fragments.size(); i++) {
            Base_Frag base_frag = (Base_Frag) fragments.get(i);
            if (base_frag.getCustomTag().equals("shoePattern_frag")) {
                ShoePattern_Frag shoePattern_frag = (ShoePattern_Frag) base_frag;

                if (shoePattern_frag.getPatternAction().getSaveColor().isEmpty()
                        || shoePattern_frag.getPatternAction().getSaveSize().isEmpty()) {
                    Log.i("Result", "click save color");
                    ((Base_Act) context).showSnack(0, "请选择颜色或尺码");
                    return;
                }

                if (shoePattern_frag.getStock() == 0) {
                    ((Base_Act) context).showSnack(0, "该款产品暂无库存，请选择其他产品");
                    return;
                }

                shoePattern_frag.getPatternAction().onAddInCart();
                break;
            }
        }
//        ObjectShoe shoe = Methods.cast(view.getTag());
//
//        Bundle bundle = new Bundle();
//        bundle.putString("brand", shoe.getBrand());
//        bundle.putString("product_name", shoe.getProductName());
//
//        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//
//        Pattern_Frag pattern_frag = new Pattern_Frag();
//        pattern_frag.setArguments(bundle);
//
//        transaction.add(R.id.activity_product_layout, pattern_frag, "pattern_frag");
//        transaction.commit();
    }
}
