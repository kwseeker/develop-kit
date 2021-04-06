package top.kwseeker.developkit.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;

/**
 * HttpClient客户端的功能
 *  客户端链接(连接目标，连接配置，连接创建,连接释放)
 *  连接管理（连接复用提升吞吐量、连接路由、安全连接）
 *  请求
 *  响应处理
 *  安全认证
 *  其他处理（性能监控、日志、）
 *  缓存
 *  同步异步请求
 */
@Slf4j
public class HttpClientManager {

    private static final HttpClientManager instance = new HttpClientManager();

    public static HttpClientManager getInstance() {
        return instance;
    }

    public HttpClient buildClient

}
