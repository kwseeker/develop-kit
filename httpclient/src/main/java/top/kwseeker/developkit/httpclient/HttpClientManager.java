package top.kwseeker.developkit.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * HttpClient管理器
 * HttpClient客户端的功能
 *  客户端链接(连接目标，连接配置，连接创建,连接释放)
 *  连接管理（连接复用提升吞吐量、连接路由、安全连接）
 *  请求
 *  响应处理
 *  安全认证
 *  其他处理（性能监控、日志、...）
 *  缓存
 *  同步异步请求
 * 参考：官方demo工程单元测试
 */
public class HttpClientManager {

    private static final int KEEP_ALIVE_TTL = 60;                   //和后台tomcat连接保存时间保一致
    private static final int CONN_MAX_TOTAL = 256;                  //这个数字是否是最合适的存疑
    private static final int CONN_MAX_PER_ROUTE = 50;               //这个数字是否是最合适的存疑
    private static final int REQ_SO_TIMEOUT = 5*1000;               //这个数字是否是最合适的存疑
    private static final int REQ_CONN_TIMEOUT = 5*1000;             //这个数字是否是最合适的存疑
    private static final int REQ_CONN_REQUEST_TIMEOUT = 5*1000;     //这个数字是否是最合适的存疑

    private static final HttpClientManager instance = new HttpClientManager();

    //private ThreadLocal<HttpClientContext> httpClientContext;
    //可以建立多个HttpClient但应该由同一个连接池管理
    private final HttpClient httpClient;
    //连接池管理尽量只保持一个实例，并在里面合理控制连接数和超时时间
    private final PoolingHttpClientConnectionManager poolingConnMgr;
    //默认的请求配置
    RequestConfig defaultRequestConfig;
    //代理服务器
    private HttpHost proxy;
    //SSL配置
    private SSLContext sslContext;
    //重试策略
    private HttpRequestRetryHandler retryHandler;

    public static HttpClientManager getInstance() {
        return instance;
    }

    public HttpClientManager() {
        //Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
        //        .register("http", PlainConnectionSocketFactory.INSTANCE)
        //        .register("https", new SSLConnectionSocketFactory(sslcontext))
        //        .build();
        this.poolingConnMgr = new PoolingHttpClientConnectionManager(KEEP_ALIVE_TTL, TimeUnit.SECONDS);
        this.poolingConnMgr.setMaxTotal(CONN_MAX_TOTAL);                     //缺省默认: 20
        this.poolingConnMgr.setDefaultMaxPerRoute(CONN_MAX_PER_ROUTE);       //缺省默认: 2, 路由：ip＋port 或者 域名

        //设置默认请求配置，每个请求也可以单独设置
        this.defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(REQ_SO_TIMEOUT)
                .setConnectTimeout(REQ_CONN_TIMEOUT)
                .setConnectionRequestTimeout(REQ_CONN_REQUEST_TIMEOUT)
                .build();

        //SSL添加信任证书(只有以HTTPS请求的目标网站没有被CA备案证书时才需要设置)
        //try {
        //    this.sslContext = SSLContexts.custom()
        //            .loadTrustMaterial(new File("/etc/ssl/certs/java/mystore"), "123456".toCharArray(), new TrustSelfSignedStrategy())
        //            .build();
        //} catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException | CertificateException | IOException e) {
        //    e.printStackTrace();
        //}

        //重试策略
        HttpRequestRetryHandler retryHandler = (e, executionCount, httpContext) -> {
            if (executionCount > 3) {
                return false;
            }
            //某些异常不需要重试
            return !(e instanceof InterruptedIOException)
                    && !(e instanceof UnknownHostException)
                    && !(e instanceof SSLException)
                    && !(e instanceof HttpHostConnectException);
        };

        httpClient = buildHttpClient(true, true);
    }

    //TODO 支持http和https
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

    /**
     * 构建客户端连接对象
     * 配置很多，基本都有默认配置，但是默认配置不一定符合业务需要
     * 常用关键配置([]内为官方默认值)：
     * 1) 连接池配置(持久连接存活时间[-1]，连接器最大连接数[20]和每个路由最大连接数[2])，合理配置可有效降低延迟和系统开销
     * 2) 请求配置（获取连接超时[-1]、请求超时[-1]、响应超时[-1]），
     * 3) 重试机制
     * 4) SSL配置
     * @return HttpClient
     */
    private HttpClient buildHttpClient(boolean withDefaultRequestConfig, boolean withRetryConfig) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setConnectionManager(this.poolingConnMgr);

        if(withDefaultRequestConfig && this.defaultRequestConfig != null) {
            httpClientBuilder.setDefaultRequestConfig(this.defaultRequestConfig);
        }
        if(withRetryConfig && this.retryHandler != null) {
            httpClientBuilder.setRetryHandler(this.retryHandler);
        }
        //其他很多功能用到再拓展吧

        return httpClientBuilder.build();
    }

    /* ==================== getter ======================*/

    public HttpClient getHttpClient() {
        return httpClient;
    }

}
