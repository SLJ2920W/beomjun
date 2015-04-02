<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- <%@ page session="false" %> --%>
<%-- <%@include file="include.jsp" %> --%>
<!doctype html>
<!--[if lt IE 7 ]> <html lang="pt-BR" xmlns:fb="http://ogp.me/ns/fb#" class="no-js oldie ie6 lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7 ]>    <html lang="pt-BR" xmlns:fb="http://ogp.me/ns/fb#" class="no-js oldie ie7 lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8 ]>    <html lang="pt-BR" xmlns:fb="http://ogp.me/ns/fb#" class="no-js oldie ie8 lt-ie9"> <![endif]-->
<!--[if IE 9 ]>    <html lang="pt-BR" xmlns:fb="http://ogp.me/ns/fb#" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--><html lang="pt-BR" xmlns:fb="http://ogp.me/ns/fb#" class="no-js" ><!--<![endif]-->



<head>
	<meta charset="utf-8" />
	<title>박범준		<spring:message code="resume.title"/>		</title>
	<meta name="description" content="Park Beomjun" />
	<meta name="author" content="Park Beomjun. http://www.people-wiki.com" />
	<meta name="robots" content="index,follow" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
	
	<!-- Meta Facebook -->
	<meta property="og:title" content="Park Beomjun"/>
	<meta property="og:description" content="Park Beomjun. http://www.people-wiki.com"/>
	
	<!-- Favicon -->
	<link rel="shortcut icon" href="${rootPath}/resources/images/resume/favicon.ico" />
	
	<!-- Style -->
	<link rel="stylesheet" href="${rootPath}/resources/css/resume/default.css" media="screen" />
	<link rel="stylesheet" href="${rootPath}/resources/css/resume/style.css" media="screen" />
	
	
	<!-- Respond JS -->
	<script src="${rootPath}/resources/js/resume/respond.min.js"></script>
	
	<!-- Modernizr JS -->
	<script src="${rootPath}/resources/js/resume/modernizr-2.5.3.min.js"></script>
	
	<!-- Flexslider JS -->
	<link rel="stylesheet" href="${rootPath}/resources/js/resume/flexslider/flexslider.css" type="text/css" media="screen" />
	
	
	<!-- Google Fonts -->
	<style>
		@import url(http://fonts.googleapis.com/earlyaccess/nanummyeongjo.css);
		@import url(http://fonts.googleapis.com/css?family=Yanone+Kaffeesatz);
		body{
			font-family: 'Yanone Kaffeesatz','Nanum Myeongjo', monospace !important;
		}
	</style>
	
</head>




<body class="pattern3 blue home">
	<div id="all" class="center">
		<div id="main" class="center">
	      <!-- BEGIN STYLE CHANGER -->
	        <div id="change-color">
	            Color
	            <ul>
	                <li><a class="color-blue" href="blue">Blue</a></li>
	                <li><a class="color-orange" href="orange">Orange</a></li>
	                <li><a class="color-red" href="red">Red</a></li>
	                <li><a class="color-green" href="green">Green</a></li>
	            </ul>
	        </div>
	        <div id="change-pattern">
	            Pattern
	            <ul>
	                <li><a href="pattern1"><img src="${rootPath}/resources/images/resume/pattern1-thumb.jpg" alt="Pattern1 Thumb" width="20" height="20"></a></li>
	                <li><a href="pattern2"><img src="${rootPath}/resources/images/resume/pattern2-thumb.jpg" alt="Pattern2 Thumb" width="20" height="20"></a></li>
	                <li><a href="pattern3"><img src="${rootPath}/resources/images/resume/pattern3-thumb.jpg" alt="Pattern3 Thumb" width="20" height="20"></a></li>
	                <li><a href="pattern4"><img src="${rootPath}/resources/images/resume/pattern4-thumb.jpg" alt="pattern4 Thumb" width="20" height="20"></a></li>
	            </ul>
	        </div>
	        <!-- END STYLE CHANGER -->
	        <!-- BEGIN HEADER -->
	        <header id="header">
	            <div id="title">
	             <h1>Park Beomjun</h1>
	               <div id="subtitle">Web Developer</div>
	            </div>
	            <div id="myphoto">
	                <figure><img src="${rootPath}/resources/images/resume/myphoto.jpg" alt="Park Beomjun | Web Developer" width="150" height="150"></figure>
	            </div>
	            <nav id="nav-global">
	                <ul id="nav-global-ul">
	                    <li class="nav-global-li nav-home"><a href="#sec-home" title="HOME" class="nav-global-a">HOME</a></li>
	                    <li class="nav-global-li nav-resume"><a href="#sec-resume" title="RESUME" class="nav-global-a">RESUME</a></li>
	                    <li class="nav-global-li nav-portfolio"><a href="#sec-portfolio" title="PORTFOLIO" class="nav-global-a">PORTFOLIO</a></li>
	                    <li class="nav-global-li nav-contact"><a href="#sec-contact" title="CONTACT" class="nav-global-a">CONTACT</a></li>
	                </ul>
	            </nav> 
	        </header>
	        <!-- END HEADER -->
	        <!-- BEGIN CONTENT -->
	    	<section id="content">
	             <!-- BEGIN HOME -->
	            <section id="sec-home">
	            
	            	<!-- 본문 소개글 시작-->
	                <p>
	                	안녕하세요 박범준 입니다  
	                </p>
	                <p>
	                	RESUME에는 제가 수행했던 프로젝트 와 재직한 회사 그리고 간단한 기술이력이 있으며
	                </p>
	                <p>
	                	PORTFOLIO에는 제가 개인적으로 하였던 프로젝트가 명시되어 있습니다
	                </p>
	                <p>
	                	현재 관심사
	                </p>	
					<ul>
						<li>mail server</li>
						<li>SSL, secureCoding, hhtps</li>  
						<li>linux, samba, svn server</li>
						<li>tomcat 8, mysql(대용량처리)</li>
						<li>jenkins, maven</li>
						<li>spring4.x (security, aop, transaction, restful, socket)</li>
						<li>OAuth, SSO, owasp</li>
						<li>html5, css3, bootstrap</li>
						<li>node.js, vert.x, angularjs, backbonejs</li>
					</ul>	
	                
	                <!-- 본문 소개글 종료-->
	                
	                <h2>Personal Info</h2>
	                <div class="clearfix">
	                <table class="table fl" border="0" cellspacing="0" cellpadding="0">
	                  <tr>
	                    <td class="td-title">Name</td>
	                    <td>박 범준</td>
	                  </tr>
	                  <tr>
	                    <td class="td-title">Date of birth</td>
	                    <td>1986. 08. 08</td>
	                  </tr>
	                  <tr>
	                    <td class="td-title">Address</td>
	                    <td>서울특별시 영등포구 영등포동</td>
	                  </tr>
	                </table>
	                <table class="table fr" border="0" cellspacing="0" cellpadding="0">
	                  <tr>
	                    <td class="td-title">Email</td>
	                    <td>bjpark@people-wiki.com</td>
	                  </tr>
	                  <tr>
	                    <td class="td-title">Phone</td>
	                    <td>010-96759-6662</td>
	                  </tr>
	                  <tr>
	                    <td class="td-title">Website</td>
	                    <td><a href="www.people-wiki.com" target="_blank">www.people-wiki.com</a></td>
	                  </tr>
	                </table>
	                </div>
	            </section>
	            <!-- END HOME -->
	            <!-- BEGIN RESUME -->
	            <section id="sec-resume">
	                <div id="all-resume">
	                    <div class="fl">
	                        <section id="employment">
	                            <h2>Employment</h2>
	                            <ul>
	                                <li>
	                                    <span>Manager - Company (2012 - Present)</span><br/>
	                                    <p>웹폰트를 만들어서 사용하는 것보다 웹폰트를 제공하는 사이트를 이용하는게 훨씬 편합니다. 간단한 코드 몇 줄로 바로 적용시킬 수 있습니다.
	
	대표적인 웹폰트 제공 사이트는 Google Fonts입니다. 600개가 넘는 웹폰트가 있으며, 누구나 무료로 사용할 수 있습니다.
	
	</p>
	                                </li>
	                            </ul>
	                        </section>
	                        <section id="education">
	                            <h2>Project</h2>
	                            <ul>
	                                <li>
	                                    <span>Graphic Design - School Of Design (2010 - 2012)</span><br/>
	                                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce rutrum sapien quis purus fringilla cursus.</p>
	                                </li>
	                                <li>
	                                    <span>Graphic Design - School Of Design (2010 - 2012)</span><br/>
	                                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce rutrum sapien quis purus fringilla cursus.</p>
	                                </li>
	                            </ul>
	                        </section>
	                    </div>
	                    <div class="fr">
	                    	
	                        <section id="design-skills">
	                            <h2>STATUS</h2>
	                            <ul>
	                                <li>
	                                    <div class="stars">
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-0">&nbsp;</span>
	                                    </div>
										의지 / 끈기
	                                </li>
	                                <li>
	                                    <div class="stars">
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                    </div>
										체력
	                                </li>
	                                <li>
	                                    <div class="stars">
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-0">&nbsp;</span>
	                                        <span class="star-0">&nbsp;</span>
	                                    </div>
										대인관계
	                                </li>
	                            </ul>
	                        </section>
	                        
	                        <section id="programming-skills">
	                            <h2>Programming Skills</h2>
	                            <ul>
	                                <li>
	                                    <div class="stars">
	                                        <span class="star-100">&nbsp;	</span>
	                                        <span class="star-100">&nbsp;	</span>
	                                        <span class="star-100">&nbsp;	</span>
	                                        <span class="star-50">&nbsp;	</span>
	                                        <span class="star-0">&nbsp;		</span>
	                                    </div>
										JAVA / JSP 
	                                </li>
	                                <li>
	                                    <div class="stars">
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-0">&nbsp;</span>
	                                        <span class="star-0">&nbsp;</span>
	                                    </div>
	                                    MySQL
	                                </li>
	                                <li>
	                                    <div class="stars">
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-0">&nbsp;</span>
	                                    </div>
	                                    Javascript / jQuery
	                                </li>
	                                <li>
	
	                                    <div class="stars">
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-100">&nbsp;</span>
	                                        <span class="star-0">&nbsp;</span>
	                                        <span class="star-0">&nbsp;</span>
	                                    </div>HTML / CSS
	                                </li>
	                            </ul>
	                        </section>
	                        
							
	                        <section id="tool-skills">
	                            <h2>USED</h2>
	                            <ul>
	                                <li>
	                                    Spring 3.x / iBatis / Tiles2 / Struts2 
	                                </li>
	                                <li>
	                                    Apache / Tomcat 
	                                </li>
	                                <li>
	                                	SVN / MAVEN 
	                                </li>
	                                <li>
	                                	LINUX / WINDOW 
	                                </li>
	                                <li>
										MySQL / Oracle / PostgreSQL / Tibero
	                                </li>
	                            </ul>
	                        </section>
	                    </div>
	                </div>
	                <section id="testimonials">
	                    <h2>Testimonials</h2>
	                    <ul>
	                        <li class="fl">
	                            <div class="aspas">"</div>
	                            <img src="${rootPath}/resources/images/resume/photo-testimonials.jpg" width="50" height="50">
	                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce rutrum sapien quis purus fringilla. Integer a turpis sed nunc pellentesque vulputate sed.
	                            <br/><span>Thiago Nunes - CEO and Web Designer</span></p>
	                        </li>
	                        <li class="fr">
	                            <div class="aspas">"</div>
	                            <img src="${rootPath}/resources/images/resume/photo-testimonials.jpg" width="50" height="50">
	                            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce rutrum sapien quis purus fringilla. Integer a turpis sed nunc pellentesque vulputate sed.
	                            <br/><span>Thiago Nunes - CEO and Web Designer</span></p>
	                        </li>
	                    </ul>
	                </section>
	            </section>
	            <!-- END RESUME -->
	            <!-- BEGIN PORTFOLIO -->
	            <section id="sec-portfolio">
	                <section id="portfolio">
	                    <div class="flexslider">
	                        <ul class="slides">
	                            <li>
	                                <ul class="portfolio-slide">
	                                    <li>
	                                    	<figure>
	                                    		<a class="show-portfolio-text" href="#port1">
	                                    			<span></span>
	                                    			<img alt="Portfolio" src="${rootPath}/resources/images/resume/thumbnail/thumbnail_board.png" style="width:180px;height:120px;"/>
	                                    		</a>
	                                    	</figure>
	                                    	<a class="show-portfolio-text" href="#port1">멀티 게시판</a>
	                                    </li>
	                                    <li>
	                                    	<figure>
	                                    		<a class="show-portfolio-text" href="#port2">
	                                    			<span></span>
	                                    			<img alt="Portfolio" src="${rootPath}/resources/images/resume/thumbnail/thumbnail_privacySite.png" style="width:180px;height:120px;"/>
	                                    		</a>
	                                    	</figure>
	                                    	<a class="show-portfolio-text" href="#port1">개인화 사이트</a>
	                                    </li>
	                                    <li>
	                                    	<figure>
	                                    		<a class="show-portfolio-text" href="#port3">
	                                    			<span></span>
	                                    			<img alt="Portfolio" src="${rootPath}/resources/images/resume/thumbnail/thumbnail_twitter.png" style="width:180px;height:120px;"/>
	                                    		</a>
	                                    	</figure>
	                                    	<a class="show-portfolio-text" href="#port1">트위터 실시간 검색</a>
	                                    </li>
	                                    <li>
	                                    	<figure>
	                                    		<a class="show-portfolio-text" href="#port4">
	                                    			<span></span>
	                                    			<img alt="Portfolio" src="${rootPath}/resources/images/resume/thumbnail/thumbnail_daum.jpg" style="width:180px;height:120px;"/>
	                                    		</a>
	                                    	</figure>
	                                    	<a class="show-portfolio-text" href="#port1">다음 에디터 업로드 기능</a>
	                                    </li>
	                                    
<%-- 	                                
										<li><figure><a class="show-portfolio-img" href="${rootPath}/resources/images/resume/portfolio-default-big.jpg"><span></span><img alt="Portfolio" src="${rootPath}/resources/images/resume/portfolio-default.jpg" /></a></figure><a class="show-portfolio-img" href="${rootPath}/resources/images/resume/portfolio-default-big.jpg">Projetct Title</a></li>
	                                    <li><figure><a class="show-portfolio-link" href="#" target="_blank"><span></span><img alt="Portfolio" src="${rootPath}/resources/images/resume/portfolio-default.jpg" /></a></figure><a class="show-portfolio-link" href="#" target="_blank">Projetct Title</a></li>
	                                    <li><figure><a class="show-portfolio-text" href="#port1"><span></span><img alt="Portfolio" src="${rootPath}/resources/images/resume/portfolio-default.jpg" /></a></figure><a class="show-portfolio-text" href="#port1">Projetct Title</a></li>
	                                    <li><figure><a class="show-portfolio-img" href="${rootPath}/resources/images/resume/portfolio-default-big.jpg"><span></span><img alt="Portfolio" src="${rootPath}/resources/images/resume/portfolio-default.jpg" /></a></figure><a class="show-portfolio-img" href="${rootPath}/resources/images/resume/portfolio-default-big.jpg">Projetct Title</a></li>
	                                    <li><figure><a class="show-portfolio-link" href="#" target="_blank"><span></span><img alt="Portfolio" src="${rootPath}/resources/images/resume/portfolio-default.jpg" /></a></figure><a class="show-portfolio-link" href="#" target="_blank">Projetct Title</a></li>
	                                    <li><figure><a class="show-portfolio-text" href="#port1"><span></span><img alt="Portfolio" src="${rootPath}/resources/images/resume/portfolio-default.jpg" /></a></figure><a class="show-portfolio-text" href="#port1">Projetct Title</a></li>
	                                    <li><figure><a class="show-portfolio-img" href="${rootPath}/resources/images/resume/portfolio-default-big.jpg"><span></span><img alt="Portfolio" src="${rootPath}/resources/images/resume/portfolio-default.jpg" /></a></figure><a class="show-portfolio-img" href="${rootPath}/resources/images/resume/portfolio-default-big.jpg">Projetct Title</a></li> 
--%>
	                                </ul>
	                            </li>
	                            <%-- 
	                            <li>
	                                <ul class="portfolio-slide">
	                                    <li><figure><a class="show-portfolio-img" href="${rootPath}/resources/images/resume/portfolio-default-big.jpg"><span></span><img alt="Portfolio" src="${rootPath}/resources/images/resume/portfolio-default.jpg" /></a></figure><a class="show-portfolio-img" href="${rootPath}/resources/images/resume/portfolio-default-big.jpg">Projetct Title</a></li>
	                                    <li><figure><a class="show-portfolio-link" href="#" target="_blank"><span></span><img alt="Portfolio" src="${rootPath}/resources/images/resume/portfolio-default.jpg" /></a></figure><a class="show-portfolio-link" href="#" target="_blank">Projetct Title</a></li>
	                                    <li><figure><a class="show-portfolio-text" href="#port1"><span></span><img alt="Portfolio" src="${rootPath}/resources/images/resume/portfolio-default.jpg" /></a></figure><a class="show-portfolio-text" href="#port1">Projetct Title</a></li>
	                                    <li><figure><a class="show-portfolio-img" href="${rootPath}/resources/images/resume/portfolio-default-big.jpg"><span></span><img alt="Portfolio" src="${rootPath}/resources/images/resume/portfolio-default.jpg" /></a></figure><a class="show-portfolio-img" href="${rootPath}/resources/images/resume/portfolio-default-big.jpg">Projetct Title</a></li>
	                                </ul>
	                            </li>
	                             --%>
	                        </ul>
	                    </div>
	                </section>
	            </section>
	            <!-- BEGIN PORTFOLIO DETAIL -->
	            <div style="display: none;">
	
					<%@include file="./popup.jsp" %>
	
	            </div>
	            <!-- END PORTFOLIO DETAIL -->
	            <!-- END PORTFOLIO -->
	            <!-- BEGIN CONTACT -->
	            <section id="sec-contact">
	                <div class="fl">
	                    <section id="contact-info">
	                        <h2>Contact Info</h2>
							<!--
	                        <iframe width="100%" height="160" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src="https://maps.google.com.br/maps?f=q&amp;source=s_q&amp;hl=pt-BR&amp;geocode=&amp;q=21+New+York+Avenue,+New+York,+USA&amp;aq=&amp;sll=-14.239424,-53.186502&amp;sspn=56.118961,107.138672&amp;ie=UTF8&amp;hq=&amp;hnear=21+New+York+Ave,+Brooklyn,+Kings,+New+York+11216,+EUA&amp;t=m&amp;ll=40.679466,-73.946829&amp;spn=0.010415,0.032358&amp;z=14&amp;iwloc=A&amp;output=embed"></iframe>
							-->
	                        <p>
	                        	<span>서울특별시 영등포구 영등포동</span> <br/><br/>
	                            <span>Email:</span> bjpark@people-wiki.com<br/>
	                            <span>Phone:</span> 010-9759-6662<br/>
	                            <span>Web site:</span> <a href="${rootPath}/list" target="_blank">포트폴리오 리스트</a>
							</p>
	                    </section>
	                </div>
	                <div class="fr">
	                    <section id="contact-me">
	                        <h2>Contact Me</h2>
	                            <form id="form-contact-me" name="form-contact-me" action="#">
	                              <dl>
	                                <dt>Your Name</dt>
	                                <dd><input type="text" name="name" id="name" class="field f1" value="" /></dd>
	                                <dt>Your Email</dt>
	                                <dd><input type="text" name="email" id="email" class="field f1" value="" /></dd>
	                                <dt>Your Message</dt>
	                                <dd><textarea name="message" id="message" class="field f2"></textarea></dd>
	                                <dt>&nbsp;</dt>
	                                <dd><input type="submit" value="Send Message" /></dd>
	                              </dl>
	                            </form>
	                    </section>
	                </div>
	            </section>
	            <!-- END CONTACT -->
	        </section>
	    <!-- END CONTENT -->	
	    <!-- BEGIN FOOTER -->
			<footer id="footer">
				<div>
	              	<section id="download-vcard">
	                    <a href="https://www.dropbox.com/s/zkvcyeeimuqmmoq/Beomjun%20Park_.vcf" target="_blank" title="Download vCard">Download vCard</a>
	                </section>
	                <section id="social">
	                    <ul>
	                        <li class="social-rss"><a href="" target="_blank" title="RSS">RSS</a></li>
	                        <li class="social-twitter"><a href="http://me2.do/GAOPR5l1" target="_blank" title="Twitter">Twitter</a></li>
	                        <li class="social-googleplus"><a href="http://me2.do/5HdIaMpb" target="_blank" title="Google Plus">Google Plus</a></li>
	                        <li class="social-facebook"><a href="http://me2.do/58SWdQZ4" target="_blank" title="Facebook">Facebook</a></li>
	                        <li class="social-linkedin"><a href="http://me2.do/xzCZmYjE" target="_blank" title="Linkedin">Linkedin</a></li>
	                    </ul>
	                </section>
	            </div>
	            <section id="copyright">
	              © 2014 Park Beomjun
	            </section>
	        </fotter>
	    <!-- END FOOTER -->
	    </div>
	</div>
	
	
	<!-- JS DEFAUL FOOTER -->
	    <!-- jQuery file -->
 	    <script src="${rootPath}/resources/js/resume/jquery-1.7.2.min.js"></script> 
	
	    <!-- Flexslider -->
	    <script src="${rootPath}/resources/js/resume/flexslider/jquery.flexslider-min.js"></script>
	
	    <!-- Easytabs -->
	    <script src="${rootPath}/resources/js/resume/jquery.hashchange.min.js"></script>
	    <script src="${rootPath}/resources/js/resume/jquery.easytabs.min.js"></script>
	
	    <!-- jQuery validate -->
	    <script src="${rootPath}/resources/js/resume/jquery.validate.min.js"></script>
	
	    <!-- Fancybox -->
	    <script type="text/javascript" src="${rootPath}/resources/js/resume/fancybox/jquery.mousewheel-3.0.4.pack.js"></script>
	    <script type="text/javascript" src="${rootPath}/resources/js/resume/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
	    <link rel="stylesheet" type="text/css" href="${rootPath}/resources/js/resume/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
	
	    <!-- Custom.JS file -->
	    <script src="${rootPath}/resources/js/resume/custom.js"></script>

	<!-- END JS DEFAUL FOOTER -->


<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-52060021-1', 'people-wiki.com');
  ga('send', 'pageview');

</script>

<div id="spot-im-root"></div>
<script type="text/javascript">!function(t,o,p){function e(){var t=o.createElement("script");t.type="text/javascript",t.async=!0,t.src=("https:"==o.location.protocol?"https":"http")+":"+p,o.body.appendChild(t)}t.spotId="1ae628905610175f4bfe43c28fb2e356",t.spotName="",t.allowDesktop=!0,t.allowMobile=!1,t.containerId="spot-im-root",e()}(window.SPOTIM={},document,"//www.spot.im/embed/scripts/launcher.js");</script>

</body>
</html>