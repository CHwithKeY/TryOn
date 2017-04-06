package wale_tech.tryon.product;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.List;

import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.base.Base_Frag;
import wale_tech.tryon.user.cart.Cart_Act;
import wale_tech.tryon.user.favourite.FavAction;
import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.BaseClickListener;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicSet.MapSet;

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

        Log.i("Result", "click os");
        if (!productAction.checkLoginStatus()) {
            Log.i("Result", "click");
            return;
        }

        switch (v.getId()) {
            case R.id.product_coupon_img:
                onGetCoupon();
                break;

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
//        if (!new BaseAction(context).checkLoginStatus()) {
//            return;
//        }

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
                    ((Base_Act) context).showSnack(0, context.getString(R.string.pattern_snack_select_color_size));
                    return;
                }

                if (shoePattern_frag.getStock() == 0) {
                    ((Base_Act) context).showSnack(0, context.getString(R.string.pattern_snack_no_stock));
                    return;
                }

                shoePattern_frag.getPatternAction().onAddInCart();
                break;
            }
        }
    }
}
