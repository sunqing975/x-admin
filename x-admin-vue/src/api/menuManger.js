import request from "@/utils/request";

// 整合写法
export default {
  getAllMenu() {
    return request({
      url: "/sys/menu/getAllMenu",
      method: "get",
    });
  },
};
