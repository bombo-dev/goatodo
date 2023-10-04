package com.goatodo.application.todo;

import com.goatodo.application.todo.dto.TodoResponse;
import com.goatodo.application.todo.dto.TodosResponse;
import com.goatodo.application.todo.dto.request.*;
import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.NotExistIdRequestException;
import com.goatodo.domain.member.Member;
import com.goatodo.domain.member.repository.MemberRepository;
import com.goatodo.domain.todo.Tag;
import com.goatodo.domain.todo.Todo;
import com.goatodo.domain.todo.repository.TagRepository;
import com.goatodo.domain.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;

    @Transactional
    public Long postTodo(TodoServiceCreateRequest request) {
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Tag tag = tagRepository.findById(request.tagId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Todo todo = Todo.createTodo(member, tag, request.title(), request.description());
        Todo savedTodo = todoRepository.save(todo);
        return savedTodo.getId();
    }

    public TodoResponse findOne(Long todoId, TodoServiceReadRequest request) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        return new TodoResponse(todo);
    }

    public TodosResponse findAllByMember(Long memberId) {
        List<TodoResponse> findTodoList = todoRepository.findAllByMember_Id(memberId)
                .stream()
                .map(TodoResponse::new)
                .toList();

        return new TodosResponse(findTodoList);
    }

    @Transactional
    public void changeCompleteStatus(Long todoId, TodoServiceCompleteStatusUpdateRequest request) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));
        todo.validActive();
        todo.validOwn(request.memberId(), ErrorCode.EDIT_REQUEST_IS_FORBIDDEN);

        todo.changeCompleteStatus(request.completeStatus());
    }

    @Transactional
    public void updateTodo(Long todoId, TodoServiceUpdateRequest request) {

        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Tag tag = tagRepository.findById(request.tagId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        tag.validOwn(member.getId(), ErrorCode.EDIT_REQUEST_IS_FORBIDDEN);
        todo.validOwn(member.getId(), ErrorCode.EDIT_REQUEST_IS_FORBIDDEN);
        todo.validActive();

        Todo updateTodo = Todo.createTodo(member, tag, request.title(), request.description());

        todo.updateTodo(updateTodo);
    }

    @Transactional
    public void deleteTodo(Long todoId, TodoServiceDeleteRequest request) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        todo.validOwn(request.memberId(), ErrorCode.DELETE_REQUEST_IS_FORBIDDEN);
        todo.validActive();

        todoRepository.delete(todo);
    }
}
