import request from "@/utils/request";

export function login(data) {
  return request({
    url: "/sys/user/login",
    method: "post",
    data,
  });
}

export function getInfo(token) {
  return request({
    url: "/sys/user/info",
    method: "get",
    params: { token },
  });
}

export function logout() {
  return request({
    url: "/sys/user/logout",
    method: "post",
  });
}
