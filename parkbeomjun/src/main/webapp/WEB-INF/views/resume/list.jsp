<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<!DOCTYPE html>
<html lang="ko">
	<head>
	    <title>박범준 프로젝트 리스트</title>
	    <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="description" content="사이트 설명">
		<meta name="author" content="작성자">  
	    <meta name="designer" content="개발자 / 디자이너">        
	    <meta name="dcterms.rightsHolder" content="저작권 안내">
	    <meta name="keywords" content="키워드">  
	    <link rel="shortcut icon" href="assets/ico/favicon.ico">
		<link rel="apple-touch-icon-precomposed" href="assets/ico/apple-touch-icon.png">
	    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
	    <!-- 부트스트랩 -->
	    <link href="${rootPath}/resources/css/resume/bootstrap.min.css" rel="stylesheet" media="screen">
	    
	    <link href="${rootPath}/resources/css/resume/custom.css" rel="stylesheet">
	    
		<!-- jQuery (부트스트랩의 자바스크립트 플러그인을 위해 필요한) -->
	    <script src="//code.jquery.com/jquery.js"></script>
	    <!-- 모든 합쳐진 플러그인을 포함하거나 (아래) 필요한 각각의 파일들을 포함하세요 -->
	    <script src="${rootPath}/resources/js/resume/bootstrap.min.js"></script>
	    <!-- Respond.js 으로 IE8 에서 반응형 기능을 활성화하세요 (https://github.com/scottjehl/Respond) -->
	    <script src="${rootPath}/resources/js/resume/respond.min.js"></script>
	    <script src="${rootPath}/resources/js/resume/holder.js"></script>
		<style type="text/css">
		    /* Custom Styles */
		    ul.nav-tabs{
		        width: 140px;
		        margin-top: 20px;
		        border-radius: 4px;
		        border: 1px solid #ddd;
		        box-shadow: 0 1px 4px rgba(0, 0, 0, 0.067);
		    }
		    ul.nav-tabs li{
		        margin: 0;
		        border-top: 1px solid #ddd;
		    }
		    ul.nav-tabs li:first-child{
		        border-top: none;
		    }
		    ul.nav-tabs li a{
		        margin: 0;
		        padding: 8px 16px;
		        border-radius: 0;
		    }
		    ul.nav-tabs li.active a, ul.nav-tabs li.active a:hover{
		        color: #fff;
		        background: #0088cc;
		        border: 1px solid #0088cc;
		    }
		    ul.nav-tabs li:first-child a{
		        border-radius: 4px 4px 0 0;
		    }
		    ul.nav-tabs li:last-child a{
		        border-radius: 0 0 4px 4px;
		    }
		    ul.nav-tabs.affix{
		        top: 30px; /* Set the top position of pinned element */
		    }
		    
			/* Google Fonts */
			@import url(http://fonts.googleapis.com/earlyaccess/nanummyeongjo.css);
			/* @import url(http://fonts.googleapis.com/css?family=Yanone+Kaffeesatz); */
			@import url(http://fonts.googleapis.com/css?family=Lobster);
			body{
				font-family: 'Lobster','Nanum Myeongjo', serif !important;
			}
			
			h1{
				text-shadow: 4px 4px 4px #aaa;
			}
			ul li{
				text-shadow: 4px 4px 4px #aaa;
			}
			
			hr{
				margin:70px 0px;
			}
		    
		</style>
		
		
  	</head>
<body data-spy="scroll" data-target="#myScrollspy">
<div class="container">
	<div class="jumbotron">
        <h1>Project List</h1>
       	<p>&nbsp;&nbsp;&nbsp;연습한 프로젝트</p>
    </div>
    <div class="row">
        <div class="col-xs-3" id="myScrollspy">
            <ul class="nav nav-tabs nav-stacked" data-spy="affix" data-offset-top="125">
                <li class="active"><a href="#section-1">Section One</a></li>
                <li><a href="#section-2">Section Two</a></li>
                <li><a href="#section-3">Section Three</a></li>
                <li><a href="#section-4">Section Four</a></li>
            </ul>
        </div>
        <div class="col-xs-9" role="main">
        
        
        	<hr id="section-1">
        	
        	
            <h2>게시판위주의 커뮤니티지향 사이트</h2>
            <p>개발기간 -&gt; [1달반].</p>
            <p>
            	<a href="http://me.people-wiki.com/index.do" target="_blank" >[방문하기]</a>
            </p>
            <pre>
게시판을 계층쿼리를 어떠어떠하게 했는데 10만건데이터 처리가 너무 느려서 ㅡㅡ
다 만들고 게시판 쿼리부터 설계까지 싹다 바꿨다 그래서 너무 오래걸렸다 흐헉흐헉
망할..분명 mysql사이트 포럼인가 교육인가 소개인가 거기서 계층 쿼리글보고 
이런것도 있구나 해서 당연...이런곳에 소개됐으니 성능도 끝내주겠지 하는
단순한 맘으로 도전했다가 ..좌절
			</pre>
            
            
            
            <hr id="section-2">
            
            
            
            <h2>위젯형 사이트</h2>
            <p>개발기간 - &gt; [3주]</p>
            <p>
            	<a href="http://me.people-wiki.com/widget/widget.do" target="_blank" >[방문하기]</a>
            </p>            
            <pre>		
위젯형 페이지 위자드닷컴 사이트 표방..힘들었음 지금까지 하면서 제일
힘든 사이트 제이쿼리에 심취에 있어서 도전했지만...
제이쿼리에 반하기만 했을뿐 실력은 흐헉흐헉;;
처음에 dom4j로 했다가 완전 느려서...물론 스크립트에도 문제가 있었지만
엄청난 파워의 json과 구글api로 고고싱..
			</pre>
            
            
            
            <hr id="section-3">
            
            
            
            <h2>실시간 검색 표방 <br>(현재 트위터 API 버전업으로 인해 작동 중지.. 변경된 API 이용한 재개발 해야함)</h2>
            <p>개발기간 - &gt; [일주일인가 열흘인가 정도하다가 도중 포기]</p>
            <p>
            	<a href="http://me.people-wiki.com/person.do" target="_blank" >[방문하기]</a>
            </p>            
            <pre>		
실시간검색을 처음 알게 된거는 라이브k라는 사이트인데 처음본거 완전 뻑가서
나도 한번 만들어봐야지 해서 만들게 되었다 대충 보니 디비를 전혀 사용안하다는데
뭐 그렇다 치고 만들게 되었다 근데 이게 왠일 어느정도 진척이 나가다보니
라이브k랑 내가 사용하는 api가 다른것 같다 ㅡ,.ㅡ...트위터 홈피에 나와있는
api로 한글 검색을 했는데 내가쓰는건 검색이 좀 예전시간만 되고 
라이브k나 한글트위터사이트는 바로바로 검색이 된다 ㅠㅠ
제휴를 맺은것인가 아니면 뭔가 다른게 있느것인가 결국 포기하게 이른다..흐헉흐헉.
허나 또하나의 이유가 있는데 이건 기술적인 문제라 ㅠㅠ...이것도 머리 아플것 같아..
			</pre>
            
            
            
            <hr id="section-4">
             
            
            
            <h2>다음 오픈 에디터 업로드 기능 jsp버전.</h2>
            <p>개발기간 - &gt; 하루</p>
            <p>
            	<a href="http://me.people-wiki.com/daumeditor_jsp/daumEditor.jsp" target="_blank" >[방문하기]</a>
            </p>            
           	<pre>	
php버전을봤는데 그걸 참고하여 jsp버전으로 만든거..ㅋㅋ
블로그에 포스팅을 했는데 확인차            		
			</pre>
			
			
				
            
        </div>
    </div>
</div>
</body>

<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>
<nav class="navbar navbar-default navbar-fixed-bottom" role="navigation">
<div style="color: rgb(153, 153, 153);text-align:center;padding:20px 0px;">2014 Park Beomjun</div>
</nav>
 
 
</html>