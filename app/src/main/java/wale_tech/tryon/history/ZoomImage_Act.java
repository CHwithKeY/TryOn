package wale_tech.tryon.history;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import wale_tech.tryon.ImageControl;
import wale_tech.tryon.ImageScaleView;
import wale_tech.tryon.MatrixImageView;
import wale_tech.tryon.R;
import wale_tech.tryon.ZoomImageView;
import wale_tech.tryon.publicClass.BitmapCache;
import wale_tech.tryon.publicClass.Methods;

public class ZoomImage_Act extends AppCompatActivity {

    private BitmapCache cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_image_layout);

        cache = new BitmapCache();

        String url = getIntent().getStringExtra("image_url");
        setupZoomImage(url);
    }

    private void setupZoomImage(String url) {
        final ImageScaleView matrixImageView = (ImageScaleView) findViewById(R.id.zoom_matimg);
        Log.i("Result", "url is : " + url);

        Methods.downloadImage(matrixImageView, url, cache);

//        zoomImageView.setImageResource(R.drawable.image_coupon_unable);
//        zoomImageView.setImageURI(uri);
    }
}
