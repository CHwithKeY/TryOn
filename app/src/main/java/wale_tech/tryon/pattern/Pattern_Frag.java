package wale_tech.tryon.pattern;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import wale_tech.tryon.R;
import wale_tech.tryon.base.Base_Frag;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicClass.BitmapCache;
import wale_tech.tryon.publicClass.Methods;
import wale_tech.tryon.publicClass.ScreenSize;
import wale_tech.tryon.publicObject.ObjectShoe;

/**
 * Created by KeY on 2016/7/4.
 */
public class Pattern_Frag extends Base_Frag {
    public final static int TYPE_COLOR = 0;
    public final static int TYPE_SIZE = 1;

    protected ClickListener clickListener;

    protected ColorListener colorListener;
    protected SizeListener sizeListener;

    protected PatternAction patternAction;

    protected String brand;
    protected String product_name;

    protected ArrayList<ObjectShoe> shoeList;

    protected ArrayList<Button> colorBtnList;
    protected ArrayList<Button> sizeBtnList;

    // 迫于无奈，只能先将就实现“先选择颜色，再选择尺寸”的方案了
    protected boolean isColorChosen = false;

    public void setIsColorChosen(boolean isColorChosen) {
        this.isColorChosen = isColorChosen;
    }

    public boolean getIsColorChosen() {
        return isColorChosen;
    }

    public void setColorListener(ColorListener colorListener) {
        this.colorListener = colorListener;
    }

