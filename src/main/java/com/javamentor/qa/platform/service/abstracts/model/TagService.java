package com.javamentor.qa.platform.service.abstracts.model;

import com.javamentor.qa.platform.models.entity.question.Tag;

import java.util.List;

public interface TagService extends ReadWriteService<Tag, Long> {
    List<Tag> getListTagsByListOfTagName(List<String> listTagName);
    boolean checkedAndIgnoredContainTag(Long tagId);
}
