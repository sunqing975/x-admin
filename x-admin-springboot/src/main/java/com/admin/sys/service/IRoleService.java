package com.admin.sys.service;

import com.admin.sys.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sunqing
 * @since 2023-03-16
 */
public interface IRoleService extends IService<Role> {

    void addRole(Role role);

    Role getRoleById(Integer roleId);

    void updateRole(Role role);

    void deleteRoleById(Integer roleId);
}
