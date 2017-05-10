package wale_tech.tryon.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.Base_Frag;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicAdapter.BaseRycAdapter;
import wale_tech.tryon.publicAdapter.ViewTitleAdapter;
import wale_tech.tryon.publicClass.DatePicker;
import wale_tech.tryon.publicSet.BundleSet;

/**
 * Created by lenovo on 2017/2/21.
 */

public class HistoryImage_Frag extends Base_Frag {

//    private HistoryAction historyAction;
//    private RecyclerView history_image_rv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_image_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        varInit();

        setupViewPager();
    }

    private void varInit() {
//        historyAction = new HistoryAction(getContext(), this);
//        if (BaseAction.ACTION_NET_DOWN == historyAction.getHistoryImage(BaseAction.REQUEST_DEFAULT)) {
//            showNetDownPage(R.id.fragment_history_image_layout);
//        }
    }

    private void setupViewPager() {
        if (getView() != null) {
            final ViewPager history_image_vp = (ViewPager) getView().findViewById(R.id.history_image_vp);

            List<Fragment> fragments = new ArrayList<>();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            String yesterday = format.format(DatePicker.getYesterday(new Date()));
            String today = format.format(DatePicker.getToday(new Date()));
            String tomorrow = format.format(DatePicker.getTomorrow(new Date()));

            String[] date = {yesterday, today, tomorrow};

            for (int i = 0; i < 3; i++) {
                HistoryImage_Image_Frag image_frag = new HistoryImage_Image_Frag();
                fragments.add(image_frag);

                Bundle bundle = new Bundle();
                bundle.putString(BundleSet.KEY_DATE_TIME, date[i]);
                image_frag.setArguments(bundle);
            }
//            SmarkEdit_Frag tab01 = new SmarkEdit_Frag();
//            SmarkAction_Frag tab02 = new SmarkAction_Frag();
//            SmarkTask_Frag tab03 = new SmarkTask_Frag();
//
//            fragments.add(tab01);
//            fragments.add(tab02);
//            fragments.add(tab03);

            String[] titles = {
                    yesterday,
                    today,
                    tomorrow};

            ViewTitleAdapter adapter = new ViewTitleAdapter(getChildFragmentManager());
            adapter.setFragments(fragments);
            adapter.setTitles(titles);

            history_image_vp.setAdapter(adapter);
            history_image_vp.setOffscreenPageLimit(fragments.size());
            history_image_vp.setCurrentItem(1);

            setupTabLayout(history_image_vp);
        }
    }

    private void setupTabLayout(ViewPager viewPager) {
        if (getView() != null) {
            final TabLayout image_info_tab = (TabLayout) getView().findViewById(R.id.history_image_tab);
            image_info_tab.setupWithViewPager(viewPager);
            // 设置tab的文字，在被选中后和没被选中的时候，分别显示的颜色
            image_info_tab.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));
            image_info_tab.setTabTextColors(getResources().getColor(android.R.color.darker_gray), getResources().getColor(android.R.color.white));
            image_info_tab.setSelectedTabIndicatorHeight(7);
        }
    }

//    private void setupRecyclerView(ArrayList<String> imageList) {
//        if (getView() != null) {
//            BaseRycAdapter adapter = new HistoryImageRycAdapter(imageList) {
//                @Override
//                public void onLoadMore() {
//                }
//            };
//
//            history_image_rv = (RecyclerView) getView().findViewById(R.id.history_image_rv);
//            history_image_rv.setLayoutManager(new GridLayoutManager(getContext(), 3));
//            history_image_rv.setAdapter(adapter);
//        } else {
//            Log.e(getClass().getName(), "该 fragment 的父 view 的值为空");
//        }
//    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
//        switch (tag) {
//            case HttpTag.HISTORY_GET_IMAGE:
//                onHandleHistory(result);
//                break;
//
//            default:
//                break;
//        }
    }

//    private void onHandleHistory(String result) throws JSONException {
//        ArrayList<String> imageList = historyAction.handleImageResponse(result);
//
//        switch (historyAction.getRequest()) {
//            case BaseAction.REQUEST_DEFAULT:
//                handleDefaultList(imageList);
//                break;
//
//            case BaseAction.REQUEST_LOAD_MORE:
//                handleLoadList(imageList);
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    private void handleDefaultList(ArrayList<String> imageList) {
//        if (imageList.size() == 0) {
//            showEmptyPage(R.id.fragment_history_image_layout);
//        }
//        setupRecyclerView(imageList);
//    }
//
//    private void handleLoadList(ArrayList<String> imageList) {
//        BaseRycAdapter adapter = (BaseRycAdapter) history_image_rv.getAdapter();
//        if (imageList.size() != 0) {
//            removeLoadMoreItem(adapter, imageList);
//        } else {
//            removeLoadMoreItem(adapter, null);
//        }
//    }

    @Override
    public void onNullResponse(String tag) throws JSONException {
//        switch (tag) {
//            case HttpTag.HISTORY_GET_IMAGE:
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
//                showNetDownPage(R.id.fragment_history_image_layout);
//                break;
//
//            case BaseAction.REQUEST_LOAD_MORE:
//                BaseRycAdapter adapter = (BaseRycAdapter) history_image_rv.getAdapter();
//                removeLoadMoreItem(adapter, null);
//                break;
//
//            default:
//                break;
//        }
//    }
}
