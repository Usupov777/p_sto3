package com.javamentor.qa.platform.service.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.IgnoredTagDao;
import com.javamentor.qa.platform.models.entity.question.IgnoredTag;
import com.javamentor.qa.platform.service.abstracts.model.IgnoredTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IgnoredTagServiceImpl extends ReadWriteServiceImpl<IgnoredTag, Long> implements IgnoredTagService {
    private final IgnoredTagDao ignoredTagDao;

    @Autowired
    public IgnoredTagServiceImpl(IgnoredTagDao ignoredTagDao) {
        super(ignoredTagDao);
        this.ignoredTagDao = ignoredTagDao;
    }

    @Override
    public Boolean getTagIfNotExist(Long tagId, Long userId) {
        return ignoredTagDao.getTagIfNotExist(tagId, userId);
    }

    @Override
    public IgnoredTag getIgnoredTagByTagIdAndUserId(Long tagId, Long userId) {
        return ignoredTagDao.getIgnoredTagByTagIdAndUserId(tagId, userId);
    }
}
