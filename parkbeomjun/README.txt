주소는 호스트:8080/parkbeomjun/

실행전 서버 실행 옵션에 아래 3 가지 중에 한가지를 넣어줘야 함 
-Denv=1-LOCAL
-Denv=2-DEV
-Denv=3-REAL


개발 환경 ( 자세한것은 pom.xml 참조 )
- JDK1.7
- MYSQL
- MAVEN
- ECLIPSE LUNA
- TOMCAT 7
- MABTIS
- SPRING 4.x (SECURITY, AOP)
- TILES 3
- LOGBACK

리소스 경로를 잡을때 theme 를 빼고 잡아야함 맵핑 설정을 그렇게 하였음
실제 경로 - {WEBROOT}/resources/theme<?>/css/resume/default.css
맵핑 경로 - {WEBROOT}/resources/css/resume/default.css

