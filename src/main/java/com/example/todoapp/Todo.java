package com.example.todoapp;


import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "todo")
public class Todo {
	
	// フィールド
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	@NotNull
	private long id;
	
//	投稿したusername
	@Column(nullable = false)
	private String username;
	
//	投稿内容
	@Column(length = 50, nullable = false)
	@Size(max = 50, message = "50字以内で入力してください。")
	@NotBlank(message = "内容は必須です。")
	private String content;
	
//	完了状態
	@Column(nullable = false)
	private boolean done;
	
//	作成日
	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate created;
	
//	期日（値は必須ではない、値は現在以降に限定する）
	@Column(nullable = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@FutureOrPresent(message = "値は現在、または未来の日付にしてください。")
	private LocalDate deadline;

	
	// 以下アクセサー
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public LocalDate getCreated() {
		return created;
	}

	public void setCreated(LocalDate create) {
		this.created = create;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
		
}