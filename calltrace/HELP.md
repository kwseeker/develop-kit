## 帮助说明

#### Spring Boot 引入 Logback

关于日志配置文件的引入: Spring Boot 会默认解析 logback-spring.xml , logback-spring.groovy , logback.xml or logback.groovy 等配置文件；

Spring 官方推荐使用 logback-spring.xml 作为配置文件，可以在此配置文件中添加 Spring 对日志优化的高级功能。
而如果使用 logback.xml 作为配置文件则无法使用高级配置功能。

#### 测试结果

使用UUID作为每个请求ID

```
2019-10-26 17:49:07 INFO  [7135f243feaa4ef0ab22ac5a9878d574] [http-nio-8080-exec-1]t.k.l.s.c.MDCTestController - MDCTestController- mdcTest() called
2019-10-26 17:49:07 INFO  [7135f243feaa4ef0ab22ac5a9878d574] [http-nio-8080-exec-1]t.k.l.s.c.MDCTestController - MDCTestController- mdcTest() called
2019-10-26 17:49:07 INFO  [7135f243feaa4ef0ab22ac5a9878d574] [http-nio-8080-exec-1]t.k.l.s.s.MDCTestService - MDCTestService- sayHello() called
2019-10-26 17:49:07 INFO  [7135f243feaa4ef0ab22ac5a9878d574] [http-nio-8080-exec-1]t.k.l.s.s.MDCTestService - MDCTestService- sayHello() called
```