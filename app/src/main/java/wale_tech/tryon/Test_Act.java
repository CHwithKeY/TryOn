package wale_tech.tryon;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.json.JSONException;

import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.publicView.ColorSnackBar;

public class Test_Act extends Base_Act {

    private ColorSnackBar snackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_layout);

        varInit();

        setupSnackBtn();
    }

    @Override
    public void varInit() {
        snackBar = new ColorSnackBar(this);
    }

    private void setupSnackBtn() {
        final Button snack_btn = (Button) findViewById(R.id.test_btn);
        snack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.show("Success");
//                showSnack("Success");
            }
        });
    }

    @Override
    protected void setupToolbar() {

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

    private ProgressBar createProgressBar(Drawable customIndeterminateDrawable) {
        // activity根部的ViewGroup，其实是一个FrameLayout
        ContentFrameLayout rootContainer = (ContentFrameLayout) findViewById(android.R.id.content);
        // 给progressbar准备一个FrameLayout的LayoutParams
        ContentFrameLayout.LayoutParams lp = new ContentFrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置对其方式为：屏幕居中对其
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.GONE);
        progressBar.setLayoutParams(lp);
        // 自定义小菊花
        if (customIndeterminateDrawable != null) {
            progressBar.setIndeterminateDrawable(customIndeterminateDrawable);
        }
        // 将菊花添加到FrameLayout中
        rootContainer.addView(progressBar);

        Log.i("Result", "create");

        return progressBar;
    }
}
