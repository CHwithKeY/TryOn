package wale_tech.tryon.history;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.publicAdapter.BaseRycAdapter;
import wale_tech.tryon.publicClass.BitmapCache;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicObject.ObjectHistoryImage;
import wale_tech.tryon.publicSet.IntentSet;

import static wale_tech.tryon.publicClass.Methods.cast;

/**
 * Created by lenovo on 2017/2/21.
 */

public abstract class HistoryImageRycAdapter extends BaseRycAdapter implements View.OnClickListener {

    public HistoryImageRycAdapter(ArrayList<ObjectHistoryImage> dataList) {
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
    public ArrayList<ObjectHistoryImage> getDataList() {
        return cast(super.getDataList());
    }

    @Override
    public void onBindDataViewHolder(DataViewHolder parent, int position) {
        HistoryImageViewHolder holder = (HistoryImageViewHolder) parent;

        ObjectHistoryImage historyImage = getDataList().get(position);
//        holder.imageView.setTag(historyImage.getImagePath());

//        Glide.with(context).load(HttpSet.BASE_URL + historyImage.getImagePath()).placeholder(R.drawable.ic_public_none_image).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.ic_public_none_image).into(holder.imageView);
//        holder.imageView.setTag(R.id.image_tag, historyImage.getImagePath());
//        holder.imageView.setOnClickListener(this);

        holder.draweeView.setImageURI(HttpSet.DEDICATED_URL + historyImage.getImagePath());
        holder.draweeView.setTag(R.id.image_tag, historyImage.getImagePath());
        holder.draweeView.setOnClickListener(this);
        //        Methods.downloadImage(holder.imageView, HttpSet.BASE_URL + historyImage.getImagePath(), cache);
    }

    @Override
    public void onClick(View v) {
//        String url = (String) v.getTag(R.id.image_tag);

        String url = (String) v.getTag(R.id.image_tag);

        Log.i("Result", "tag url is : " + url);

//        Intent zoom_int = new Intent(context, ZoomImage_Act.class);
//        zoom_int.putExtra("image_url", HttpSet.BASE_URL + url);
//        context.startActivity(zoom_int);
    }

    private class HistoryImageViewHolder extends DataViewHolder {

        private SimpleDraweeView draweeView;
//        private ImageView imageView;

        public HistoryImageViewHolder(View itemView) {
            super(itemView);

//            imageView = (ImageView) itemView.findViewById(R.id.rv_history_img);

            draweeView = (SimpleDraweeView) itemView.findViewById(R.id.rv_history_image_sdv);
        }
    }

}
