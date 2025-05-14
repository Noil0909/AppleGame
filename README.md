# 사과게임 (AppleGame)

![AppleGameGraphicImage](https://github.com/user-attachments/assets/34a9c8b3-ea58-48f9-98ac-d132b7095aed)

숫자가 적힌 사과를 드래그하여 합이 10이 되면 사과가 사라지는 퍼즐 게임입니다.  
인터넷 방송에서 유행했던 "사과게임"을 Kotlin과 Jetpack Compose로 구현했습니다.

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/96fd1216-ca53-46fa-8f96-a8b4cd99f7d4" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/e3cec1df-1d56-4f79-acd7-546b0f75f4c9" width="200"></td>
    <td><img src="https://github.com/user-attachments/assets/96fd1216-ca53-46fa-8f96-a8b4cd99f7d4" width="200"></td>
  </tr>
</table>
---
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
---

## 핵심 기능
-  **드래그로 사과 선택 및 제거**
-  **점수 기록 저장, 조회 및 삭제**
-  **BGM, 효과음, 진동 설정 관리**
-  **페이지네이션이 적용된 기록 조회**
-  **게임 플레이 중 가로/세로 모드 지원**


## 기술 스택
- 언어 : Kotlin
- UI : Jetpack Compose
- Local DB : RoomDB
- 사운드 : SoundPool, MediaPlayer
- 네비게이션  : Navigation Compose

## 앱 다운로드 링크
* 구글 플레이 스토어 링크 추가 예정(비공개 테스트 진행중)

## 배운 점
- **MVVM 아키텍처 설계**
  - UI와 로직의 분리를 통해 유지보수성과 확장성을 높였습니다.
- **드래그 입력 처리**
  - 드래그 박스를 시각적으로 보여주며 선택 정확도를 개선했습니다.
- **안드로이드 생명주기 대응**
  - 앱 포그라운드/백그라운드 상태에 따라 BGM을 자연스럽게 제어했습니다.
- **로컬 데이터베이스 활용**
  - Room DB를 통해 점수를 기록하고 페이지네이션으로 조회 기능을 구현했습니다.
- **UX 고려**
  - 효과음, 진동, BGM 설정을 제공해 사용자 경험을 개선했습니다.
  - 
## 개선할 점
- 난이도 조절 모드(easy, hard ..)
- 사과 삭제 애니메이션
- 사과 이미지 캐싱 개선
- 멀티 플레이
