package com.goatodo.api.todo.presentation;

import com.bombo.goatodo.domain.todo.service.TodoService;
import com.bombo.goatodo.domain.todo.service.dto.TodoResponse;
import com.bombo.goatodo.domain.todo.service.dto.TodosResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/todo")
    public ResponseEntity<Void> createTodo(@Validated @RequestBody TodoCreateRequest todoCreateRequest) {
        todoService.postTodo(todoCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<TodoResponse> findOne(
            @PathVariable Long id,
            @Validated @RequestBody TodoReadRequest todoReadRequest) {
        TodoResponse findTodoResponse = todoService.findOne(todoReadRequest);
        return ResponseEntity.status(HttpStatus.OK).body(findTodoResponse);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<TodosResponse> findMemberTodos(
            @PathVariable Long id) {
        TodosResponse findTodosResponse = todoService.findAllByMember(id);
        return ResponseEntity.status(HttpStatus.OK).body(findTodosResponse);
    }

    @PostMapping("/todo/{id}/modify/completeStatus")
    public ResponseEntity<Void> modifyTodoCompleteStatus(
            @PathVariable Long id,
            @Validated @RequestBody TodoCompleteStatusUpdateRequest todoCompleteStatusUpdateRequest) {
        todoService.changeCompleteStatus(todoCompleteStatusUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/todo/{id}/modify")
    public ResponseEntity<Void> modifyTodo(
            @PathVariable Long id,
            @Validated @RequestBody TodoUpdateRequest todoUpdateRequest) {
        todoService.changeTodo(todoUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long id,
            @Validated @RequestBody TodoDeleteRequest todoDeleteRequest) {
        todoService.deleteTodo(todoDeleteRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
