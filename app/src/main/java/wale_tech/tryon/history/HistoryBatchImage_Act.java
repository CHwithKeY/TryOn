package wale_tech.tryon.history;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicAdapter.BaseRycAdapter;
import wale_tech.tryon.publicSet.IntentSet;

public class HistoryBatchImage_Act extends Base_Act {

    private HistoryAction historyAction;
    private RecyclerView history_batch_image_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_batch_image_layout);

        varInit();

        setupToolbar();

    }

    @Override
    public void varInit() {
        String batch_image_code = getIntent().getStringExtra(IntentSet.KEY_BATCH_IMAGE_CODE);

        historyAction = new HistoryAction(this);
        if (BaseAction.ACTION_NET_DOWN == historyAction.getHistoryBatchImage(BaseAction.REQUEST_DEFAULT, batch_image_code)) {
            showNetDownPage(R.id.activity_history_batch_image_layout);
        }
    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupRecyclerView(ArrayList<String> imageList) {
//            for (int i = 0; i < imageList.size(); i++) {
//                Log.i("Result", "image con:" + imageList.get(i).getImagePath());
//            }
//
        BaseRycAdapter adapter = new HistoryImageRycAdapter(imageList) {
            @Override
            public void onLoadMore() {
            }
        };

        history_batch_image_rv = (RecyclerView) findViewById(R.id.history_batch_image_rv);
        history_batch_image_rv.setLayoutManager(new GridLayoutManager(this, 3));
        history_batch_image_rv.setAdapter(adapter);
    }


    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.HISTORY_GET_BATCH_IMAGE:
                onHandleHistory(result);
                break;

            default:
                break;
        }
    }

    private void onHandleHistory(String result) throws JSONException {
        ArrayList<String> imageList = historyAction.handleImageResponse(result);

        switch (historyAction.getRequest()) {
            case BaseAction.REQUEST_DEFAULT:
                handleDefaultList(imageList);
                break;

            case BaseAction.REQUEST_LOAD_MORE:
                break;

            default:
                break;
        }
    }

    private void handleDefaultList(ArrayList<String> imageList) {
        if (imageList.size() == 0) {
            showEmptyPage(R.id.activity_history_batch_image_layout);
        }
        setupRecyclerView(imageList);
    }

    @Override
    public void onNullResponse(String tag) throws JSONException {
        switch (tag) {
            case HttpTag.HISTORY_GET_BATCH_IMAGE:
                onHandleNullHistory();
                break;

            default:
                break;
        }
    }

    private void onHandleNullHistory() {
        switch (historyAction.getRequest()) {
            case BaseAction.REQUEST_DEFAULT:
                showNetDownPage(R.id.activity_history_batch_image_layout);
                break;

            case BaseAction.REQUEST_LOAD_MORE:
                BaseRycAdapter adapter = (BaseRycAdapter) history_batch_image_rv.getAdapter();
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
}
