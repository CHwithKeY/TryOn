package wale_tech.tryon.result;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import org.json.JSONException;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicAdapter.BaseRycAdapter;
import wale_tech.tryon.publicAdapter.BaseShoeRycAdapter;
import wale_tech.tryon.publicAdapter.GeneralShoeRycAdapter;
import wale_tech.tryon.publicClass.loadMore.LoadMoreTouchListener;
import wale_tech.tryon.publicObject.ObjectShoe;
import wale_tech.tryon.publicSet.FilterSet;
import wale_tech.tryon.publicView.EmptyRecyclerView;

public class Result_Act extends Base_Act implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private ResultAction resultAction;
    private EmptyRecyclerView result_rv;

    private boolean init;

    private ArrayList<String> brandList;
    private ArrayList<String> colorList;
    private ArrayList<String> sizeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_layout);

        varInit();

        setupToolbar();

        setupFilterNav();

        setupBackTopBtn();

    }

    @Override
    public void varInit() {
        init = true;

        resultAction = new ResultAction(this);
        if (resultAction.getFilter(BaseAction.REQUEST_DEFAULT, null, null) == BaseAction.ACTION_NET_DOWN) {
            showNetDownPage(R.id.activity_result_layout);
        }

        brandList = new ArrayList<>();
        colorList = new ArrayList<>();
        sizeList = new ArrayList<>();
    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupRecyclerView(ArrayList<ObjectShoe> shoeList) {
        BaseRycAdapter adapter = new GeneralShoeRycAdapter(shoeList) {
            @Override
            public void onLoadMore() {
                resultAction.getFilter(BaseAction.REQUEST_LOAD_MORE, null, null);
            }
        };

        LinearLayoutManager manager = new LinearLayoutManager(this);
        result_rv = (EmptyRecyclerView) findViewById(R.id.result_rv);
        result_rv.setLayoutManager(manager);
        result_rv.setAdapter(adapter);
        result_rv.setEmptyView(View.inflate(this, R.layout.fragment_public_empty_page_layout, null));
        result_rv.setOnTouchListener(new LoadMoreTouchListener());
    }

    private void setupFilterNav() {
        final FrameLayout filter_fl = (FrameLayout) findViewById(R.id.result_call_filter_fl);
        filter_fl.setOnClickListener(this);
    }

    private void setupBackTopBtn() {
        final ImageButton bt_imgbtn = (ImageButton) findViewById(R.id.result_back_top_imgbtn);
        bt_imgbtn.setOnClickListener(this);
    }

    private void setupFilterBar() {
        brandList.add("所有");
        colorList.add("所有");
        sizeList.add("所有");

        final AppCompatSpinner brand_spn = (AppCompatSpinner) findViewById(R.id.result_brand_spn);
        final AppCompatSpinner color_spn = (AppCompatSpinner) findViewById(R.id.result_color_spn);
        final AppCompatSpinner size_spn = (AppCompatSpinner) findViewById(R.id.result_size_spn);

        ArrayAdapter<String> brand_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, brandList);
        ArrayAdapter<String> color_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, colorList);
        ArrayAdapter<String> size_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sizeList);

        brand_spn.setAdapter(brand_adapter);
        color_spn.setAdapter(color_adapter);
        size_spn.setAdapter(size_adapter);

        brand_spn.setTag(ResultAction.FILTER_TYPE_BRAND);
        color_spn.setTag(ResultAction.FILTER_TYPE_COLOR);
        size_spn.setTag(ResultAction.FILTER_TYPE_SIZE);

        brand_spn.setOnItemSelectedListener(this);
        color_spn.setOnItemSelectedListener(this);
        size_spn.setOnItemSelectedListener(this);

        brand_spn.setSelection(brandList.size() - 1);
        color_spn.setSelection(colorList.size() - 1);
        size_spn.setSelection(sizeList.size() - 1);
    }


    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.RESULT_FILTER:
                onHandleResult(result);
                if (init) {
                    resultAction.getFilterOption(FilterSet.TABLE_WAREHOUSE, FilterSet.OPTION_BRAND);
                }
                break;

            case HttpTag.RESULT_FILTER_BRAND:
                onHandleBrand(result);
                resultAction.getFilterOption(FilterSet.TABLE_WAREHOUSE, FilterSet.OPTION_COLOR);
                break;

            case HttpTag.RESULT_FILTER_COLOR:
                onHandleColor(result);
                resultAction.getFilterOption(FilterSet.TABLE_WAREHOUSE, FilterSet.OPTION_SIZE);
                break;

            case HttpTag.RESULT_FILTER_SIZE:
                onHandleSize(result);
                setupFilterBar();
                break;

            default:
                break;
        }
    }

    private void onHandleBrand(String result) throws JSONException {
        brandList = resultAction.handleBrandResponse(result);
    }

    private void onHandleColor(String result) throws JSONException {
        colorList = resultAction.handleColorResponse(result);
    }

    private void onHandleSize(String result) throws JSONException {
        sizeList = resultAction.handleSizeResponse(result);
    }

    private void onHandleResult(String result) throws JSONException {
        ArrayList<ObjectShoe> shoeList = resultAction.handleListResponse(result);

        switch (resultAction.getRequest()) {
            case BaseAction.REQUEST_DEFAULT:
                handleDefaultList(shoeList);
                break;

            case BaseAction.REQUEST_FILTER:
                handleFilter(shoeList);
                break;

            case BaseAction.REQUEST_LOAD_MORE:
                handleLoadList(shoeList);
                break;

            default:
                break;
        }
    }

    private void handleDefaultList(ArrayList<ObjectShoe> shoeList) {
//        if (shoeList.size() == 0) {
//            showEmptyPage(R.id.activity_result_layout);
//        }
        setupRecyclerView(shoeList);
    }

    private void handleFilter(ArrayList<ObjectShoe> shoeList) {
//        if (shoeList.size() == 0) {
//            showEmptyPage(R.id.activity_result_layout);
//        }
        setupRecyclerView(shoeList);
    }

    private void handleLoadList(ArrayList<ObjectShoe> shoeList) {
        BaseShoeRycAdapter adapter = (BaseShoeRycAdapter) result_rv.getAdapter();

        if (shoeList.size() != 0) {
            removeLoadMoreItem(adapter, shoeList);
        } else {
            removeLoadMoreItem(adapter, null);
        }
    }


    @Override
    public void onNullResponse(String tag) throws JSONException {
        switch (tag) {
            case HttpTag.FAVOURITE_GET_FAVOURITE:
                // showNetDownPage(R.id.activity_result_layout);
                onHandleNullResult();
                break;

            default:
                break;
        }
    }

    private void onHandleNullResult() {
        switch (resultAction.getRequest()) {
            case BaseAction.REQUEST_DEFAULT:
                showNetDownPage(R.id.activity_result_layout);
                break;

            case BaseAction.REQUEST_LOAD_MORE:
                BaseShoeRycAdapter adapter = (BaseShoeRycAdapter) result_rv.getAdapter();
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i("Result", "type is : " + adapterView.getTag().toString());
        String filter_type = adapterView.getTag().toString();
        String filter_content = adapterView.getItemAtPosition(i).toString();

        if (filter_content.equals("所有")) {
            filter_content = "All";
        }

        if (init) {
            init = false;
        } else {
            resultAction.getFilter(BaseAction.REQUEST_FILTER, filter_content, filter_type);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.result_call_filter_fl:
                onCallFilterNav();
                break;

            case R.id.result_back_top_imgbtn:
                onBackTop();
                break;

            default:
                break;
        }
    }

    private void onCallFilterNav() {
        final DrawerLayout filter_dl = (DrawerLayout) findViewById(R.id.result_filter_dl);
        if (!filter_dl.isDrawerOpen(GravityCompat.END)) {
            filter_dl.openDrawer(GravityCompat.END);
        }
    }

    private void onBackTop() {
        result_rv.smoothScrollToPosition(0);
    }
}
