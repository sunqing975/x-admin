前后端连接

1. 修改./src/api/下的路径，对应后端的 url
2. 修改根目录下的.env.development
   VUE_APP_BASE_API = 'http://localhost:9999'
3. 注释 mock 服务，修改根目录下的 vue.config.js
   // before: require("./mock/mock-server.js"),
4. 解决跨域问题，
   4.1 cors
   一种是 cors 全称为 Cross Origin Resource Sharing（跨域资源共享）。这种方案对于前端来说没有什么工作量，和正常发送请求写法上没有任何区别，工作量基本都在后端这里。每一次请求，浏览器必须先以 OPTIONS 请求方式发送一个预请求（也不是所有请求都会发送 options），通过预检请求从而获知服务器端对跨源请求支持的 HTTP 方法。在确认服务器允许该跨源请求的情况下，再以实际的 HTTP 请求方法发送那个真正的请求。推荐的原因是：只要第一次配好了，之后不管有多少接口和项目复用就可以了，一劳永逸的解决了跨域问题，而且不管是开发环境还是正式环境都能方便的使用。

   @CrossOrigin 跨域处理，但是注解方式需要在每一个控制器都加上

   定义配置类，全部拦截

   4.2 proxy 与 nginx
   在 dev 开发模式下可以下使用 webpack 的 proxy 使用也是很方便。但这种方法在生产环境是不能使用的。在生产环境中需要使用 nginx 进行反向代理。不管是 proxy 和 nginx 的原理都是一样的，通过搭建一个中转服务器来转发请求规避跨域的问题。

5. 使用 ElementUI 进行页面布局
   注意事项：
   使用分页功能时，默认是英文显示，为了修改中文模式，需要修改 main.js
   // import locale from 'element-ui/lib/locale/lang/en' // lang i18n
   import locale from "element-ui/lib/locale/lang/zh-CN"; // lang i18n
