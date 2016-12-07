package wale_tech.tryon.history;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

import wale_tech.tryon.publicClass.loadMore.LoadMoreTouchListener;
import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.publicAdapter.BaseShoeRycAdapter;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicObject.ObjectShoe;

public class History_Act extends Base_Act {

    private RecyclerView history_rv;
    private HistoryAction historyAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_layout);

        varInit();

        setupToolbar();

    }

    @Override
    public void varInit() {
        historyAction = new HistoryAction(this);
        if (historyAction.getHistory(BaseAction.REQUEST_DEFAULT) == BaseAction.ACTION_NET_DOWN) {
            showNetDownPage(R.id.activity_history_layout);
        }
    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupRecyclerView(ArrayList<ObjectShoe> shoeList) {
        BaseShoeRycAdapter adapter = new HistoryShoeRycAdapter(shoeList) {
            @Override
            public void onLoadMore() {
                historyAction.getHistory(BaseAction.REQUEST_LOAD_MORE);
            }
        };

        LinearLayoutManager manager = new LinearLayoutManager(this);

        history_rv = (RecyclerView) findViewById(R.id.history_rv);
        history_rv.setLayoutManager(manager);
        history_rv.setAdapter(adapter);
        history_rv.setOnTouchListener(new LoadMoreTouchListener());
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
            showEmptyPage(R.id.activity_history_layout);
        }
        setupRecyclerView(shoeList);
    }

    private void handleLoadList(ArrayList<ObjectShoe> shoeList) {
        BaseShoeRycAdapter adapter = (BaseShoeRycAdapter) history_rv.getAdapter();
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
                BaseShoeRycAdapter adapter = (BaseShoeRycAdapter) history_rv.getAdapter();
                removeLoadMoreItem(adapter, null);
                break;

            default:
                break;
        }
    }

    @Override
    public void onPermissionAccepted(int permission_code) {

    }

    @Override
    public void onPermissionRefused(int permission_code) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}
