package com.javamentor.qa.platform.service.impl.dto;

import com.javamentor.qa.platform.dao.abstracts.dto.CommentDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.QuestionDtoDao;
import com.javamentor.qa.platform.dao.abstracts.dto.TagDtoDao;
import com.javamentor.qa.platform.dao.abstracts.model.BookMarksDao;
import com.javamentor.qa.platform.models.dto.PageDto;
import com.javamentor.qa.platform.models.dto.QuestionDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.dto.TagDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.QuestionDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Ali Veliev 10.12.2021
 */

@Service
public class QuestionDtoServiceImpl extends PageDtoServiceImpl<QuestionViewDto> implements QuestionDtoService {

    private final QuestionDtoDao questionDtoDao;
    private final CommentDtoDao commentDtoDao;
    private final TagDtoDao tagDtoDao;
    private final BookMarksDao bookMarksDao;

    @Autowired
    public QuestionDtoServiceImpl(QuestionDtoDao questionDtoDao, CommentDtoDao commentDtoDao, TagDtoDao tagDtoDao, BookMarksDao bookMarksDao) {
        this.questionDtoDao = questionDtoDao;
        this.commentDtoDao = commentDtoDao;
        this.tagDtoDao = tagDtoDao;
        this.bookMarksDao = bookMarksDao;
    }

    @Override
    public Optional<QuestionDto> getQuestionById(Long id) {
        Optional<QuestionDto> questionDto = questionDtoDao.getQuestionById(id);
        questionDto.ifPresent(dto -> dto.setListCommentDto(commentDtoDao.getCommentDtosByQuestionId(id)));
        questionDto.ifPresent(dto -> dto.setListTagDto(tagDtoDao.getTagsByQuestionId(id)));
        questionDto.ifPresent(dto -> dto.setIsUserBookmark(bookMarksDao.isQuestionAlreadyExistOnUserBookmarks(
                ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId(), id)));
        return questionDto;
    }

    @Override
    public PageDto<QuestionViewDto> getPageQuestionsWithTags(String pageDtoDaoName, Map<String, Object> params) {

        PageDto<QuestionViewDto> pageDto = super.getPageDto(pageDtoDaoName, params);
        List ids = pageDto.getItems().stream().map(
                questionViewDto -> questionViewDto.getId()).collect(Collectors.toList());
        Map<Long, List<TagDto>> tags = tagDtoDao.getTagsByQuestionIds(ids);
        for (QuestionViewDto q : pageDto.getItems()) {
            q.setListTagDto(tags.get(q.getId()));
        }
        return pageDto;
    }
}
