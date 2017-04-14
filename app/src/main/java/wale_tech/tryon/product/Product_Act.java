package wale_tech.tryon.product;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wale_tech.tryon.publicSet.BundleSet;
import wale_tech.tryon.user.setting.PermissionAction;
import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.http.HttpResult;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicAdapter.ViewBaseAdapter;
import wale_tech.tryon.publicAdapter.ViewTitleAdapter;
import wale_tech.tryon.publicObject.ObjectShoe;
import wale_tech.tryon.publicObject.ObjectShoeImage;
import wale_tech.tryon.publicSet.IntentSet;
import wale_tech.tryon.publicSet.MapSet;
import wale_tech.tryon.trigger.TriggerSet;
import wale_tech.tryon.user.favourite.FavAction;

public class Product_Act extends Base_Act implements ViewPager.OnPageChangeListener {
    private ImageButton fav_imgbtn;

    private ObjectShoe shoe;
    private ArrayList<ObjectShoeImage> shoeImageList;

    private ProductAction productAction;
    private ClickListener clickListener;

    private boolean isHaveFav;

    public final static String PATH_SCAN = "Scan";
    public final static String PATH_NFC = "Nfc";
    public final static String PATH_OTHER = "Other";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_layout);

        varInit();

        setupToolbar();
    }

    @Override
    public void varInit() {
        if (getIntent() != null) {
            String sku_code = getIntent().getStringExtra(IntentSet.KEY_SKU_CODE);
            String path = getIntent().getStringExtra(IntentSet.KEY_TRIGGER_PATH);

            if (path == null || path.isEmpty()) {
                path = TriggerSet.PATH_OTHER;
            }

            Log.i("Result", "sku code is :" + sku_code);

            productAction = new ProductAction(this);
            clickListener = new ClickListener(this, productAction);
            productAction.getShoeDetails(sku_code, path);

            shoeImageList = new ArrayList<>();

            isHaveFav = false;

            if (path.equals(PATH_NFC) || path.equals(PATH_SCAN)) {
                // 根据 SkuCode 和 username 的关联情况，实现一个账户只能对一个新的Sku获取优惠券的限制
                productAction.checkCouponAward(sku_code);
//                if (!productAction.isGetCoupon()) {
//                    createCouponDialog();
//                }
            }
        } else {
            finish();
        }
    }

    public ObjectShoe getShoe() {
        return shoe;
    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.COUPON_CHECK_COUPON:
                if (!productAction.handleCouponCheckResponse(result)) {
                    createCouponDialog();
                }
                break;

            case HttpTag.COUPON_AWARD_COUPON:
                productAction.handleCouponAwardResponse(result);
                break;

            case HttpTag.PRODUCT_GET_SHOE_DETAILS:
                shoe = productAction.handleDetailsResponse(result);
                productAction.onColorPick(shoe.getBrand(), shoe.getProductName());

                if (sharedAction.getLoginStatus()) {
                    productAction.onFavouriteOperate(FavAction.OPERATION_CHECK, shoe.getSkuCode());
                } else {
                    setupBottomBar();
                }

                setupDetailsViewPager();
                break;

            case HttpTag.PRODUCT_GET_SHOE_COLOR:
                shoeImageList = productAction.handleColorPickResponse(result);

                ArrayList<String> imageList = new ArrayList<>();

                for (int i = 0; i < shoeImageList.size(); i++) {
                    imageList.add(shoeImageList.get(i).getImagePath());
                }

                setupImageViewPager(imageList);
                break;

            case HttpTag.FAVOURITE_CHECK_FAVOURITE:
                // 返回值有两个，"已有该收藏"和"没有该收藏"
                String result_str = new JSONObject(result).getString(HttpResult.RESULT);
                if (result_str.equals(HttpResult.FAVOURITE_ITEM_EXIST)) {
                    isHaveFav = true;
                }

                setupBottomBar();
                break;

            case HttpTag.FAVOURITE_ADD_FAVOURITE:
                productAction.handleFavouriteResponse(result);
                fav_imgbtn.setImageResource(R.drawable.ic_product_favorite_light);
                isHaveFav = true;
                onWrapFavTag();
                break;

            case HttpTag.FAVOURITE_DELETE_FAVOURITE:
                productAction.handleFavouriteResponse(result);
                fav_imgbtn.setImageResource(R.drawable.ic_product_favorite_dark);
                isHaveFav = false;
                onWrapFavTag();
                break;

            default:
                break;
        }
    }

    @Override
    public void onNullResponse(String tag) throws JSONException {
        switch (tag) {
            case HttpTag.PRODUCT_GET_SHOE_DETAILS:
                finish();
                break;

            default:
                break;
        }
    }

//    private void setupCouponImg() {
//        final ImageView coupon_img = (ImageView) findViewById(R.id.product_coupon_img);
//        coupon_img.setVisibility(View.VISIBLE);
//        Log.i("Result", "setup img");
//        coupon_img.setOnClickListener(clickListener);
//    }

    private void createCouponDialog() {
        final View view = View.inflate(this, R.layout.dialog_get_coupon_view, null);
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setPositiveButton("点击领取", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                productAction.getCouponAward(shoe.getSkuCode());
            }
        });

