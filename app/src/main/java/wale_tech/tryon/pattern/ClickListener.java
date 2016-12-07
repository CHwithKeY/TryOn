package wale_tech.tryon.pattern;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.base.BaseClickListener;
import wale_tech.tryon.publicClass.Methods;

/**
 * Created by KeY on 2016/7/13.
 */
class ClickListener extends BaseClickListener {

    private PatternAction patternAction;

    ClickListener(Context context, BaseAction baseAction) {
        super(context, baseAction);
        patternAction = (PatternAction) baseAction;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pattern_close_imgbtn:
            case R.id.pattern_shadow_tv:
                Pattern_Frag pattern_frag = Methods.cast(v.getTag());

                closePattern(context, pattern_frag);
                break;

            case R.id.pattern_btn0:
                patternAction.onAddInCart();
                break;

            default:
                break;
        }
    }

    public static void closePattern(Context context, Pattern_Frag pattern_frag) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(pattern_frag);
        transaction.commit();
    }
}
