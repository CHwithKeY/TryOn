package wale_tech.tryon;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by KeY on 2016/12/8.
 */

public class VolleyInitApplication extends Application {

    private static RequestQueue queue;

    public RequestQueue createQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(getApplicationContext());
        }

        return queue;
    }

}
