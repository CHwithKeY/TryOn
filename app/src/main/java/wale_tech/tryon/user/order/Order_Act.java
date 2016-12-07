package wale_tech.tryon.user.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

import wale_tech.tryon.publicClass.loadMore.LoadMoreTouchListener;
import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.publicAdapter.BaseRycAdapter;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicObject.ObjectOrder;

public class Order_Act extends Base_Act {

    private RecyclerView order_rv;
    private OrderAction orderAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_layout);

        varInit();

        setupToolbar();
    }

    @Override
    public void varInit() {
        orderAction = new OrderAction(this);
        if(orderAction.getOrder(BaseAction.REQUEST_DEFAULT) == BaseAction.ACTION_NET_DOWN) {
            showNetDownPage(R.id.activity_order_layout);
        }
    }


    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupRecyclerView( ArrayList<ObjectOrder> orderList) {
        OrderRycAdapter adapter = new OrderRycAdapter(orderList) {
            @Override
            public void onLoadMore() {
                orderAction.getOrder(BaseAction.REQUEST_LOAD_MORE);
            }
        };

        LinearLayoutManager manager = new LinearLayoutManager(this);

        order_rv = (RecyclerView) findViewById(R.id.order_rv);
        order_rv.setLayoutManager(manager);
        order_rv.setAdapter(adapter);
        order_rv.setOnTouchListener(new LoadMoreTouchListener());
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.ORDER_GET_ORDER:
                onHandleOrder(result);
//                ArrayList<ObjectOrder> orderList = orderAction.handleResponse(result);
//                if (orderList.size() == 0) {
//                    showEmptyPage(R.id.activity_order_layout);
//                } else {
//                    setupRecyclerView(orderList);
//                }
                break;

            default:
                break;
        }
    }

    private void onHandleOrder(String result) throws JSONException {
        ArrayList<ObjectOrder> orderList = orderAction.handleResponse(result);

        switch (orderAction.getRequest()) {
            case BaseAction.REQUEST_DEFAULT:
                handleDefaultList(orderList);
                break;

            case BaseAction.REQUEST_LOAD_MORE:
                handleLoadList(orderList);
                break;

            default:
                break;
        }
    }

    private void handleDefaultList(ArrayList<ObjectOrder> orderList) {
        if (orderList.size() == 0) {
            showEmptyPage(R.id.activity_order_layout);
        }
        setupRecyclerView(orderList);
    }

    private void handleLoadList(ArrayList<ObjectOrder> orderList) {
        OrderRycAdapter adapter = (OrderRycAdapter) order_rv.getAdapter();
        if (orderList.size() != 0) {
            removeLoadMoreItem(adapter, orderList);
        } else {
            removeLoadMoreItem(adapter, null);
        }
    }

    @Override
    public void onNullResponse(String tag) throws JSONException {
        switch (tag) {
            case HttpTag.ORDER_GET_ORDER:
                onHandleNullOrder();
                break;

            default:
                break;
        }

    }

    private void onHandleNullOrder() {
        switch (orderAction.getRequest()) {
            case BaseAction.REQUEST_DEFAULT:
                showNetDownPage(R.id.activity_order_layout);
                break;

            case BaseAction.REQUEST_LOAD_MORE:
                BaseRycAdapter adapter = (BaseRycAdapter) order_rv.getAdapter();
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
