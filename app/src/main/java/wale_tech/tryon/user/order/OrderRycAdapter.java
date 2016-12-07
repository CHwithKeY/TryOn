package wale_tech.tryon.user.order;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.publicAdapter.BaseRycAdapter;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicObject.ObjectOrder;
import wale_tech.tryon.publicSet.IntentSet;

/**
 * Created by KeY on 2016/7/26.
 */
public abstract class OrderRycAdapter extends BaseRycAdapter implements View.OnClickListener {

    private Context context;

    public OrderRycAdapter(ArrayList dataList) {
        super(dataList);
    }

    @Override
    public ArrayList<ObjectOrder> getDataList() {
        return Methods.cast(super.getDataList());
    }

    @Override
    public DataViewHolder onCreateDataViewHolder(ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_order_general_item, null);
        context = parent.getContext();

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindDataViewHolder(DataViewHolder parent, int position) {

        ObjectOrder order = getDataList().get(position);
        OrderViewHolder holder = (OrderViewHolder) parent;

        holder.date_tv.setText(order.getDateTime());

        String order_num_str = context.getString(R.string.order_item_order_number) + order.getOrderNum();
        holder.order_num_tv.setText(order_num_str);
        holder.order_num_tv.setTag(position);
        holder.order_num_tv.setOnClickListener(this);

        String total_price_str = context.getString(R.string.order_item_whole_price) + order.getTotalPrice();
        holder.total_price_tv.setText(total_price_str);

    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        int position = Methods.cast(v.getTag());
        ObjectOrder order = getDataList().get(position);

        Intent item_int = new Intent(context, Order_Item_Act.class);
        item_int.putExtra(IntentSet.KEY_ORDER_NUM, order.getOrderNum());
        context.startActivity(item_int);
    }

    class OrderViewHolder extends DataViewHolder {

        TextView date_tv;
        TextView order_num_tv;

        TextView total_price_tv;

        public OrderViewHolder(View itemView) {
            super(itemView);

            date_tv = (TextView) itemView.findViewById(R.id.rv_date_tv);
            order_num_tv = (TextView) itemView.findViewById(R.id.rv_order_num_tv);

            total_price_tv = (TextView) itemView.findViewById(R.id.rv_total_price_tv);
        }
    }
}
