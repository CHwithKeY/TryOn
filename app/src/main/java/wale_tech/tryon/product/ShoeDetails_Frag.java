package wale_tech.tryon.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.Arrays;
import java.util.List;

import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Frag;
import wale_tech.tryon.publicObject.ObjectShoe;
import wale_tech.tryon.publicSet.BundleSet;

/**
 * Created by lenovo on 2016/11/16.
 */

public class ShoeDetails_Frag extends Base_Frag {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_shoe_details_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupDetailsList(view);
    }

    private void setupDetailsList(View view) {
        ObjectShoe shoe = ((Product_Act) getActivity()).getShoe();

        final ListView standard_lv = (ListView) view.findViewById(R.id.shoe_details_standard_lv);
        final ListView params_lv = (ListView) view.findViewById(R.id.shoe_details_params_lv);

        String[] detail_info = {shoe.getBrand(), shoe.getProductName(), shoe.getColor(), shoe.getSize(), shoe.getType(), shoe.getGender(), shoe.getPrice()};
        List<String> list = Arrays.asList(detail_info);

        ArrayAdapter standard_adapter = ArrayAdapter.createFromResource(getContext(), R.array.product_standard, R.layout.public_listview_text_item);
        standard_lv.setAdapter(standard_adapter);

        ArrayAdapter params_adapter = new ArrayAdapter<>(getContext(), R.layout.public_listview_text_item, list);
        params_lv.setAdapter(params_adapter);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {

    }

    @Override
    public void onNullResponse(String tag) throws JSONException {

    }
}
