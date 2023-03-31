package com.admin;

import com.admin.sys.entity.Role;
import com.admin.sys.entity.User;
import com.admin.sys.mapper.RoleMapper;
import com.admin.sys.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class XAdminSpringbootApplicationTests {

    @Resource
    UserMapper userMapper;

    @Resource
    RoleMapper roleMapper;

    @Test
    void testMapper() {
        // List<User> users = userMapper.selectList(null);
        // users.forEach(System.out::println);
        List<Role> roles = roleMapper.selectList(null);
        roles.forEach(System.out::println);
    }

}
