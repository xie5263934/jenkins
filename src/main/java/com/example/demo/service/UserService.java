package com.example.demo.service;

import com.example.demo.entity.UserInfo;

public interface UserService {
    UserInfo findByUserName(String userName);
}
