package com.cyberaakash.redissessionspringmysql.repository.impl;

import com.cyberaakash.redissessionspringmysql.model.User;
import com.cyberaakash.redissessionspringmysql.repository.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String KEY = "USER";

    @Override
    public boolean saveUser(User user) {
        try {
            redisTemplate.opsForHash().put(KEY, user.getId().toString(), user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> fetchAllUser() {
        List<User> users;
        users = redisTemplate.opsForHash().values(KEY);
        return  users;
    }

    @Override
    public User fetchUserById(Long id) {
        log.info("User ID: {}", id);
//        Object res = redisTemplate.opsForHash().get(KEY,id.toString());
        User user = (User) redisTemplate.opsForHash().get(KEY, id.toString());
        if (user != null) {
            log.info("User: {}", user);
        } else {
            log.info("User with ID {} not found in Redis.", id);
        }
        return user;
    }

    @Override
    public boolean deleteUser(Long id) {
        try {
            redisTemplate.opsForHash().delete(KEY,id.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUser(Long id, User user) {
        try {
            redisTemplate.opsForHash().put(KEY, id, user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
