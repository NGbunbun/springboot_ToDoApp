package com.example.todoapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todoapp.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

}
