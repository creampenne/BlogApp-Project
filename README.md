# BlogApp-Project
### 안드로이드 프로그래밍 기말 프로젝트 - Blog App

![Android](https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=Android&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=flat-square&logo=Firebase&logoColor=white)
![Java](https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white)
</br>
![Android](https://img.shields.io/badge/AndroidStudio-3DDC84?style=flat-square&logo=AndroidStudio&logoColor=white)

- #### 추가하면 좋을 기능 :
1. 로그인 : 회원가입 버튼 생성, 구글 로그인 버튼 생성
2. 세부 게시물 : 댓글 등록 버튼 변경
3. 댓글을 입력하세요

- #### 어려웠던 부분 :
1. RegisterActivity : startActivityForResult 대체 registerForActivityResult
2. LoginActivity, RegisterActivity : this vs getApplicationContext()

- #### 해결해야 할 문제 : 
1. RegisterActivity : 저장소 권한 없을 경우 재요청 안하는 현상
2. RegisterActivity : 회원가입시 사진이 없을 경우 앱 종료 현상(onFailureListener 업로드 실패 콜백)
3. app_bar_home.xml : 타이틀바 색상 수정
