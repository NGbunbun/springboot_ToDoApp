package com.example.todoapp;

import java.util.Collection;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TodoController {
	
	@GetMapping("/")
	@PreAuthorize("permitAll")
	public ModelAndView index(ModelAndView mav) {
		mav.setViewName("index");
		mav.addObject("title", "Todo List Application");
		mav.addObject("msg", "This is top page.");
		return mav;
	}
	
	@GetMapping("/secret")
	@PreAuthorize("isAuthenticated()")
	public ModelAndView secret(ModelAndView mav) {
		// ログイン中のユーザー名、ユーザー権限を取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Collection<? extends GrantedAuthority > authorities =  authentication.getAuthorities();
		// 取得したユーザー権限によりテンプレートファイルの表示を切り替える
		String roleName = authorities.toString();
		boolean isAdmin = roleName.equals("[ROLE_ADMIN]");
		
		mav.setViewName("secret");
		mav.addObject("title", "MEMBERS ONLY PAGE");
		mav.addObject("msg", "ようこそ \"" + username + "\" さん！");
		mav.addObject("isAdmin", isAdmin);
		return mav;
	}
	
	@GetMapping("/login")
	@PreAuthorize("permitAll")
	public ModelAndView login(ModelAndView mav, @RequestParam(value = "error", required = false) String error) {
		mav.setViewName("login");
		
		if(error != null) {
			mav.addObject("msg", "ユーザー名、またはパスワードが正しくありません。");			
		} else {
			mav.addObject("msg", "ユーザー名、パスワードを入力してください。");						
		}
		
		return mav;
	}
}