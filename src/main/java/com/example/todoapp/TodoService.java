package com.example.todoapp;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todoapp.repositories.TodoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TodoService {
	
	@Autowired
	TodoRepository repository;
	
	// ToDo登録機能
	public Todo create(String username, Todo item) {
		item.setUsername(username);
		item.setCreated(LocalDate.now()); // TODO作成日
		item.setDone(false);			// 完了状態（初期値は未完）
		repository.saveAndFlush(item);
		return item;
	}

}
