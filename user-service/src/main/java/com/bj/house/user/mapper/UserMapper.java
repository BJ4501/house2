package com.bj.house.user.mapper;

import com.bj.house.user.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by BJ on 2018/2/9.
 */
@Mapper
public interface UserMapper {

    User selectById(Long id);

    //用户列表
    List<User> select(User user);

    //更新
    int update(User user);

    //插入
    int insert(User account);

    //删除
    int delete(String email);

}
