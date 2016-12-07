package wale_tech.tryon.emptyPage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;

import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Frag;

/**
 * Created by lenovo on 2016/11/10.
 */

public class EmptyPage_Frag extends Base_Frag {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_public_empty_page_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {

    }

    @Override
    public void onNullResponse(String tag) throws JSONException {

    }

    @Override
    public void setCustomTag(String tag) {

    }

    @Override
    public String getCustomTag() {
        return null;
    }
}
