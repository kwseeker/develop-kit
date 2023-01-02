# 响应式编程-[Reactive Streams规范](https://github.com/reactive-streams/reactive-streams-jvm)

`Reactive Streams`编程是一种**异步编程的示范**，这种示范与**数据流式处理以及变换传播**相关联，同时也经常被面向对象语言表示，作为一种观察者模式的拓展。

随着时间的推移，通过`Reactive Streams`工作出现了Java的标准化（Reactive Stream API），这一规范定义了`JVM`上的反应库的一组接口和交互规则。它的接口已经在父类Flow下集成到Java 9中。

`Reactive Streams`  作为一种观察者模式的拓展。在Reactive 实现上可以看到类似观察者模式、责任链模式、迭代器模式、发布订阅模式的影子。

现在有三种常见实现：`Reactor`、 `Rx`、`Java9 Flow`。

**Reactive Stream 解决了什么问题？**

<u>Reactive Stream 之前异步编程的局限性</u>：

+ 阻塞编程
  + 无法并行计算
  + 资源低效使用
+ 异步编程
  + Callback：回调地狱（导致代码难以理解和维护）
  + Future：无法对任务编排（即任务间没有关联，后面出现了 CompletableFuture 优化了这个问题，不过对于多个处理的组合仍不够好用）、不确定何时结束需要同步等待获取结果
+ 不支持函数式编程

<u>Reactive Stream 出现后带来的改变</u>：

提升系统能力的两种方式：提高资源利用率（异步非阻塞），并行化使用更多资源。

革新了异步非阻塞两种编程模型(`CallBacks`、`Futures`)的问题。

支持函数式编程。

比如任务关联问题，通过角色间的信号/事件onXxx等方法建立了关联。

**四种角色**

+ 发布者（Publisher）
+ 订阅者（Subscriber）
+ 订阅控制器（Subscription）
+ 发布/订阅者（Processor）：既可以发布事件也可以订阅事件

**对比迭代器模式**

+ 数据方向

  Reactive Stream : 推模式

  Iterator: 拉模式

+ 编程模式：

  Reactive Stream : 发布-订阅模式，用户代码只需要定义收到事件后需要做什么，有几步，这几步的顺序不需要关心。

  Iterator: 命令式模式（Imperative）, 即用户代码需要编码说明有几步，哪一步需要做什么。

**信号（事件）**

+ onSubscribe() 订阅事件
+ onNext() 数据到达事件
+ onComplete() 订阅完成事件
+ onError() 订阅异常
+ request() 请求
+ cancel() 取消

**Reactive 特点**：

- 响应的（Responsive）
- 适应性强的（Resilient）
- 弹性的（Elastic）
- 消息驱动的（Message Driven）

**参考**：

[Reactive programming 一种技术 各自表述](https://mercyblitz.github.io/2018/07/25/Reactive-Programming-%E4%B8%80%E7%A7%8D%E6%8A%80%E6%9C%AF-%E5%90%84%E8%87%AA%E8%A1%A8%E8%BF%B0/)

