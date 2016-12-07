package wale_tech.tryon.publicObject;

import wale_tech.tryon.http.HttpResult;

/**
 * Created by KeY on 2016/6/10.
 */
public class ObjectShoe {

    private String brand;
    private String product_name;

    private String gender;
    private String color;
    private String size;
    private String type;
    private String count;
    private String price;

    private String epc_code;
    private String sku_code;

    private String shop_name;
    private String status;

    private String image_path;
    private String last_id;

    private String remark;

    public ObjectShoe() {
    }

    public final String[] value_set = {
            // Params
            brand, product_name, gender, color, size, type, count, price,
            //
            epc_code,
            sku_code,
            shop_name,
            // Backup Status
            status,
            // Image Path
            image_path,
            //
            last_id,
            //
            remark
    };

    public final static String[] key_set = {
            HttpResult.SHOE_BRAND, HttpResult.SHOE_PRODUCT_NAME,
            HttpResult.SHOE_GENDER, HttpResult.SHOE_COLOR, HttpResult.SHOE_SIZE,
            HttpResult.SHOE_TYPE, HttpResult.SHOE_COUNT, HttpResult.SHOE_PRICE,

            HttpResult.EPC_CODE,
            HttpResult.SKU_CODE,

            HttpResult.SHOP_NAME,

            HttpResult.STATUS,

            HttpResult.IMAGE_PATH,

            HttpResult.LAST_ID,

            HttpResult.REMARK
    };

    public String getBrand() {
        return value_set[0];
    }

    public String getProductName() {
        return value_set[1];
    }

    public String getGender() {
        return value_set[2];
    }

    public String getColor() {
        return value_set[3];
    }

    public String getSize() {
        return value_set[4];
    }

    public String getType() {
        return value_set[5];
    }

    public String getCount() {
        return value_set[6];
    }

    public String getPrice() {
        return value_set[7];
    }

    public String getEpcCode() {
        return value_set[8];
    }

    public String getSkuCode() {
        return value_set[9];
    }

    public String getShopName() {
        return value_set[10];
    }

    public String getStatus() {
        return value_set[11];
    }

    public String getImagePath() {
        return value_set[12];
    }

    public String getLastId() {
        return value_set[13];
    }

    public String getRemark() {
        return value_set[14];
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//
//    }
//
//    protected ObjectShoe(Parcel in) {
//        in.readStringArray(value_set);
//    }
//
//    public static final Creator<ObjectShoe> CREATOR = new Creator<ObjectShoe>() {
//        @Override
//        public ObjectShoe createFromParcel(Parcel in) {
//            return new ObjectShoe(in);
//        }
//
//        @Override
//        public ObjectShoe[] newArray(int size) {
//            return new ObjectShoe[size];
//        }
//    };
}
