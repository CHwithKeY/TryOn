package wale_tech.tryon.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import org.json.JSONException;

import java.io.Serializable;

import wale_tech.tryon.emptyPage.EmptyPage_Frag;

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
            transaction.add(parent_resId, new EmptyPage_Frag(), "emptypage_frag");
        } catch (Exception exception) {
            Log.e(getClass().getName(), "空数据 EmptyPage_Frag 导入的父布局的id错误或不存在");
        }
        transaction.commit();
    }
}
