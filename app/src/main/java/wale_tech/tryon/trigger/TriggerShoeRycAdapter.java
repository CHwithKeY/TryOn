package wale_tech.tryon.trigger;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.util.ArrayList;

import wale_tech.tryon.publicAdapter.GeneralShoeRycAdapter;
import wale_tech.tryon.http.HttpAction;
import wale_tech.tryon.product.Product_Act;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicObject.ObjectShoe;
import wale_tech.tryon.publicSet.IntentSet;

/**
 * Created by lenovo on 2016/11/23.
 */

public abstract class TriggerShoeRycAdapter extends GeneralShoeRycAdapter {

    public TriggerShoeRycAdapter(ArrayList<ObjectShoe> dataList) {
        super(dataList);
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        String path = ((TriggerList_Act) context).getTrigger().getPath();

        if (!HttpAction.checkNet(context)) {
            return;
        }

        int position = Methods.cast(v.getTag());

        ObjectShoe shoe = getDataList().get(position);
        String sku_code_str = shoe.getSkuCode();

        Intent product_int = new Intent(context, Product_Act.class);
        product_int.putExtra(IntentSet.KEY_SKU_CODE, sku_code_str);
        product_int.putExtra(IntentSet.KEY_TRIGGER_PATH, path);
        context.startActivity(product_int);
    }
}
