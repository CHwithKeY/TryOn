package wale_tech.tryon.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.Base_Frag;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicAdapter.BaseShoeRycAdapter;
import wale_tech.tryon.publicClass.loadMore.LoadMoreTouchListener;
import wale_tech.tryon.publicObject.ObjectShoe;

/**
 * Created by lenovo on 2017/2/21.
 */

public class HistoryRecord_Frag extends Base_Frag {

    private RecyclerView history_record_rv;
    private HistoryAction historyAction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_record_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        varInit();
    }

    private void varInit() {
        historyAction = new HistoryAction(getContext(), this);
        if (historyAction.getHistory(BaseAction.REQUEST_DEFAULT) == BaseAction.ACTION_NET_DOWN) {
            showNetDownPage(R.id.activity_history_layout);
        }
    }

    private void setupRecyclerView(ArrayList<ObjectShoe> shoeList) {
        if (getView() != null) {
            BaseShoeRycAdapter adapter = new HistoryShoeRycAdapter(shoeList) {
                @Override
                public void onLoadMore() {
                    historyAction.getHistory(BaseAction.REQUEST_LOAD_MORE);
                }
            };

            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            history_record_rv = (RecyclerView) getView().findViewById(R.id.history_record_rv);
            history_record_rv.setLayoutManager(manager);
            history_record_rv.setAdapter(adapter);
            history_record_rv.setOnTouchListener(new LoadMoreTouchListener());
        } else {
            Log.e(getClass().getName(), "该 fragment 的父 view 的值为空");
        }
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.HISTORY_GET_HISTORY:
                onHandleHistory(result);
                break;

            default:
                break;
        }
    }

    private void onHandleHistory(String result) throws JSONException {
        ArrayList<ObjectShoe> shoeList = historyAction.handleResponse(result);

        switch (historyAction.getRequest()) {
            case BaseAction.REQUEST_DEFAULT:
                handleDefaultList(shoeList);
                break;

            case BaseAction.REQUEST_LOAD_MORE:
                handleLoadList(shoeList);
                break;

            default:
                break;
        }
    }

    private void handleDefaultList(ArrayList<ObjectShoe> shoeList) {
        if (shoeList.size() == 0) {
            showEmptyPage(R.id.history_record_layout);
        }
        setupRecyclerView(shoeList);
    }

    private void handleLoadList(ArrayList<ObjectShoe> shoeList) {
        BaseShoeRycAdapter adapter = (BaseShoeRycAdapter) history_record_rv.getAdapter();
        if (shoeList.size() != 0) {
            removeLoadMoreItem(adapter, shoeList);
//            ArrayList<ObjectShoe> dataList = adapter.getDataList();
//            adapter.setShowLoadItem(false);
//            dataList.remove(dataList.size() - 1);
//
//            adapter.getDataList().addAll(shoeList);
//            adapter.notifyDataSetChanged();
        } else {
            removeLoadMoreItem(adapter, null);
//            adapter.setShowLoadItem(false);
//            adapter.getDataList().remove(adapter.getDataList().size() - 1);
//            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNullResponse(String tag) throws JSONException {
        switch (tag) {
            case HttpTag.HISTORY_GET_HISTORY:
                onHandleNullHistory();
                break;

            default:
                break;
        }
    }

    private void onHandleNullHistory() {
        switch (historyAction.getRequest()) {
            case BaseAction.REQUEST_DEFAULT:
                showNetDownPage(R.id.activity_history_layout);
                break;

            case BaseAction.REQUEST_LOAD_MORE:
                BaseShoeRycAdapter adapter = (BaseShoeRycAdapter) history_record_rv.getAdapter();
                removeLoadMoreItem(adapter, null);
                break;

            default:
                break;
        }
    }
}
