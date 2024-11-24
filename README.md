# [Java, SpringBoot] On-campus-facility-management-program
3학년 1학기 Java, SpringBoot 팀프로젝트 입니다.<br>
교내 스프링을 학습하기전 예습목적과 캡스톤 프로젝트 준비로
시작한 "교내 시설 예약 시스템"을 주제로한 프로젝트 입니다.

교내 시설 이용에 대한 예약 기능과 게시판 기능을 구현하였습니다.

## 기획
### 1. 프로젝트 개요
  교내 시설 예약 시스템으로써 사용자의 경우는 회원가입을 하고 사용 등록을 하여 관리자의 승인을 받아야 하며, 관리자는 사용자의 승인 신청과 사용 기록을 확인 할 수 있어야 합니다.

### 2. 시스템 주요 기능
○ 사용자
- 회원 가입 (사용자): 시설물 사용을 위한 회원 정보 기입.
- 회원 정보 창 (사용자): 회원 정보 확인 및 수정, 탈퇴, 로그아웃, 사용 시설 내역 조회.
- 교내 시설 사용 등록 (사용자): 빈 시설과 사용 중인 시설 조회가 실시간으로 가능. <br>
 빈 시설의 경우 회원 정보를 통해 예약 요청.
- 공지사항 조회 (사용자): 관리자가 작성한 공지사항을 조회할 수 있음.
- 질문게시판 기능 (사용자): 사용자 간의 질의응답을 주고받을 수 있는 게시판의 기능들.

○관리자
- 회원 관리 창 (관리자): 회원 조회 및 임의로 가입, 수정, 삭제 가능.
- 관리자 정보 창 (관리자): 관리자 정보 수정 가능.
- 교내 시설 관리 창 (관리자): 사용자에게 제공될 시설을 등록 및 예약 상태를 조회 가능.
- 입, 퇴실 확인 (관리자): 회원들의 입 퇴실 시간과 회원 정보 조회.
- 공지사항 (관리자): 공지사항을 조회 및 작성, 수정, 삭제할 수 있음.
- 질문게시판 기능 (관리자): 사용자 간의 질의응답을 주고받을 수 있는 게시판이지만 관리자도 동일하게 사용 가능.

### 3. 시스템 주요 요구사항
◎사용자<br>

○ 회원 가입 (사용자)
  - 정보 기입 및 가입 할 때 아이디 중복 확인이 가능하게 해야 함

○ 교내 시설 사용 등록 (사용자)
  - 등록되어 있는 시설들을 보여주며 해당 시설을 선택 시 예약 상태를 확인하여 예약할 
수 있어야 한다.

○ 회원 정보 창 (사용자)
  - 자신의 정보를 조회 및 일부 수정이 가능해야 하며, 예약했던 시설들의 사용 기록을
 조회할 수 있어야 한다.

○  공지사항 조회 (사용자)
   - 공지사항을 리스트로 조회 가능하며, 클릭 시 해당 공지사항의 상세 페이지로 이동되어야한다.

○ 질문게시판 기능 (사용자): 사용자 간의 질의응답을 주고받을 수 있는 게시판으로
- 게시글 조회 및 수정, 삭제가 가능.
- 댓글 조회 및 작성, 수정, 삭제가 가능
- 게시글과 댓글에는 좋아요 버튼을 사용할 수 있음.

◎관리자<br>

○회원 관리 창 (관리자): 
  - 조회 시 정렬 및 검색이 가능하며, 임의로 회원을 추가 및 수정 삭제 가능.

○교내 시설 관리 창 (관리자):
  - 사용자에게 제공될 시설을 등록 및 예약 상태를 조회 가능.

○입, 퇴실 확인 (관리자): 
  - 회원들의 입 퇴석 시간과 회원 일부의 정보 조회가 되어야 함
  - 시설이 예약되었던 기록을 조회할 수 있어야 함.

○ 공지사항 (관리자): 
  - 자신이 공지사항을 조회할 수 있으며, 공지사항 작성 및 수정, 삭제할 수 있음.

○ 질문게시판 기능 (관리자):
  - 사용자 간의 질의응답을 주고받을 수 있는 게시판이지만 관리자도 동일하게 사용 가능.
  - 게시글 조회 및 수정, 삭제가 가능.
  - 댓글 조회 및 작성, 수정, 삭제가 가능
  - 게시글과 댓글에는 좋아요 버튼을 사용할 수 있음.

### 4. 개발기간
- 2023-01-23 ~ 2032-06-22

### 5. 개발환경
- 운영체제 : Window 10
- 개발도구: IntelliJ, VS code
- 개발언어 
   - 프론트 엔드 : HTML5, CSS, Javascript
   - 백 엔드 : Java, SpringBoot, Spring Data JPA, Spring Security
   - DataBase : MySql

## 역할
- 진태흠: 프로젝트 기획 및 문서화, DB설계, 기능구현
- 정승민: 프로젝트 기획 및 기능구현
- 조다운: 프로젝트 기획 및 DB설계, 기능구현

## 🛠 기능구현
- 커뮤니티 관련 Paging 구현<br>
→ 커뮤니티 리스트 및 상세페이지 front 추가<br>
→ 사용자, 관리자 게시글 관련 CRUD<br>
→ 댓글 관련 CRUD 및 추천기능<br>

<div><img src="https://github.com/user-attachments/assets/b790a7b8-e49d-4cad-8b32-6539247f4a3f" width="45%" height="45%"/>  <img src="https://github.com/user-attachments/assets/d0759d77-bd31-4b90-b154-340c404e5cca" width="45%" height="45%"/></div>
<div><img src="https://github.com/user-attachments/assets/bfeffbfc-cba9-44d8-a44e-7c756b9085ab" width="45%" height="45%"/>  <img src="https://github.com/user-attachments/assets/fcc63770-8f07-41da-9f7d-cc4baabfe582" width="45%" height="45%"/></div>
