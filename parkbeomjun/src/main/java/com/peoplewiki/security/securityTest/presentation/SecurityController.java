package com.peoplewiki.security.securityTest.presentation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;

import net.sf.json.JSONObject;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.peoplewiki.security.common.presentation.ObjectController;
import com.peoplewiki.security.securityTest.domain.Article;
import com.peoplewiki.security.securityTest.domain.SecurityBean;
import com.peoplewiki.security.securityTest.service.SecurityService;
import com.peoplewiki.security.springSecurity.domain.MemberInfo;
import com.peoplewiki.security.springSecurity.domain.ReloadableFilterInvocationSecurityMetadataSource;

@Controller
@RequestMapping("/security/*")
public class SecurityController extends ObjectController {

	// http://stackoverflow.com/questions/7027366/spring-3-0-multiple-pathvariables-problem

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SecurityService securityService;

	@Autowired
	@Qualifier("reloadableFilterInvocationSecurityMetadataSource")
	private ReloadableFilterInvocationSecurityMetadataSource qr;

	public void setResumeService(SecurityService securityService) {
		this.securityService = securityService;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public LinkedList<Article> get(Model model) {
		FileReader reader;
		LinkedList<Article> articles = null;
		try {
			reader = new FileReader("D:\\DEV\\Project\\2015_luna_project\\parkbeomjun\\article.xml");
			// convert "unmarshal" data from XML "articles.xml" to Java object
			// LinkedList<Article>
			articles = (LinkedList) Unmarshaller.unmarshal(LinkedList.class, reader);
			model.addAttribute("articles", articles);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (MarshalException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}

		return articles;
	}
	
	// URL 리스트
	@RequestMapping(value = "/auth")
	public String urlList(ModelMap model, SecurityBean securityBean) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		logger.info("Method Name = {}", methodName);

		try {
			// URL 리스트
			model.putAll(securityService.urlList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "securityAuth.auth.main";
	}

	// URL 권한 설정 보기
	@RequestMapping(value = "/auth/{resource_id}")
	public String authUrlView(@PathVariable String resource_id, ModelMap model) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		logger.info("Method Name = {}", methodName);

		try {
			SecurityBean s = new SecurityBean();
			s.setResource_id(resource_id);
			model.put("resource_id", resource_id);

			// URL 리스트
			model.putAll(securityService.urlList());

			// 권한 URL 보기
			model.put("urlView", securityService.authUrlView(s));
			// 권한 URL 리스트
			model.put("urlListView", securityService.authUrlList(s));

			// 권한 리스트
			model.put("authList", securityService.authList());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "securityAuth.auth.view";
	}

	// 권한 URL 수정
	@RequestMapping(value = "/auth/update")
	public String authUrlUpdate(ModelMap model, SecurityBean securityBean) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		logger.info("Method Name = {}", methodName);

		String redirectResourceId = securityBean.getResource_id();
		try {
			// 권한 URL 수정
			securityService.authUrlUpdate(securityBean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/security/auth/" + redirectResourceId;
	}

	// URL 추가
	@RequestMapping(value = "/auth/insert")
	public String urlInsert(ModelMap model, SecurityBean securityBean) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		logger.info("Method Name = {}", methodName);

		try {
			securityService.urlInsert(securityBean);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/security/auth/" + securityBean.getResource_id();
	}

	// URL 수정 _ AJAX
	@RequestMapping(value = "/auth/urlUpdateAjax")
	public @ResponseBody JSONObject urlUpdateAjax(ModelMap model, SecurityBean securityBean) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		logger.info("Method Name = {}", methodName);

		// ModelAndView mv = new ModelAndView();

		JSONObject json = new JSONObject();
		try {
			securityService.urlUpdate(securityBean);
			json.put("flag", "true");
		} catch (Exception e) {
			json.put("flag", "false");
			e.printStackTrace();
		}
		return json;
	}

	// 메인 페이지
	@RequestMapping(value = "/main")
	public String home(ModelMap model) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		logger.info("Method Name = {}", methodName);

		try {

			// qr.reload();

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return "security.sub.main";
	}

	// 메인 페이지
	@RequestMapping(value = "/")
	public String redirect(ModelMap model) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		logger.info("Method Name = {}", methodName);
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "security.sub.main";
	}
	// 메인 페이지
	@RequestMapping(value = "/login")
	public String login(ModelMap model) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		logger.info("Method Name = {}", methodName);

		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "security.sub.main";
	}

	// 게시판 카테고리
	@RequestMapping(value = "/board/{path}")
	public String boardCategory(@PathVariable String path, ModelMap model) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		logger.info("Method Name = {}", methodName);

		try {
			model.addAttribute("category", path);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			// 로그인 하지 않았을 경우 anonymousUser 란 String 객체가 리턴된다(로그인 했으면
			// MemberInfo객체)
			Object principal = auth.getPrincipal();
			String name = "";
			if (principal != null && principal instanceof MemberInfo) {
				name = ((MemberInfo) principal).getName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "security.sub.boardList";
	}

	// 게시판 카테고리 테스트
	@RequestMapping(value = "/test/{path}")
	public String boardTest(@PathVariable String path, ModelMap model) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		logger.info("Method Name = {}", methodName);

		try {
			model.addAttribute("category", path);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			// 로그인 하지 않았을 경우 anonymousUser 란 String 객체가 리턴된다(로그인 했으면
			// MemberInfo객체)
			Object principal = auth.getPrincipal();
			String name = "";
			if (principal != null && principal instanceof MemberInfo) {
				name = ((MemberInfo) principal).getName();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "security.sub.boardList";
	}

	// 게시판 카테고리 테스트
	@RequestMapping(value = "/common/{path}")
	public String boardTest1(@PathVariable String path, ModelMap model) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		logger.info("Method Name = {}", methodName);

		try {
			model.addAttribute("category", path);
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "security.sub.boardList";
	}

	// 게시판 내용 보기
	@RequestMapping(value = "/{path}")
	public String boardView(@PathVariable int path, ModelMap model) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		logger.info("Method Name = {}", methodName);

		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "security.sub." + path;
	}

	// 사용자 정보와 관련 로그인, 로그아웃, 사용자 정보 수정...
	@RequestMapping(value = "/user/{path}")
	public String user(@PathVariable String path, ModelMap model) {
		String methodName = new Object() {
		}.getClass().getEnclosingMethod().getName();
		logger.info("Method Name = {}", methodName);

		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "security.sub.main";
	}

}
