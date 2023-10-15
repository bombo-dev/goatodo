package com.goatodo.application.todo;

import com.goatodo.application.todo.dto.TodoResponse;
import com.goatodo.application.todo.dto.TodosResponse;
import com.goatodo.application.todo.dto.request.TodoServiceCompleteStatusUpdateRequest;
import com.goatodo.application.todo.dto.request.TodoServiceCreateRequest;
import com.goatodo.application.todo.dto.request.TodoServiceDeleteRequest;
import com.goatodo.application.todo.dto.request.TodoServiceUpdateRequest;
import com.goatodo.application.user.LevelService;
import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.NotExistIdRequestException;
import com.goatodo.domain.tag.Tag;
import com.goatodo.domain.tag.repository.TagRepository;
import com.goatodo.domain.todo.CompleteStatus;
import com.goatodo.domain.todo.Todo;
import com.goatodo.domain.todo.repository.TodoRepository;
import com.goatodo.domain.user.User;
import com.goatodo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final LevelService levelService;

    @Transactional
    public Long save(TodoServiceCreateRequest request) {
        // TODO User는 이후에 인증된 상태여야 한다. 로그인 된 상태에서만 수행되도록 변경
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Tag tag = tagRepository.findById(request.tagId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Todo todo = Todo.createTodo(user, tag, request.title(), request.description(), request.difficulty());
        Todo savedTodo = todoRepository.save(todo);
        return savedTodo.getId();
    }

    public TodoResponse findOne(Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        return new TodoResponse(todo);
    }

    public TodosResponse findAllByMember(Long userId) {
        List<TodoResponse> findTodoList = todoRepository.findAllByMember_Id(userId)
                .stream()
                .map(TodoResponse::new)
                .toList();

        return new TodosResponse(findTodoList);
    }

    @Transactional
    public void changeCompleteStatus(Long todoId, TodoServiceCompleteStatusUpdateRequest request) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));
        todo.validOwn(request.userId(), ErrorCode.EDIT_REQUEST_IS_FORBIDDEN);

        CompleteStatus before = todo.getCompleteStatus();
        todo.changeCompleteStatus(request.completeStatus());
        CompleteStatus after = todo.getCompleteStatus();

        levelService.changeExperience(request.userId(), todo.getExp(), before, after);
    }

    @Transactional
    public void updateTodo(Long todoId, TodoServiceUpdateRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Tag tag = tagRepository.findById(request.tagId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        tag.validOwn(user.getId());
        todo.validOwn(user.getId(), ErrorCode.EDIT_REQUEST_IS_FORBIDDEN);

        Todo updateTodo = Todo.createTodo(user, tag, request.title(), request.description(), request.difficulty());
        todo.updateTodo(updateTodo);
    }

    @Transactional
    public void deleteTodo(Long todoId, TodoServiceDeleteRequest request) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));
        todo.validOwn(request.userId(), ErrorCode.DELETE_REQUEST_IS_FORBIDDEN);

        todoRepository.delete(todo);
    }
}
