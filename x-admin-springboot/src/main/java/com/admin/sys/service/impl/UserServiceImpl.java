package com.admin.sys.service.impl;

import com.admin.common.utils.JwtUtil;
import com.admin.common.vo.Result;
import com.admin.sys.entity.Menu;
import com.admin.sys.entity.User;
import com.admin.sys.entity.UserRole;
import com.admin.sys.mapper.UserMapper;
import com.admin.sys.mapper.UserRoleMapper;
import com.admin.sys.service.IMenuService;
import com.admin.sys.service.IUserService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sunqing
 * @since 2023-03-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;
    // @Autowired
    // private RedisTemplate redisTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private IMenuService menuService;

    @Override
    public Map<String, Object> login(User user) {
        // 根据用户名和密码查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User loginUser = userMapper.selectOne(queryWrapper);
        // 结果不为空，则生成token，并将用户信息存入redis
        if (loginUser != null && passwordEncoder.matches(user.getPassword(), loginUser.getPassword())) {
            // 暂时用UUID，终极方案为jwt
            // String key = "user:" + UUID.randomUUID();
            // 存入redis
            loginUser.setPassword(null);

            // redisTemplate.opsForValue().set(key, loginUser, 30, TimeUnit.MINUTES);

            // JWT 不需要存在Redis中
            String token = jwtUtil.createToken(loginUser);
            // 返回数据
            HashMap<String, Object> data = new HashMap<>();
            // data.put("token", key);
            data.put("token", token);
            return data;

        }
        return null;
    }
    /*@Override
    public Map<String, Object> login(User user) {
        // 根据用户名和密码查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        queryWrapper.eq(User::getPassword, user.getPassword());
        User loginUser = userMapper.selectOne(queryWrapper);
        // 结果不为空，则生成token，并将用户信息存入redis
        if (loginUser != null) {
            // 暂时用UUID，终极方案为jwt
            String key = "user:" + UUID.randomUUID();
            // 存入redis
            loginUser.setPassword(null);

            redisTemplate.opsForValue().set(key, loginUser, 30, TimeUnit.MINUTES);

            // 返回数据
            HashMap<String, Object> data = new HashMap<>();
            data.put("token", key);
            return data;

        }
        return null;
    }*/

    @Override
    public Map<String, Object> getUserInfo(String token) {
        // 从redis获取用户信息
        // Object obj = redisTemplate.opsForValue().get(token);
        //  从JWT中获取信息
        User loginUser = null;
        try {
            loginUser = jwtUtil.parseToken(token, User.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // if (obj != null) {
        if (loginUser != null) {
            // User user = JSON.parseObject(JSON.toJSONString(obj), User.class);
            Map<String, Object> data = new HashMap<>();
            // data.put("name", user.getUsername());
            // data.put("avatar", user.getAvatar());
            data.put("name", loginUser.getUsername());
            data.put("avatar", loginUser.getAvatar());

            // 数据量很大的话，不使用关联查询，效率较差
            // 角色
            // List<String> roleList = userMapper.getRoleNameByUserId(user.getId());
            // 这里是第一季的内容，仅用于获取用户的角色名称
            List<String> roleList = userMapper.getRoleNameByUserId(loginUser.getId());
            data.put("roles", roleList);

            List<Menu> menuList = menuService.getMenuListByUserId(loginUser.getId());
            data.put("menuList", menuList);
            return data;
        }

        return null;
    }

    @Override
    public void logout(String token) {
        // redisTemplate.delete(token);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void addUser(User user) {
        userMapper.insert(user);
        if (user.getRoleIdList() != null) {
            for (Integer roleId : user.getRoleIdList()) {
                userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
            }
        }
    }

    @Override
    public User getUserById(Integer id) {
        User user = userMapper.selectById(id);

        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        List<UserRole> userRoles = userRoleMapper.selectList(wrapper);
        List<Integer> roldIdList = userRoles.stream().map(userRole -> {
            return userRole.getRoleId();
        }).collect(Collectors.toList());
        user.setRoleIdList(roldIdList);
        return user;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateUser(User user) {
        userMapper.updateById(user);
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, user.getId());
        userRoleMapper.delete(wrapper);
        if (user.getRoleIdList() != null) {
            for (Integer roleId : user.getRoleIdList()) {
                userRoleMapper.insert(new UserRole(null, user.getId(), roleId));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteUserById(Integer id) {
        userMapper.deleteById(id);
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, id);
        userRoleMapper.delete(wrapper);
    }


}
