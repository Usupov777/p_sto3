package com.javamentor.qa.platform.dao.abstracts.dto;

import com.javamentor.qa.platform.models.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentDtoDao {
    List<CommentDto> getCommentDtosByQuestionId(Long id);
    Optional<CommentDto> getCommentDtoByCommentId(Long id);
}
