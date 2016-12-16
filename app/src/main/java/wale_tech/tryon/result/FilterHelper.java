package wale_tech.tryon.result;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;

/**
 * Created by lenovo on 2016/12/15.
 */

public class FilterHelper {
    private static int spnInitCount = 0;

    public static int getSpnInitCount() {
        return spnInitCount;
    }

    public static void setupFilterLayout(Context context, final ResultAction resultAction, ArrayList<String> brandList, ArrayList<String> colorList, ArrayList<String> sizeList) {
        Log.i("Result", "what?");
        AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i("Result", "type is : " + adapterView.getTag().toString());
                String filter_type = adapterView.getTag().toString();
                String filter_content = adapterView.getItemAtPosition(i).toString();

                if (filter_content.equals("所有")) {
                    filter_content = "All";
                }

                spnInitCount++;
                if (spnInitCount > 3) {
                    resultAction.getFilter(BaseAction.REQUEST_FILTER, filter_content, filter_type);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        Log.i("Result", "init");
        setupFilterCallItem(context);

        setupFilterNav(context, selectedListener, brandList, colorList, sizeList);
    }

    private static void setupFilterCallItem(Context context) {
        final Activity activity = (Activity) context;
        Log.i("Result", "setup Filter Call");

        try {
            final FrameLayout filter_fl = (FrameLayout) activity.findViewById(R.id.item_filter_call_fl);
            filter_fl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("Result", "Filter click");

                    final DrawerLayout filter_dl = (DrawerLayout) activity.findViewById(R.id.result_filter_dl);
                    if (!filter_dl.isDrawerOpen(GravityCompat.END)) {
                        filter_dl.openDrawer(GravityCompat.END);
                    }
                }
            });
        } catch (Exception exception) {
            Toast.makeText(context, "没有载入相关布局文件，无法找到资源ID", Toast.LENGTH_SHORT).show();
        }
    }

    private static void setupFilterNav(Context context, AdapterView.OnItemSelectedListener selectedListener,
                                       ArrayList<String> brandList, ArrayList<String> colorList, ArrayList<String> sizeList) {

        brandList.add(0, "所有");
        colorList.add(0, "所有");
        sizeList.add(0, "所有");

        Activity activity = (Activity) context;
        try {
            final AppCompatSpinner brand_spn = (AppCompatSpinner) activity.findViewById(R.id.filter_brand_spn);
            final AppCompatSpinner color_spn = (AppCompatSpinner) activity.findViewById(R.id.filter_color_spn);
            final AppCompatSpinner size_spn = (AppCompatSpinner) activity.findViewById(R.id.filter_size_spn);

            ArrayAdapter<String> brand_adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, brandList);
            ArrayAdapter<String> color_adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, colorList);
            ArrayAdapter<String> size_adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, sizeList);

            brand_spn.setAdapter(brand_adapter);
            color_spn.setAdapter(color_adapter);
            size_spn.setAdapter(size_adapter);

            brand_spn.setTag(ResultAction.FILTER_TYPE_BRAND);
            color_spn.setTag(ResultAction.FILTER_TYPE_COLOR);
            size_spn.setTag(ResultAction.FILTER_TYPE_SIZE);

            brand_spn.setOnItemSelectedListener(selectedListener);
            color_spn.setOnItemSelectedListener(selectedListener);
            size_spn.setOnItemSelectedListener(selectedListener);

            brand_spn.setSelection(0);
            color_spn.setSelection(0);
            size_spn.setSelection(0);

            final Button reset_btn = (Button) activity.findViewById(R.id.filter_reset_btn);
            reset_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    brand_spn.setSelection(0);
                    color_spn.setSelection(0);
                    size_spn.setSelection(0);
                }
            });

        } catch (Exception exception) {
            Toast.makeText(context, "没有载入相关布局文件，无法找到资源ID", Toast.LENGTH_SHORT).show();
        }
    }
}