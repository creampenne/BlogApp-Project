package com.cookandroid.blogappproject.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.cookandroid.blogappproject.Fragments.HomeFragment;
import com.cookandroid.blogappproject.Models.Post;
import com.cookandroid.blogappproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PReqCode = 2;
    Intent intent;
    Uri pickedImgUri;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    Dialog popupAddPost;
    ImageView popupUserImage, popupPostImage, popupAddBtn;
    TextView popupTitle, popupDescription;
    ProgressBar loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        
        Popup();
        setupPopupImageClick();

        FloatingActionButton fab = findViewById(R.id.fab);
        // Lambda ??????
        fab.setOnClickListener(view -> popupAddPost.show());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();

        // HomeFragment??? ?????? ???????????? ??????
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }

    // Toast ?????????
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // ????????? ????????? ?????????
    private void setupPopupImageClick() {
        // Lambda ??????
        popupPostImage.setOnClickListener(view -> checkAndRequestForPermission());
    }

    // ??? ????????? ?????? ??????
    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(Home.this, "????????? ???????????????", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        } else
            openGallery();
    }

    // ????????? ??????
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        launcher.launch(galleryIntent);
    }

    // ????????? ?????? ??????
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult data) {
            if (data.getResultCode() == RESULT_OK) {
                intent = data.getData();
                assert intent != null;
                pickedImgUri = intent.getData();
                popupPostImage.setImageURI(pickedImgUri);
            }
        }
    });

    // PopUp ??????
    private void Popup() {
        popupAddPost = new Dialog(this);
        popupAddPost.setContentView(R.layout.popup_add_post);
        popupAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popupAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        popupUserImage = popupAddPost.findViewById(R.id.popup_user_image);
        popupPostImage = popupAddPost.findViewById(R.id.popup_image);
        popupTitle = popupAddPost.findViewById(R.id.popup_title);
        popupDescription = popupAddPost.findViewById(R.id.popup_description);
        popupAddBtn = popupAddPost.findViewById(R.id.popup_add);
        loadingProgress = popupAddPost.findViewById(R.id.popup_progressBar);

        loadingProgress.setVisibility(View.INVISIBLE);

        // ?????? ????????? ????????? ????????? ????????????
        Glide.with(Home.this).load(currentUser.getPhotoUrl()).into(popupUserImage);

        // ????????? ?????? ?????????
        // Lambda ??????
        popupAddBtn.setOnClickListener(view -> {
            popupAddBtn.setVisibility(View.INVISIBLE);
            loadingProgress.setVisibility(View.VISIBLE);

            // ??????, ??????, ?????? ?????????????????? ????????? ??? ??????
            if (!popupTitle.getText().toString().isEmpty() && !popupDescription.toString().isEmpty() &&pickedImgUri != null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());

                // Lambda ??????
                imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageDownloadLink = uri.toString();
                    Post post = new Post(popupTitle.getText().toString(), popupDescription.getText().toString(), imageDownloadLink, currentUser.getUid(), Objects.requireNonNull(currentUser.getPhotoUrl()).toString());

                    addPost(post);
                }).addOnFailureListener(e -> {
                    showMessage("???????????? ??????????????????\n" + e.getMessage());
                    loadingProgress.setVisibility(View.VISIBLE);
                }));
            } else {
                showMessage("?????? ????????? ??????????????????");
                popupAddBtn.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);
            }
        });

    }

    // ?????????????????? ????????? ??? ??????
    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        // postKey(Setter)
        String key = myRef.getKey();
        post.setPostKey(key);

        // ????????? ??????
        // Lambda ??????
        myRef.setValue(post).addOnSuccessListener(unused -> {
            showMessage("????????? ??????!");
            loadingProgress.setVisibility(View.INVISIBLE);
            popupAddBtn.setVisibility(View.VISIBLE);
            popupAddPost.dismiss();
        });
    }

    // ???????????? ?????? ??????
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // ?????? ?????? ??????
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("???");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        } else if (id == R.id.nav_signout) {
            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // nav_header_home ?????? ????????????
    public void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        ImageView navUserPhot = headerView.findViewById(R.id.nav_user_photo);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);

        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());
        // Glide??? ????????? ????????? ????????????
        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhot);
    }
}