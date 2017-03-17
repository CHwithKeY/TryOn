package wale_tech.tryon.publicObject;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import wale_tech.tryon.http.HttpResult;
import wale_tech.tryon.http.HttpSet;

/**
 * Created by lenovo on 2017/2/21.
 */

public class ObjectUtil {

    public static ObjectHistoryImage wrapHistoryImage(JSONObject object) throws JSONException {
        ObjectHistoryImage historyImage = new ObjectHistoryImage();

        historyImage.setUsername(object.getString(HttpResult.USERNAME));
        historyImage.setDateTime(object.getString(HttpResult.DATE_TIME));
        historyImage.setImagePath(object.getString(HttpResult.IMAGE_PATH));
        historyImage.setSkuCode(object.getString(HttpResult.SKU_CODE));
        historyImage.setLastId(object.getString(HttpResult.LAST_ID));

        return historyImage;
    }
}
