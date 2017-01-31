package wale_tech.tryon.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import wale_tech.tryon.R;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.publicClass.BitmapCache;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicSet.BundleSet;

/**
 * Created by KeY on 2016/4/24.
 */
public class Image_Frag extends Fragment {

    private String img_url;
    private BitmapCache cache;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.public_image_page, container, false);
    }

    @Override
    public void setArguments(Bundle bundle) {
        super.setArguments(bundle);

        Log.i("Result", "img url is : " + bundle.getString("img_url"));
        img_url = HttpSet.BASE_URL + bundle.getString(BundleSet.KEY_IMG_URL);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        varInit();

        setupImageView(view);
    }

    private void varInit() {
        cache = new BitmapCache();
    }

    private void setupImageView(View view) {
        final ImageView pro_img = (ImageView) view.findViewById(R.id.image_img);

        if (cache.getBitmap(img_url) == null) {
            // 请求下载图片
            Methods.downloadImage(pro_img, img_url, cache);
        } else {
            Log.i("Result", "get Url");
            pro_img.setImageBitmap(cache.getBitmap(img_url));
        }
    }
}
