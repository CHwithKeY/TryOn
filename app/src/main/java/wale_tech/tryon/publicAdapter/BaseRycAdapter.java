package wale_tech.tryon.publicAdapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.http.HttpAction;

/**
 * Created by lenovo on 2016/11/14.
 */

public abstract class BaseRycAdapter extends RecyclerView.Adapter {

    public final static int VIEW_TYPE_LOAD = 1;
    public final static int VIEW_TYPE_DATA = 2;

    private ArrayList dataList;
    private boolean showLoadItem;

    public BaseRycAdapter getAdapter() {
        return this;
    }

    public BaseRycAdapter(ArrayList dataList) {
        this.dataList = dataList;
    }

    public ArrayList getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList dataList) {
        this.dataList = dataList;
    }

    public void setShowLoadItem(boolean showLoadItem) {
        this.showLoadItem = showLoadItem;
    }

    public boolean isShowLoadItem() {
        return showLoadItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_DATA:
                return onCreateDataViewHolder(parent);
            case VIEW_TYPE_LOAD:
                return onCreateLoadViewHolder(parent);
            default:
                return onCreateDataViewHolder(parent);
        }
    }

    public abstract DataViewHolder onCreateDataViewHolder(ViewGroup parent);

    public LoadViewHolder onCreateLoadViewHolder(ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.public_load_more_item, null);
        return new LoadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadViewHolder) {
            onBindLoadViewHolder((LoadViewHolder) holder);
            onLoadMore();
        } else if (holder instanceof DataViewHolder) {
            onBindDataViewHolder((DataViewHolder) holder, position);
        }
    }

    public abstract void onBindDataViewHolder(DataViewHolder parent, int position);

    private void onBindLoadViewHolder(LoadViewHolder holder) {
        holder.load_ll.setGravity(Gravity.CENTER);

        Context context = holder.load_ll.getContext();
        if (!HttpAction.checkNet(context)) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("Result", "handler remove");
                    setShowLoadItem(false);
                    dataList.remove(dataList.size() - 1);
                    notifyDataSetChanged();
                }
            }, 1000);
        }
//        removeLoadMoreItem(getAdapter(), null);
    }

    public abstract void onLoadMore();

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (dataList.size() != 0 && (position == (dataList.size() - 1)) && showLoadItem) {
            return VIEW_TYPE_LOAD;
        } else {
            return VIEW_TYPE_DATA;
        }
        // return super.getItemViewType(position);
    }

    public abstract class DataViewHolder extends RecyclerView.ViewHolder {
        public DataViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class LoadViewHolder extends RecyclerView.ViewHolder {

        LinearLayout load_ll;

        LoadViewHolder(View itemView) {
            super(itemView);

            load_ll = (LinearLayout) itemView.findViewById(R.id.item_load_more_layout);
        }
    }
}
