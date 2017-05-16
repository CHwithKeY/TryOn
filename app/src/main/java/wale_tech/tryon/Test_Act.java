package wale_tech.tryon;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import junit.framework.Test;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import wale_tech.tryon.base.Base_Act;
import wale_tech.tryon.publicView.ColorSnackBar;

public class Test_Act extends Base_Act {

    private ColorSnackBar snackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_layout);

        varInit();

        setupSnackBtn();

        setupDownloadBtn();

        setupPicBtn();

    }

    @Override
    public void varInit() {
        snackBar = new ColorSnackBar(this);
    }

    private void setupSnackBtn() {
        final Button snack_btn = (Button) findViewById(R.id.test_btn);
        snack_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackBar.show("Success");
//                showSnack("Success");
            }
        });
    }

    private void setupDownloadBtn() {
        final Button download_btn = (Button) findViewById(R.id.download_btn);
        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建下载任务,downloadUrl就是下载链接
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://119.29.154.234:8080/TryOnService/TryOn.apk"));
                DownloadManager.Request request2 = new DownloadManager.Request(Uri.parse("http://119.29.154.234:8080/TryOnService/capture/ws00342716659.jpg"));
                DownloadManager.Request request3 = new DownloadManager.Request(Uri.parse("http://119.29.154.234:8080/TryOnService/capture/ws00359215730877.jpg"));

                //指定下载路径和下载文件名
                request.setDestinationInExternalPublicDir("/download/", "TryOnDownload.apk");
                request2.setDestinationInExternalPublicDir("/download/", "ws00342716659.jpg");
                request3.setDestinationInExternalPublicDir("/download/", "ws00359215730877.jpg");

                //获取下载管理器
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                //将下载任务加入下载队列，否则不会进行下载
//                downloadManager.enqueue(request);
                downloadManager.enqueue(request2);
                downloadManager.enqueue(request3);

            }
        });

    }

    private void setupPicBtn() {
        final Button pic_btn = (Button) findViewById(R.id.pic_btn);
        pic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        String path = Environment.getExternalStorageDirectory().getPath();
                        path = path + "/download/";
                        ArrayList<String> picList = getFiles(path);

                        RecyclerView image_rv;

                        TestImageRycAdapter adapter = new TestImageRycAdapter(picList) {
                            @Override
                            public void onLoadMore() {

                            }
                        };
                        image_rv = (RecyclerView) findViewById(R.id.pic_rv);
                        image_rv.setLayoutManager(new GridLayoutManager(Test_Act.this, 3));
                        image_rv.setAdapter(adapter);

                    }
                }.start();

//                Intent intent = new Intent();
//                /* 开启Pictures画面Type设定为image */
//                intent.setType("image/*");
//                /* 使用Intent.ACTION_GET_CONTENT这个Action */
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                /* 取得相片后返回本画面 */
//                startActivityForResult(intent, 1);

            }
        });
    }

    private ArrayList<String> getFiles(String string) {
        // TODO Auto-generated method stub
        ArrayList<String> picList = new ArrayList<>();

        File file = new File(string);
        File[] files = file.listFiles();
        for (int j = 0; j < files.length; j++) {
            String name = files[j].getName();
            if (files[j].isDirectory()) {
                String dirPath = files[j].toString().toLowerCase();
                System.out.println(dirPath);
                getFiles(dirPath + "/");
            } else if (files[j].isFile() & name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".bmp") || name.endsWith(".gif") || name.endsWith(".jpeg")) {
                System.out.println("FileName===" + files[j].getName());
                picList.add(files[j].getName());
            }
        }

        return picList;
    }

    @Override
    protected void setupToolbar() {

    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {

    }

    @Override
    public void onNullResponse(String tag) throws JSONException {

    }

    @Override
    public void onPermissionAccepted(int permission_code) {

    }

    @Override
    public void onPermissionRefused(int permission_code) {

    }

    private ProgressBar createProgressBar(Drawable customIndeterminateDrawable) {
        // activity根部的ViewGroup，其实是一个FrameLayout
        ContentFrameLayout rootContainer = (ContentFrameLayout) findViewById(android.R.id.content);
        // 给progressbar准备一个FrameLayout的LayoutParams
        ContentFrameLayout.LayoutParams lp = new ContentFrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置对其方式为：屏幕居中对其
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.GONE);
        progressBar.setLayoutParams(lp);
        // 自定义小菊花
        if (customIndeterminateDrawable != null) {
            progressBar.setIndeterminateDrawable(customIndeterminateDrawable);
        }
        // 将菊花添加到FrameLayout中
        rootContainer.addView(progressBar);

        Log.i("Result", "create");

        return progressBar;
    }
}
