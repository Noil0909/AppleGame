# 사과게임 (AppleGame)

![AppleGameGraphicImage](https://github.com/user-attachments/assets/34a9c8b3-ea58-48f9-98ac-d132b7095aed)

숫자가 적힌 사과를 드래그하여 합이 10이 되면 사과가 사라지는 퍼즐 게임입니다.  
인터넷 방송에서 유행했던 "사과게임"을 Kotlin과 Jetpack Compose로 구현했습니다.
<hr/>

## 시연 영상 클립
<div align="center">
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/96fd1216-ca53-46fa-8f96-a8b4cd99f7d4" width="200" style="margin-right: 10px;"></td>
    <td><img src="https://github.com/user-attachments/assets/e3cec1df-1d56-4f79-acd7-546b0f75f4c9" width="200" style="margin-right: 10px;"></td>
    <td><img src="https://github.com/user-attachments/assets/3ffb24e6-a98b-415d-9f52-1ca365a0b8cf" width="200"></td>
  </tr>
</table>
</div>

## 앱 주요 화면
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/58a0ee0e-f9c8-45dc-ab88-e46cbe0b1004" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/67627df7-d9df-4c7e-881e-18aaf5bfee67" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/49763774-9ef2-4916-b843-3195c9b3b3fb" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/9414873d-4224-4b65-ba57-c660e506b236" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/aeb33209-70a7-4d2b-a84f-a9b34346bcd0" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/582c7c78-dacd-4f12-ba9c-8ce8d5383900" width="200"></td>
  </tr>
</table>


## 주요 기능
-  드래그로 사과 선택 및 제거
-  점수 기록 저장, 조회 및 삭제
-  게임 플레이 중 가로/세로 모드 지원
-  BGM, 효과음, 진동 설정 관리
-  페이지네이션이 적용된 기록 조회


## 기술 스택
- 언어 : Kotlin
- UI : Jetpack Compose
- Local DB : RoomDB
- 사운드 : SoundPool, MediaPlayer
- 네비게이션  : Navigation Compose

## 앱 다운로드 링크
* 구글 플레이 스토어 링크 추가 예정(비공개 테스트 진행중)

## 설계 및 구현 요약
**MVVM 아키텍처 설계**
- ViewModel을 사용해 UI 로직과 분리하고, StateFlow와 MutableState를 통해 UI 상태를 실시간으로 관리하는 구조를 설계했습니다.
  
**드래그 입력 처리**
- pointerInput과 detectDragGestures를 사용해 사용자의 드래그 입력을 실시간으로 감지하고, 드래그 영역을 ViewModel에 전달해 상태를 업데이트하도록 설계했습니다. 드래그 영역은 Canvas로 그려서 시각적으로 보일 수 있게 만들었습니다.
  
**안드로이드 생명주기**
- BGM 재생 라이브러리로 선택한 MediaPlayer는 앱이 백그라운드로 가거나 화면이 꺼져도 재생이 계속 되는 특징이 있습니다. 이를 제어하기 위해 Lifecycle Observer를 사용해 앱이 포그라운드로 가거나 백그라운드로 전환될 때 BGM을 일시정지하거나 다시 재생하는 로직을 구현했습니다.
  
**로컬 데이터베이스 활용**
- RoomDB를 사용해 게임 점수를 저장하고, 기록을 최신순으로 조회하도록 구현했습니다. 기록에는 점수와 날짜가 포함됩니다. 가장 높은 점수 3개의 기록은 첫 페이지 상단에 보이도록 하였습니다.
- 또한 페이지네이션으로 이전의 기록을 효율적으로 볼 수 있도록 하고, 기록 삭제 기능을 구현했습니다. 
    
**UX를 고려한 게임 설정**
- BGM, 효과음, 진동 설정을 제공하여 UX적인 부분을 신경써서 만들었습니다.
- BGM은 MediaPlayer 라이브러리, 효과음은 SoundPool 라이브러리를 사용했습니다.
    
## 개선할 점
- 난이도 조절 모드(easy, hard ..)
- 사과 삭제 애니메이션
- 사과 이미지 캐싱 개선
- 멀티 플레이

## 출처
#### 폰트
- 메인 폰트는 **㈜여기어때컴퍼니**의 **여기어때 잘난체**를 사용했습니다.
- 폰트 정보: [https://gccompany.co.kr/font](https://gccompany.co.kr/font)
- 라이센스 전문 (PDF): [https://image.goodchoice.kr/images/jalnan_font/jalnan-font.pdf](https://image.goodchoice.kr/images/jalnan_font/jalnan-font.pdf)

#### 사과 이미지
- <a href="https://www.flaticon.com/kr/free-icons/" title="과일 아이콘">과일 아이콘 제작자: Sudowoodo - Flaticon</a>

- <a href="https://www.flaticon.com/kr/free-icons/" title="과일 아이콘">과일 아이콘 제작자: Pixel perfect - Flaticon</a>

#### BGM
- BGM 정보: "Creative Fun" by Scott Holmes Music, obtained from [Pixabay](https://pixabay.com/music/creative-fun-259223/).
- 라이선스 요약: https://pixabay.com/ko/service/license-summary/

#### 효과음(SoundEffect)
- 효과음 정보: ✔ SFX provided by 셀바이뮤직 https://sellbuymusic.com/md/seaczxk-affxnzt

#### 구글플레이 스크린샷 이미지
- [https://previewed.app/template/CFA62417](https://previewed.app/template/CFA62417)
