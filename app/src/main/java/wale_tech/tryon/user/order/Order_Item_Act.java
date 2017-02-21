package wale_tech.tryon.user.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import org.json.JSONException;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.publicAdapter.BaseShoeRycAdapter;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicObject.ObjectShoe;
import wale_tech.tryon.publicSet.IntentSet;

public class Order_Item_Act extends Base_Act {

    private OrderItemAction orderItemAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item_layout);

        varInit();

        setupToolbar();

    }

    @Override
    public void varInit() {
        String order_num = getIntent().getStringExtra(IntentSet.KEY_ORDER_NUM);

        orderItemAction = new OrderItemAction(this);
        orderItemAction.getShoeList(order_num);
    }


    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupRecyclerView(ArrayList<ObjectShoe> shoeList) {
        BaseShoeRycAdapter adapter = new OrderItemRycAdapter(shoeList) {
            @Override
            public void onLoadMore() {
            }
        };

        final RecyclerView order_item_rv = (RecyclerView) findViewById(R.id.order_item_rv);
        order_item_rv.setLayoutManager(new LinearLayoutManager(this));
        order_item_rv.setAdapter(adapter);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        ArrayList<ObjectShoe> shoeList = orderItemAction.handleResponse(result);
        setupRecyclerView(shoeList);
    }

    @Override
    public void onNullResponse(String tag) throws JSONException {
        switch (tag) {
            case HttpTag.ORDER_GET_ORDER_ITEM:
                showNetDownPage(R.id.activity_order_item_layout);
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
