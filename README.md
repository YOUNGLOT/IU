# 해당 툴의 기능

  (DataBase 구축 이후)
  
  Dto Class 자동생성
  
  Dao Class 자동생성
  
  LoadingClass 자동생성
  
  예외 발생 시 Exception Pakage에 Exception Txt 파일 생성
  

# 개발 배경

DB에 데이터 적재
   - TXT, CSV, JSON file 로 된 Data 들을 DB에 적재 시 매번 툴을 개발하여 적재
  1. DB연결
  2. Dto
  3. Dao
  4. file data -> Java String -> Dto 형태로 정제
  5. 적재
    -중복체크
    -정규화 DB의 경우 Code index로 변환
  
DB METADATA가 있다는 것을 습득
DB SysTable에서 Table의 정보를 Java로 가져 올수 있다는 것을 습득

위의 작업을 자동으로 구현 가능 인지

# 구현 방법
  
  Dto Class 생성
  
    Lombok 의 @Data 어노테이션 사용 하여 코드량 압축
    
    Column Name과  Column Type 을 받아온 순서대로 write
  
  Dao Class 생성
  
    ParameterSetter Interface 사용
    
    Dao Class 에 필요한 Help Method를 부모 클래스에 구현
    
    PrimaryKey 값과 Identity 값을 사용하여 쿼리문 작성 Method를 부모 클래스에 구현
    
    TableData를 활용하여 PreparedStatement, ResultSet 에 Entity 매칭하는 함수 구현
    
  LoadClass 생성  
    
    Txt와 Json Class Maker 분리
    
    각각의 Class에 맞게 구현 
    

# 극복 했던 점


  CASE1-
  
  DB의 MetaData, Connection, SystemData 들을 가져와 멤버 변수로 활요하다보니
  
    but) 각각의 Class에 겹치는 서로 다른 멤버 변수들이 너무 많아서 코드정리와 리딩이 힘들었습니다.
  
  -//region 을 사용하여 구획을 나눔
  
     but)만족스럽지 않았음
      
  -TableData라는 데이터 클래스를 생성 
    TableData와 관련이 있는 멤버변수들을 모두 선언하고 
    MetaData, SystemData 가져오는 코드와 그와 관련된 코드들도 해당 클래스에 선언
   
   TableData라는 멤버변수 활용!
   
   후에 이것이 캡슐화 라는것을 깨닳게 됨
   
   
   
   CASE2-
   
   File 에서 가져온 Data 들을 적재할 때 중복체크를 위해 Select를 하였음 
   
    but) insert 1회당 1 Select 를 하여 성능에 엄청난 문제가 생김
   
   -적재 전에 ArrayList를 멤버번수로 선언 후 해당 Table의 모든 데이터를 받아옴
   
    but) insert 시 Identity 값이 없기 때문에 Entity 비교가 힘듬
    
   -equals() 재정의 hashcode() 재정의 를 통하여 Key, Candidate값들을 비교하여 중복제거에 성공
   
   
   
   CASE3-
   
   많은 양의 데이터 적재 시 속도가 너무 느림 (초당 200건 정도의 insert)
   
   -runnable 인터페이스로 Thread 개수를 늘려 진행
   
      but) 중복된 작업을 멀티 Thread로 하는 것을 발견
   
   -Thread 적제 단계에서 늘리지 않고 File Data를 리딩하는 for문 앞에서 늘림, synchronized 공부
   
      이유 : file Data 1개당 Insert 1회이기 때문
      
   Thread를 늘릴 때 스코프의 중요성을 배움.

    
    
