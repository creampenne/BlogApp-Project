package com.cookandroid.blogappproject.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cookandroid.blogappproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    static int PReqCode = 1;
    Intent intent;
    Uri pickedImgUri;

    ImageView ImgUserPhoto;
    private EditText userName, userEmail, userPassword, userPassword2;
    private Button regBtn;
    private ProgressBar loadingProgress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImgUserPhoto = findViewById(R.id.regUserPhoto);

        userName = findViewById(R.id.regName);
        userEmail = findViewById(R.id.regMail);
        userPassword = findViewById(R.id.regPassword);
        userPassword2 = findViewById(R.id.regPassword2);
        regBtn = findViewById(R.id.regBtn);
        loadingProgress = findViewById(R.id.regProgressBar);

        // progressbar default : invisible
        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        // Anonymous Class -> Lambda
        regBtn.setOnClickListener(view -> {
            regBtn.setVisibility(View.INVISIBLE);
            loadingProgress.setVisibility(View.VISIBLE);

            final String name = userName.getText().toString();
            final String email = userEmail.getText().toString();
            final String password = userPassword.getText().toString();
            final String password2 = userPassword2.getText().toString();

            if (email.isEmpty() || name.isEmpty() || password.isEmpty() || password2.isEmpty() || !password.equals(password2)) {
                // error
                showMessage("모든 항목을 입력해주세용");
                regBtn.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);
            } else {
                // OK
                CreateUserAccount(email, name, password);
            }
        });

        // Anonymous Class -> Lambda
        ImgUserPhoto.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= 22) {
                checkAndRequestForPermission();
            } else {
                openGallery();
            }
        });
    }

    // Create user account
    private void CreateUserAccount(String email, String name, String password) {
        // Anonymous Class -> Lambda
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // user account created successfully
                showMessage("계정을 생성했습니당");
                // update name, picture
                updateUserInfo(name, pickedImgUri, mAuth.getCurrentUser());
            } else {
                // account creation failed
                showMessage("계정 생성을 실패했습니당\n" + Objects.requireNonNull(task.getException()).getMessage());
                regBtn.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Update name, picture
    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        // upload picture to firebase storage, get url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());

        // Anonymous Class -> Lambda
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {
            // picture uploaded successfully, get picture url

            // Anonymous Class -> Lambda
            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(uri)
                        .build();
                // Anonymous Class -> Lambda
                currentUser.updateProfile(profileUpdate).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // user info updated successfully
                        showMessage("회원가입 완료!");
                        updateUI();
                    }
                });
            });
        });
    }

    // simple method to show toast message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Open Gallery
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        launcher.launch(galleryIntent);
    }

    // Change Activity
    private void updateUI() {
        Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(homeActivity);
        finish();
    }

    // Check Permission
    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(RegisterActivity.this, "권한이 필요합니당", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        } else
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