package wale_tech.tryon.history;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;

import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.publicClass.BitmapCache;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicSet.IntentSet;
import wale_tech.tryon.publicView.ZoomImageView;

public class ZoomImage_Act extends Base_Act {

    private BitmapCache cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image_layout);

        varInit();

        setupToolbar();
    }

    @Override
    public void varInit() {
        cache = new BitmapCache();

        String url = getIntent().getStringExtra(IntentSet.KEY_IMAGE_PATH);
        setupZoomImage(url);
    }

    @Override
    protected void setupToolbar() {
        setBackBtn();
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

    private void setupZoomImage(String url) {
//        String url = HttpSet.BASE_URL + "images/nike_air_max_run_black_single_side.jpg";

        final ZoomImageView zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image);

//        Log.i("Result", "url is : " + url);
//
        Methods.downloadImage(zoomImageView, url, cache);

//        zoomImageView.setImageResource(R.drawable.image_coupon_unable);
//        zoomImageView.setImageURI(uri);
    }
}
