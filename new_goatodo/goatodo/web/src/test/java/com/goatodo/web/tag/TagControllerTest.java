package com.goatodo.web.tag;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goatodo.application.tag.TagService;
import com.goatodo.application.tag.response.TagResponse;
import com.goatodo.common.error.ErrorCode;
import com.goatodo.web.tag.request.TagCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TagService tagService;

    @DisplayName("유저는 태그를 생성 할 수 있다.")
    @Test
    void createTag() throws Exception {
        // given
        Long userId = 1L;
        TagCreateRequest request = TagCreateRequest.builder()
                .userId(userId)
                .name("태그")
                .build();

        TagResponse response = new TagResponse(1L, 1L, "태그");
        BDDMockito.given(tagService.createTag(userId, request.toService()))
                .willReturn(response);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/api/v1/tags/1"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.tagId").value(response.tagId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId").value(response.userId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(response.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.serverDateTime").exists());
    }

    @DisplayName("유저가 태그를 생성 할 때 유저의 아이디는 필수값이다.")
    @Test
    void createTagWithEmptyUserId() throws Exception {
        // given
        TagCreateRequest request = TagCreateRequest.builder()
                .name("태그")
                .build();

        ErrorCode errorCode = ErrorCode.INVALID_ARGUMENT;

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(errorCode.getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorCode.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("userId"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("userId는 null 일 수 없습니다."));
    }

    @DisplayName("유저가 태그를 생성 할 때 태그 내용은 공백 일 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void createTagWithBlankName(String name) throws Exception {
        // given
        TagCreateRequest request = TagCreateRequest.builder()
                .userId(1L)
                .name(name)
                .build();

        ErrorCode errorCode = ErrorCode.INVALID_ARGUMENT;

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(errorCode.getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorCode.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("name은 공백이거나 null 일 수 없습니다."));
    }

    @DisplayName("유저가 태그를 생성 할 때 태그 내용은 필수값이다.")
    @Test
    void createTagWithBlankName() throws Exception {
        // given
        TagCreateRequest request = TagCreateRequest.builder()
                .userId(1L)
                .build();

        ErrorCode errorCode = ErrorCode.INVALID_ARGUMENT;

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(errorCode.getCode()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(errorCode.getStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(errorCode.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].field").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("name은 공백이거나 null 일 수 없습니다."));
    }
}