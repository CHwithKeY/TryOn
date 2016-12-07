package wale_tech.tryon.publicObject;

import wale_tech.tryon.http.HttpResult;

/**
 * Created by KeY on 2016/11/2.
 */

public class ObjectCoupon {

    private String number;
    private String name;
    private String type;

    private String content;
    private String discount;
    private String store;
    private String brand;
    private String product_name;

    private String time;
    private String count;
    private String rest;
    private String status;

    private String user_level;
    private String shareable;

    private String remark;

    public final String[] value_set = {
            number, name, type,
            content, discount, store, brand, product_name,
            time, count, rest, status,
            user_level, shareable,
            remark
    };

    public final static String[] key_set = {
            HttpResult.COUPON_NUMBER, HttpResult.COUPON_NAME, HttpResult.COUPON_TYPE,
            HttpResult.COUPON_CONTENT, HttpResult.COUPON_DISCOUNT, HttpResult.COUPON_STORE, HttpResult.COUPON_BRAND, HttpResult.COUPON_PRODUCT_NAME,
            HttpResult.COUPON_TIME, HttpResult.COUPON_COUNT, HttpResult.COUPON_REST, HttpResult.COUPON_STATUS,
            HttpResult.COUPON_USER_LEVEL, HttpResult.COUPON_SHAREABLE,
            HttpResult.COUPON_REMARK
    };

    public String getNumber() {
        return value_set[0];
    }

    public String getName() {
        return value_set[1];
    }

    public String getType() {
        return value_set[2];
    }

    public String getContent() {
        return value_set[3];
    }

    public String getDiscount() {
        return value_set[4];
    }

    public String getStore() {
        return value_set[5];
    }

    public String getBrand() {
        return value_set[6];
    }

    public String getProductName() {
        return value_set[7];
    }

    public String getTime() {
        return value_set[8];
    }

    public String getCount() {
        return value_set[9];
    }

    public String getRest() {
        return value_set[10];
    }

    public String getStatus() {
        return value_set[11];
    }

    public String getUserLevel() {
        return value_set[12];
    }

    public String getShareable() {
        return value_set[13];
    }

    public String getRemark() {
        return value_set[14];
    }
}
