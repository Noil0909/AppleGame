# 사과게임 (AppleGame)

![AppleGameGraphicImage](https://github.com/user-attachments/assets/34a9c8b3-ea58-48f9-98ac-d132b7095aed)

숫자가 적힌 사과를 드래그하여 합이 10이 되면 사과가 사라지는 퍼즐 게임입니다.  
인터넷 방송에서 유행했던 "사과게임"을 Kotlin과 Jetpack Compose로 구현했습니다.
<hr/>

### 시연 영상 클립
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/96fd1216-ca53-46fa-8f96-a8b4cd99f7d4" width="200" style="margin-right: 10px;"></td>
    <td><img src="https://github.com/user-attachments/assets/e3cec1df-1d56-4f79-acd7-546b0f75f4c9" width="200" style="margin-right: 10px;"></td>
    <td><img src="https://github.com/user-attachments/assets/3ffb24e6-a98b-415d-9f52-1ca365a0b8cf" width="200"></td>
  </tr>
</table>
<hr/>

### 앱 주요 화면
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/58a0ee0e-f9c8-45dc-ab88-e46cbe0b1004" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/67627df7-d9df-4c7e-881e-18aaf5bfee67" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/49763774-9ef2-4916-b843-3195c9b3b3fb" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/9414873d-4224-4b65-ba57-c660e506b236" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/c20351e3-6348-468b-8dd3-65398ae63cf8" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/582c7c78-dacd-4f12-ba9c-8ce8d5383900" width="200"></td>
  </tr>
</table>
<hr/>

## 핵심 기능
-  **드래그로 사과 선택 및 제거**
-  **점수 기록 저장, 조회 및 삭제**
-  **게임 플레이 중 가로/세로 모드 지원**
-  **BGM, 효과음, 진동 설정 관리**
-  **페이지네이션이 적용된 기록 조회**


## 기술 스택
- 언어 : Kotlin
- UI : Jetpack Compose
- Local DB : RoomDB
- 사운드 : SoundPool, MediaPlayer
- 네비게이션  : Navigation Compose

## 앱 다운로드 링크
* 구글 플레이 스토어 링크 추가 예정(비공개 테스트 진행중)

## 배운 점
**MVVM 아키텍처 설계**
- UI와 로직의 분리를 통해 유지보수성과 확장성을 높였습니다.

**드래그 입력 처리**
- pointerInput과 detectDragGestures를 사용해 사용자의 드래그 입력을 실시간으로 감지하고, 드래그 영역을 ViewModel에 전달해 상태를 업데이트하도록 설계했습니다.
- 드래그 박스를 시각적으로 보여주며 선택 정확도를 개선했습니다.
  
**안드로이드 생명주기**
- BGM 재생 라이브러리로 선택한 MediaPlayer는 앱이 백그라운드로 가거나 화면이 꺼져도 재생이 계속 되는 특징이 있습니다. 이를 제어하기 위해 Lifecycle Observer를 사용해 앱이 포그라운드로 가거나 백그라운드로 전환될 때 BGM을 일시정지하거나 다시 재생하는 로직을 구현했습니다.
  
**로컬 데이터베이스 활용**
  - Room DB를 통해 점수를 기록하고 페이지네이션으로 조회 기능을 구현했습니다.
    
**UX 고려**
  - 효과음, 진동, BGM 설정을 제공해 사용자 경험을 개선했습니다.
    
## 개선할 점
- 난이도 조절 모드(easy, hard ..)
- 사과 삭제 애니메이션
- 사과 이미지 캐싱 개선
- 멀티 플레이
