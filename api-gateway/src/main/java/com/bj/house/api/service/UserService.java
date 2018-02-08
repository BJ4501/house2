package com.bj.house.api.service;

import com.bj.house.api.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by BJ on 2018/1/26.
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public String getUsername(Long id){
        return userDao.getUsername(id);
    }


}
