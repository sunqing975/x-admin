import request from "@/utils/request";

// 整合写法
export default {
  getRoleByCondition(searchModel) {
    return request({
      url: "/sys/role/getRoleByCondition",
      method: "get",
      // get需要拼接字段
      params: {
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize,
        roleName: searchModel.roleName,
      },
    });
  },
  addRole(role) {
    return request({
      url: "/sys/role/addRole",
      method: "post",
      data: role,
    });
  },
  updateRole(role) {
    return request({
      url: "/sys/role/updateRole",
      method: "put",
      data: role,
    });
  },
  saveRole(role) {
    if (role.roleId == null && role.roleId == undefined) {
      return this.addRole(role);
    }
    return this.updateRole(role);
  },
  getRoleById(id) {
    return request({
      url: `/sys/role/getRoleById/${id}`,
      method: "get",
    });
  },
  deleteRoleById(id) {
    return request({
      url: `/sys/role/deleteRoleById/${id}`,
      method: "delete",
    });
  },
  getAllRole() {
    return request({
      url: "/sys/role/getAllRole",
      method: "get",
    });
  },
};
