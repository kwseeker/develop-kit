package top.kwseeker.developkit.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * HttpClient客户端的功能
 * 客户端链接(连接目标，连接配置，连接创建,连接释放)
 * 连接管理（连接复用提升吞吐量、连接路由、安全连接）
 * 请求
 * 响应处理
 * 安全认证
 * 其他处理（性能监控、日志、）
 * 缓存
 * 同步异步请求
 * 参考：官方demo工程单元测试
 */
@Slf4j
public class HttpClientManager {

    private static final HttpClientManager instance = new HttpClientManager();

    private HttpClient httpClient = null;
    private ThreadLocal<HttpClientContext> httpClientContext;
    //private HttpClient defaultClient = HttpClients.createDefault();
    //private Http
    //private HttpClientContext context = HttpClientContext.create();
    private HttpClientConnectionManager connMgr = new BasicHttpClientConnectionManager();
    private PoolingHttpClientConnectionManager poolConnMgr = new PoolingHttpClientConnectionManager();

    public static HttpClientManager getInstance() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(64);

        connectionManager.setMaxTotal(256);
        httpManager.getParams().setDefaultMaxConnectionsPerHost(50);
        httpManager.getParams().setConnectionTimeout(10 * 1000);
        httpManager.getParams().setSoTimeout(10 * 1000);
        httpManager.getParams().setMaxTotalConnections(256);
        return instance;
    }

    private static final int KEEP_ALIVE_TTL = 60;

    public static HttpClient buildHttpClient(int ttl, int maxTotal, int maxPerRoute,
                                             int socketTimeout, int connTimeout, int connReqTimeout) {
        //连接池配置
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(ttl, TimeUnit.SECONDS);
        connectionManager.setMaxTotal(maxTotal);                    //default: 20
        connectionManager.setDefaultMaxPerRoute(maxPerRoute);       //default: 2, 路由：ip＋port 或者 域名
        //设置默认请求配置，每个请求也可以单独设置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connTimeout)
                .setConnectionRequestTimeout(connReqTimeout)
                .build();
        //重试
        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
                return false;
            }
        };

        return buildHttpClient(connectionManager, defaultRequestConfig, retryHandler);
    }

    /**
     * 构建客户端连接对象
     * 配置很多，基本都有默认配置，但是默认配置不一定符合业务需要
     * 常用关键配置([]内为默认值)：
     * 1) 连接池配置(持久连接存活时间[-1]，连接器最大连接数[20]和每个路由最大连接数[2])，合理配置可有效降低延迟和系统开销，TODO：设置多少比较合适，根据业务量设置
     * 2) 请求配置（获取连接超时[-1]、请求超时[-1]、响应超时[-1]），
     * 3) 重试机制
     * 4) SSL配置
     * 5)
     * @return HttpClient
     */
    public static HttpClient buildHttpClient(HttpClientConnectionManager connectionManager,
                                             RequestConfig requestConfig,
                                             HttpRequestRetryHandler retryHandler) {
        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(retryHandler)
                .evictExpiredConnections()
                .evictIdleConnections(3, TimeUnit.MINUTES)

                //.setConnectionTimeToLive()
                //.setConnectionRequestTimeout(44)
                //.setExpectContinueEnabled(true)
                //.setAuthenticationEnabled(false)
                //.setRedirectsEnabled(false)
                //.setRelativeRedirectsAllowed(false)
                //.setCircularRedirectsAllowed(true)
                //.setMaxRedirects(100)
                //.setCookieSpec(CookieSpecs.STANDARD)
                //.setLocalAddress(InetAddress.getLocalHost())
                //.setProxy(new HttpHost("someproxy"))
                //.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM))
                //.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.DIGEST))
                //.setContentCompressionEnabled(false)
                //.setNormalizeUri(false)
                .build();
    }


    //TODO 解析HttpResponse返回值的各个成分（如获取header）
    //TODO HttpEntity解析的三种方式
    //TODO 异常处理
    //TODO 线程安全
    //TODO 连接关闭 HttpClient close()
    //TODO 执行上下文保持连续请求的状态
    //TODO 拦截器
    //TODO 重试机制（HttpRequestRetryHandler）与幂等
    //TODO 连接eviction
    //Keep-alive 策略
    //状态管理
    //Http缓存
    //


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