    public void setSizeListener(SizeListener sizeListener) {
        this.sizeListener = sizeListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_public_pattern_layout, container, false);
    }

    @Override
    public void setArguments(Bundle bundle) {
        super.setArguments(bundle);

        brand = bundle.getString("brand");
        product_name = bundle.getString("product_name");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        varInit();

        patternAction.getPattern(brand, product_name, true);

        setupAssistView(view);
    }

    protected void varInit() {
        patternAction = new PatternAction(getContext(), this);
        clickListener = new ClickListener(getContext(), patternAction);

        colorListener = new ColorListener(this);
        sizeListener = new SizeListener(this);
    }

    public ArrayList<Button> getColorBtnList() {
        return colorBtnList;
    }

    public ArrayList<Button> getSizeBtnList() {
        return sizeBtnList;
    }

    public ArrayList<ObjectShoe> getShoeList() {
        return shoeList;
    }

    public PatternAction getPatternAction() {
        return patternAction;
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {

            case HttpTag.PATTERN_GET_PATTERN:
                shoeList = patternAction.handlePatternResponse(result);
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
        switch (tag) {
            case HttpTag.PATTERN_GET_PATTERN:
                ClickListener.closePattern(getContext(), this);
                break;

            default:
                break;
        }
    }

    private void setupAssistView(View view) {
        final TextView shadow_tv = (TextView) view.findViewById(R.id.pattern_shadow_tv);
        shadow_tv.setTag(this);
        shadow_tv.setOnClickListener(clickListener);

        final ImageButton close_imgbtn = (ImageButton) view.findViewById(R.id.pattern_close_imgbtn);
        close_imgbtn.setTag(this);
        close_imgbtn.setOnClickListener(clickListener);
        // this ---> Pattern_Frag，传递的是Pattern_Frag
    }

    protected void initPatternData(ArrayList<ObjectShoe> shoeList) {
        ArrayList<String> colorList = new ArrayList<>();
        ArrayList<String> sizeList = new ArrayList<>();

        HashSet<String> colorSet = new HashSet<>();
        HashSet<String> sizeSet = new HashSet<>();

        for (int i = 0; i < shoeList.size(); i++) {
            ObjectShoe shoe = shoeList.get(i);

            colorSet.add(shoe.getColor());
            sizeSet.add(shoe.getSize());
        }

        Iterator<String> colorIterator = colorSet.iterator();
        Iterator<String> sizeIterator = sizeSet.iterator();

        while (colorIterator.hasNext()) {
            colorList.add(colorIterator.next());
        }

        while (sizeIterator.hasNext()) {
            sizeList.add(sizeIterator.next());
        }

        setupPatternView(colorList, sizeList);
    }

    protected void setupPatternView(ArrayList<String> colorList, ArrayList<String> sizeList) {
        if (getView() != null) {
            View view = getView();

            ScreenSize size = new ScreenSize(getContext());
            int parent_width = size.getWidth();

            final LinearLayout color_ll = (LinearLayout) view.findViewById(R.id.item_pick_pattern_color_ll);
            final LinearLayout size_ll = (LinearLayout) view.findViewById(R.id.item_pick_pattern_size_ll);

            colorBtnList = setupPatternItem(TYPE_COLOR, parent_width, color_ll, colorList);
            sizeBtnList = setupPatternItem(TYPE_SIZE, parent_width, size_ll, sizeList);
        }
    }

    public void setupShoeSize(ArrayList<String> matchSizeList) {
        for (int i = 0; i < sizeBtnList.size(); i++) {
            sizeBtnList.get(i).setBackgroundResource(R.drawable.dw_pattern_uncheck_btn);

            String size_str = sizeBtnList.get(i).getText().toString();
            Log.i("Result", "size_str is : " + size_str);
            for (int k = 0; k < matchSizeList.size(); k++) {
                if (size_str.equals(matchSizeList.get(k))) {
                    sizeBtnList.get(i).setEnabled(true);
                    break;
                } else {
                    sizeBtnList.get(i).setEnabled(false);
                }
            }
        }
    }

    public void setupShoeImage(String image_path) {
        BitmapCache cache = new BitmapCache();

        if (getView() != null) {
            View view = getView();
            final ImageView shoe_img = (ImageView) view.findViewById(R.id.pattern_img);
            Methods.downloadImage(shoe_img, HttpSet.BASE_URL + image_path, cache);
        }
    }

    protected void setupStockAndPriceText(ObjectShoe shoe) {
        if (getView() != null) {
            View view = getView();
            final TextView price_tv = (TextView) view.findViewById(R.id.pattern_price_tv);
            final TextView stock_tv = (TextView) view.findViewById(R.id.pattern_stock_tv);

            String price_str = "价格：" + shoe.getPrice();
            price_tv.setText(price_str);

            String count_str = "库存：" + shoe.getCount();
            stock_tv.setText(count_str);

            final Button add_cart_btn = (Button) view.findViewById(R.id.pattern_btn0);
            add_cart_btn.setTag(shoe.getSkuCode());
            add_cart_btn.setOnClickListener(clickListener);
        }
    }

    /**
     * @param itemType     根据类型判断，所要产生的 RadioButtons 所要监听什么样的事件
     * @param parent_width 屏幕的宽度
     * @param itemLinear   所要添加 RadioButtons 的父布局/容器
     * @param itemList     所有 RadioButtons 的数据的集合
     * @return 返回的是一个包含了所有 RadioButtons 的集合
     */
    // 可怕的方法
    // 主要目的就是产生多行多列的 RadioButton
    @Nullable
    public ArrayList<Button> setupPatternItem(int itemType, int parent_width, LinearLayout itemLinear, ArrayList<String> itemList) {
        if (getView() != null) {
            View view = getView();

            final Button brand_standard_btn = (Button) view.findViewById(R.id.item_pick_standard_btn);
            brand_standard_btn.setVisibility(View.VISIBLE);

            int btn_width = brand_standard_btn.getLayoutParams().width;
            int btn_height = brand_standard_btn.getLayoutParams().height;

            // 所需要的行数
            // 获取标准的一行所能放下的 button 的个数
            int line_count = 1;
            int single_line_count = parent_width / btn_width;

            // 然后自减 1，使得这一行两边可以多一点距离出来，更美观
            single_line_count = single_line_count - 1;

            // 判断实际情况的 list 的个数
            if (itemList.size() < single_line_count) {
                single_line_count = itemList.size();
                line_count = 1;
            }

            if (itemList.size() == single_line_count) {
                line_count = 1;
            }

            if (itemList.size() > single_line_count) {
                line_count = itemList.size() / single_line_count + 1;
            }

//            if (single_line_count > 4) {
//                single_line_count = 4;
//            }
//
//            if (line_count == 0) {
//                line_count = 1;
//            }
            brand_standard_btn.setVisibility(View.GONE);

            int index = 0;
            ArrayList<Button> buttonList = new ArrayList<>();

            for (int k = 0; k < line_count; k++) {
                Log.i("Result", "add linear");
                LinearLayout linear = new LinearLayout(getContext());
                linear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linear.setOrientation(LinearLayout.HORIZONTAL);
                linear.setGravity(Gravity.CENTER);
                linear.setPadding(0, 5, 0, 5);

//                int step_count = itemList.size();
//                if (line_count != 1) {
//                    step_count = single_line_count;
//                }

                if (k == line_count - 1) {
                    single_line_count = itemList.size() - k * single_line_count;
                }

                for (int i = 0; i < single_line_count; i++) {
                    Button button = new Button(getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(btn_width, btn_height);
                    params.setMargins(5, 5, 5, 5);
                    button.setLayoutParams(params);
                    button.setText(itemList.get(index));

                    button.setTextSize(12);
                    button.setPadding(5, 5, 5, 5);
                    button.setBackgroundResource(R.drawable.dw_pattern_uncheck_btn);

                    button.setTag(index);
                    switch (itemType) {
                        case TYPE_COLOR:
                            button.setOnClickListener(colorListener);
                            break;

                        case TYPE_SIZE:
                            button.setOnClickListener(sizeListener);
                            break;

                        default:
                            break;
                    }

                    buttonList.add(button);

                    linear.addView(button);
                    index++;
                }

                itemLinear.addView(linear);
            }

            return buttonList;
        } else {
            return null;
        }
    }

}
