package wale_tech.tryon.history;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.base.Base_Frag;

public class HistoryImage_Act extends Base_Act implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_image_layout);

        varInit();

        setupToolbar();

        setupTitleText();
    }

    @Override
    public void varInit() {
        HistoryImage_Online_Frag online_frag = new HistoryImage_Online_Frag();
        showFragment(online_frag, "online_frag");
    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
    }

    private void setupTitleText() {
        final TextView history_record_tv = (TextView) findViewById(R.id.history_image_online_tv);
        final TextView history_image_tv = (TextView) findViewById(R.id.history_image_local_tv);

        history_record_tv.setOnClickListener(this);
        history_image_tv.setOnClickListener(this);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {

    }

    @Override
    public void onNullResponse(String tag) throws JSONException {

    }

    @Override
    public void onPermissionAccepted(int permission_code) {

    }

    @Override
    public void onPermissionRefused(int permission_code) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.history_image_online_tv:
                HistoryImage_Online_Frag online_frag = new HistoryImage_Online_Frag();
                showFragment(online_frag, "online_frag");
                switchStatus(R.id.history_image_online_tv, R.id.history_image_local_tv);
                break;

            case R.id.history_image_local_tv:
                HistoryImage_Local_Frag local_frag = new HistoryImage_Local_Frag();
                showFragment(local_frag, "local_frag");
                switchStatus(R.id.history_image_local_tv, R.id.history_image_online_tv);
                break;

            default:
                break;
        }
    }

    private void switchStatus(int checked_resId, int unchecked_resId) {
        try {
            TextView checked_tv = (TextView) findViewById(checked_resId);
            checked_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.dw_color_assist_eclipse_entire_tv));
            checked_tv.setTextColor(getResources().getColor(android.R.color.white));

            TextView unchecked_tv = (TextView) findViewById(unchecked_resId);
            unchecked_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.dw_color_assist_eclipse_empty_tv));
            unchecked_tv.setTextColor(getResources().getColor(R.color.colorAssist));

        } catch (NullPointerException exception) {
            Log.e(getClass().getName(), "找不到对应的资源ID");
        }
    }

    private void showFragment(Base_Frag fragment, String fragment_tag) {
        RelativeLayout child_rl = (RelativeLayout) findViewById(R.id.history_image_child_rl);
        child_rl.removeAllViews();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.history_image_child_rl, fragment, fragment_tag);
        transaction.commit();
    }
}
