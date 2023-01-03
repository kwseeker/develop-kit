# Reactor3

官方文档：[Reactor 3 Reference Guide](https://projectreactor.io/docs/core/release/reference/)

中文翻译：[Reactor3 中文文档](https://www.cnblogs.com/crazymakercircle/p/14292098.html#autoid-h2-1-0-0)



## 简介

Reactor 是一个用于JVM的完全非阻塞的响应式编程框架，具备高效的需求管理（即对 “背压（backpressure）”的控制）能力。它与 Java 8 函数式 API 直接集成，比如 `CompletableFuture`， `Stream`， 以及 `Duration`。它提供了异步序列 API `Flux`（用于[N]个元素）和 `Mono`（用于 [0|1]个元素），并完全遵循和实现了“响应式扩展规范”（Reactive Extensions Specification）。

**核心接口**：

+ Mono: 异步0-1元素序列 Future<Optional<?>>

  比如一个HTTP请求产生一个响应，应该用`Mono<HttpResponse> `而不是`Flux<HttpResponse>`。

+ Flux：异步0-N元素序列 Future<Collection<?>>

**编程方式**:

+ 接口编程
+ 函数式编程

**Reactor3如何解决下面问题**：

+ 阻塞编程
  + 无法并行计算
  + 资源低效使用
+ 异步编程
  + Callback：就是观察者模式，回调地狱
  + Future：无法对任务编排（即任务间没有关联，后面出现了 CompletableFuture 优化了这个问题）、不确定何时结束需要同步等待获取结果



## Reactor工作流程

### 核心调用





## 参考

+ [响应式编程](https://blog.csdn.net/qian_348840260/category_10546462.html)