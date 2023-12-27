# portfolioMyDB
데이터베이스 조회 어플 모바일 개인 프로젝트 포트폴리오

# 소개
AWS EC2 및 RDS 와 웹 서버를 통해 DB 등록, 데이터 조회가 가능한 안드로이드 어플입니다.


# 제작기간 & 참여 인원
<UL>
  <LI>2023.12.02 ~ 2023.12.26</LI>
  <LI>개인 프로젝트</LI>
</UL>



# 사용기술
![js](https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![js](https://img.shields.io/badge/androidstudio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white)
![js](https://img.shields.io/badge/sqlite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)


# 핵심 기능 및 페이지 소개



<H3>웹 API 서버 깃허브 주소</H3>
<BR>
  
https://github.com/oals/MyDB-ApiServer

<BR>

<hr>

<H3>메인 엑티비티</H3>
<BR>


<img src="https://github.com/oals/portfolioMyDB/assets/136543676/7b00271f-7856-4af8-866c-c8057d137448" height="650">


<BR>
<BR>



<UL>
  <LI>해당 게정에 등록된 데이터베이스 목록을 리사이클 뷰로 구현했습니다.</LI>
<LI>프래그먼트를 사용해 메인 엑티비티 화면을 분할 했습니다.</LI>
</UL>


<BR>


<HR>


<H3>데이터 베이스 연결 엑티비티</H3>
<BR>
<img src="https://github.com/oals/portfolioMyDB/assets/136543676/1735e9e3-a6b4-48d2-98e2-f74df1fcbda1"  height="650">



<br>
<br>
<details>
 <summary> 데이터 베이스 연결 플로우 차트
 
 </summary> 

 ![db 등록 플로우차트](https://github.com/oals/portfolioMyDB/assets/136543676/0df42529-776b-46f6-843c-0106e0991557)

</details>
<br>
<br>



<UL>
  <LI>입력 정보를 웹 서버로 전송해 DB와 연결을 시도하도록 구현했습니다. </LI>
  <LI>현재 마리아DB와 오라클DB 연결을 지원합니다.</LI>
  

</UL>



<HR>

<H3>데이터 베이스 조회 엑티비티</H3>
<BR>


<img src="https://github.com/oals/portfolioMyDB/assets/136543676/9a6e3d88-938c-4821-9a08-114627a43c5a"  height="650">



<br>
<br>
<details>
 <summary> 데이터 베이스 조회 엑티비티 플로우 차트
 
 </summary> 
 
![데이터 베이스 조회](https://github.com/oals/portfolioMyDB/assets/136543676/ea20b72b-28ca-4448-b614-ef798083e922)


</details>
<br>
<br>

<UL>
  <LI>등록된 데이터베이스 터치 시 웹 서버로 해당 데이터 베이스의 모든 DB (Maria DB)혹은 테이블을(Oracle DB) 반환 하도록 구현했습니다.</LI>
    <LI> 계정에 등록된 데이터베이스 선택  -> DB 선택 -> 테이블 선택 -> 해당 테이블의 데이터 정보 조회 </LI>
</UL>




<HR>
<H3>데이터 베이스 데이터 검색 엑티비티</H3>
<BR>

<img src="https://github.com/oals/portfolioMyDB/assets/136543676/ece54e3c-a777-4a82-9612-b24baf00a51b"  height="650">
<img src="https://github.com/oals/portfolioMyDB/assets/136543676/03a02863-d8e6-420f-9b86-3f293963c754"  height="650">



<br>
<details>
 <summary> 데이터 베이스 데이터 검색 엑티비티 플로우 차트
 
 </summary> 

 ![데이터 베이스 데이터 검색](https://github.com/oals/portfolioMyDB/assets/136543676/d6494ff7-f87f-430e-84d4-47af8ae36e17)


</details>
<br>
<br>


<UL>

  <LI>스피너를 통해 지정된 검색 칼럼명과 검색어를 웹 서버로 전송해 데이터를 반환하도록 구현했습니다.</LI>
  <LI>현재 선택한 테이블의 모든 칼럼명을 리스트로 만들어 동적 스피너를 구현 했습니다.</LI>

</UL>




<HR>


<H3>데이터 베이스 삭제</H3>
<BR>

<img src="https://github.com/oals/portfolioMyDB/assets/136543676/65d7de1e-e421-409c-8e06-0d408ff646d6" height="650">


<br>
<br>
<HR>

<UL>

  <LI>리사이클 뷰와 스와이프를 통해 삭제 버튼을 구현했습니다.</LI>
  <LI>DB에서 해당 계정에 등롣괸 데이터베이스 연결 정보를 삭제합니다.</LI>


</UL>






<H3>로그인 및 회원가입</H3>
<BR>

<img src="https://github.com/oals/portfolioMyDB/assets/136543676/9bc1c3a4-31a8-41b8-b94f-48bbf1e1e191"  height="650">





<HR>



# 프로젝트를 통해 느낀 점과 소감

두번 째 안드로이드 프로젝트다.
저번 프로젝트에는 쓰지 않았던 리사이클 뷰와 스와이프를 통해 삭제 버튼을 구현해봣다. <br>
안드로이드에서는 바로 서버의 데이터베이스로 연결이 불가능 하다는 사실에 웹 서버를 통해 우회하여 접근하도록 구현했다<br>
API 서버는 이런 느낌이구나 하는 걸 알게 됐다 <br>
물론 http 코드 등 중요한 정보들을 반환하지는 못했지만 음.. 제대로된 API 서버를 만들어 보고 싶다는 생각도 들었다<br>
개인적으로 나름 좋은 아이디어라고 생각 하며 시작 했지만 결과물이 아쉽게 나온 것만 같다<br>
다음에는 마켓에 올릴 수 있는 수준의 어플을 만들어 보고 싶다















