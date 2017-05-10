package wale_tech.tryon.history;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.base.Base_Frag;

public class History_Act extends Base_Act implements View.OnClickListener {

//    private RecyclerView history_rv;
//    private HistoryAction historyAction;

    public static AppCompatActivity history_act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_layout);

        varInit();

        setupToolbar();

        setupTitleText();

    }

    @Override
    public void varInit() {
//        historyAction = new HistoryAction(this);
//        if (historyAction.getHistory(BaseAction.REQUEST_DEFAULT) == BaseAction.ACTION_NET_DOWN) {
//            showNetDownPage(R.id.activity_history_layout);
//        }
        history_act = this;

        HistoryRecord_Frag historyRecord_frag = new HistoryRecord_Frag();
        showFragment(historyRecord_frag, "historyRecord_frag");
    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupTitleText() {
        final TextView history_record_tv = (TextView) findViewById(R.id.history_record_tv);
        final TextView history_image_tv = (TextView) findViewById(R.id.history_image_tv);

        history_record_tv.setOnClickListener(this);
        history_image_tv.setOnClickListener(this);
    }

//    private void setupRecyclerView(ArrayList<ObjectShoe> shoeList) {
//        BaseShoeRycAdapter adapter = new HistoryShoeRycAdapter(shoeList) {
//            @Override
//            public void onLoadMore() {
//                historyAction.getHistory(BaseAction.REQUEST_LOAD_MORE);
//            }
//        };
//
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//
//        history_rv = (RecyclerView) findViewById(R.id.history_rv);
//        history_rv.setLayoutManager(manager);
//        history_rv.setAdapter(adapter);
//        history_rv.setOnTouchListener(new LoadMoreTouchListener());
//    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
//        switch (tag) {
//            case HttpTag.HISTORY_GET_HISTORY:
//                onHandleHistory(result);
//                break;
//
//            default:
//                break;
//        }
    }

//    private void onHandleHistory(String result) throws JSONException {
//        ArrayList<ObjectShoe> shoeList = historyAction.handleResponse(result);
//
//        switch (historyAction.getRequest()) {
//            case BaseAction.REQUEST_DEFAULT:
//                handleDefaultList(shoeList);
//                break;
//
//            case BaseAction.REQUEST_LOAD_MORE:
//                handleLoadList(shoeList);
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    private void handleDefaultList(ArrayList<ObjectShoe> shoeList) {
//        if (shoeList.size() == 0) {
//            showEmptyPage(R.id.activity_history_layout);
//        }
//        setupRecyclerView(shoeList);
//    }
//
//    private void handleLoadList(ArrayList<ObjectShoe> shoeList) {
//        BaseShoeRycAdapter adapter = (BaseShoeRycAdapter) history_rv.getAdapter();
//        if (shoeList.size() != 0) {
//            removeLoadMoreItem(adapter, shoeList);
//        } else {
//            removeLoadMoreItem(adapter, null);
//        }
//    }

    @Override
    public void onNullResponse(String tag) throws JSONException {
//        switch (tag) {
//            case HttpTag.HISTORY_GET_HISTORY:
//                onHandleNullHistory();
//                break;
//
//            default:
//                break;
//        }
    }

//    private void onHandleNullHistory() {
//        switch (historyAction.getRequest()) {
//            case BaseAction.REQUEST_DEFAULT:
//                showNetDownPage(R.id.activity_history_layout);
//                break;
//
//            case BaseAction.REQUEST_LOAD_MORE:
//                BaseShoeRycAdapter adapter = (BaseShoeRycAdapter) history_rv.getAdapter();
//                removeLoadMoreItem(adapter, null);
//                break;
//
//            default:
//                break;
//        }
//    }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.history_record_tv:
                HistoryRecord_Frag historyRecord_frag = new HistoryRecord_Frag();
                showFragment(historyRecord_frag, "historyRecord_frag");
                switchStatus(R.id.history_record_tv, R.id.history_image_tv);
                break;

            case R.id.history_image_tv:
                HistoryImage_Frag historyImage_frag = new HistoryImage_Frag();
                showFragment(historyImage_frag, "historyImage_frag");
                switchStatus(R.id.history_image_tv, R.id.history_record_tv);
                break;

            default:
                break;
        }
    }

    private void switchStatus(int checked_resId, int unchecked_resId) {
        try {
            TextView checked_tv = (TextView) findViewById(checked_resId);
            checked_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.dw_color_assist_eclipse_entire_tv));
            checked_tv.setTextColor(getResources().getColor(android.R.color.white));

            TextView unchecked_tv = (TextView) findViewById(unchecked_resId);
            unchecked_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.dw_color_assist_eclipse_empty_tv));
            unchecked_tv.setTextColor(getResources().getColor(R.color.colorAssist));

        } catch (NullPointerException exception) {
            Log.e(getClass().getName(), "找不到对应的资源ID");
        }
    }

    private void showFragment(Base_Frag fragment, String fragment_tag) {
        RelativeLayout child_rl = (RelativeLayout) findViewById(R.id.history_child_rl);
        child_rl.removeAllViews();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.history_child_rl, fragment, fragment_tag);
//        HistoryRecord_Frag historyRecord_frag = new HistoryRecord_Frag();
//        transaction.add(R.id.history_child_rl, historyRecord_frag, "historyrecord_frag");
        transaction.commit();
    }
}
