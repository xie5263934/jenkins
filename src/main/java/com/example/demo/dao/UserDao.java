package com.example.demo.dao;

import com.example.demo.entity.UserInfo;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<UserInfo, Integer> {
    /**
     * g根据名字查询用户信息
     *
     * @param userName
     * @return
     */
    UserInfo getByUserName(String userName);
}
