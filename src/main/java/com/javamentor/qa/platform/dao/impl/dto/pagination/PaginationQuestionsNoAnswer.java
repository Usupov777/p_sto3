package com.javamentor.qa.platform.dao.impl.dto.pagination;

import com.javamentor.qa.platform.dao.abstracts.dto.PageDtoDao;
import com.javamentor.qa.platform.models.dto.QuestionViewDto;
import com.javamentor.qa.platform.models.dto.QuestionViewDtoResultTransformer;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Repository
public class PaginationQuestionsNoAnswer implements PageDtoDao<QuestionViewDto> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<QuestionViewDto> getItems(Map<String, Object> params) {
        int page = (int) params.get("currentPageNumber");
        int itemsOnPage = (int) params.get("itemsOnPage");

        return em.createNativeQuery(
                        "SELECT " +
                                "DISTINCT q.id AS q_id, " +
                                "q.title, " +
                                "q.description, " +
                                "q.last_redaction_date, " +
                                "q.persist_date, " +
                                "u.id , " +
                                "u.full_name, " +
                                "u.image_link, " +

                                "(SELECT coalesce(sum(r.count),0) FROM reputation r " +
                                "   WHERE r.author_id = u.id) AS reputation, " +

                                "(SELECT coalesce(count(up.vote), 0) FROM votes_on_questions up " +
                                "   WHERE up.vote = 'UP_VOTE' AND up.question_id = q.id) " +
                                "- " +
                                "(SELECT coalesce(count(down.vote), 0) FROM votes_on_questions down " +
                                "   WHERE down.vote = 'DOWN_VOTE' AND down.question_id = q.id) AS votes, " +

                                "(SELECT coalesce(count(a.id),0) FROM answer a " +
                                "   WHERE a.question_id = q.id) AS answers, " +

                                "(SELECT coalesce(count(qv.id), 0) FROM question_viewed qv " +
                                "   WHERE qv.question_id = q.id) AS views, " +

                                "(SELECT CASE WHEN b.question_id = q.id AND b.user_id = :userId " +
                                "   THEN 1 ELSE 0 END AS is_user_bookmark FROM bookmarks b LIMIT 1 OFFSET q.id-1) " +

                                "FROM question q " +
                                "JOIN user_entity u ON u.id = q.user_id " +
                                "JOIN question_has_tag qht ON q.id = qht.question_id " +
                                "WHERE q.id NOT IN(SELECT a.question_id FROM answer a WHERE a.question_id = q.id) " +
                                "AND CASE " +
                                "   WHEN -1 IN :ignoredTag AND -1 IN :trackedTag THEN TRUE " +
                                "   WHEN -1 IN :ignoredTag THEN qht.tag_id IN :trackedTag " +
                                "   WHEN -1 IN :trackedTag THEN q.id NOT IN " +
                                "   (" +
                                "       SELECT q_ign.id FROM question q_ign " +
                                "       JOIN question_has_tag q_ign_tag ON q_ign.id = q_ign_tag.question_id " +
                                "       WHERE q_ign_tag.tag_id IN :ignoredTag" +
                                "   ) " +
                                "   ELSE qht.tag_id IN :trackedTag AND q.id NOT IN " +
                                "   (" +
                                "       SELECT q_ign.id FROM question q_ign " +
                                "       JOIN question_has_tag q_ign_tag ON q_ign.id = q_ign_tag.question_id " +
                                "       WHERE q_ign_tag.tag_id IN :ignoredTag" +
                                "   ) " +
                                "   END " +
                                "ORDER BY q.id")
                .setParameter("ignoredTag", params.get("ignoredTag"))
                .setParameter("trackedTag", params.get("trackedTag"))
                .setParameter("userId", ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId())
                .setFirstResult((page - 1) * itemsOnPage)
                .setMaxResults(itemsOnPage)
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(new QuestionViewDtoResultTransformer()).getResultList();
    }

    @Override
    public int getTotalResultCount(Map<String, Object> params) {


        return ((BigInteger) em.createNativeQuery(
                        "SELECT " +
                                "COUNT(DISTINCT q.id) FROM question q JOIN question_has_tag qht ON q.id = qht.question_id " +
                                "WHERE q.id NOT IN(SELECT a.question_id FROM answer a WHERE a.question_id = q.id) " +
                                "AND CASE " +
                                "   WHEN -1 IN :ignoredTag AND -1 IN :trackedTag THEN TRUE " +
                                "   WHEN -1 IN :ignoredTag THEN qht.tag_id IN :trackedTag " +
                                "   WHEN -1 IN :trackedTag THEN q.id NOT IN " +
                                "   (" +
                                "       SELECT q_ign.id FROM question q_ign " +
                                "       JOIN question_has_tag q_ign_tag ON q_ign.id = q_ign_tag.question_id " +
                                "       WHERE q_ign_tag.tag_id IN :ignoredTag" +
                                "   ) " +
                                "   ELSE qht.tag_id IN :trackedTag AND q.id NOT IN " +
                                "   (" +
                                "       SELECT q_ign.id FROM question q_ign " +
                                "       JOIN question_has_tag q_ign_tag ON q_ign.id = q_ign_tag.question_id " +
                                "       WHERE q_ign_tag.tag_id IN :ignoredTag" +
                                "   ) " +
                                "END ")
                .setParameter("ignoredTag", params.get("ignoredTag"))
                .setParameter("trackedTag", params.get("trackedTag"))
                .getSingleResult()).intValue();
    }
}
