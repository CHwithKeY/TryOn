package wale_tech.tryon.result;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import wale_tech.tryon.publicSet.FilterSet;
import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.http.HttpAction;
import wale_tech.tryon.http.HttpHandler;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicObject.ObjectShoe;

/**
 * Created by lenovo on 2016/11/20.
 */

public class ResultAction extends BaseAction {

    public final static String FILTER_TYPE_BRAND = "brand";
    public final static String FILTER_TYPE_PRODUCT_NAME = "product_name";
    public final static String FILTER_TYPE_COLOR = "color";
    public final static String FILTER_TYPE_SIZE = "size";


    private String save_brand;
    private String save_product_name;
    private String save_color;
    private String save_size;

    public ResultAction(Context context) {
        super(context);
    }

    public int getFilter(int request, String filter_content, String filter_type) {
        if (!checkNet()) {
            return ACTION_NET_DOWN;
        }

        checkRequest(request);

        if (request == REQUEST_DEFAULT || request == REQUEST_LOAD_MORE) {
            save_brand = "All";
            save_product_name = "All";
            save_color = "All";
            save_size = "All";
        } else {
            switch (filter_type) {
                case FILTER_TYPE_BRAND:
                    save_brand = filter_content;
                    break;

                case FILTER_TYPE_PRODUCT_NAME:
                    save_product_name = filter_content;
                    break;

                case FILTER_TYPE_COLOR:
                    save_color = filter_content;
                    break;

                case FILTER_TYPE_SIZE:
                    save_size = filter_content;
                    break;

                default:
                    break;
            }
        }

        String[] key = {HttpSet.KEY_BRAND, HttpSet.KEY_PRODUCT_NAME, HttpSet.KEY_COLOR, HttpSet.KEY_SIZE, HttpSet.KEY_LAST_ID};
        String[] value = {save_brand, save_product_name, save_color, save_size, "" + sharedAction.getLastId()};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_RESULT_FILTER);
        action.setTag(HttpTag.RESULT_FILTER);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();

        return ACTION_DONE;
    }

    public void getFilterOption(String table_tag, String filter_option_tag) {
        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_TABLE_TAG, HttpSet.KEY_FILTER_OPTION_TAG};
        String[] value = {sharedAction.getUsername(), table_tag, filter_option_tag};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_RESULT_FILTER_OPTION);
        switch (filter_option_tag) {
            case FilterSet.OPTION_BRAND:
                action.setTag(HttpTag.RESULT_FILTER_BRAND);
                break;

            case FilterSet.OPTION_COLOR:
                action.setTag(HttpTag.RESULT_FILTER_COLOR);
                break;

            case FilterSet.OPTION_SIZE:
                action.setTag(HttpTag.RESULT_FILTER_SIZE);
                break;

            default:
                break;
        }
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public ArrayList<ObjectShoe> handleListResponse(String result) throws JSONException {
        JSONArray array = new JSONArray(result);
        ArrayList<ObjectShoe> shoeList = new ArrayList<>();

        if (array.length() == 0) {
            return shoeList;
        }

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            ObjectShoe shoe = new ObjectShoe();

            for (int k = 0; k < obj.length(); k++) {
                shoe.value_set[k] = obj.getString(ObjectShoe.key_set[k]);
            }
            shoeList.add(shoe);
        }

        String last_id = shoeList.get(shoeList.size() - 1).getLastId();
        sharedAction.setLastId(Integer.parseInt(last_id));

        return shoeList;
    }

    public ArrayList<String> handleBrandResponse(String result) throws JSONException {
        JSONArray array = new JSONArray(result);
        ArrayList<String> brandList = new ArrayList<>();

        if (array.length() == 0) {
            return brandList;
        }

        for (int i = 0; i < array.length(); i++) {
            String brand = array.getString(i);
            brandList.add(brand);
        }

        return brandList;
    }

    public ArrayList<String> handleColorResponse(String result) throws JSONException {
        JSONArray array = new JSONArray(result);
        ArrayList<String> colorList = new ArrayList<>();

        if (array.length() == 0) {
            return colorList;
        }

        for (int i = 0; i < array.length(); i++) {
            String color = array.getString(i);
            colorList.add(color);
        }

        return colorList;
    }

    public ArrayList<String> handleSizeResponse(String result) throws JSONException {
        JSONArray array = new JSONArray(result);
        ArrayList<String> sizeList = new ArrayList<>();

        if (array.length() == 0) {
            return sizeList;
        }

        for (int i = 0; i < array.length(); i++) {
            String size = array.getString(i);
            sizeList.add(size);
        }

        return sizeList;
    }

}
