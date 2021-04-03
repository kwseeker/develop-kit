# HTTP客户端

常见客户端框架：

+ **apache commons-httpclient  & httpcomponents:httpclient**

  commons-httpclient 是 httpcomponents:httpclient 的前身，但是现在已经不维护了，apache 现在维护的是 httpcomponents:httpclient。

  httpcomponents:httpclient 功能丰富，但不支持HTTP/2。

  [HttpClient4.5](http://hc.apache.org/httpcomponents-client-4.5.x/index.html)

  > 官网有这么一段话：
  >
  > The Commons HttpClient project used to be a part of Commons, but is now part of [Apache HttpComponents](http://hc.apache.org/) - see [Jakarta Commons HttpClient](http://hc.apache.org/httpclient-3.x/index.html).

+ **okhttp**

  OkHttp 接口设计友好，支持 HTTP/2。

+ **HttpURLConnection & JDK9 HttpClient**

  JDK自带的HTTP客户端，但是使用麻烦。无法支持 HTTP/2，缺乏连接池管理、域名控制等特性。

  为了支持 HTTP/2, 以及优化使用，自JDK9 引入了一个 high level、支持 HTTP/2 的 [HttpClient](https://docs.oracle.com/javase/9/docs/api/jdk/incubator/http/HttpClient.html)。

+ **webclient**

  响应式非阻塞的客户端。非常适合流式的传输方案，并且依赖于较低级别的HTTP客户端库来执行请求，是可插拔的。





## 附录

+ **HTTP1.0 & HTTP1.1 & HTTP2.0 区别**	

  HTTP1.0最早在网页中使用是在1996年，那个时候只是使用一些较为简单的网页上和网络请求。

  HTTP1.1则在1999年才开始广泛应用于现在的各大浏览器网络请求中，同时HTTP1.1也是当前使用最为广泛的HTTP协议。

  > HTTP1.1 相对HTTP1.0的优化
  >
  > + 缓存处理
  > + 带宽优化及网络连接的使用
  > + 错误通知的管理
  > + Host头处理
  > + 长连接
  > + ......

  HTTP 2.0 的出现，相比于 HTTP 1.x ，大幅度的提升了 web 性能。

  > HTTP2.0 相对HTTP1.x的优化
  >
  > + 多路复用（通过二进制分帧的方式让一个连接可以同时传输多条请求，而HTTP1.x只能一个请求一个请求地处理）
  > + 首部压缩（不仅仅是消息主体，状态行、请求 / 响应头部、消息主体都会压缩）
  > + 支持服务器推送（一个页面需要很多附加资源, 获取全部资源分为很多请求，但是服务器一般都知道页面都需要哪些资源，可以在它响应浏览器第一个请求的时候，就可以开始推送这些资源。）
  > + ......

+ **参考资料**

  + 协议标准说明书（RFC规范、W3组织）
    + [Hypertext Transfer Protocol Version 2 (HTTP/2)](https://www.rfc-editor.org/info/rfc7540) (rfc-edtor.org)
    + [Hypertext Transfer Protocol -- HTTP/1.1](https://www.rfc-editor.org/info/rfc2616) (rfc-edtor.org)
    + [HTTP - Hypertext Transfer Protocol](https://www.w3.org/Protocols/) (w3.org)
    + [RFC 7230](http://tools.ietf.org/html/rfc7230) - Hypertext Transfer Protocol (HTTP/1.1): Message Syntax and Routing
    + [RFC 7231](http://tools.ietf.org/html/rfc7231) - Hypertext Transfer Protocol (HTTP/1.1): Semantics and Content
    + [RFC 7235](http://tools.ietf.org/html/rfc7235) - Hypertext Transfer Protocol (HTTP/1.1): Authentication
    + [RFC 1945](http://tools.ietf.org/html/rfc1945) - Hypertext Transfer Protocol – HTTP/1.0
    + [RFC 2817](http://tools.ietf.org/html/rfc2817) - Upgrading to TLS Within HTTP/1.1
    + [RFC 2818](http://tools.ietf.org/html/rfc2818) - HTTP Over TLS
    + [RFC 6265](http://tools.ietf.org/html/rfc6265) - HTTP State Management Mechanism (Cookies)
    + [RFC 2396](http://tools.ietf.org/html/rfc2396) - Uniform Resource Identifiers (URI): Generic Syntax

  + [HTTP/2 简介](https://developers.google.com/web/fundamentals/performance/http2)

  + 《高性能浏览器网络》

