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
        // Lambda 변경
        fab.setOnClickListener(view -> popupAddPost.show());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();

        // HomeFragment를 메인 화면으로 지정
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }

    // Toast 메시지
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // 게시글 이미지 업로드
    private void setupPopupImageClick() {
        // Lambda 변경
        popupPostImage.setOnClickListener(view -> checkAndRequestForPermission());
    }

    // 앱 저장소 권환 확인
    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(Home.this, "권한이 필요합니당", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
            }
        } else
            openGallery();
    }

    // 갤러리 열기
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        launcher.launch(galleryIntent);
    }

    // 갤러리 사진 콜백
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

    // PopUp 위젯
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

        // 현재 사용자 프로필 이미지 불러오기
        Glide.with(Home.this).load(currentUser.getPhotoUrl()).into(popupUserImage);

        // 업로드 버튼 클릭시
        // Lambda 변경
        popupAddBtn.setOnClickListener(view -> {
            popupAddBtn.setVisibility(View.INVISIBLE);
            loadingProgress.setVisibility(View.VISIBLE);

            // 제목, 내용, 사진 파이어베이스 업로드 및 게시
            if (!popupTitle.getText().toString().isEmpty() && !popupDescription.toString().isEmpty() &&pickedImgUri != null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());

                // Lambda 변경
                imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageDownloadLink = uri.toString();
                    Post post = new Post(popupTitle.getText().toString(), popupDescription.getText().toString(), imageDownloadLink, currentUser.getUid(), Objects.requireNonNull(currentUser.getPhotoUrl()).toString());

                    addPost(post);
                }).addOnFailureListener(e -> {
                    showMessage("업로드를 실패했습니당\n" + e.getMessage());
                    loadingProgress.setVisibility(View.VISIBLE);
                }));
            } else {
                showMessage("모든 항목을 입력해주세용");
                popupAddBtn.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);
            }
        });

    }

    // 파이어베이스 업로드 및 게시
    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        // postKey(Setter)
        String key = myRef.getKey();
        post.setPostKey(key);

        // 업로드 성공
        // Lambda 변경
        myRef.setValue(post).addOnSuccessListener(unused -> {
            showMessage("업로드 완료!");
            loadingProgress.setVisibility(View.INVISIBLE);
            popupAddBtn.setVisibility(View.VISIBLE);
            popupAddPost.dismiss();
        });
    }

    // 뒤로가기 버튼 종료
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // 메뉴 정보 선택
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("홈");
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

    // nav_header_home 정보 업데이트
    public void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        ImageView navUserPhot = headerView.findViewById(R.id.nav_user_photo);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);

        navUserMail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());
        // Glide로 사용자 이미지 불러오기
        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhot);
    }
}