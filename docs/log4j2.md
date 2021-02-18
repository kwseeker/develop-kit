# Log4j2最佳配置

[Log4j2 Manual](https://logging.apache.org/log4j/2.x/manual/)

[Log4j2中文文档](https://www.docs4dev.com/docs/zh/log4j2/2.x/all/index.html#)

[Log4j2 中文手册](https://www.docs4dev.com/docs/zh/log4j2/2.x/all/manual-index.html)

+ 日志配置文件加载顺序

  + JSON格式配置
  + Yaml格式配置

+ 默认配置

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <Configuration status="WARN">
    <Appenders>
      <Console name="Console" target="SYSTEM_OUT">
        <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
      </Console>
    </Appenders>
    <Loggers>
      <Root level="error">
        <AppenderRef ref="Console"/>
      </Root>
    </Loggers>
  </Configuration>
  ```

+ [Configuration标签属性](https://logging.apache.org/log4j/2.x/manual/configuration.html#ConfigurationSyntax)

  + monitorInterval

    自动检测配置更新。

+ Logger

  + `<Root>`和`<Logger>` 

  + `<Logger>`属性

    + additivity

      默认为true, 即事件会传递到任何父Logger。

+ Async Loggers

+ [Appender](https://www.docs4dev.com/docs/zh/log4j2/2.x/all/manual-appenders.html)

  负责将 LogEvents 传递到其目的地（STDOUT File etc）。

  + 通用属性

    + Layout 

      格式化日志输出。可以输出多种格式日志 CSV, GELF, HTML, JSON, Pattern, RFC-5424, Serialized, Syslog, XML, YAML, Location Information.

      + Pattern Layout

        转换模式与 C 中的 printf 函数的转换模式密切相关。

        [转换模式](https://logging.apache.org/log4j/2.x/manual/layouts.html#PatternLayout)参考Conversion Pattern小节。

        ```xml
        <property name="LOG_PATTERN">%d{yyyy-MM-dd HH:mm:ss.SSS} %highlight{%-5p} %style{[%pid][%16t] %-33maxLen{%c{1}:%L}{30}}{cyan} : %msg%n</property>
        ```

        + %c | logger

          发布日志事件的Logger名称，默认为类名。

        + %C | class

        + %d | date

        + %enc | encode

        + %equals | equalsIgnoreCase

        + %ex|exception|throwable

          ```java
          // %throwable{full}
          log.error("exception: ", e);
          ```

        + %F | file

        + %highlight

          设置高亮显示。

        + %K | map | MAP

        + %I | location

        + %L | line

        + %m | msg | message

        + %M | method

        + %marker

        + %maxLen

        + %n

        + %N | nano

        + %pid | processId

        + %p | level

        + %replace

        + %sn
        + %style
        + %T | tid | threadId
        + %t | tn | thread
        + %tp
        + %x
        + %X
        + %u

    + Filters & Filter

      筛选器允许对日志事件进行评估，以确定是否或如何发布它们。

      + BurstFilter

        达到最大限制后通过静默丢弃事件来控制处理 LogEvent 的速率。

        可能丢失事件。

      + CompositeFilter

      + DynamicThresholdFilter

      + MapFilter

      + MarkerFilter

      + NoMarkerFilter

      + RegexFilter

      + ScriptFilter

      + StructuredDataFilter

      + ThreadContextMapFilter

      + ThresholdFilter

        ```xml
        <ThresholdFilter level="TRACE" onMatch="ACCEPT" onMismatch="DENY"/>
        ```

      + TimeFilter

        ```xml
        <TimeFilter start="05:00:00" end="05:30:00" onMatch="ACCEPT" onMismatch="DENY"/>
        ```

      

  + 分类

    + Async

      修饰其他Appender, 将日志事件异步处理。

      ```xml
      <File name="MyFile" fileName="logs/app.log">
      	<PatternLayout>
      		<Pattern>%d %p %c{1.} [%t] %m%n</Pattern>
      	</PatternLayout>
      </File>
      <Async name="Async">
      	<AppenderRef ref="MyFile"/>
      </Async>
      ```

    + Cassandra

      将日志写入到Cassandra数据库。

    + Console

      将日志写入到System.out 或 System.err

    + Failover

      也是用于修饰其他Appender, FailoverAppender 包装了一组附加程序。如果主 Appender 失败，则将按 Sequences 尝试次要 Appender，直到一个成功或没有其他次要 Appender 为止。

    + File

      属性：

      + append

      + bufferedIO

        先写缓冲缓冲满了再写入磁盘。

      + immiedateFlush

      + bufferSize

      + createOnDemand

      + filter

      + fileName

    + Flume

    + JDBC/JPA/NoSQL/MongoDB

    + HTTP

    + Kafka

    + RandomAccessFile

      相对于FIleAppender,它始终被缓冲(无法关闭)，并且在内部使用 ByteBuffer RandomAccessFile 而不是 BufferedOutputStream。与[measurements](https://www.docs4dev.com/docs/zh/log4j2/2.x/all/performance.html#whichAppender)中具有“ bufferedIO = true”的 FileAppender 相比，我们看到了 20-200％的性能提升。

    + Rewrite

      也是用于修饰其他Appender,允许实现在将 LogEvent 传递给 Appender 之前检查并可能对其进行修改。 

      如：隐藏密码数据， 调整日志事件级别。

    + RollingFile

      + 滚动触发策略（4种）

        

      + 滚动过渡策略

    + RollingRandomAccessFile

    + Routing

      评估 LogEvents，然后将它们路由到下级 Appender。

    + Socket

    + Syslog

    + ZeroMQ/JeroMQ



