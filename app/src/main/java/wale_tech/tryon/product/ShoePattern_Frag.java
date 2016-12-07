package wale_tech.tryon.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.pattern.PatternAction;
import wale_tech.tryon.pattern.Pattern_Frag;
import wale_tech.tryon.publicObject.ObjectShoe;

/**
 * Created by lenovo on 2016/11/16.
 */

public class ShoePattern_Frag extends Pattern_Frag {

    private int stock;

    public String getColor() {
        return getPatternAction().getSaveColor();
    }

    public String getSize() {
        return getPatternAction().getSaveSize();
    }

    public int getStock() {
        return stock;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_shoe_pattern_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        stock = 0;

        varInit();

        setupStockOnlySwitch(view);

        patternAction = new PatternAction(getContext(), this);
        patternAction.getPattern(brand, product_name, false);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.PATTERN_GET_PATTERN:
                shoeList = patternAction.handlePatternResponse(result);
//                colorBtnList = new ArrayList<>();
//                sizeBtnList = new ArrayList<>();

                initPatternData(shoeList);
                break;

            case HttpTag.PATTERN_STOCK_PRICE:
                ObjectShoe shoe = patternAction.handleStockPriceResponse(result);
                setupStockAndPriceText(shoe);
                break;

            case HttpTag.PATTERN_GET_SIZE:
                ArrayList<String> filterSizeList = patternAction.handleSizeResponse(result);
                setupShoeSize(filterSizeList);
                break;

            case HttpTag.PATTERN_ADD_CART:
                patternAction.handleAddCartResponse(result);
                break;

            default:
                break;
        }
    }

    @Override
    public void onNullResponse(String tag) throws JSONException {
    }

    @Override
    protected void setupStockAndPriceText(ObjectShoe shoe) {
        if (getView() != null) {
            View view = getView();
            final TextView price_tv = (TextView) view.findViewById(R.id.shoe_pattern_price_tv);
            final TextView stock_tv = (TextView) view.findViewById(R.id.shoe_pattern_stock_tv);

            String price_str = "价格：" + shoe.getPrice();
            price_tv.setText(price_str);

            String count_str = "库存：" + shoe.getCount();
            stock_tv.setText(count_str);
            stock = Integer.parseInt(shoe.getCount());
        }
    }

    @Override
    public void setupShoeImage(String image_path) {
        ((Product_Act) getActivity()).setCurrentImage(image_path);
    }

    private void setupStockOnlySwitch(View view) {
        final SwitchCompat stock_switch = (SwitchCompat) view.findViewById(R.id.shoe_pattern_stock_only_sw);

        final LinearLayout color_ll = (LinearLayout) view.findViewById(R.id.item_pick_pattern_color_ll);
        final LinearLayout size_ll = (LinearLayout) view.findViewById(R.id.item_pick_pattern_size_ll);

        stock_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                Log.i("Result", "remove all views");
                color_ll.removeAllViews();
                size_ll.removeAllViews();
                patternAction.clearSaveParams();

                if (checked) {
                    patternAction.getPattern(brand, product_name, true);
                } else {
                    patternAction.getPattern(brand, product_name, false);
                }

                colorListener.setStock(checked);
            }
        });
    }
}
