package wale_tech.tryon.http;

import android.content.Context;

import wale_tech.tryon.sharedinfo.SharedAction;

/**
 * Created by KeY on 2016/6/3.
 */

public final class HttpSet {
    // 183.62.156.108:427
    // 192.168.137.1:8080
    // 10.10.4.122:8080
    // 10.10.5.119
    // 192.168.2.211:8080

    public final static int NORMAL_NET = 0;
    public final static int DEDICATED_NET = 1;

    public static String DEDICATED_IP = "192.168.2.211:8080";
    public final static String GZD_DEDICATED_IP = "192.168.137.1:8080";
    public final static String BS_DEDICATED_IP = "192.168.2.211:8080";

    public static String NORMAL_URL = "http://183.62.156.108:427/TryOnService/";
    public static String DEDICATED_URL = "http://" + DEDICATED_IP + "/TryOnService/";

    public static String BASE_URL = "http://183.62.156.108:427/TryOnService/";

    public static void setBaseUrl(String baseUrl) {
        BASE_URL = baseUrl;
    }

    public static void setDedicatedIp(String dedicatedIp) {
        DEDICATED_IP = dedicatedIp;
        StringBuilder durl_sb = new StringBuilder("http://").append(dedicatedIp);
        if (DEDICATED_URL.endsWith("EN/")) {
            durl_sb.append("/TryOnServiceEN/");
        } else {
            durl_sb.append("/TryOnService/");
        }
        DEDICATED_URL = durl_sb.toString();
    }

    //
    public static final String URL_GET_SERVICE = "GetServiceAction.action";
    public static final String URL_PING = "PingAction.action";

    // Login_Act
    public static final String URL_LOGIN = "LoginAction.action";
    public static final String URL_LOGIN_RECORD = "LoginRecordAction.action";
    public static final String URL_REGISTER = "RegisterAction.action";

    // Favourite_Act
    public final static String URL_GET_FAVOURITE = "GetFavouriteAction.action";
    public final static String URL_FAVOURITE = "FavouriteAction.action";

    // Cart_Act
    public final static String URL_CART = "CartAction.action";
    public final static String URL_GET_CART = "GetCartAction.action";
    public final static String URL_UPDATE_CART = "UpdateCartAction.action";

    public final static String URL_CREATE_ORDER = "OrderAction.action";

    // Order_Confirm_Act
    public final static String URL_GET_CONFIRM_ITEM = "GetOrderConfirmItemAction.action";

    // Order_Act
    public final static String URL_GET_ORDER = "GetOrderAction.action";
    public final static String URL_GET_ORDER_ITEM = "GetOrderItemAction.action";

    // Product_Act
    public final static String URL_GET_SHOE_DETAILS = "GetShoeDetailsAction.action";
    public final static String URL_GET_SHOE_COLOR = "GetShoeColorAction.action";

    // TriggerList_Act
    public final static String URL_GET_TRIGGER = "GetTriggerListAction.action";

    // Pattern
    public final static String URL_GET_PATTERN = "GetShoePatternAction.action";
    public final static String URL_GET_PATTERN_COLOR = "GetPatternColorAction.action";
    public final static String URL_GET_PATTERN_SIZE = "GetShoePatternSizeAction.action";
    public final static String URL_GET_PATTERN_STOCK_PRICE = "GetShoePatternStockPriceAction.action";

    // History
    public final static String URL_HISTORY = "HistoryAction.action";

    // Coupon
    public final static String URL_COUPON = "GetUserCouponAction.action";
    public final static String URL_COUPON_AWARD = "GetCouponAwardAction.action";

    // Search & Result_Act
    public final static String URL_RESULT_FILTER = "FilterAction.action";
    public final static String URL_RESULT_FILTER_OPTION = "GetFilterOptionAction.action";

    // Comment_Act
    public final static String URL_COMMENT_COMMIT = "CommitCommentAction.action";

    // Update_Act
    public final static String URL_CHECK_VERSION = "GetAppVersionAction.action";
    public static final String URL_VERSION = "TryOn.apk";

    //
    public final static String URL_EDIT_INFO_UPDATE_NICKNAME = "UpdateNickNameAction.action";
    public final static String URL_EDIT_INFO_UPDATE_PASSWORD = "UpdatePassWordAction.action";

    // HttpHandler
    public final static int httpResponse = 1000;
    public final static int httpNull = 1001;

    // Public Set
    public final static String KEY_LAST_ID = "last_id";

    //
    public final static String KEY_LANGUAGE = "app_language";

    // User Set
    public final static String KEY_USERNAME = "username";
    public final static String KEY_PASSWORD = "password";
    public final static String KEY_NEW_PASSWORD = "new_password";
    public final static String KEY_SEX = "sex";

    public final static String KEY_DATE_TIME = "date_time";

    public final static String KEY_TABLE_TAG = "table_tag";
    public final static String KEY_FILTER_OPTION_TAG = "filter_option_tag";

    // Shoe Set
    public final static String KEY_BRAND = "brand";
    public final static String KEY_PRODUCT_NAME = "product_name";

    public final static String KEY_GENDER = "gender";
    public final static String KEY_COLOR = "color";
    public final static String KEY_SIZE = "size";

    // Product Set
    public final static String KEY_EPC_CODE = "epc_code";
    public final static String KEY_COUNT = "count";
    public final static String KEY_IS_STOCK = "is_stock";

    // Coupon Set
    public final static String KEY_GET_COUPON_TYPE = "get_coupon_type";

    // Cart Set
    public final static String KEY_SKU_CODE = "sku_code";
    public final static String KEY_NEW_COUNT = "new_count";

    public final static String KEY_ORDER_NUM = "order_num";
    public final static String KEY_PRICE = "price";
    public final static String KEY_TOTAL_PRICE = "total_price";

    //
    public final static String KEY_COUPON_NUMBER = "coupon_number";

    public final static String KEY_TRIGGER_RESULT = "trigger_result";
    public final static String KEY_TRIGGER_PATH = "trigger_path";
    public final static String KEY_TRIGGER_WORK_SPACE = "trigger_work_space";

    // Push Set
    public final static String KEY_CLIENT_ID = "client_id";

    // Comment Set
    public final static String KEY_EXP_WIDTH = "exp_width";
    public final static String KEY_EXP_MATERIAL = "exp_material";
    public final static String KEY_EXP_SPORT = "exp_sport";
    public final static String KEY_EXP_ADVICE = "exp_advice";

    public final static String KEY_OPERATION_CODE = "operation_code";

    //
    public final static String KEY_APP_VERSION = "app_version";

}
