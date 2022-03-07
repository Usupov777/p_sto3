package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.TagDao;
import com.javamentor.qa.platform.models.entity.question.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TagDaoImpl extends ReadWriteDaoImpl<Tag, Long> implements TagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> getListTagsByListOfTagName(List<String> listTagName) {
        String hql = "select tag from Tag tag where tag.name in (:listTagName)";
        Query query = entityManager.createQuery(hql).setParameter("listTagName", listTagName);
        return query.getResultList();
    }

    @Override
    public boolean checkedAndIgnoredContainTag(Long tagId) {
        String hql = "SELECT count(it) FROM IgnoredTag it JOIN TrackedTag tt ON it.user.id=tt.user.id WHERE " +
                "it.ignoredTag.id =:tagId AND tt.trackedTag.id=:tagId";
        return entityManager.createQuery(hql).setParameter("tagId", tagId).getFirstResult()==0;
    }
}