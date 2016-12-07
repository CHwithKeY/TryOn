package wale_tech.tryon.user.favourite;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

import wale_tech.tryon.publicClass.loadMore.LoadMoreTouchListener;
import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.publicAdapter.BaseRycAdapter;
import wale_tech.tryon.publicAdapter.BaseShoeRycAdapter;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicObject.ObjectShoe;

public class Favourite_Act extends Base_Act {

    private FavAction favAction;
    private RecyclerView favourite_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_layout);

        varInit();

        setupToolbar();
    }

    @Override
    public void varInit() {
        favAction = new FavAction(this);
        if (favAction.getFavourite(BaseAction.REQUEST_DEFAULT) == BaseAction.ACTION_NET_DOWN) {
            showNetDownPage(R.id.activity_favourite_layout);
        }
    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupRecyclerView(ArrayList<ObjectShoe> shoeList) {
        BaseRycAdapter adapter = new FavouriteShoeRycAdapter(shoeList) {
            @Override
            public void onLoadMore() {
                favAction.getFavourite(BaseAction.REQUEST_LOAD_MORE);
            }
        };

        LinearLayoutManager manager = new LinearLayoutManager(this);
        favourite_rv = (RecyclerView) findViewById(R.id.favourite_rv);
        favourite_rv.setLayoutManager(manager);
        favourite_rv.setAdapter(adapter);
        favourite_rv.setOnTouchListener(new LoadMoreTouchListener());
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.FAVOURITE_GET_FAVOURITE:
                onHandleFavourite(result);
                break;

            default:
                break;
        }
    }

    private void onHandleFavourite(String result) throws JSONException {
        ArrayList<ObjectShoe> shoeList = favAction.handleListResponse(result);

        switch (favAction.getRequest()) {
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
            showEmptyPage(R.id.activity_favourite_layout);
        } else {
            setupRecyclerView(shoeList);
        }
    }

    private void handleLoadList(ArrayList<ObjectShoe> shoeList) {
        BaseShoeRycAdapter adapter = (BaseShoeRycAdapter) favourite_rv.getAdapter();

        if (shoeList.size() != 0) {
            // adapter.getDataList().addAll(shoeList);
            // adapter.notifyDataSetChanged();
            removeLoadMoreItem(adapter, shoeList);
        } else {
            removeLoadMoreItem(adapter, null);
        }
    }


    @Override
    public void onNullResponse(String tag) throws JSONException {
        switch (tag) {
            case HttpTag.FAVOURITE_GET_FAVOURITE:
                // showNetDownPage(R.id.activity_favourite_layout);
                onHandleNullFavourite();
                break;

            default:
                break;
        }
    }

    private void onHandleNullFavourite() {
        switch (favAction.getRequest()) {
            case BaseAction.REQUEST_DEFAULT:
                showNetDownPage(R.id.activity_favourite_layout);
                break;

            case BaseAction.REQUEST_LOAD_MORE:
                BaseShoeRycAdapter adapter = (BaseShoeRycAdapter) favourite_rv.getAdapter();
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
