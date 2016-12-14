package wale_tech.tryon.sharedinfo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by KeY on 2016/6/3.
 */
public final class SharedAction {

    private SharedPreferences sp;

    public SharedAction() {

    }

    public SharedAction(Context context) {
        sp = context.getSharedPreferences(SharedSet.NAME, Context.MODE_PRIVATE);
    }

    public void setShared(SharedPreferences sp) {
        this.sp = sp;
    }

    public void clearLastIdInfo() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(SharedSet.KEY_LAST_ID, 0);
        editor.apply();
    }

    public void setLoginStatus(String username) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SharedSet.KEY_IS_LOGIN, true);
        editor.putString(SharedSet.KEY_USERNAME, username);
        editor.apply();
    }

    public boolean getLoginStatus() {
        return sp.getBoolean(SharedSet.KEY_IS_LOGIN, false);
    }

    public String getUsername() {
        return sp.getString(SharedSet.KEY_USERNAME, null);
    }

    public void clearLoginStatus() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SharedSet.KEY_IS_LOGIN, false);
        editor.putString(SharedSet.KEY_USERNAME, "");
        editor.apply();
    }

    // Last_Id
    public void setLastId(int last_id) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(SharedSet.KEY_LAST_ID, last_id);
        editor.apply();
    }

    public int getLastId() {
        return sp.getInt(SharedSet.KEY_LAST_ID, 0);
    }


    public void setAppLanguage(String language) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SharedSet.KEY_APP_LANGUAGE, language);
        editor.apply();
    }

    public String getAppLanguage() {
        return sp.getString(SharedSet.KEY_APP_LANGUAGE, SharedSet.LANGUAGE_CHINESE);
    }

    public void setRefreshTime(int refresh_time) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(SharedSet.KEY_REFRESH_TIME, refresh_time);
        editor.apply();
    }

    public int getRefreshTime() {
        return sp.getInt(SharedSet.KEY_REFRESH_TIME, 2500);
    }

    public void setNet(int net) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(SharedSet.KEY_NET, net);
        editor.apply();
    }

    public int getNet() {
        return sp.getInt(SharedSet.KEY_NET, 0);
    }

    public void setAppLaunch(boolean isLaunch) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SharedSet.KEY_APP_LAUNCH, isLaunch);
        editor.apply();
    }

    public boolean getAppLaunch() {
        return sp.getBoolean(SharedSet.KEY_APP_LAUNCH, false);
    }

    public void setAppFstLaunch(boolean isFstLaunch) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SharedSet.KEY_APP_FST_LAUNCH, isFstLaunch);
        editor.apply();
    }

    public boolean getAppFstLaunch() {
        return sp.getBoolean(SharedSet.KEY_APP_FST_LAUNCH, false);
    }

    public void setNoticeEnter(boolean isNoticeEnter) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SharedSet.KEY_NOTICE_ENTER, isNoticeEnter);
        editor.apply();
    }

    public boolean getNoticeEnter() {
        return sp.getBoolean(SharedSet.KEY_NOTICE_ENTER, false);
    }

}
