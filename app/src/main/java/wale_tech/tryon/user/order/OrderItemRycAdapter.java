package wale_tech.tryon.user.order;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.publicAdapter.BaseShoeRycAdapter;
import wale_tech.tryon.publicObject.ObjectShoe;

/**
 * Created by KeY on 2016/7/26.
 */
public abstract class OrderItemRycAdapter extends BaseShoeRycAdapter {

    public OrderItemRycAdapter(ArrayList<ObjectShoe> dataList) {
        super(dataList);
    }

    @Override
    public DataViewHolder onCreateDataViewHolder(ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_shoe_order_item, null);
        return new OrderShoeViewHolder(view);

    }

    @Override
    public void onBindDataViewHolder(DataViewHolder parent, int position) {
        super.onBindDataViewHolder(parent, position);

        ObjectShoe shoe = getDataList().get(position);
        OrderShoeViewHolder holder = (OrderShoeViewHolder) parent;
        Context context = holder.item_ll.getContext();

        holder.item_ll.setOnClickListener(null);

        String count_str = context.getString(R.string.order_item_item_count) + "\n" + shoe.getCount();
        holder.count_tv.setText(count_str);

        String price_str = context.getString(R.string.order_item_item_price) + "\n" + shoe.getPrice();
        holder.price_tv.setText(price_str);
    }

    private class OrderShoeViewHolder extends BaseShoeViewHolder {
        TextView count_tv;
        TextView price_tv;

//        TextView missed_tv;

        OrderShoeViewHolder(View itemView) {
            super(itemView);

            count_tv = (TextView) itemView.findViewById(R.id.rv_count_tv);
            price_tv = (TextView) itemView.findViewById(R.id.rv_price_tv);

//            missed_tv = (TextView) itemView.findViewById(R.id.rv_missed_tv);
        }
    }
}
