package com.example.todoapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class TodoController {
	
	@Autowired
	TodoService todoService;
	
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
	
	@GetMapping("/todo/create")
	@PreAuthorize("isAuthenticated()")
	public ModelAndView create(ModelAndView mav, @ModelAttribute Todo todoItem) {
		mav.setViewName("todo/create");
		mav.addObject("title", "TODO管理ページ");
		mav.addObject("msg", "TODO編集");
		mav.addObject("formModel", todoItem);
		return mav;
	}
	
	@PostMapping("/todo/create")
	@PreAuthorize("isAuthenticated()")
	public ModelAndView createForm(ModelAndView mav, @ModelAttribute("formModel") @Validated Todo todoItem, BindingResult result) {
		if(result.hasErrors()) {
			System.out.println("Validation errors: " + result.getAllErrors());
			
			mav.setViewName("todo/create");
			mav.addObject("title", "TODO管理ページ");
			mav.addObject("msg", "※入力内容に誤りがあります。");
			mav.addObject("formModel", todoItem);
			return mav;
		}
		
		// ログイン中のユーザー情報を取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		todoService.create(username, todoItem);
		return new ModelAndView("redirect:/todo/create");
		
	}
}













