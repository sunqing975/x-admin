package com.admin.sys.service;

import com.admin.sys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sunqing
 * @since 2023-03-16
 */
public interface IUserService extends IService<User> {

    Map<String, Object> login(User user);

    Map<String, Object>  getUserInfo(String token);

    void logout(String token);

    void addUser(User user);

    User getUserById(Integer id);

    void updateUser(User user);

    void deleteUserById(Integer id);
}