//        Button button = (Button) view.findViewById(R.id.dialog_gc_get_btn);
//        button.setOnClickListener(clickListener);

        builder.show();
    }

    private void setupImageViewPager(ArrayList<String> imageList) {
        final ViewPager image_vp = (ViewPager) findViewById(R.id.product_shoe_img_vp);

        List<Fragment> fragments = new ArrayList<>();

        for (int i = 0; i < imageList.size(); i++) {
            Image_Frag image_tab = new Image_Frag();
            Bundle bundle = new Bundle();
            bundle.putString(BundleSet.KEY_IMG_URL, imageList.get(i));
            image_tab.setArguments(bundle);

            fragments.add(image_tab);
        }

        ViewBaseAdapter adapter = new ViewBaseAdapter(getSupportFragmentManager());
        adapter.setFragments(fragments);

        image_vp.setAdapter(adapter);
        image_vp.setOffscreenPageLimit(4);

        for (int i = 0; i < imageList.size(); i++) {
            if (shoe.getImagePath().equals(imageList.get(i))) {
                image_vp.setCurrentItem(i);
            }
        }
    }

    public void setCurrentImage(String image_path) {
        final ViewPager image_vp = (ViewPager) findViewById(R.id.product_shoe_img_vp);

        for (int i = 0; i < shoeImageList.size(); i++) {
            ObjectShoeImage shoeImage = shoeImageList.get(i);
            if (shoeImage.getImagePath().equals(image_path)) {
                image_vp.setCurrentItem(i);
            }
        }
    }

    private void setupDetailsViewPager() {
        final ViewPager details_vp = (ViewPager) findViewById(R.id.product_details_vp);

        List<Fragment> fragments = new ArrayList<>();

        final ShoeDetails_Frag tab01 = new ShoeDetails_Frag();
        final ShoePattern_Frag tab02 = new ShoePattern_Frag();

        tab01.setCustomTag("shoeDetails_frag");
        tab02.setCustomTag("shoePattern_frag");

        Bundle bundle = new Bundle();
        bundle.putString(BundleSet.KEY_BRAND, shoe.getBrand());
        bundle.putString(BundleSet.KEY_PRODUCT_NAME, shoe.getProductName());
        tab02.setArguments(bundle);

        fragments.add(tab01);
        fragments.add(tab02);

        String[] titles = {
                getString(R.string.product_details_frag_title),
                getString(R.string.product_pattern_frag_title)};

        ViewTitleAdapter adapter = new ViewTitleAdapter(getSupportFragmentManager());
        adapter.setFragments(fragments);
        adapter.setTitles(titles);

        details_vp.setAdapter(adapter);
        details_vp.setOffscreenPageLimit(fragments.size());
        details_vp.addOnPageChangeListener(this);

        setupTabLayout(details_vp);
    }

    private void setupTabLayout(ViewPager viewPager) {
        final TabLayout details_tab = (TabLayout) findViewById(R.id.product_details_tab);
        details_tab.setupWithViewPager(viewPager);
        // 设置tab的文字，在被选中后和没被选中的时候，分别显示的颜色
        details_tab.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAssist));
        details_tab.setTabTextColors(getResources().getColor(R.color.colorMain), getResources().getColor(R.color.colorAssist));
        details_tab.setSelectedTabIndicatorHeight(7);
    }

    public void setCurrentFragment(int position) {
        final ViewPager details_vp = (ViewPager) findViewById(R.id.product_details_vp);
        details_vp.setCurrentItem(position);
    }

    private void setupBottomBar() {
        final Button add_cart_btn = (Button) findViewById(R.id.product_add_cart_btn);
        add_cart_btn.setOnClickListener(clickListener);

        // Favourite
        fav_imgbtn = (ImageButton) findViewById(R.id.product_fav_imgbtn);
        if (isHaveFav) {
            fav_imgbtn.setImageResource(R.drawable.ic_product_favorite_light);
        }
        // 传递到 ClickListener 中，便于 FavAction() 的判断
        onWrapFavTag();
        fav_imgbtn.setOnClickListener(clickListener);

        // Cart
        final ImageButton cart_imgbtn = (ImageButton) findViewById(R.id.product_cart_imgbtn);
        cart_imgbtn.setOnClickListener(clickListener);
    }

    private void onWrapFavTag() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(MapSet.KEY_IS_HAVE_FAVOURITE, isHaveFav);
        map.put(MapSet.KEY_SKU_CODE, shoe.getSkuCode());
        fav_imgbtn.setTag(map);
    }

    @Override
    public void onPermissionAccepted(int permission_code) {
    }

    @Override
    public void onPermissionRefused(int permission_code) {
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // Check if the key event was the Back button and if there's history
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            collapsePattern();
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    // 两次点击返回键退出
//    public void collapsePattern() {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        if (fragmentManager.findFragmentByTag("pattern_frag") != null) {
//            transaction.remove(fragmentManager.findFragmentByTag("pattern_frag"));
//            transaction.commit();
//        } else {
//            System.gc();
//            finish();
//        }
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            setCurrentImage(shoe.getImagePath());
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
