package wale_tech.tryon.user.coupon;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.publicAdapter.ViewTitleAdapter;
import wale_tech.tryon.user.coupon.couponFragment.CouponEnable_Frag;
import wale_tech.tryon.user.coupon.couponFragment.CouponUnable_Frag;
import wale_tech.tryon.user.coupon.couponFragment.CouponUsed_Frag;

public class Coupon_Act extends Base_Act {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_layout);

        varInit();

        setupToolbar();

        setupViewPager();
    }

    @Override
    public void varInit() {
    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupViewPager() {
        final ViewPager main_vp = (ViewPager) findViewById(R.id.coupon_info_vp);

        List<Fragment> fragments = new ArrayList<>();

        CouponEnable_Frag tab01 = new CouponEnable_Frag();
        CouponUsed_Frag tab02 = new CouponUsed_Frag();
        CouponUnable_Frag tab03 = new CouponUnable_Frag();

        fragments.add(tab01);
        fragments.add(tab02);
        fragments.add(tab03);

        String[] titles = {
                getString(R.string.coupon_tab_coupon_enable),
                getString(R.string.coupon_tab_coupon_used),
                getString(R.string.coupon_tab_coupon_unable)};

        ViewTitleAdapter adapter = new ViewTitleAdapter(getSupportFragmentManager());
        adapter.setFragments(fragments);
        adapter.setTitles(titles);

        main_vp.setAdapter(adapter);
        main_vp.setOffscreenPageLimit(fragments.size());

        setupTabLayout(main_vp);
    }

    private void setupTabLayout(ViewPager viewPager) {
        final TabLayout lockinfo_tab = (TabLayout) findViewById(R.id.coupon_info_tab);
        lockinfo_tab.setupWithViewPager(viewPager);
        // 设置tab的文字，在被选中后和没被选中的时候，分别显示的颜色
        lockinfo_tab.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAssist));
        lockinfo_tab.setTabTextColors(getResources().getColor(R.color.colorMain), getResources().getColor(R.color.colorAssist));
        lockinfo_tab.setSelectedTabIndicatorHeight(7);
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

}
