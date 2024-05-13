package com.ren.dianav2.screens;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ren.dianav2.R;
import com.ren.dianav2.listener.CameraImagePermissionHandler;
import com.ren.dianav2.listener.GalleryPermissionHandler;

public class EditProfileScreen extends AppCompatActivity {

    private static final String[] REQUIRED_PERMISSIONS = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.CAMERA
    };
    private static final String TAG = "Permission";
    private Button takePhotoButton;
    private Button uploadPhotoButton;
    private ImageButton backButton;
    private boolean isStorageImagePermitted = false;
    private boolean isCameraPermitted = false;
    private Uri uri;
    private ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.edit_profile_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        takePhotoButton = findViewById(R.id.button_take_photo);
        uploadPhotoButton = findViewById(R.id.button_upload_photo);
        ivProfile = findViewById(R.id.iv_profile);
        backButton = findViewById(R.id.ib_back);

        onClickTakePhoto(takePhotoButton);
        onClickUploadPhoto(uploadPhotoButton);
        onClickBackButton(backButton);
    }

    private void onClickTakePhoto(Button button) {
        button.setOnClickListener(v ->
                cameraImagePermissionHandler.requestStorageImageAndCameraPermission());
    }

    private void onClickUploadPhoto(Button button) {
        button.setOnClickListener(v -> galleryPermissionHandler.requestGalleryPermission());
    }

    private void onClickBackButton(ImageButton button) {
        button.setOnClickListener(v -> finish());
    }
    //---------------------------- INIT PERMISSIONS ----------------------------------//

    private final CameraImagePermissionHandler cameraImagePermissionHandler = () -> {
        if (!isStorageImagePermitted) {
            requestStorageImagePermission();
        }
        if (isCameraPermitted) {
            openCamera();
        } else {
            requestPermissionsCamera();
        }
    };

    private final GalleryPermissionHandler galleryPermissionHandler = () -> {
        if (!isStorageImagePermitted) {
            requestStorageImagePermission();
        }
        if (isCameraPermitted) {
            openGallery();
        }
    };

    //---------------------------- INIT OPEN CAMERA ----------------------------------//

    public void openCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "New Picture");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Captured by User name");
        uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        launcherCamera.launch(intent);
    }

    public void requestStorageImagePermission() {
        if (ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[0]) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, REQUIRED_PERMISSIONS[0] + "Granted");
            isStorageImagePermitted = true;
            requestPermissionsCamera();
        } else {
            requestPermissionLauncherStorageImages.launch(REQUIRED_PERMISSIONS[0]);
        }
    }

    private void requestPermissionsCamera() {
        if (ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSIONS[1]) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, REQUIRED_PERMISSIONS[1] + "Granted");
            isCameraPermitted = true;
        } else {
            requestPermissionLauncherCamera.launch(REQUIRED_PERMISSIONS[1]);
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncherStorageImages =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            Log.d(TAG, REQUIRED_PERMISSIONS[0] + "Granted");
                            isStorageImagePermitted = true;
                        } else {
                            Log.d(TAG, REQUIRED_PERMISSIONS[0] + "Denied");
                            isStorageImagePermitted = false;
                        }
                        requestPermissionsCamera();
                    });

    private final ActivityResultLauncher<String> requestPermissionLauncherCamera =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            Log.d(TAG, REQUIRED_PERMISSIONS[1] + "Granted");
                            isCameraPermitted = true;
                        } else {
                            Log.d(TAG, REQUIRED_PERMISSIONS[1] + "Denied");
                            isCameraPermitted = false;
                        }
                    });

    private final ActivityResultLauncher<Intent> launcherCamera =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    o -> {
                        if (o.getResultCode() == RESULT_OK) {
                            ivProfile.setImageURI(uri);
                        }
                    });

    //---------------------------- FINISH OPEN CAMERA ----------------------------------//

    //---------------------------- INIT OPEN GALLERY ----------------------------------//
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            uri = data.getData();
                            if (uri != null) {
                                ivProfile.setImageURI(uri);
                            }
                        }
                    }
                }
            });

    //---------------------------- FINISH OPEN GALLERY ----------------------------------//
}