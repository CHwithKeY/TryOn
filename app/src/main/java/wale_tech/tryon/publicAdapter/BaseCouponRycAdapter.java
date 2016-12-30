package wale_tech.tryon.publicAdapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.http.HttpAction;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicObject.ObjectCoupon;

/**
 * Created by KeY on 2016/6/29.
 */
public abstract class BaseCouponRycAdapter extends BaseRycAdapter implements View.OnClickListener {

    public BaseCouponRycAdapter(ArrayList<ObjectCoupon> objectList) {
        super(objectList);
    }

    @Override
    public DataViewHolder onCreateDataViewHolder(ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_coupon_base_item, null);
        return new CouponViewHolder(view);
    }

    @Override
    public ArrayList<ObjectCoupon> getDataList() {
        return Methods.cast(super.getDataList());
    }

    @Override
    public void onBindDataViewHolder(DataViewHolder parent, int position) {
        Context context = parent.itemView.getContext();

        CouponViewHolder holder = (CouponViewHolder) parent;
        ObjectCoupon coupon = getDataList().get(position);
        holder.item_ll.setTag(position);
        holder.item_ll.setOnClickListener(this);

        String discount_str = coupon.getDiscount() + "%";
//        int discount_percent = Integer.parseInt(discount_str);
//        float discount = discount_percent / 10;
//        discount_str = discount + context.getString(R.string.coupon_discount);

        holder.discount_tv.setText(discount_str);

        String name_str = coupon.getName();
        holder.name_tv.setText(name_str);

        String time_str = context.getString(R.string.coupon_list_params_time) + " : " + coupon.getTime();
        holder.time_tv.setText(time_str);
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();

        if (!HttpAction.checkNet(context)) {
            return;
        }

        int position = Methods.cast(v.getTag());

        ObjectCoupon coupon = getDataList().get(position);

        String[] item_title = context.getResources().getStringArray(R.array.coupon_full);
        String[] item_content = {
                coupon.getNumber(), coupon.getName(),
                coupon.getDiscount() + "%", coupon.getStore(), coupon.getBrand(),
                coupon.getContent(), coupon.getTime()
        };

        ArrayList<String> itemList = new ArrayList<>();
        for (int i = 0; i < item_title.length; i++) {
            String item = item_title[i] + " : " + item_content[i];
            itemList.add(item);
        }

        String[] items = itemList.toArray(new String[itemList.size()]);

        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.coupon_dialog_coupon_title))
                .setItems(items, null)
                .show();
    }

    public class CouponViewHolder extends DataViewHolder {

        public RelativeLayout item_ll;

        public TextView discount_tv;
        public TextView name_tv;
        public TextView time_tv;

        public CouponViewHolder(View itemView) {
            super(itemView);

            item_ll = (RelativeLayout) itemView.findViewById(R.id.rv_coupon_rl);

            discount_tv = (TextView) itemView.findViewById(R.id.rv_coupon_discount_tv);
            name_tv = (TextView) itemView.findViewById(R.id.rv_coupon_name_tv);
            time_tv = (TextView) itemView.findViewById(R.id.rv_coupon_time_tv);
        }
    }

}
