package wale_tech.tryon.publicAdapter;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.publicAdapter.BaseRycAdapter;
import wale_tech.tryon.publicAdapter.BaseShoeRycAdapter;
import wale_tech.tryon.publicObject.ObjectShoe;

/**
 * Created by lenovo on 2016/11/21.
 */

public abstract class GeneralShoeRycAdapter extends BaseShoeRycAdapter {

    public GeneralShoeRycAdapter(ArrayList<ObjectShoe> dataList) {
        super(dataList);
    }

    @Override
    public DataViewHolder onCreateDataViewHolder(ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_shoe_general_item, null);
        return new BaseShoeViewHolder(view);
    }
}
