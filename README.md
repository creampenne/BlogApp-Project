# <p align="center">PortLog</p>
### <p align="center">안드로이드 프로그래밍 프로젝트</p>

<img src="/app/src/main/ic_icon-playstore.png" width="50" height="50">

![Android](https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=Android&logoColor=white)
![Java](https://img.shields.io/badge/Java-007396?style=flat-square&logo=Java&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=flat-square&logo=Firebase&logoColor=white)  
![Android](https://img.shields.io/badge/AndroidStudio-3DDC84?style=flat-square&logo=AndroidStudio&logoColor=white)

## 프로젝트 개요
- 수업 시간에 배운 이론을 바탕으로 실질적으로 가능하면서 데이터베이스를 활용한 프로젝트를 고민하여 위의 주제를 선정하였다. 이번 프로젝트는 다음과 같은 내용으로 진행하고자 한다. 우선, 애플리케이션에서 동작하게 될 화면을 구성하고 각 화면들의 기능을 구현한다. 구성되고자 하는 주요한 기능으로는 회원가입 및 로그인 인증, 게시물 업로드, 게시물 댓글 작성 등이 있다. 때문에, 수업 시간에 배운 이론의 복습과 심화과정으로 스스로 공부하며 개발하는 이번 프로젝트를 통해 안드로이드 프로그래밍 기술을 보다 심화할 수 있는 시간이 될 것이다.

- 이번 프로젝트는 소스코드의 안정성에 집중하여 진행하고자 한다. 우선, 소스코드를 기능 단위로 재사용이 가능하도록 모듈화하여 작성하고 애플리케이션의 경량화를 위해 간결하게 작성한다. 또한, 컴파일을 통해 각 화면의 기능들이 원활히 동작하는지 확인하고, 디버깅을 통해 오류와 경고 메시지를 확인하여 소스코드를 수정한다. 이렇게 최대한 활용 가능한 소스코드를 작성함에 따라 추후에 배포 가능한 애플리케이션을 개발하는 것을 목표로 진행한다.

## 프로젝트 목적
- 현재 많은 사람들이 포트폴리오 작성을 위해 GitHub Pages, Velog, Tistory 등을 활용하고 있다. 그러나, 이러한 방법들은 편리성이 다소 떨어진다는 단점을 가지고 있다. 예를 들어 개인이 문득 떠오른 아이디어나 가볍게 정리하고 싶은 내용을 작성하려면 운영하고 있는 페이지에 따라 마크다운 문법을 사용하여 작성해야 하는데, 급하게 사용하기에 불편하다는 점이 있어 메모장에 적고 나중에 옮기는 경우가 있다. 그러나, 애플리케이션을 활용한다면 필요할 때 바로 작성이 가능하다는 장점이 있다. 또한, 애플리케이션 기능 중 댓글 작성 기능을 구현하여 포트폴리오 블로그에서 SNS로 발전시킨 형태가 된다면 같은 분야의 사람들이 정보를 주고받으며 공감할 수 있을 것이라고 생각하였다. 이러한 이유들로 위의 주제를 선정하게 되었다.

## 프로젝트 세부 내용
### 애플리케이션 개발 환경
1. Android Studio
> Version : 2021.1.1 RC 1 (Android Studio Bumblebee)  
> Java Version : JDK 1.8 (Java 8)

2. AVD
> Name : S(Pixel2)  
> API Level : 31  
> Android Version : 12.0  
> Resolution : 1080 x 1920 (412 x 732 dp)

3.	Hardware Device
>	Name : SM-N950N (Galaxy Note8)  
>	API Level : 28  
>	Android Version : 9  
>	Resolution : 1440 x 2960 (549 x 1128 dp)

## 애플리케이션 기능 설명
#### 1. 로그인 / 회원가입

<img src="/screenshot/screenshot1.png" width="50%" height="50%">

- Firebase Authentication 통한 이메일 로그인
- Firebase Authentication 통한 이메일 회원가입

#### 2. 메인화면 / 메뉴

<img src="/screenshot/screenshot2.png" width="50%" height="50%">

- Firebase Realtime Database, Storage 통한 게시물 표시
- 게시물 업로드 팝업 버튼
- 사용자 계정 정보 메뉴
- 메인화면 이동, 사용자 계정 로그아웃 메뉴

#### 3. 게시물 업로드 / 상세 게시물

<img src="/screenshot/screenshot3.png" width="50%" height="50%">

- Firebase Realtime Database 통한 게시물 업로드
- Firebase Storage 통한 게시물 사진 업로드
- Firebase Realtime Database, Storage 통한 게시물 상세 보기
- Firebase Realtime Database 통한 댓글 업로드 및 보기

## 애플리케이션 기능 구현

|**클래스**|**기능**|**Layout**|
| :-: | :-: | :-: |
|LoginActivity|로그인|activity\_login.xml|
|RegisterActivity|회원가입|activity\_register.xml|
|Home|메인화면 구성|activity\_home.xml|
||메인화면 레이아웃|content\_home.xml|
||메뉴 사용자 정보|nav\_header\_home.xml|
||메뉴 아이템|activity\_home\_drawer.xml|
||툴바, 게시물 추가 버튼|app\_bar\_home.xml|
||게시물 업로드|popup\_add\_post.xml|
|PostDetailActivity|게시물 상세보기, 댓글 작성|activity\_post\_detail.xml|
|HomeFragment|메인화면 리사이클러뷰|fragment\_home.xml|
|PostAdapter|메인화면 게시물|row\_post\_item.xml|
|CommentAdapter|댓글|row\_comment.xml|
|Post|게시물 데이터 모델||
|Comment|댓글 데이터 모델||

## 애플리케이션 사용 방법
1. 회원가입 및 로그인을 합니다.
- 사전에 프로필 이미지, 게시물 이미지를 준비합니다.
- 회원가입 시 프로필 이미지 등록을 위해 파일 및 미디어 권한을 허용합니다.
- 프로필 이미지, 이름, 이메일, 비밀번호를 입력하고 회원가입 합니다.
2. 로그인 사용자를 확인합니다.
- 메뉴를 통해 사용자를 확인하고 로그아웃 할 수 있습니다.
3. 게시물을 업로드합니다.
- 팝업 버튼을 통해 게시물을 업로드 합니다.
- 게시물 업로드시 게시물 제목, 내용, 사진을 입력합니다.
4. 업로드 한 게시물을 확인합니다.
- 메인화면의 업로드 된 게시물을 확인하고 터치하여 게시물을 상세적으로 확인합니다.
5. 댓글을 작성합니다.
- 다른 사용자들과 댓글을 통해 소통합니다.

## 프로젝트 관리 방안
### WaterFall Model
- 애플리케이션 요구사항 분석, 애플리케이션 설계, 애플리케이션 구현, 애플리케이션 테스트, 애플리케이션 유지보수 단계를 순차적으로 실행하여 프로젝트 진행 상황을 명확히 파악하고자 폭포수 방식으로 프로젝트를 관리한다.

### 버전 관리 시스템 – Git (GitHub)
- 프로젝트 실행 중 파일의 변화를 시간에 따라 버전별로 기록하기 위해 버전 관리 시스템 Git (GitHub)을 사용한다.

## 프로젝트 시장성
- PortBlog 애플리케이션은 포트폴리오에 중점을 두고 개발한 애플리케이션이지만, SNS와 블로그의 경계를 허무는 애플리케이션을 목표로 하기 때문에 활용 가능성이 높아 넓은 수요층을 가질 수 있다.

- 게시물 등록 횟수에 따른 기여도 측정, 댓글 작성에 따른 타 사용자와의 호감도 측정 등 다양한 기능들의 개발 가능성이 높아 지속적인 사용자 유입이 가능할 것이다.

## 프로젝트 기대효과
- 안드로이드 프로그램 동작 원리와 프로그래밍을 위한 지식을 습득하고, 알고리즘의 작성과 프로그래밍 언어를 이용하여 안드로이드 프로그래밍 능력을 배양할 수 있다.

- 모바일 이용의 증가에 따른 편리함을 추구하는 학생들을 위해 보다 편리하게 포트폴리오 작성이 가능할 수 있도록 제공할 수 있다. 또한, 애플리케이션을 활용하는 방법으로는 아이디어 작성, 프로젝트 및 학습 내용 정리 등으로 사용자의 다양한 목적에 맞게 활용될 수 있으며, 다른 사용자들의 게시물 확인이 가능하므로 사용자들이 서로 공감하고 공유하며 사용자 친화적인 애플리케이션이 될 것이라 기대한다.

## 프로젝트 마무리
- Java로 애플리케이션을 개발하는 것을 수업을 통해 처음 해봤지만, Java에 대한 이해도가 있어서 어렵지 않았고 다양한 안드로이드 라이브러리를 사용하며 개발하는 것 또한 즐거웠다. 완성도 높은 애플리케이션을 만들고자 노력했지만, 프로젝트 작업 기간이 2주 정도로 짧았기 때문에 다양한 페이지와 기능을 담지 못했다. 기회가 있다면 추후에 PortLog에 담고 싶었던 기능들을 추가하는 시간을 가져보도록 해야겠다.
