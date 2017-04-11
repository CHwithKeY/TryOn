package wale_tech.tryon.publicView;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import wale_tech.tryon.R;

/**
 * Created by KeY on 2016/10/12.
 */
public class ColorSnackBar {

    private Context context;

    private Snackbar snackbar;

    private int cos = 0;

//    public ColorSnackBar(Context context, int parent_resId) {
//        this.context = context;
//        if (parent_resId == 0) {
//            snack_col = (CoordinatorLayout) ((AppCompatActivity) context).findViewById(R.id.snack_col);
//        } else {
//            snack_col = (CoordinatorLayout) ((AppCompatActivity) context).findViewById(parent_resId);
//        }
//    }

    public ColorSnackBar(Context context) {
        this.context = context;
    }

    public void show(String text) {
        CoordinatorLayout snack_col = (CoordinatorLayout) ((AppCompatActivity) context).findViewById(R.id.snack_col);

        if (snack_col == null) {
            Log.i("Result", "snack col null");
        }

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
            Log.e(getClass().getName(), context.getString(R.string.error_snackbar_can_not_find_coordinatorLayout));
            return;
        }

        Snackbar.SnackbarLayout snackbarView = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbarView.setBackgroundColor(0xFF7EC8DB);
        ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.parseColor("#FF2E4F92"));

        snackbar.show();
    }

//    public void showCoordinate(int resId, String text) {
//        CoordinatorLayout snack_col = (CoordinatorLayout) ((AppCompatActivity) context).findViewById(resId);
//
//        if (snackbar != null) {
//            snackbar = null;
//            cos++;
//            if (cos > 5) {
//                System.gc();
//                cos = 0;
//            }
//        }
//
//        try {
//            snackbar = Snackbar.make(snack_col, text, Snackbar.LENGTH_SHORT);
//        } catch (NullPointerException exception) {
//            Log.e(getClass().getName(), "SnackBar 引入自定义 Coordinator 布局错误");
//            return;
//        }
//
//        Snackbar.SnackbarLayout snackbarView = (Snackbar.SnackbarLayout) snackbar.getView();
//        snackbarView.setBackgroundColor(0xFF7EC8DB);
//        ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.parseColor("#FF2E4F92"));
//
//        snackbar.show();
//    }

}
