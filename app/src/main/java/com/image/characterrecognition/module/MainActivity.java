package com.image.characterrecognition.module;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.image.characterrecognition.R;
import com.image.characterrecognition.utils.DialogLoadingFMV4;
import com.image.characterrecognition.utils.DialogUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.io.FileOutputStream;

import io.reactivex.functions.Consumer;


public class MainActivity extends AppCompatActivity implements MainContract.View, TakePhoto.TakeResultListener, InvokeListener, View.OnClickListener {
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private Button btnPhoto;
    private Button btnPhotos;
    private Button btn_reset;
    private TextView tv_content;
    private TextView tv_path;
    private CropOptions cropOptions;    //裁剪参数
    private CompressConfig compressConfig;  //压缩参数
    private Uri imageUri;       //图片保存路径
    private MainPresenter mainPresenter;
    private File file1;
    private RxPermissions rxPermissions;
    private Boolean isPrand = true;
    private DialogLoadingFMV4 dialogLoadingFMV4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getTakePhoto().onCreate(savedInstanceState);
        initView();
        if (Build.VERSION.SDK_INT >= 23) {  //6.0才用动态权限
            //申请相关权限
            initPermission();
        }
        initData();  //设置压缩、裁剪参数
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    private void initData() {
        mainPresenter = new MainPresenter(this);
        takePhoto = getTakePhoto();
        initFileDir();
        tv_path.setText("当前文件的路径" + getFilePath());
    }

    @SuppressLint("CheckResult")
    private void initPermission() {
        rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            isPrand = aBoolean;
                        } else {
                            Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void initView() {
        btnPhoto = findViewById(R.id.btnPhoto1);
        btnPhotos = findViewById(R.id.btnPhoto2);
        tv_content = findViewById(R.id.tv_content);
        tv_path = findViewById(R.id.tv_path);
        btn_reset = findViewById(R.id.btn_reset);
        btnPhoto.setOnClickListener(this);
        btnPhotos.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        dialogLoadingFMV4 = DialogUtil.getProgressDialogV4(this, "图片识别中...");
    }

    @Override
    public void onClick(View v) {
        if (!isPrand) {
            Toast.makeText(this, "请授权app读取文件权限和打开相机权限", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()) {
            case R.id.btnPhoto1:
                showDialog();
                imageUri = getImageCropUri();
                takePhoto.onPickFromCapture(imageUri);
                break;
            case R.id.btnPhoto2:
                showDialog();
                imageUri = getImageCropUri();
                takePhoto.onPickFromGallery();
                break;
            case R.id.btn_reset:
                initFileDir();
                tv_content.setText("");
                tv_path.setText("当前文件的路径" + getFilePath());
                Toast.makeText(this, "已经重新建立文件保存路径", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        getAsscessToken(result);
    }

    private TResult result;

    public void getAsscessToken(TResult result) {
        this.result = result;
        mainPresenter.getAccessToken();

    }

    @Override
    public void getTokenSuccess() {
        TImage image = result.getImage();
        String originalPath = image.getOriginalPath();
        if (image.getFromType() == TImage.FromType.CAMERA) {
            originalPath = Environment.getExternalStorageDirectory() + originalPath;
        }
        mainPresenter.getRecognitionResultByImage(originalPath);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Toast.makeText(this, "识别失败" + msg, Toast.LENGTH_SHORT).show();
        hideDialog();
    }

    @Override
    public void takeCancel() {
        hideDialog();
    }

    //获得照片的输出保存Uri
    private Uri getImageCropUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        return Uri.fromFile(file);
    }


    @Override
    public void updateUI(String s) {
        hideDialog();
        String text = tv_content.getText().toString();
        if (TextUtils.isEmpty(text)) {
            tv_content.setText(s);
        } else {
            tv_content.append(s);
        }
        writeFile(s);
    }

    @Override
    public void showDialog() {
        DialogUtil.show(this, dialogLoadingFMV4);
    }

    @Override
    public void hideDialog() {
        DialogUtil.dismiss(this, dialogLoadingFMV4);
    }

    @Override
    public void errorMessage(String mesage) {
        Toast.makeText(this, "识别失败" + mesage, Toast.LENGTH_SHORT).show();
        hideDialog();
    }

    private void writeFile(final String content) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file1.getAbsoluteFile() + "/1.txt", true);
                    //true表示在文件末尾追加
                    fos.write(content.getBytes());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void initFileDir() {
        file1 = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis());
        if (!file1.exists()) {
            file1.mkdirs();
        }
    }

    public String getFilePath() {
        return file1.getAbsolutePath() + "/1.txt";
    }


}
