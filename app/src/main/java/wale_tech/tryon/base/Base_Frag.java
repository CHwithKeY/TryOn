package wale_tech.tryon.base;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

import wale_tech.tryon.emptyPage.EmptyPage_Frag;
import wale_tech.tryon.netDown.NetDown_Frag;
import wale_tech.tryon.publicAdapter.BaseRycAdapter;

/**
 * Created by KeY on 2016/6/6.
 */
public abstract class Base_Frag extends Fragment {

    protected String tag;

    // 根据不同的Handler可以处理不同的反馈结果，最强大
    public abstract void onMultiHandleResponse(String tag, String result) throws JSONException;

    public abstract void onNullResponse(String tag) throws JSONException;

    public void setCustomTag(String tag) {
        this.tag = tag;
    }

    public String getCustomTag() {
        return tag;
    }

    public void showEmptyPage(int parent_resId) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        try {
            transaction.add(parent_resId, new EmptyPage_Frag(), "emptyPage_Frag");
        } catch (Exception exception) {
            Log.e(getClass().getName(), "空数据 EmptyPage_Frag 导入的父布局的id错误或不存在");
        }
        transaction.commit();
    }

    public void showNetDownPage(int parent_resId) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        try {
            transaction.add(parent_resId, new NetDown_Frag(), "netDown_Frag");
        } catch (Exception exception) {
            Log.e(getClass().getName(), "空数据 EmptyPage_Frag 导入的父布局的id错误或不存在");
        }
        transaction.commit();
    }

    public void removeLoadMoreItem(final BaseRycAdapter adapter, final ArrayList newDataList) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.setShowLoadItem(false);
                ArrayList oldDataList = adapter.getDataList();

                if (oldDataList != null && oldDataList.size() != 0 && oldDataList.get(oldDataList.size() - 1) == null) {
                    oldDataList.remove(oldDataList.size() - 1);
                }

                if (newDataList != null) {
                    oldDataList.addAll(newDataList);
                }

                adapter.notifyDataSetChanged();
            }
        }, 1500);
    }
}
