package com.admin.sys.controller;

import com.admin.common.vo.Result;
import com.admin.sys.entity.Role;
import com.admin.sys.entity.User;
import com.admin.sys.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

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
@Api(tags = {"角色相关接口列表"})
@RestController
@RequestMapping("/sys/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @GetMapping("/getAllRole")
    public Result<List<Role>> getAllRole() {
        List<Role> list = roleService.list();
        return Result.success("查询成功", list);
    }


    @GetMapping("/getRoleByCondition")
    public Result<Map<String, Object>> getRoleByCondition(@RequestParam(value = "roleName", required = false) String roleName,
                                                          @RequestParam(value = "pageNo", defaultValue = "1") Long pageNo,
                                                          @RequestParam(value = "pageSize", defaultValue = "10") Long pageSize) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(roleName), Role::getRoleName, roleName);

        Page<Role> page = new Page<>(pageNo, pageSize);

        roleService.page(page, wrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());
        return Result.success(data);
    }

    @PostMapping("/addRole")
    public Result<?> addRole(@RequestBody Role role) {
        roleService.addRole(role);
        return Result.success("新增成功");
    }

    @PutMapping("/updateRole")
    public Result<?> updateRole(@RequestBody Role role) {
        roleService.updateRole(role);
        return Result.success("修改成功");
    }

    @GetMapping("/getRoleById/{roleId}")
    public Result<Role> getRoleById(@PathVariable("roleId") Integer roleId) {
        Role role = roleService.getRoleById(roleId);
        return Result.success(role);
    }

    @DeleteMapping("/deleteRoleById/{roleId}")
    public Result<?> deleteRoleById(@PathVariable("roleId") Integer roleId) {
        roleService.deleteRoleById(roleId);
        return Result.success("删除成功");
    }
}
