package wale_tech.tryon.user.coupon;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.publicAdapter.BaseCouponRycAdapter;
import wale_tech.tryon.publicObject.ObjectCoupon;

/**
 * Created by KeY on 2016/6/29.
 */
public abstract class UsedCouponRycAdapter extends BaseCouponRycAdapter {

    public UsedCouponRycAdapter(ArrayList<ObjectCoupon> objectList) {
        super(objectList);
    }

    @Override
    public DataViewHolder onCreateDataViewHolder(ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_shoe_general_used_item, null);
        return new CouponViewHolder(view);
    }

    @Override
    public void onBindDataViewHolder(DataViewHolder parent, int position) {
        super.onBindDataViewHolder(parent, position);

        CouponViewHolder holder = (CouponViewHolder) parent;
        holder.item_ll.setOnClickListener(null);
    }
}
