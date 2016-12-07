package wale_tech.tryon.update;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import wale_tech.tryon.R;
import wale_tech.tryon.base.BaseAction;
import wale_tech.tryon.http.HttpAction;
import wale_tech.tryon.http.HttpHandler;
import wale_tech.tryon.http.HttpResult;
import wale_tech.tryon.http.HttpSet;
import wale_tech.tryon.http.HttpTag;
import wale_tech.tryon.publicClass.Methods;

/**
 * Created by KeY on 2016/8/8.
 */
public class UpdateAction extends BaseAction {

    private DownloadThread thread;

    public UpdateAction(Context context) {
        super(context);
    }

    public void checkVersion(boolean isShowDialog) {
        if (!checkNet()) {
            return;
        }

        String version = Methods.getVersionName(context);

        String[] key = {HttpSet.KEY_APP_VERSION};
        String[] value = {version};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_CHECK_VERSION);
        action.setTag(HttpTag.UPDATE_CHECK_VERSION);
        if(isShowDialog) {
            action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        }
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public void handleResponse(String result, boolean isShowToast) throws JSONException {
//        Log.i("Result", "update Result is : " + result);

        JSONObject obj = new JSONObject(result);
        String description = obj.getString(HttpResult.RESULT);

        if (!description.isEmpty()) {
            String[] description_arr = description.split("-");

            StringBuilder builder = new StringBuilder("");
            for (String aDescription_arr : description_arr) {
                builder.append(aDescription_arr).append("\n\n");
            }

            builder.append(context.getString(R.string.update_dialog_msg));
            String description_msg = builder.toString();

            new AlertDialog.Builder(context)
                    .setTitle(context.getString(R.string.update_dialog_title))
//                    .setMessage("检查到有新版本，是否更新？")
                    .setMessage(description_msg)
                    .setPositiveButton(context.getString(R.string.base_dialog_btn_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downLoadApk();
                        }
                    })
                    .setNegativeButton(context.getString(R.string.base_dialog_btn_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        } else {
            if (isShowToast) {
                showSnack(context.getString(R.string.update_snack_is_Latest_version));
//                toast.showToast(context.getString(R.string.update_toast_is_Latest_version));
            }
        }
    }

    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage(context.getString(R.string.update_downloading_latest_version));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        pd.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.base_dialog_btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.cancel();
                    dialog.dismiss();

                    if (thread != null && !thread.isInterrupted()) {
                        Log.i("Result", "thread interrupt");
                        thread.interrupt();
//                        thread = null;
                    }
                }
            }
        });

        pd.show();

        thread = new DownloadThread(pd);
        thread.start();
    }

    private class DownloadThread extends Thread {
        private ProgressDialog pd;

        DownloadThread(ProgressDialog pd) {
            this.pd = pd;
        }

        @Override
        public void run() {
            try {
                File file = getFileFromServer(HttpSet.BASE_URL + HttpSet.URL_VERSION, pd);
//                sleep(1500);
                installApk(file);
                pd.dismiss(); //结束掉进度条对话框
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd", Locale.CHINA);
        String date = format.format(new Date());

        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdcard_dir = Environment.getExternalStorageDirectory();
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            String file_path = sdcard_dir.getPath() + "/WaleSmart";
            File app_dir = new File(file_path);

            if (!app_dir.exists()) {
                app_dir.mkdir();
            }

            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();

            String apk_path = file_path + "/";
            File apk_file = new File(apk_path);

            if (!apk_file.exists()) {
                apk_file.createNewFile();
            }
            String apk_name = "WaleSmart" + date + ".apk";

            File file = new File(apk_file, apk_name);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
