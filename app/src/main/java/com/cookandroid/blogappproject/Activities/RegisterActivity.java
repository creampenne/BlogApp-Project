package com.cookandroid.blogappproject.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.cookandroid.blogappproject.R;

public class RegisterActivity extends AppCompatActivity {

    ImageView ImgUserPhoto;
    static int PReqCode = 1;
    Intent intent;
    Uri pickedImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImgUserPhoto = findViewById(R.id.regUserPhoto);

        // Anonymous Class -> Lambda
        ImgUserPhoto.setOnClickListener(view -> {

            if (Build.VERSION.SDK_INT >= 22) {

                checkAndRequestForPermission();

            } else {
                openGallery();
            }

        });

    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        launcher.launch(galleryIntent);

    }

    // Check Permission
    // TODO : 권한 부여되지 않았을 때 재요청
    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(RegisterActivity.this, "권한이 필요합니당", Toast.LENGTH_SHORT).show();

            }
            else {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                                    PReqCode);
            }

        }
        else
            openGallery();

    }

    // Gallery Photo Callback
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult data) {

            if (data.getResultCode() == RESULT_OK) {

                intent = data.getData();
                assert intent != null;
                pickedImgUri = intent.getData();
                ImgUserPhoto.setImageURI(pickedImgUri);

            }

        }

    });

}