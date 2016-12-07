package wale_tech.tryon.pattern;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.BaseClickListener;
import wale_tech.tryon.publicObject.ObjectShoe;

/**
 * Created by KeY on 2016/7/13.
 */
public class ColorListener implements View.OnClickListener {

    private Pattern_Frag pattern_frag;
    private boolean isStock;

    public ColorListener(Pattern_Frag pattern_frag) {
        this.pattern_frag = pattern_frag;
        isStock = false;
    }

    public void setStock(boolean stock) {
        isStock = stock;
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        btn.setBackgroundResource(R.drawable.dw_pattern_check_btn);

        pattern_frag.setIsColorChosen(true);

        Log.i("Result", "btn getText is : " + btn.getText());
        ArrayList<Button> buttonList = pattern_frag.getColorBtnList();

        int tag = (int) v.getTag();
        for (int i = 0; i < buttonList.size(); i++) {
            int btn_tag = (int) buttonList.get(i).getTag();

            if (btn_tag != tag) {
                buttonList.get(i).setBackgroundResource(R.drawable.dw_pattern_uncheck_btn);
            }
        }

        String btn_str = btn.getText().toString();
        setupSizeAndImageMatch(btn_str);
    }

    private void setupSizeAndImageMatch(String btn_str) {
        ArrayList<ObjectShoe> shoeList = pattern_frag.getShoeList();

        for (int i = 0; i < shoeList.size(); i++) {
            ObjectShoe shoe = shoeList.get(i);
            if (shoe.getColor().equals(btn_str)) {
                String brand = shoe.getBrand();
                String product_name = shoe.getProductName();
                String image_path = shoe.getImagePath();

                pattern_frag.getPatternAction().getSizeMatch(brand, product_name, btn_str, isStock);
                pattern_frag.setupShoeImage(image_path);
                break;
            }
        }
    }

}
