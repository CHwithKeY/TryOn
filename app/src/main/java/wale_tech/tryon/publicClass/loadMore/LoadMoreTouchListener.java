package wale_tech.tryon.publicClass.loadMore;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import wale_tech.tryon.publicAdapter.BaseRycAdapter;
import wale_tech.tryon.publicClass.ScreenSize;

/**
 * Created by lenovo on 2016/11/14.
 */

public class LoadMoreTouchListener implements View.OnTouchListener {

    private float downY = 0;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        RecyclerView recyclerView = (RecyclerView) view;

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = motionEvent.getY();
                break;

            case MotionEvent.ACTION_UP:
                float upY = motionEvent.getY();
                BaseRycAdapter adapter = (BaseRycAdapter) recyclerView.getAdapter();

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                ArrayList dataList = adapter.getDataList();

                if (dataList.size() == 0 || dataList.size() == 1) {
                    return false;
                }
                int recyclerViewHeight = recyclerView.getHeight();
                int screenHeight = new ScreenSize(recyclerView.getContext()).getHeight();

                int itemHeight = recyclerViewHeight / (manager.findLastCompletelyVisibleItemPosition() - manager.findFirstCompletelyVisibleItemPosition());
                int totalHeight = itemHeight * recyclerView.getChildCount();

                if ((downY - upY > 200)
                        && (!adapter.isShowLoadItem())
                        && (manager.findLastCompletelyVisibleItemPosition() == dataList.size() - 1)
                        && (totalHeight > screenHeight)) {
                    dataList.add(null);
                    adapter.notifyDataSetChanged();
                    adapter.setShowLoadItem(true);
                }

                break;
            default:
                break;
        }
        return false;
    }

}
