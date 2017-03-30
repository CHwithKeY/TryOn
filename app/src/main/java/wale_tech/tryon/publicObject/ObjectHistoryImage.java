package wale_tech.tryon.publicObject;

/**
 * Created by lenovo on 2017/2/21.
 */

public class ObjectHistoryImage {

    private String username;
    private String date_time;
    private String image_path;
    private String sku_code;
    private String last_id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDateTime() {
        return date_time;
    }

    public void setDateTime(String date_time) {
        this.date_time = date_time;
    }

    public String getImagePath() {
        return image_path;
    }

    public void setImagePath(String image_path) {
        this.image_path = image_path;
    }

    public String getSkuCode() {
        return sku_code;
    }

    public void setSkuCode(String sku_code) {
        this.sku_code = sku_code;
    }

    public String getLastId() {
        return last_id;
    }

    public void setLastId(String last_id) {
        this.last_id = last_id;
    }
}
