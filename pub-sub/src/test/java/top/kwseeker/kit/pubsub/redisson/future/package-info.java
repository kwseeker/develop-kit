package top.kwseeker.kit.pubsub.redisson.future;

/*
* Redisson future模式 工作原理
*
* 以初始化Redis服务节点连接信息为例：masterSlaveEntry.setupMasterEntry(new RedisURI(config.getMasterAddress()))
*
* 将业务逻辑全部剔除后，上面代码future模式的体现就是 RPromiseTest#testRPromis()
*
* */