package com.doif.ccbebe.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.doif.ccbebe.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.IOException;


public class UploadChooser extends BottomSheetDialogFragment {

    private String TAG = "[UploadChsr] ";

    private final int CAMERA_PERMISSION_REQUEST = 1000;
    private final int GALLERY_PERMISSION_REQUEST = 1001;
    private String FILE_NAME = "picture.jpg";

    private String[] permissions = new String[] {Manifest.permission.CAMERA,
                                                Manifest.permission.READ_EXTERNAL_STORAGE};

    private TextView selecting_camera;
    private TextView selecting_gallery;

    private static ImageView uploaded_image;

    private static Activity activity;
    private static Context context;

    private static String temp;

    public UploadChooser(){

        Log.d(TAG, temp);
    }

    public UploadChooser(Context context, Activity activity, ImageView uploaded_image){
        this.context = context;
        this.activity = activity;
        this.uploaded_image = uploaded_image;

        Log.d(TAG, "constructor");
    }

    public static UploadChooser getInstance(Context c, Activity a, ImageView u) {

        setArgument(c, a, u);

        return new UploadChooser();

    }

    public static void setArgument(Context c, Activity a, ImageView u){
        context = c;
        activity = a;
        uploaded_image = u;
        temp = "djfkjfjdkjfdjkfjk";
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_setting_baby_photo, container,false);

        selecting_camera = view.findViewById(R.id.selecting_camera);
        selecting_gallery = view.findViewById(R.id.selecting_gallery);

        Log.d(TAG, "onCreateView");

        selecting_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "cameraOnClick");
                hasPermissions("camera");
                if(hasPermissions("camera")){
                    openCamera();
                }else{
                    requestPermissions();
                }
            }
        });

        selecting_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "galleryOnClick");
                hasPermissions("gallery");
                if(hasPermissions("gallery")){
                    openGallery();
                }else{
                    requestPermissions();
                }
            }
        });

        return view;
    }

    private void openCamera(){

        Log.d(TAG, "openCamera");
        Uri photoUri = FileProvider.getUriForFile(context,
                context.getOpPackageName() + ".provider",
                createCameraFile());

        Log.d(TAG, "create end");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivityForResult(intent, CAMERA_PERMISSION_REQUEST);
    }

    private void openGallery(){

        Log.d(TAG, "openGallery");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select to photo"),
                GALLERY_PERMISSION_REQUEST);
    }

    private File createCameraFile(){

        Log.d(TAG, "createCameraFile");
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    private void uploadImage(Uri imageUri){

        Log.d(TAG, "uploadImage");
        try{
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

            SharedPreferences sharedPreferences = context.getSharedPreferences("ccbebe", AppCompatActivity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("babyPhoto", BitmapUtil.bitmapToString(bitmap));

            editor.commit();

            uploaded_image.setImageBitmap(bitmap);
            uploaded_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            uploaded_image.setBackground(new ShapeDrawable(new OvalShape()));

            if(Build.VERSION.SDK_INT >= 21){
                uploaded_image.setClipToOutline(true);
            }

            dismiss();

        }catch (IOException e){
            Log.e(TAG, "IOException is occurs..",e);
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult");

        switch (requestCode){
            case CAMERA_PERMISSION_REQUEST:
                if(resultCode != Activity.RESULT_OK) break;
                Uri photoUri = FileProvider.getUriForFile(context,
                        context.getOpPackageName() + ".provider",
                        createCameraFile());
                uploadImage(photoUri);
                break;

            case GALLERY_PERMISSION_REQUEST:
                uploadImage(data.getData());
                break;
        }
    }


    /**
     * permission
     * */
    private boolean hasPermissions(String type){

        int res = 0;

        if(type.equals("camera")){
            res = context.checkCallingOrSelfPermission(permissions[0]);

            if(! (res == PackageManager.PERMISSION_GRANTED))
                return  false;
        }else if(type.equals("gallery")){
            res = context.checkCallingOrSelfPermission(permissions[1]);

            if(! (res == PackageManager.PERMISSION_GRANTED))
                return  false;
        }

        return true;
    }

    private void requestPermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(permissions, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult");

        switch (requestCode){
            case CAMERA_PERMISSION_REQUEST:
                if(new PermissionUtil().permissionGranted(requestCode, CAMERA_PERMISSION_REQUEST, grantResults)){
                    openCamera();
                    dismiss();
                }

            case GALLERY_PERMISSION_REQUEST:
                if(new PermissionUtil().permissionGranted(requestCode, GALLERY_PERMISSION_REQUEST, grantResults)){
                    openGallery();
                    dismiss();
                }

        }
    }
}
