package wale_tech.tryon.http;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import wale_tech.tryon.R;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicView.ColorSnackBar;
import wale_tech.tryon.sharedinfo.SharedAction;
import wale_tech.tryon.sharedinfo.SharedSet;

/**
 * Created by KeY on 2016/3/29.
 */
public final class HttpAction {

    private Context context;
    private HttpHandler handler;

    private String url;

    private HashMap<String, String> map;

    private RequestQueue queue;

    private ProgressDialog dialog;

    private String tag;

    private ColorSnackBar snackBar;

    public HttpAction(Context context) {
        this.context = context;
        snackBar = new ColorSnackBar(context);
    }

    public void setUrl(String url) {
//        this.url = url;
        this.url = HttpSet.BASE_URL + url;
        Log.i("Result", "url is : " + this.url);
    }

    public void setMap(String[] key, String[] value) {
        map = new HashMap<>();

        if (new SharedAction(context).getAppLanguage().equals(SharedSet.LANGUAGE_CHINESE)) {
            map.put(HttpSet.KEY_LANGUAGE, SharedSet.LANGUAGE_CHINESE);
        } else {
            map.put(HttpSet.KEY_LANGUAGE, SharedSet.LANGUAGE_ENGLISH);
        }

        for (int i = 0; i < key.length; i++) {
            map.put(key[i], value[i]);
        }
        Log.i("Result", "map value is : " + map.get(HttpSet.KEY_LANGUAGE));

    }

    public void setDialog(String title, String msg) {
        dialog = new ProgressDialog(context);

        dialog.setTitle(title);
        dialog.setMessage(msg);

        // 不允许用户点击 dialog 外部从而导致 dialog 消失
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        // 不允许用户点击“返回键”从而导致 dialog 消失
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
            }
        });
    }

    public void setHandler(HttpHandler handler) {
        this.handler = handler;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    // 与网络交互
    public void interaction() {
        if (dialog != null) {
            dialog.show();
        }

//        RequestQueue queue = Volley.newRequestQueue(context);

        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }

        StringRequest request = new StringRequest(Request.Method.POST, url, resListener, errListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };

        // 这条语句意思是让这个傻逼的 volley 能够重连，保持时间为10s，不然TMD服务器还没反应过来呢
        request.setRetryPolicy(new DefaultRetryPolicy(7 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }

    private Response.Listener<String> resListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            if (dialog != null) {
                dialog.dismiss();
            }

            Log.i("Result", "get result is : " + s);

            HashMap<String, String> res_map = new HashMap<>();
            res_map.put(tag, s);

            // 发送id
            Message msg = new Message();
            msg.what = HttpSet.httpResponse;
            msg.obj = res_map;
            handler.sendMessage(msg);

//            queue = null;
        }
    };

    private Response.ErrorListener errListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            // 这个是Volley自带的，例如网络404等等错误都可以在这里处理
            if (dialog != null) {
                dialog.dismiss();
            }

            Message msg = new Message();
            msg.what = HttpSet.httpNull;
            msg.obj = tag;
            handler.sendMessage(msg);

            snackBar.show(context.getString(R.string.base_toast_net_worse));

            queue.stop();
        }
    };

    public static boolean checkNet(Context context) {
        ColorSnackBar snackBar = new ColorSnackBar(context);

        if (!Methods.isNetworkAvailable(context)) {
            snackBar.show(context.getString(R.string.base_toast_net_down));
        }

        return Methods.isNetworkAvailable(context);
    }
}
