package wale_tech.tryon;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by KeY on 2016/12/8.
 */

public class VolleySingleton extends Application {

    private static VolleySingleton instance;
    private RequestQueue mRequestQueue;
    //    private ImageLoader mImageLoader;
    private static Context context;

    private VolleySingleton(Context context) {
        VolleySingleton.context = context;
        mRequestQueue = getRequestQueue();

//        mImageLoader = new ImageLoader(mRequestQueue,n
//        new ImageLoader.ImageCache(){
//            private final LruCache<String,Bitmap>(20)
//            cache = new LruCache<String,Bitmap>(20);
//
//            @Override
//            public Bitmap getBitmap(String url){
//                return cache.get(url)；
//            }
//            @Override
//            public void putBitmap(String url,Bitmap bitmap){
//                cache.put(url,bitmap);
//            }
//        });
    }

    //异步获取单实例
    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            //getApplication()方法返回一个当前进程的全局应用上下文，这就意味着
            //它的使用情景为：你需要一个生命周期独立于当前上下文的全局上下文，
            //即就是它的存活时间绑定在进程中而不是当前某个组建。
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

//    public ImageLoader getImageLoader() {
//        return mImageLoader;
//    }

}
