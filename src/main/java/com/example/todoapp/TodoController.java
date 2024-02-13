package com.example.todoapp;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

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
	public ModelAndView secret(ModelAndView mav, HttpServletRequest request) {
		String user = request.getRemoteUser();
		String msg = "ようこそ \"" + user + "\" さん！";
		
		mav.setViewName("secret");
		mav.addObject("title", "MEMBERS ONLY PAGE");
		mav.addObject("msg", msg);
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
