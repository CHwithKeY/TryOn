package wale_tech.tryon.user.cart;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import wale_tech.tryon.publicClass.loadMore.LoadMoreTouchListener;
import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.publicAdapter.BaseRycAdapter;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicObject.ObjectShoe;
import wale_tech.tryon.publicSet.MapSet;

public class Cart_Act extends Base_Act {

    private RecyclerView cart_rv;

    private CartAction cartAction;
    private ClickListener clickListener;

    public static Activity cart_act;
//    private ArrayList<ObjectShoe> shoeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_layout);

        varInit();

        setupToolbar();
    }

    @Override
    public void varInit() {
        cart_act = this;
        cartAction = new CartAction(this);
        if(cartAction.getCart(BaseAction.REQUEST_DEFAULT) == BaseAction.ACTION_NET_DOWN){
            showNetDownPage(R.id.activity_cart_layout);
        }

        clickListener = new ClickListener(this, cartAction);
    }


    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupRecyclerView(ArrayList<ObjectShoe> shoeList) {
        CartRycAdapter adapter = new CartRycAdapter(shoeList) {
            @Override
            public void onLoadMore() {
                cartAction.getCart(BaseAction.REQUEST_LOAD_MORE);
            }
        };
        adapter.setCartAction(cartAction);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        cart_rv = (RecyclerView) findViewById(R.id.cart_rv);
        cart_rv.setLayoutManager(manager);
        cart_rv.setAdapter(adapter);
        cart_rv.setOnTouchListener(new LoadMoreTouchListener());

        setupBuyBar(shoeList);
    }

    private void setupBuyBar(ArrayList<ObjectShoe> shoeList) {
        CartRycAdapter adapter = (CartRycAdapter) cart_rv.getAdapter();
        ArrayList<String> checkList = adapter.getCheckList();

        HashMap<String, Object> map = new HashMap<>();
        map.put(MapSet.KEY_SHOE_LIST, shoeList);
        map.put(MapSet.KEY_CHECK_LIST, checkList);

        final TextView price_tv = (TextView) findViewById(R.id.rv_price_tv);
        price_tv.setText(getString(R.string.cart_whole_price_tv));

        final TextView make_order_tv = (TextView) findViewById(R.id.rv_make_order_tv);
        make_order_tv.setTag(map);
        make_order_tv.setOnClickListener(clickListener);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.CART_GET_CART:
                onHandleCart(result);
                break;

            case HttpTag.CART_UPDATE_CART:
                cartAction.handleResponse(result);
                cartAction.getCart(BaseAction.REQUEST_DEFAULT);
                break;

            default:
                break;
        }
    }

    private void onHandleCart(String result) throws JSONException {
        ArrayList<ObjectShoe> shoeList = cartAction.handleListResponse(result);

        switch (cartAction.getRequest()) {
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
            showEmptyPage(R.id.activity_cart_layout);
        }
        setupRecyclerView(shoeList);
    }

    private void handleLoadList(ArrayList<ObjectShoe> shoeList) {
        CartRycAdapter adapter = (CartRycAdapter) cart_rv.getAdapter();
        if (shoeList.size() != 0) {
            removeLoadMoreItem(adapter, shoeList);
        } else {
            removeLoadMoreItem(adapter, null);
        }
    }

    @Override
    public void onNullResponse(String tag) throws JSONException {
        switch (tag) {
            case HttpTag.CART_GET_CART:
                onHandleNullCart();
                break;

            default:
                break;
        }
    }

    private void onHandleNullCart() {
        switch (cartAction.getRequest()) {
            case BaseAction.REQUEST_DEFAULT:
                showNetDownPage(R.id.activity_cart_layout);
                break;

            case BaseAction.REQUEST_LOAD_MORE:
                BaseRycAdapter adapter = (BaseRycAdapter) cart_rv.getAdapter();
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

    private void onDeleteItem(CartRycAdapter adapter) {
        ArrayList<String> checkList = adapter.getCheckList();
        ArrayList<ObjectShoe> shoeList = adapter.getDataList();

        if (checkList == null || checkList.size() == 0) {
            showSnack(0, getString(R.string.base_snack_no_item_been_selected));
        } else {
            StringBuilder sku_code_builder = new StringBuilder("");

            for (int i = 0; i < checkList.size(); i++) {
                ObjectShoe shoe = shoeList.get(i);

                if (i == (checkList.size() - 1)) {
                    sku_code_builder.append(shoe.getRemark());
                } else {
                    sku_code_builder.append(shoe.getRemark()).append("-");
                }
            }
            cartAction.operate(CartAction.OPERATION_DELETE_BATCH, sku_code_builder.toString());
        }
    }

    public void onModifyPrice(CartRycAdapter adapter) {
        int whole_price = 0;

        ArrayList<String> checkList = adapter.getCheckList();
        ArrayList<ObjectShoe> shoeList = adapter.getDataList();

        for (int i = 0; i < checkList.size(); i++) {
            int position = Integer.parseInt(checkList.get(i));
            ObjectShoe shoe = shoeList.get(position);

            int price = Integer.parseInt(shoe.getPrice()) * Integer.parseInt(shoe.getCount());
            whole_price = whole_price + price;
        }

        final TextView whole_price_tv = (TextView) findViewById(R.id.rv_whole_price_tv);
        String whole_price_str = getString(R.string.cart_whole_price_tv) + whole_price + " " + getString(R.string.cart_rmb_tv);
        whole_price_tv.setText(whole_price_str);
    }

    public void onMenuItemClick(MenuItem item) {

        final CartRycAdapter adapter = (CartRycAdapter) cart_rv.getAdapter();
//                new AlertDialog.Builder(this)
//                        .setTitle(getString(R.string.base_dialog_delete_title))
//                        .setMessage(getString(R.string.base_dialog_delete_message))
//                        .setPositiveButton(getString(R.string.base_dialog_btn_yes), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                onDeleteItem(adapter);
//                            }
//                        })
//                        .setNegativeButton(getString(R.string.base_dialog_btn_no), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .show();

//                if (item.getTitle().equals(getString(R.string.cart_menu_item_finish))) {
//                    item.setTitle(getString(R.string.cart_menu_item_edit));
//                    adapter.setIsEditMode(false);
//
//                    cartAction.getCart();
//                } else {
//                    item.setTitle(getString(R.string.cart_menu_item_finish));
//                    adapter.setIsEditMode(true);
//                }
//
//                for (int i = 0; i < adapter.getItemCount(); i++) {
//                    adapter.notifyItemChanged(i);
//                }

    }

}
