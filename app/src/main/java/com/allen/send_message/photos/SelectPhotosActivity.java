package com.allen.send_message.photos;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.send_message.R;
import com.allen.send_message.adapter.PictureAdapter;
import com.allen.send_message.utils.ImageFloder;
import com.allen.send_message.utils.ImageItem;
import com.allen.send_message.utils.SelectPhotosEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by allen on 2016/12/2.
 * <p>
 * 选择照片
 */

public class SelectPhotosActivity extends Activity {


    @BindView(R.id.select_cancel_bt)
    Button selectCancelBt;
    @BindView(R.id.select_ok_bt)
    Button selectOkBt;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.select_photo_gv)
    GridView selectPhotoGv;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    private ImageFloder imageAll, currentImageFolder;
    /**
     * 最多选择图片的个数
     */
    private static int MAX_NUM = 6;
    private static final int TAKE_PICTURE = 520;
    public static final String INTENT_MAX_NUM = "intent_max_num";
    public static final String INTENT_SELECTED_PICTURE = "intent_selected_picture";
    private Context context;
    private  PictureAdapter adapter;
//    private ImageLoader loader;
    private ContentResolver mContentResolver;
    //选择的照片怎么通过这个传出去
    private ArrayList<String> selectedPicture = new ArrayList<String>();
    private String cameraPath = null;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();
    private ArrayList<ImageFloder> mDirPaths = new ArrayList<ImageFloder>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        ButterKnife.bind(this);
        init();
    }
    private void init() {
        MAX_NUM = getIntent().getIntExtra(INTENT_MAX_NUM, 6);
        context = this;
        mContentResolver = getContentResolver();
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        imageAll = new ImageFloder();
        currentImageFolder = imageAll;
        mDirPaths.add(imageAll);
        selectOkBt.setText("完成0/" + MAX_NUM);
        adapter = new PictureAdapter(this,currentImageFolder.images);
        selectPhotoGv.setAdapter(adapter);
        selectPhotoGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    goCamare();
                }
            }
        });
        getThumbnail();
    }
    /**
     * 得到缩略图
     */
    private void getThumbnail() {
        Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.ImageColumns.DATA }, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        if (mCursor.moveToFirst()) {
            int _date = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                // 获取图片的路径
                String path = mCursor.getString(_date);
                // Log.e("TAG", path);
                ImageItem item= new ImageItem();
                item.setPath(path);
                imageAll.images.add(item);
                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                ImageFloder imageFloder = null;
                String dirPath = parentFile.getAbsolutePath();
                if (!tmpDir.containsKey(dirPath)) {
                    // 初始化imageFloder
                    imageFloder = new ImageFloder();
                    imageFloder.setDir(dirPath);
                    imageFloder.setFirstImagePath(path);
                    mDirPaths.add(imageFloder);
                    // Log.d("zyh", dirPath + "," + path);
                    tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
                } else {
                    imageFloder = mDirPaths.get(tmpDir.get(dirPath));
                }
                imageFloder.images.add(new ImageItem(path));
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        tmpDir = null;
    }

    /**
     * 使用相机拍照
     *
     * @version 1.0
     * @author zyh
     */
    protected void goCamare() {
        if (selectedPicture.size() + 1 > MAX_NUM) {
            Toast.makeText(context, "最多选择" + MAX_NUM + "张", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = getOutputMediaFileUri();
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }
    /**
     * 用于拍照时获取输出的Uri
     *
     * @version 1.0
     * @author zyh
     * @return
     */
    protected Uri getOutputMediaFileUri() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Night");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        File mediaFile = new File(".jpg");
        cameraPath = mediaFile.getAbsolutePath();
        return Uri.fromFile(mediaFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && cameraPath != null) {
            selectedPicture.add(cameraPath);
            Intent data2 = new Intent();
            data2.putExtra(INTENT_SELECTED_PICTURE, selectedPicture);
            setResult(RESULT_OK, data2);
            this.finish();
        }
    }

    @OnClick({R.id.select_cancel_bt, R.id.select_ok_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_cancel_bt:
                this.finish();
                break;
            case R.id.select_ok_bt:
                Intent data = new Intent();
                data.putExtra(INTENT_SELECTED_PICTURE, selectedPicture);
                setResult(RESULT_OK, data);
                this.finish();
                break;
        }
    }

    /**
     * 获取选了多少张图片
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectPhotosEvent(SelectPhotosEvent event) {
       int photoNum= event.getSize();
        selectOkBt.setEnabled(photoNum>0);
        selectOkBt.setText("完成" + photoNum + "/" + MAX_NUM);
    }
}
