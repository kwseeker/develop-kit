package top.kwseeker.developkit.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private HttpClient defaultClient = HttpClients.createDefault();
    //private Http
    private HttpClientContext context = HttpClientContext.create();
    private HttpClientConnectionManager connMgr = new BasicHttpClientConnectionManager();
    private PoolingHttpClientConnectionManager poolConnMgr = new PoolingHttpClientConnectionManager();

    public static HttpClientManager getInstance() {
        return instance;
    }


    //TODO 解析HttpResponse返回值的各个成分（如获取header）
    //TODO HttpEntity解析的三种方式
    //TODO 异常处理
    //TODO 线程安全
    //TODO 连接关闭 HttpClient close()
    //TODO 执行上下文保持连接状态
    //TODO 拦截器
    //TODO 重试机制（HttpRequestRetryHandler）与幂等



    public HttpResponse get(URI uri) throws IOException {
        HttpGet httpGet = new HttpGet(uri);
        return defaultClient.execute(httpGet);
    }

    public HttpResponse get(String uri) throws IOException {
        HttpGet httpGet = new HttpGet(uri);
        return defaultClient.execute(httpGet);
    }

    public URI buildUri(String scheme, String host, String path, Map<String, String> params) throws URISyntaxException {
        List<NameValuePair> pairList = params.entrySet().stream()
                .map(paramEntry -> (NameValuePair) new BasicNameValuePair(paramEntry.getKey(), paramEntry.getValue()))
                .collect(Collectors.toList());
        return new URIBuilder()
                .setScheme(scheme)
                .setHost(host)
                .setPath(path)
                .setParameters(pairList)
                .build();
    }

}
