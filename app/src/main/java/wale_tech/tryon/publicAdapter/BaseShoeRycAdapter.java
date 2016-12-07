package wale_tech.tryon.publicAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.http.HttpAction;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.product.Product_Act;
import wale_tech.tryon.publicClass.BitmapCache;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicObject.ObjectShoe;
import wale_tech.tryon.publicSet.IntentSet;

/**
 * Created by KeY on 2016/6/29.
 */
public abstract class BaseShoeRycAdapter extends BaseRycAdapter implements View.OnClickListener {

    public final static String GENDER_MALE = "男鞋";
    public final static String GENDER_FEMALE = "女鞋";
    public final static String GENDER_GENERAL = "通用";

    private BitmapCache cache;

    public BaseShoeRycAdapter(ArrayList<ObjectShoe> dataList) {
        super(dataList);
        cache = new BitmapCache();
    }

    @Override
    public ArrayList<ObjectShoe> getDataList() {
        return Methods.cast(super.getDataList());
    }

    @Override
    public DataViewHolder onCreateDataViewHolder(ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_shoe_base_item, null);
        return new BaseShoeViewHolder(view);
    }

    @Override
    public void onBindDataViewHolder(DataViewHolder parent, int position) {
        BaseShoeViewHolder holder = (BaseShoeViewHolder) parent;

        ObjectShoe shoe = getDataList().get(position);
        holder.item_ll.setTag(position);
        holder.item_ll.setOnClickListener(this);

        String name_str = shoe.getBrand() + "\t" + shoe.getProductName();
        holder.name_tv.setText(name_str);

        String params_str = shoe.getColor() + "\t" + shoe.getSize();
        holder.params_tv.setText(params_str);

        switch (shoe.getGender()) {
            case GENDER_MALE:
            case "Male":
                holder.gender_img.setVisibility(View.VISIBLE);
                holder.gender_img.setImageResource(R.drawable.image_shoe_gender_male);
                break;

            case GENDER_FEMALE:
            case "Female":
                holder.gender_img.setVisibility(View.VISIBLE);
                holder.gender_img.setImageResource(R.drawable.image_shoe_gender_female);
                break;

            case GENDER_GENERAL:
            case "General":
                holder.gender_img.setVisibility(View.GONE);
                break;

            default:
                break;
        }

        holder.shoe_img.setTag(HttpSet.BASE_URL + shoe.getImagePath());
        loadImage(holder, position);
    }

//    @Override
//    public int getItemCount() {
//        return shoeList == null ? 0 : shoeList.size();
//    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();

        if (!HttpAction.checkNet(context)) {
            return;
        }

        int position = Methods.cast(v.getTag());

        ObjectShoe shoe = getDataList().get(position);
        String sku_code_str = shoe.getSkuCode();

        Intent product_int = new Intent(context, Product_Act.class);
        product_int.putExtra(IntentSet.KEY_SKU_CODE, sku_code_str);
        context.startActivity(product_int);
    }

    public class BaseShoeViewHolder extends DataViewHolder {

        public LinearLayout item_ll;

        ImageView shoe_img;
        ImageView gender_img;

        TextView name_tv;
        TextView params_tv;

        public BaseShoeViewHolder(View itemView) {
            super(itemView);

            item_ll = (LinearLayout) itemView.findViewById(R.id.rv_ll);

            shoe_img = (ImageView) itemView.findViewById(R.id.rv_img);
            gender_img = (ImageView) itemView.findViewById(R.id.rv_gender_img);

            name_tv = (TextView) itemView.findViewById(R.id.rv_name_tv);
            params_tv = (TextView) itemView.findViewById(R.id.rv_params_tv);
        }
    }

    private void loadImage(BaseShoeViewHolder holder, int position) {

        // 先判断缓存中是否缓存了这个item的imageview所在的url的图片
        String img_url = holder.shoe_img.getTag().toString();

        // 如果没有
        if (cache.getBitmap(img_url) == null) {
            // 请求下载图片
            // downloadImage(holder, position, cache);
            ObjectShoe shoe = getDataList().get(position);
            Methods.downloadImage(holder.shoe_img, HttpSet.BASE_URL + shoe.getImagePath(), cache);
        } else {
            holder.shoe_img.setImageBitmap(cache.getBitmap(img_url));
        }

    }
}
