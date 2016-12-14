package wale_tech.tryon.user.setting;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import wale_tech.tryon.base.Base_Act;


/**
 * Created by KeY on 2016/8/21.
 */
public final class PermissionAction {

    public static boolean check(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkAutoRequest(Context context, String permission, int permission_code) {
        Activity activity = (Activity) context;

        if (ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{permission}, permission_code);

            return false;
        }

        return true;
    }

    public static void request(Context context, String permission, int permission_code) {
        Activity activity = (Activity) context;

        ActivityCompat.requestPermissions(activity,
                new String[]{permission}, permission_code);
    }

    public static void handle(Context context, int requestCode, int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            ((Base_Act) context).onPermissionAccepted(requestCode);
        } else {
            ((Base_Act) context).onPermissionRefused(requestCode);
        }
    }

}
