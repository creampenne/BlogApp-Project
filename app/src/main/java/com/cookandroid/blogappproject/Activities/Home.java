package com.cookandroid.blogappproject.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.cookandroid.blogappproject.Fragments.HomeFragment;
import com.cookandroid.blogappproject.Fragments.ProfileFragment;
import com.cookandroid.blogappproject.Fragments.SettingsFragment;
import com.cookandroid.blogappproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    Dialog popupAddPost;
    ImageView popupUserImage, popupPostImage, popupAddBtn;
    TextView popupTitle, popupDescription;
    ProgressBar loadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        
        Popup();

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
    }

    private void Popup() {
        popupAddPost = new Dialog(this);
        popupAddPost.setContentView(R.layout.popup_add_post);
        popupAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popupAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        // 팝업 위젯 초기화
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

    // 메뉴 확장
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    // ActionBar 항목 클릭
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 메뉴 정보 선택
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("홈");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        } else if (id == R.id.nav_profile) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("프로필");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
        } else if (id == R.id.nav_settings) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("설정");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment()).commit();
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