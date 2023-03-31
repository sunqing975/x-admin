import request from "@/utils/request";

// 单个写法
// export function login(data) {
//   return request({
//     url: "/sys/user/login",
//     method: "post",
//     post方式是转为JSON后进行传输
//     data,
//   });
// }

// 整合写法
export default {
  getUserByCondition(searchModel) {
    return request({
      url: "/sys/user/getUserByCondition",
      method: "get",
      // get需要拼接字段
      params: {
        pageNo: searchModel.pageNo,
        pageSize: searchModel.pageSize,
        username: searchModel.username,
        phone: searchModel.phone,
      },
    });
  },
  addUser(user) {
    return request({
      url: "/sys/user/addUser",
      method: "post",
      data: user,
    });
  },
  updateUser(user) {
    return request({
      url: "/sys/user/updateUser",
      method: "put",
      data: user,
    });
  },
  saveUser(user) {
    if (user.id == null && user.id == undefined) {
      return this.addUser(user);
    }
    return this.updateUser(user);
  },
  getUserById(id) {
    return request({
      // url: "/sys/user/getUserById/"+id,
      url: `/sys/user/getUserById/${id}`,
      method: "get",
    });
  },
  deleteUserById(id) {
    return request({
      // url: "/sys/user/getUserById/"+id,
      url: `/sys/user/deleteUserById/${id}`,
      method: "delete",
    });
  },
};
