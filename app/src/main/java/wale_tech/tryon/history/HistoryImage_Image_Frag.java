package wale_tech.tryon.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import wale_tech.tryon.publicAdapter.BaseRycAdapter;
import wale_tech.tryon.publicSet.BundleSet;

/**
 * Created by spilkaka on 2017/5/8.
 */

public class HistoryImage_Image_Frag extends Base_Frag {
    private HistoryAction historyAction;
    private RecyclerView image_rv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_image_image_layout, container, false);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        String date_time = args.getString(BundleSet.KEY_DATE_TIME);
        Log.i("Result", "args is : " + date_time);

        varInit();

        if (BaseAction.ACTION_NET_DOWN == historyAction.getHistoryImage(BaseAction.REQUEST_DEFAULT, date_time)) {
            showNetDownPage(R.id.fragment_history_image_layout);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void varInit() {
        Log.i("Result", "image frag getContext is : " + History_Act.history_act);
        historyAction = new HistoryAction(History_Act.history_act, this);
    }

    private void setupRecyclerView(ArrayList<String> imageList) {
        if (getView() != null) {
            BaseRycAdapter adapter = new HistoryImageRycAdapter(imageList) {
                @Override
                public void onLoadMore() {
                }
            };

            image_rv = (RecyclerView) getView().findViewById(R.id.history_image_image_rv);
            image_rv.setLayoutManager(new GridLayoutManager(getContext(), 3));
            image_rv.setAdapter(adapter);
        } else {
            Log.e(getClass().getName(), "该 fragment 的父 view 的值为空");
        }
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.HISTORY_GET_IMAGE:
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
                handleLoadList(imageList);
                break;

            default:
                break;
        }
    }

    private void handleDefaultList(ArrayList<String> imageList) {
        if (imageList.size() == 0) {
            showEmptyPage(R.id.fragment_history_image_layout);
        }
        setupRecyclerView(imageList);
    }

    private void handleLoadList(ArrayList<String> imageList) {
        BaseRycAdapter adapter = (BaseRycAdapter) image_rv.getAdapter();
        if (imageList.size() != 0) {
            removeLoadMoreItem(adapter, imageList);
        } else {
            removeLoadMoreItem(adapter, null);
        }
    }

    @Override
    public void onNullResponse(String tag) throws JSONException {
        switch (tag) {
            case HttpTag.HISTORY_GET_IMAGE:
                onHandleNullHistory();
                break;

            default:
                break;
        }
    }

    private void onHandleNullHistory() {
        switch (historyAction.getRequest()) {
            case BaseAction.REQUEST_DEFAULT:
                showNetDownPage(R.id.fragment_history_image_layout);
                break;

//            case BaseAction.REQUEST_LOAD_MORE:
//                BaseRycAdapter adapter = (BaseRycAdapter) image_rv.getAdapter();
//                removeLoadMoreItem(adapter, null);
//                break;

            default:
                break;
        }
    }
}
