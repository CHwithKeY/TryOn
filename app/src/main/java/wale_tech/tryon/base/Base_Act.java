package wale_tech.tryon.base;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import wale_tech.tryon.publicClass.loadMore.RemoveLoadMoreImpl;
import wale_tech.tryon.emptyPage.EmptyPage_Frag;
import wale_tech.tryon.netDown.NetDown_Frag;
import wale_tech.tryon.R;
import wale_tech.tryon.publicAdapter.BaseRycAdapter;
import wale_tech.tryon.sharedinfo.SharedAction;
import wale_tech.tryon.sharedinfo.SharedSet;

public abstract class Base_Act extends AppCompatActivity implements RemoveLoadMoreImpl {

    protected Toolbar toolbar;

    protected SharedAction sharedAction;
    private SharedPreferences sp;

    private Snackbar snackbar;
    // cos = Count of SnackBar
    private int cos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences(SharedSet.NAME, MODE_PRIVATE);
        sharedAction = new SharedAction();
        sharedAction.setShared(sp);
    }

    public abstract void varInit();

    protected abstract void setupToolbar();

    private void tbInit() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_tb);
        // title_tv = (TextView) findViewById(R.id.toolbar_tv);

        toolbar.setTitle("");

        setSupportActionBar(toolbar);
    }

    /**
     * 重载了setTbTitle方法，和上一个方法不一样的地方在于，这个方法初始化了toolbar
     * 如果要单独设置setTbTitle，那就要先调用tbInit()
     *
     * @param title 要显示的标题文字
     */
    protected void setTbTitle(String title) {
        tbInit();
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAssist));
        // title_tv.setText(title);
    }

    protected void setTbNavigation() {
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_nav_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void setBackBtn() {
        try {
            ImageButton back_btn = (ImageButton) findViewById(R.id.public_back_imgbtn);
            back_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception exception) {
            Log.e(getClass().getName(), "没有引入返回键 button 布局");
        }
    }

    public void showSnack(int parent_resId, CharSequence text) {
        if (parent_resId == 0) {
            parent_resId = R.id.snack_col;
        }

        CoordinatorLayout snack_col = (CoordinatorLayout) findViewById(parent_resId);

        if (snackbar != null) {
            snackbar = null;
            cos++;
            if (cos > 5) {
                System.gc();
                cos = 0;
            }
        }

        try {
            snackbar = Snackbar.make(snack_col, text, Snackbar.LENGTH_SHORT);
        } catch (NullPointerException exception) {
            Log.e(getClass().getName(), getString(R.string.error_snackbar_can_not_find_coordinatorLayout));
            return;
        }

        Snackbar.SnackbarLayout snackbarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarView.setBackgroundColor(0xFF7EC8DB);
        ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.parseColor("#FF2E4F92"));

        snackbar.show();
    }

    public void showNetDownPage(int parent_resId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        try {
            transaction.add(parent_resId, new NetDown_Frag(), "netdown_frag");
        } catch (Exception exception) {
            Log.e(getClass().getName(), "断网 NetDown_Frag 导入的父布局的id错误或不存在");
        }
        transaction.commit();
    }

    public void showEmptyPage(int parent_resId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        try {
            transaction.add(parent_resId, new EmptyPage_Frag(), "emptypage_frag");
        } catch (Exception exception) {
            Log.e(getClass().getName(), "空数据 EmptyPage_Frag 导入的父布局的id错误或不存在");
        }
        transaction.commit();
    }

    // 根据不同的Handler可以处理不同的反馈结果，最强大
    public abstract void onMultiHandleResponse(String tag, String result) throws JSONException;

    public abstract void onNullResponse(String tag) throws JSONException;

    public abstract void onPermissionAccepted(int permission_code);

    public abstract void onPermissionRefused(int permission_code);

    @Override
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
