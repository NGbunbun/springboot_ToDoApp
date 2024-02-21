package com.example.todoapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.todoapp.repositories.TodoRepository;

@Controller
@RequestMapping("/todo")
public class AuthenticatedUserController {
	
	@Autowired
	TodoRepository repository;
	
	@Autowired
	TodoService todoService;
	
	@GetMapping("/create")
	@PreAuthorize("isAuthenticated()")
	public ModelAndView create(ModelAndView mav, @ModelAttribute Todo todoItem) {
		// ログイン中のユーザー情報を取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		mav.setViewName("todo/create");
		mav.addObject("title", "TODO管理ページ");
		mav.addObject("formModel", todoItem);
		
		List<Todo> data = repository.findByUsername(username);
		mav.addObject("name", username + " さんのTODO一覧");
		mav.addObject("data", data);
		
		// 期日が閲覧時点より7日以内かつ未完了のTODOは赤色に表示させる
		List<Boolean> deadlineFlagList = isNearDeadlineList(data);
		mav.addObject("deadlineFlagList", deadlineFlagList);
		mav.addObject("today", LocalDate.now());
		
		return mav;
	}
	
	@PostMapping("/create")
	@PreAuthorize("isAuthenticated()")
	public ModelAndView createForm(ModelAndView mav, @ModelAttribute("formModel") @Validated Todo todoItem, BindingResult result) {
		// ログイン中のユーザー情報を取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		if(result.hasErrors()) {
			System.out.println("Validation errors: " + result.getAllErrors());
			
			mav.setViewName("todo/create");
			mav.addObject("title", "TODO管理ページ");
			mav.addObject("formModel", todoItem);
			
			List<Todo> data = repository.findByUsername(username);
			mav.addObject("name", username + " さんのTODO一覧");
			mav.addObject("data", data);
			
			List<Boolean> deadlineFlagList = isNearDeadlineList(data);
			mav.addObject("deadlineFlagList", deadlineFlagList);
			mav.addObject("today", LocalDate.now());
			
			return mav;
		}
		
		todoService.create(username, todoItem);
		
		return new ModelAndView("redirect:/todo/create");
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ModelAndView admin(ModelAndView mav) {
		mav.setViewName("todo/admin");
		mav.addObject("title", "管理者専用ページ");
		mav.addObject("msg", "全ユーザーTODO一覧");
		
		List<Todo> data = repository.findAll();
		mav.addObject("data", data);
		
		List<Boolean> deadlineFlagList = isNearDeadlineList(data);
		mav.addObject("deadlineFlagList", deadlineFlagList);
		mav.addObject("today", LocalDate.now());
		
		return mav;
	}
	
	// 各オブジェクトに設定されている期日が現時点より7日以内かつ未完了状態か判定する
	private List<Boolean> isNearDeadlineList(List<Todo> dataLists) {
		LocalDate notifyDate = LocalDate.now().plusDays(7);
		List<Boolean> isNearDeadlineList = new ArrayList<>();
		
		for(Todo list : dataLists) {
			boolean isNearDeadline = false;
			
			if(list.getDeadline() != null) {
				LocalDate dateOfDeadline = list.getDeadline();
				
				if(dateOfDeadline.isBefore(notifyDate) && !list.isDone()) {
					isNearDeadline = true;
				}
			}
			
			isNearDeadlineList.add(isNearDeadline);
		}
		
		return isNearDeadlineList;
	}
}