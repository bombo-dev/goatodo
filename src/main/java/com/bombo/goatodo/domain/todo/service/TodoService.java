package com.bombo.goatodo.domain.todo.service;

import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.domain.member.repository.MemberRepository;
import com.bombo.goatodo.domain.todo.CompleteStatus;
import com.bombo.goatodo.domain.todo.Tag;
import com.bombo.goatodo.domain.todo.Todo;
import com.bombo.goatodo.domain.todo.controller.dto.*;
import com.bombo.goatodo.domain.todo.repository.TagRepository;
import com.bombo.goatodo.domain.todo.repository.TodoRepository;
import com.bombo.goatodo.domain.todo.service.dto.TodoResponse;
import com.bombo.goatodo.domain.todo.service.dto.TodosResponse;
import com.bombo.goatodo.global.error.ErrorCode;
import com.bombo.goatodo.global.exception.NotExistIdRequestException;
import com.bombo.goatodo.global.exception.RoleException;
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
    public Long postTodo(TodoCreateRequest todoCreateRequest) {
        Member findMember = memberRepository.findById(todoCreateRequest.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Tag findTag = tagRepository.findById(todoCreateRequest.tagId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Todo todo = convertCreateTodo(todoCreateRequest, findMember, findTag);
        Todo savedTodo = todoRepository.save(todo);
        return savedTodo.getId();
    }

    private Todo convertCreateTodo(TodoCreateRequest todoCreateRequest, Member findMember, Tag findTag) {
        return Todo.builder()
                .member(findMember)
                .tag(findTag)
                .title(todoCreateRequest.title())
                .description(todoCreateRequest.description())
                .completeStatus(CompleteStatus.READY)
                .isActive(true)
                .build();
    }

    private TodoResponse findOne(TodoReadRequest todoReadRequest) {
        Todo findTodo = todoRepository.findById(todoReadRequest.todoId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Member findMember = memberRepository.findById(todoReadRequest.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));
        validateTodoOwner(findMember, findTodo, ErrorCode.READ_REQUEST_IS_FORBIDDEN);

        return new TodoResponse(findTodo);
    }

    private TodosResponse findAll(TodoReadRequest todoReadRequest) {
        List<TodoResponse> findTodoList = todoRepository.findAllByMember_Id(todoReadRequest.memberId())
                .stream()
                .map(TodoResponse::new)
                .toList();

        return new TodosResponse(findTodoList);
    }

    @Transactional
    public void changeCompleteStatus(TodoCompleteStatusUpdateRequest todoCompleteStatusUpdateRequest) {
        Member findMember = memberRepository.findById(todoCompleteStatusUpdateRequest.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Todo findTodo = todoRepository.findById(todoCompleteStatusUpdateRequest.todoId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        validateTodoOwner(findMember, findTodo, ErrorCode.EDIT_REQUEST_IS_FORBIDDEN);
        validateActive(findTodo);
        findTodo.changeCompleteStatus(todoCompleteStatusUpdateRequest.completeStatus());
    }

    @Transactional
    public void changeTodo(TodoUpdateRequest todoUpdateRequest) {
        Member findMember = memberRepository.findById(todoUpdateRequest.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Todo findTodo = todoRepository.findById(todoUpdateRequest.todoId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Tag findTag = tagRepository.findById(todoUpdateRequest.tagId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        validateTodoOwner(findMember, findTodo, ErrorCode.EDIT_REQUEST_IS_FORBIDDEN);
        validateTagOwner(findMember, findTag, ErrorCode.EDIT_REQUEST_IS_FORBIDDEN);
        validateActive(findTodo);

        Todo todo = convertUpdateTodo(todoUpdateRequest, findTag);
        findTodo.updateTodo(todo);
    }

    private Todo convertUpdateTodo(TodoUpdateRequest todoUpdateRequest, Tag findTag) {

        return Todo.builder()
                .tag(findTag)
                .title(todoUpdateRequest.title())
                .description(todoUpdateRequest.description())
                .build();
    }

    @Transactional
    public void deleteTodo(TodoDeleteRequest todoDeleteRequest) {
        Member findMember = memberRepository.findById(todoDeleteRequest.memberId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        Todo findTodo = todoRepository.findById(todoDeleteRequest.todoId())
                .orElseThrow(() -> new NotExistIdRequestException(ErrorCode.NOT_EXIST_ID_REQUEST));

        validateTodoOwner(findMember, findTodo, ErrorCode.DELETE_REQUEST_IS_FORBIDDEN);
        validateActive(findTodo);

        todoRepository.delete(findTodo);
    }

    private void validateTodoOwner(Member findMember, Todo findTodo, ErrorCode errorCode) {
        if (!findTodo.isOwnTodo(findMember)) {
            throw new RoleException(errorCode);
        }
    }

    private void validateTagOwner(Member findMember, Tag findTag, ErrorCode errorCode) {
        if (!findTag.isOwnTag(findMember)) {
            throw new RoleException(errorCode);
        }
    }


    private void validateActive(Todo findTodo) {
        if (!findTodo.getActive()) {
            throw new RoleException(ErrorCode.NOT_ACTIVE);
        }
    }
}
