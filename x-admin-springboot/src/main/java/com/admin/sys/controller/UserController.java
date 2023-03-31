package com.admin.sys.controller;

import com.admin.common.vo.Result;
import com.admin.sys.entity.User;
import com.admin.sys.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author sunqing
 * @since 2023-03-16
 */
@RestController
@RequestMapping("/sys/user")
@Api(tags = {"用户相关接口列表"})
// @CrossOrigin 跨域处理，但是注解方式需要在每一个控制器都加上
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/getAllUser")
    public Result<List<User>> getAllUser() {
        List<User> list = userService.list();
        return Result.success("查询成功", list);
    }

    @ApiOperation("登录接口")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        Map<String, Object> data = userService.login(user);
        if (data != null) {
            return Result.success(data);
        }
        return Result.fail(20002, "用户名或密码错误");
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(@RequestParam("token") String token) {
        Map<String, Object> data = userService.getUserInfo(token);
        if (data != null) {
            return Result.success(data);
        }
        return Result.fail(20002, "用户名登录信息无效");
    }

    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token) {
        userService.logout(token);
        return Result.success();
    }

    @GetMapping("/getUserByCondition")
    public Result<Map<String, Object>> getUserByCondition(@RequestParam(value = "username", required = false) String username,
                                                          @RequestParam(value = "phone", required = false) String phone,
                                                          @RequestParam(value = "pageNo", defaultValue = "1") Long pageNo,
                                                          @RequestParam(value = "pageSize", defaultValue = "10") Long pageSize) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(username), User::getUsername, username);
        wrapper.eq(StringUtils.hasLength(phone), User::getPhone, phone);

        Page<User> page = new Page<>(pageNo, pageSize);

        userService.page(page, wrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());
        return Result.success(data);
    }

    @PostMapping("/addUser")
    public Result<?> addUser(@RequestBody User user) {
        // MD5 加盐，每次加密都不一样
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);
        return Result.success("新增成功");
    }

    @PutMapping("/updateUser")
    public Result<?> updateUser(@RequestBody User user) {
        user.setPassword(null);
        userService.updateUser(user);
        return Result.success("修改成功");
    }

    @GetMapping("/getUserById/{id}")
    public Result<User> getUserById(@PathVariable("id") Integer id) {
        User user = userService.getUserById(id);
        user.setPassword(null);
        return Result.success(user);
    }

    @DeleteMapping("/deleteUserById/{id}")
    public Result<?> deleteUserById(@PathVariable("id") Integer id) {
        userService.deleteUserById(id);
        return Result.success("删除成功");
    }
}
