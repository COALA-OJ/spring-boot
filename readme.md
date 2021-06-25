Spring
======

Spring Repository

# 데이터 요청/응답 방식
기본적으로 요청/응답은
응답 측에서 도메인을 열어놓고 요청은 해당 도메인에 요청을 하여 그에 맞는 Response를 얻습니다.

|요청측|응답측|
|------|------|
|도메인존재X|도메인존재|
|요청 시 응답측 도메인에 접근|요청 방식, 리턴 값 설정해두고 도메인 오픈|

***즉, 우리가 요청을 받는 쪽이면 RequestMapping을 통해 URL을 열어두면 되는 것이고,***

***우리가 요청을 하는 쪽이면 제시된 URL에 RestTemplate로 요청하여 응답을 받아오면 됩니다.***

모든 요청에 대한 데이터 반환은 Json으로 이루어집니다.

## Json 데이터 전송 형식


### 채점 현황
20개씩 스택에 채우게 되며, 형식은 다음과 같다.

*Response*

	{
		Status : [
		{
			"SubNum" : "1"
			"Pnum" : "1001"        //문제 번호
			"ID" : "asdf1234"         //ID
			"Result" : "TIME_LIMIT_EXCEED"           //채점 결과
			"LangType" : "C++"
			"CodeSize" : "132byte"
			"Permission" : "True"/"False" //True일때 코드 열람 권한 존재
		},
		{
			"SubNum" : "1"
			"Pnum" : "1001"        //문제 번호
			"ID" : "asdf1234"         //ID
			"Result" : "TIME_LIMIT_EXCEED"           //채점 결과
			"LangType" : "C++"
			"CodeSize" : "132byte"
			"Permission" : "True"/"False" //True일때 코드 열람 권한 존재
		},
		… ] //총 20개
	}


### 회원 가입 - 아이디 중복 여부

*Request*

React -> Spring

	{
		"ID" : "ID"
	}
	
Spring -> Flask

	{
		"ID" : "ID"
	}
	
*Response*

Flask -> Spring

	{
		"ID" : "True" 	// 중복될경우 False, 중복되지 않을 경우 True
	}
	
Spring -> React
	
	{
		"ID" : "True" 	// 중복될경우 False, 중복되지 않을 경우 True
	}
	
### 회원 가입 - 회원 가입 요청
React -> Spring -> Flask

	{
		ID : ""		
		Pwd : ""	
		Name : ""
		//모든 데이터는 보안상의 이유로 해시 값으로 전달
	}
	
### 로그인

React -> Spring -> Flask

	{
		ID : ""		
		Pwd : ""	
		//모든 데이터는 보안상의 이유로 해시 값으로 전달
	}
	

Flask -> Spring -> React

	{
		"Result" : "True/False" // 로그인 결과 전달
	}
	
	
### 유저 정보 요청

*Request*

React -> Spring -> Flask

	{
		ID : ""	
	}
	
*Response*

Flask -> Spring -> React

	{
		ID : ""
		SolvedList : ["1001", "1002", ...]
	}
	

### 문제 리스트 요청/응답
React -> Spring

/spring/problemdata에 GET 요청

Spring -> Flask

/에 요청, GET

이때 따로 parameter가 요구되지 않음.
요청을 하면 바로 전달되는 방식
후에 유저 정보 추가시 변경 예정

#### Response Data Form

	{
		Problems : [
		{
			Pnum : [1001]        //문제 번호
			Pname : [A+B]         //문제 이름
			Solved : T            //T, F, Null 3가지 종류만 존재 String type
		},
		{
			Pnum : [1002]
			Pname : [A-B] 
			Solved : Null
		},
		… ]
	}

### 문제 내용 요청
React -> Spring

/spring/probleminfo에 POST로 요청

문제 번호만을 가지고 내용을 요청

#### Request Data Form

	{
  		Pnum : [문제 번호]
	}

### 문제 내용 요청에 대한 응답
Flask -> Spring

/flask_Problem_info에 POST로 요청

다음의 JSON형식을 담음

#### Response Data Form
	{
		Result : true/false, // 문제 번호 존재 여부
		Pnum : [문제 번호],
		Pname : [문제 이름],
		Solved : [풀이 여부(T, F, Null)],
		Pcond : [시간 제한, 메모리],
		Pdetail : [내용, input 조건, output 조건],
		Pinout : [{예시1 input, 예시1 output}, {예시2 input, 예시2 output}, ...],
		Pacrate : [0, 0]
		
	}
	
Spring -> React
React의 요청에 따라 Flask에서 받아온 데이터를 info라는 json array에 한번 더 감싸주는 형태로 반환

#### Response Data Form
	{
		info : [{
			Result : true/false, // 문제 번호 존재 여부
			Pnum : [문제 번호],
			Pname : [문제 이름],
			Solved : [풀이 여부(T, F, Null)],
			Pcond : [시간 제한, 메모리],
			Pdetail : [내용, input 조건, output 조건],
			Pinout : [예시 input, 예시 output],
			Pacrate : [0, 0]
		}]
	}	

### 제출 시에 오고 가는 Request
React -> Spring

/spring/submitcode로 POST로 요청

#### Request Data Form

	{
		UserID : [ID]
		Pnum : [문제 번호]
		Pcode : [제출 코드]
	}

Spring -> Flask

/flask_submit로 POST 요청

엔진과의 동기화를 위해 엔진 전송 전, 후  두 번의 전송을 거침

#### Request Data Form

	{
		SubNum : [제출 번호]
		Pnum : [문제 번호]
	}
	
엔진 측 요청에 따라 테스트 케이스 개수를 받아와야함(현재 미구현)
현재는 달리 특정 응답이 없음 (True)반환 추정

~~Response Data Form~~

	{
		tc-cnt : [테스트 케이스 갯수]
	}


Spring -> Engine

/judger-engine/1으로 POST 요청

tc-cnt는 Flask측에서 받아올 예정(미구현)

#### Request Data Form

	{
		SubNum : [제출 번호]
		Pnum : [문제 번호]
		tc-cnt : [테스트 케이스의 갯수]
		Pcode : [제출 코드]
	}
	
Spring -> Flask

/flask_submit_result에 POST요청

두 번째 제출 역시 특정 Response를 지정하지 않음

	{
		SubNum : [제출 번호]
		Pnum : [문제 번호]
		UserID : [ID]
	}

### 채점 결과 요청에 따른 Response

비슷한 형태로 전부 리턴되나, React 반환 값은 React측 요청으로 JsonArray로 감싸준 형태

Engine -> Spring

	{
		SubNum : [제출 번호]
		Result : [채점 결과]
	}

Spring -> Flask

	{
		SubNum : [제출 번호]
		Result : [채점 결과]
	}

Spring -> React

	{
		Problems:[{
			SubNum : [제출 번호]
			Result : [채점 결과]
			}]
	}
## 1/14 ~ 1/20 1주차 목표
~~문제 리스트 보관 방법 논의~~

~~문제 화면 구성 데이터 받아오기~~

~~코드 제출 시퀀스~~

## 1/20 ~ 1/27 2주차 목표
~~채점 큐 구현~~

~~코드 refactoring~~

~~코드 파싱 issue~~

## 1/27 ~ 2/3 3주차 목표
~~dockering~~

## 2/3 ~ 2/10 2주차 목표
회원 가입 및 로그인

유저 정보 페이지

서브 네비게이션

