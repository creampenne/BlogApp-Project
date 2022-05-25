package com.cookandroid.blogappproject.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.blogappproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ImageView ImgUserPhoto;
    private EditText userEmail, userPassword;
    private Button logBtn;
    private ProgressBar loadingProgress;

    private FirebaseAuth mAuth;

    private Intent homeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImgUserPhoto = findViewById(R.id.logUserPhoto);

        userEmail = findViewById(R.id.logMail);
        userPassword = findViewById(R.id.logPassword);
        logBtn = findViewById(R.id.logBtn);
        loadingProgress = findViewById(R.id.logProgressBar);

        loadingProgress.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        homeActivity = new Intent(this, com.cookandroid.blogappproject.Activities.Home.class);

        // Lambda 변경
        ImgUserPhoto.setOnClickListener(view -> {
            Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(registerActivity);
            finish();
        });

        // Lambda 변경
        logBtn.setOnClickListener(view -> {
            logBtn.setVisibility(View.INVISIBLE);
            loadingProgress.setVisibility(View.VISIBLE);

            final String mail = userEmail.getText().toString();
            final String password = userPassword.getText().toString();

            if (mail.isEmpty() || password.isEmpty()) {
                showMessage("이메일, 패스워드를 입력해주세용");
                logBtn.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);
            } else {
                signIn(mail, password);
            }
        });
    }

    // 로그인 확인
    private void signIn(String email, String password) {
        // Lambda 변경
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                loadingProgress.setVisibility(View.INVISIBLE);
                logBtn.setVisibility(View.VISIBLE);
                showMessage("로그인 완료!");
                updateUI();
            } else {
                showMessage("로그인 실패!\n" + Objects.requireNonNull(task.getException()).getMessage());
                logBtn.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);
            }
        });
    }

    // Toast 메시지
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // Home 액티비티 실행
    private void updateUI() {
        startActivity(homeActivity);
        finish();
    }

    // 앱 실행시 로그인 확인
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            updateUI();
        }
    }
}
