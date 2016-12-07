package wale_tech.tryon.pattern;

import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.publicView.ColorSnackBar;
import wale_tech.tryon.publicObject.ObjectShoe;

/**
 * Created by KeY on 2016/7/13.
 */
public class SizeListener implements View.OnClickListener {

    private ColorSnackBar snackBar;
    private Pattern_Frag pattern_frag;

    public SizeListener(Pattern_Frag pattern_frag) {
        this.pattern_frag = pattern_frag;
        snackBar = new ColorSnackBar(pattern_frag.getContext());
    }

    @Override
    public void onClick(View v) {
        if (!pattern_frag.getIsColorChosen()) {
            snackBar.show(pattern_frag.getContext().getString(R.string.pattern_toast_size_btn));
            return;
        }

        Button btn = (Button) v;
        btn.setBackgroundResource(R.drawable.dw_pattern_check_btn);

        setupOtherBtn(btn);
        setupStockAndPrice(btn.getText().toString());
    }

    private void setupOtherBtn(Button btn) {
        ArrayList<Button> buttonList = pattern_frag.getSizeBtnList();

        int tag = (int) btn.getTag();
        for (int i = 0; i < buttonList.size(); i++) {
            int btn_tag = (int) buttonList.get(i).getTag();

            if (btn_tag != tag) {
                buttonList.get(i).setBackgroundResource(R.drawable.dw_pattern_uncheck_btn);
            }
        }
    }

    private void setupStockAndPrice(String btn_str) {
        ArrayList<ObjectShoe> shoeList = pattern_frag.getShoeList();

        String sku_code = "";
        String save_color = pattern_frag.getPatternAction().getSaveColor();

        Log.i("Result", "save color is : " + save_color);

        for (int i = 0; i < shoeList.size(); i++) {
            ObjectShoe shoe = shoeList.get(i);
            if (shoe.getColor().equals(save_color) && shoe.getSize().equals(btn_str)) {
                sku_code = shoe.getSkuCode();
            }
        }
        Log.i("Result", "size sku code is : " + sku_code);
        pattern_frag.getPatternAction().getShoeStockAndPrice(sku_code, btn_str);
    }
}
