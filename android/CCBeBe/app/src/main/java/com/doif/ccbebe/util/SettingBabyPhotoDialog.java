package com.doif.ccbebe.util;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.doif.ccbebe.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SettingBabyPhotoDialog extends Dialog implements View.OnClickListener{

    private String TAG = "[BabyPhotoDlg] ";

    private final int CAMERA_PERMISSION_REQUEST = 1000;
    private final int GALLERY_PERMISSION_REQUEST = 1001;
    private String FILE_NAME = "picture.jpg";

    private Context context;
    private Activity activity;
    private DisplayMetrics displayMetrics;

    private LinearLayout setting_baby_photo_layout;
    private TextView selecting_camera;
    private TextView selecting_gallery;
    private ImageView uploaded_image;


    public SettingBabyPhotoDialog(Context context, Activity activity, DisplayMetrics displayMetrics, ImageView uploaded_image){
        super(context);

        this.context = context;
        this.activity = activity;
        this.displayMetrics = displayMetrics;
        this.uploaded_image = uploaded_image;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_setting_baby_photo);

        initialize();
    }

    private void initialize(){
        // bind view
        setting_baby_photo_layout = findViewById(R.id.setting_baby_photo_layout);
        selecting_camera = findViewById(R.id.selecting_camera);
        selecting_gallery = findViewById(R.id.selecting_gallery);

        // 크기 설정
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) setting_baby_photo_layout.getLayoutParams();
        params.width = (int)(displayMetrics.widthPixels * 0.95);
        setting_baby_photo_layout.setLayoutParams(params);

        // 배경 투명
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setGravity(Gravity.BOTTOM);

        // attach listener
        selecting_camera.setOnClickListener(this);
        selecting_gallery.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){

        switch (view.getId()){
            case R.id.selecting_camera:
                Log.d(TAG, "cameraOnClick");
                checkCameraPermission();
                break;
            case R.id.selecting_gallery:
                Log.d(TAG, "galleryOnClick");
                checkGalleryPermission();
                break;
        }

        dismiss();
    }


    private void checkCameraPermission() {
        if(new PermissionUtil().requestPermsiion(activity,
                CAMERA_PERMISSION_REQUEST,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE)){
            openCamera();
        }
    }

    private void checkGalleryPermission() {
        if(new PermissionUtil().requestPermsiion(activity,
                GALLERY_PERMISSION_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE)){
            openGallery();
        }
    }

    private void openCamera(){
        Uri photoUri = FileProvider.getUriForFile(context,
                context.getOpPackageName() + ".provider",
                createCameraFile());

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

//        startActivityForResult(intent, CAMERA_PERMISSION_REQUEST);

//        Uri photoUri = FileProvider.getUriForFile(context,
//                context.getOpPackageName() + ".provider",
//                createCameraFile());

        uploadImage(photoUri);


    }

    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        uploadImage(intent.getData());

//        startActivityForResult(Intent.createChooser(intent, "Select to photo"),
//                GALLERY_PERMISSION_REQUEST);
    }

    private File createCameraFile(){
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    private void uploadImage(Uri imageUri){
        try{
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);

            dismiss();

            SharedPreferences sharedPreferences = context.getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("babyPhoto", bitmapToString(bitmap));

            editor.commit();

            uploaded_image.setImageBitmap(bitmap);

        }catch (IOException e){
            Log.e(TAG, "IOException is occurs..",e);
            e.printStackTrace();
        }
    }

    private String bitmapToString(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);

        byte[] b = byteArrayOutputStream.toByteArray();

        String result = Base64.encodeToString(b, Base64.DEFAULT);

        return result;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode){
//            case CAMERA_PERMISSION_REQUEST:
//                if(resultCode != Activity.RESULT_OK) break;
//                Uri photoUri = FileProvider.getUriForFile(context,
//                        context.getOpPackageName() + ".provider",
//                        createCameraFile());
//                uploadImage(photoUri);
//                break;
//
//            case GALLERY_PERMISSION_REQUEST:
//                uploadImage(data.getData());
//                break;
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode){
//            case CAMERA_PERMISSION_REQUEST:
//                if(new PermissionUtil().permissionGranted(requestCode, CAMERA_PERMISSION_REQUEST, grantResults))
//                    openCamera();
//            case GALLERY_PERMISSION_REQUEST:
//                if(new PermissionUtil().permissionGranted(requestCode, GALLERY_PERMISSION_REQUEST, grantResults))
//                    openGallery();
//        }
//    }

}
