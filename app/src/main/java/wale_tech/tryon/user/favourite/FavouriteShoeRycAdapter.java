package wale_tech.tryon.user.favourite;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.publicAdapter.BaseShoeRycAdapter;
import wale_tech.tryon.publicObject.ObjectShoe;

/**
 * Created by KeY on 2016/6/29.
 */
abstract class FavouriteShoeRycAdapter extends BaseShoeRycAdapter {

    public FavouriteShoeRycAdapter(ArrayList<ObjectShoe> dataList) {
        super(dataList);
    }

    @Override
    public DataViewHolder onCreateDataViewHolder(ViewGroup parent) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_shoe_general_item, null);
        return new BaseShoeViewHolder(view);
    }
}
