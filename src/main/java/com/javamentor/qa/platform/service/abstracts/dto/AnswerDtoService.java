package com.javamentor.qa.platform.service.abstracts.dto;



import com.javamentor.qa.platform.models.dto.AnswerDto;

import java.util.List;

public interface AnswerDtoService {
    public List<AnswerDto> getAllAnswersByQuestionId(Long id);
}
