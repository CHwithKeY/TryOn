package wale_tech.tryon.user.coupon.couponFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.util.ArrayList;

import wale_tech.tryon.publicView.EmptyRecyclerView;
import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Frag;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicObject.ObjectCoupon;
import wale_tech.tryon.user.coupon.CouponAction;
import wale_tech.tryon.user.coupon.UsedCouponRycAdapter;

/**
 * Created by KeY on 2016/11/6.
 */

public class CouponUsed_Frag extends Base_Frag {
    private EmptyRecyclerView coupon_rv;
    private CouponAction couponAction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_coupon_used_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        varInit();
    }

    private void varInit() {
        couponAction = new CouponAction(getContext(), this);
        couponAction.getCoupon(CouponAction.COUPON_USED);
    }

    private void setupEmptyRecyclerView(ArrayList<ObjectCoupon> couponList) {
        UsedCouponRycAdapter adapter = new UsedCouponRycAdapter(couponList) {
            @Override
            public void onLoadMore() {

            }
        };

        coupon_rv = (EmptyRecyclerView) getActivity().findViewById(R.id.coupon_used_rv);
        coupon_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        coupon_rv.setEmptyView(View.inflate(getContext(), R.layout.fragment_public_empty_page_layout, null));
        coupon_rv.setAdapter(adapter);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.COUPON_COUPON:
                ArrayList<ObjectCoupon> couponList = couponAction.handleResponse(result);
//                if (couponList.size() == 0) {
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction transaction = fragmentManager.beginTransaction();
//
//                    List<Fragment> fragmentList = fragmentManager.getFragments();
//                    for (int i = 0; i < fragmentList.size(); i++) {
//                        Base_Frag frag = (Base_Frag) fragmentList.get(i);
//
//                        if (frag.getCustomTag().equals("tab02")) {
//                            transaction.remove(frag);
//                            transaction.commit();
//
//                            showEmptyPage(R.id.fragment_coupon_used_layout);
//                        } else {
//                            Log.i("Result", "do nothing");
//                        }
//                    }
//
////                    transaction.remove()
////                    transaction.replace(R.id.activity_coupon_layout, new EmptyPage_Frag(), "");
//                } else {
                setupEmptyRecyclerView(couponList);
//                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onNullResponse(String tag) throws JSONException {
//        switch (tag) {
//            case HttpTag.COUPON_COUPON:
//                ((Base_Act) getActivity()).showNetDownPage(R.id.activity_coupon_layout);
//                break;
//
//            default:
//                break;
//        }
    }

}
