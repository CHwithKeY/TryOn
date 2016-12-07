package wale_tech.tryon.product;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.BaseClickListener;

/**
 * Created by KeY on 2016/11/1.
 */

class ColorListener extends BaseClickListener {
    private ArrayList<Button> buttonList;

    ColorListener(Context context, BaseAction baseAction) {
        super(context, baseAction);
    }

    void setButtonList(ArrayList<Button> buttonList) {
        this.buttonList = buttonList;
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        btn.setBackgroundResource(R.drawable.dw_pattern_check_btn);

        int tag = (int) v.getTag();
        for (int i = 0; i < buttonList.size(); i++) {
            int btn_tag = (int) buttonList.get(i).getTag();

            if (btn_tag != tag) {
                buttonList.get(i).setBackgroundResource(R.drawable.dw_pattern_uncheck_btn);
            }
        }

//        ((Product_Act) context).setCurrentImage(btn.getText().toString());
    }
}
