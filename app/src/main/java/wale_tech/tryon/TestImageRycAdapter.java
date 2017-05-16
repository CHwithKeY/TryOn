package wale_tech.tryon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import wale_tech.tryon.history.ZoomImage_Act;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.publicAdapter.BaseRycAdapter;
import wale_tech.tryon.publicClass.BitmapCache;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicSet.IntentSet;

import static wale_tech.tryon.publicClass.Methods.cast;

/**
 * Created by lenovo on 2017/2/21.
 */

public abstract class TestImageRycAdapter extends BaseRycAdapter implements View.OnClickListener {

    public TestImageRycAdapter(ArrayList<String> dataList) {
        super(dataList);
    }

    private BitmapCache cache;

    private Context context;

    @Override
    public DataViewHolder onCreateDataViewHolder(ViewGroup parent) {
        context = parent.getContext();

        cache = new BitmapCache();
        View view = View.inflate(parent.getContext(), R.layout.recycler_history_image_item, null);
        return new HistoryImageViewHolder(view);
    }

    @Override
    public ArrayList<String> getDataList() {
        return cast(super.getDataList());
    }

    @Override
    public void onBindDataViewHolder(DataViewHolder parent, int position) {
        HistoryImageViewHolder holder = (HistoryImageViewHolder) parent;

        String path = getDataList().get(position);
        path = Environment.getExternalStorageDirectory().getPath() + path;

        holder.imageView.setImageBitmap(getDiskBitmap(path));

    }

    private Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }

    @Override
    public void onClick(View v) {
//        String url = (String) v.getTag(R.id.image_tag);

        String image_postfix = (String) v.getTag();

        Log.i("Result", "tag url is : " + image_postfix);

        Intent zoom_int = new Intent(context, ZoomImage_Act.class);
        zoom_int.putExtra(IntentSet.KEY_IMAGE_PATH, HttpSet.BASE_URL + image_postfix);
        context.startActivity(zoom_int);
    }

    private class HistoryImageViewHolder extends DataViewHolder {

        //        private SimpleDraweeView draweeView;
        private ImageView imageView;

        public HistoryImageViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.rv_history_img);
//            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.rv_history_image_sdv);
        }
    }

}
