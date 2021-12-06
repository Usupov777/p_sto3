package com.javamentor.qa.platform.dao.impl.model;

import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class UserDaoImpl extends ReadWriteDaoImpl<User,Long> implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User getUserByEmail(String email){
        User userOut = null;
        Query query =  entityManager.createQuery("select u from User u where u.email=:email");
        query.setParameter("email", email);
        Optional<User> userOptional = (Optional<User>) query.getSingleResult();
        if(userOptional.isPresent()){
            userOut = userOptional.get();
        }
        return userOut;
    }

}
