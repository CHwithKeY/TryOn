package wale_tech.tryon.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.Base_Frag;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicAdapter.BaseRycAdapter;
import wale_tech.tryon.publicClass.DatePicker;

/**
 * Created by spilkaka on 2017/5/12.
 */

public class HistoryImage_Online_Frag extends Base_Frag implements View.OnClickListener {
    private HistoryAction historyAction;
    private RecyclerView image_rv;

    private Date date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_image_online_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        varInit();

        setupDateBeforeText();

        setupDateAfterText();

    }

    private void varInit() {
        historyAction = new HistoryAction(getContext(), this);
        date = DatePicker.getToday(new Date());

        setupDateText(date);
    }

    private void setupDateText(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String date_time = format.format(date);

        if (BaseAction.ACTION_NET_DOWN == historyAction.getHistoryImage(BaseAction.REQUEST_DEFAULT, date_time)) {
            showNetDownPage(R.id.fragment_history_image_online_layout);
        } else {
            removeNetDownPage();
        }

        if (getView() != null) {
            final TextView date_tv = (TextView) getView().findViewById(R.id.history_image_online_date_tv);
            date_tv.setText(date_time);
        }
    }

    private void setupDateBeforeText() {
        if (getView() != null) {
            final TextView date_before_tv = (TextView) getView().findViewById(R.id.history_image_online_date_before_tv);
            date_before_tv.setOnClickListener(this);
        }
    }

    private void setupDateAfterText() {
        if (getView() != null) {
            final TextView date_after_tv = (TextView) getView().findViewById(R.id.history_image_online_date_after_tv);
            date_after_tv.setOnClickListener(this);
        }
    }

    private void setupRecyclerView(ArrayList<String> imageList) {
        if (getView() != null) {
            BaseRycAdapter adapter = new HistoryImageRycAdapter(imageList) {
                @Override
                public void onLoadMore() {

                }
            };

            image_rv = (RecyclerView) getView().findViewById(R.id.history_image_online_rv);
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
//                handleLoadList(imageList);
                break;

            default:
                break;
        }
    }

    private void handleDefaultList(ArrayList<String> imageList) {
        if (imageList.size() == 0) {
            showEmptyPage(R.id.fragment_history_image_online_layout);
        } else {
            removeEmptyPage();
        }
        setupRecyclerView(imageList);
    }

//    private void handleLoadList(ArrayList<String> imageList) {
//        BaseRycAdapter adapter = (BaseRycAdapter) image_rv.getAdapter();
//        if (imageList.size() != 0) {
//            removeLoadMoreItem(adapter, imageList);
//        } else {
//            removeLoadMoreItem(adapter, null);
//        }
//    }

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
                showNetDownPage(R.id.fragment_history_image_online_layout);
                break;

//            case BaseAction.REQUEST_LOAD_MORE:
//                BaseRycAdapter adapter = (BaseRycAdapter) image_rv.getAdapter();
//                removeLoadMoreItem(adapter, null);
//                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
//        checkDateLimited();

        switch (v.getId()) {
            case R.id.history_image_online_date_before_tv:
                resetDateRecycler(DATE_BEFORE);
                break;

            case R.id.history_image_online_date_after_tv:
                resetDateRecycler(DATE_AFTER);
                break;
        }
    }

    private static final int DATE_BEFORE = -1;
    private static final int DATE_AFTER = 1;

    private void resetDateRecycler(int date_direction) {
        switch (date_direction) {
            case DATE_BEFORE:
                date = DatePicker.getYesterday(date);
//                String date_time = format.format(date);
                setupDateText(date);
                break;

            case DATE_AFTER:
                date = DatePicker.getTomorrow(date);
                setupDateText(date);
                break;
        }
    }

    private void checkDateLimited() {
        if (date == DatePicker.getToday(new Date())) {
            abandonDateDirectionText(R.id.history_image_online_date_after_tv);
        } else {
            activateDateDirectionText();
        }
    }

    private void activateDateDirectionText() {
        setupDateBeforeText();
        setupDateAfterText();
    }

    private void abandonDateDirectionText(int resId) {
        if (getView() != null) {
            TextView date_direction_text = (TextView) getView().findViewById(resId);
            date_direction_text.setOnClickListener(null);
        }
    }
}
