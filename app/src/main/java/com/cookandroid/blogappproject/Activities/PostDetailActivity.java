package com.cookandroid.blogappproject.Activities;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.blogappproject.Adapters.CommentAdapter;
import com.cookandroid.blogappproject.Models.Comment;
import com.cookandroid.blogappproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PostDetailActivity extends AppCompatActivity {

    ImageView imgPost, imgUserPost, imgCurrentUser;
    TextView txtPostTitle, txtPostDateName, txtPostDesc ;
    EditText editTextComment;
    Button btnAddComment;
    String PostKey;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;

    static String COMMENT_KEY = "Comments";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Objects.requireNonNull(getSupportActionBar()).hide();

        imgPost = findViewById(R.id.post_detail_img);
        imgUserPost = findViewById(R.id.post_detail_user_img);
        imgCurrentUser = findViewById(R.id.post_detail_currentuser_img);

        txtPostTitle = findViewById(R.id.post_detail_title);
        txtPostDateName = findViewById(R.id.post_detail_date_name);
        txtPostDesc = findViewById(R.id.post_detail_desc);

        editTextComment = findViewById(R.id.post_detail_comment);
        btnAddComment = findViewById(R.id.post_detail_add_comment_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        RvComment = findViewById(R.id.rv_comment);

        // 댓글 등록 버튼 클릭시 데이터 넣기
        // Lambda 변경
        btnAddComment.setOnClickListener(view -> {
            final String comment = editTextComment.getText().toString();

            if (comment.isEmpty() ) {
                showMessage("댓글을 입력하세용");
            } else {
                btnAddComment.setVisibility(View.INVISIBLE);

                DatabaseReference commentReference = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey).push();

                String comment_content = editTextComment.getText().toString();
                String userId = firebaseUser.getUid();
                String userPhoto = Objects.requireNonNull(firebaseUser.getPhotoUrl()).toString();
                String userName = firebaseUser.getDisplayName();

                Comment comments = new Comment(comment_content, userId, userPhoto, userName);

                // Lambda 변경
                commentReference.setValue(comments).addOnSuccessListener(unused -> {
                    showMessage("댓글 등록 완료!");
                    editTextComment.setText("");
                    btnAddComment.setVisibility(View.VISIBLE);
                }).addOnFailureListener(e -> showMessage("댓글 등록에 실패했습니당\n" + e.getMessage()));
            }
        });

        // 게시물 데이터 받아와서 할당
        PostKey = getIntent().getExtras().getString("postKey");

        String postImage = getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImage).into(imgPost);

        String postTitle = getIntent().getExtras().getString("title");
        txtPostTitle.setText(postTitle);

        String date = timestampToString(getIntent().getExtras().getLong("postDate"));
        txtPostDateName.setText(date);

        String userpostImage = getIntent().getExtras().getString("userPhoto");
        Glide.with(this).load(userpostImage).into(imgUserPost);

        String postDescription = getIntent().getExtras().getString("description");
        txtPostDesc.setText(postDescription);

        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgCurrentUser);

        // 댓글
        iniRvComment();
    }

    // 댓글 데이터 받아오기
    private void iniRvComment() {
        RvComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap:snapshot.getChildren()) {
                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);
                }
                commentAdapter = new CommentAdapter(getApplicationContext(), listComment);
                RvComment.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Toast 메시지
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    // 시간 정보 변환 및 반환
    private String timestampToString(long postDate) {
        Calendar calendar = Calendar.getInstance(Locale.KOREAN);
        calendar.setTimeInMillis(postDate);
        return DateFormat.format("yyyy년 MM월 dd일 HH:mm",calendar).toString();
    }
}