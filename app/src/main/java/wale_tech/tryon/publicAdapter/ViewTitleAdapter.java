package wale_tech.tryon.publicAdapter;

import android.support.v4.app.FragmentManager;

/**
 * Created by KeY on 2016/11/6.
 */

public class ViewTitleAdapter extends ViewBaseAdapter {

    private String[] titles;

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public ViewTitleAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
