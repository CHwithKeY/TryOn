package wale_tech.tryon.history;

import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.publicAdapter.BaseShoeRycAdapter;
import wale_tech.tryon.publicObject.ObjectShoe;

/**
 * Created by KeY on 2016/6/29.
 */
abstract class HistoryShoeRycAdapter extends BaseShoeRycAdapter {

    public HistoryShoeRycAdapter(ArrayList<ObjectShoe> dataList) {
        super(dataList);
    }

    @Override
    public DataViewHolder onCreateDataViewHolder(ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_shoe_history_item, null);
        return new HistoryShoeViewHolder(view);
    }

    @Override
    public void onBindDataViewHolder(DataViewHolder parent, int position) {
        super.onBindDataViewHolder(parent, position);
        HistoryShoeViewHolder holder = (HistoryShoeViewHolder) parent;
        setupDateBar(holder, position);
    }

    private class HistoryShoeViewHolder extends BaseShoeViewHolder {

        CardView date_cv;
        TextView date_tv;

        HistoryShoeViewHolder(View itemView) {
            super(itemView);

            date_cv = (CardView) itemView.findViewById(R.id.rv_date_cv);
            date_tv = (TextView) itemView.findViewById(R.id.rv_date_tv);
        }
    }

    private void setupDateBar(HistoryShoeViewHolder holder, int position) {
        ObjectShoe shoe = getDataList().get(position);
        holder.date_tv.setText(shoe.getRemark());
    }
}
