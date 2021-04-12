package top.kwseeker.developkit.httpclient.component;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import top.kwseeker.developkit.httpclient.HttpClientManager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 定义请求
 */
public class HttpClientRequest {

    //http请求(get、post...)
    private final HttpRequestBase request;
    //请求配置,用于构建RequestConfig,如果不指定默认使用Manager中配置的
    private Boolean useExpectContinue;
    private Integer socketTimeout;
    private Integer connectTimeout;
    private Integer connectionRequestTimeout;
    //headers
    private Header[] headers;

    public HttpClientRequest(HttpRequestBase request) {
        this.request = request;
    }

    public static HttpClientRequest Get(final String url) {
        return Get(URI.create(url));
    }

    public static HttpClientRequest Get(final URI uri) {
        HttpGet httpGet = new HttpGet(uri);
        return new HttpClientRequest(httpGet);
    }

    public static HttpClientRequest Post(final String url) {
        return Post(URI.create(url));
    }

    public static HttpClientRequest Post(final URI uri) {
        HttpPost post = new HttpPost(uri);
        return new HttpClientRequest(post);
    }

    // 其他请求类型...

    public HttpClientResponse execute() throws IOException {

        HttpClient httpClient = HttpClientManager.getInstance().getHttpClient();
        return new HttpClientResponse(internalExecute(httpClient));
    }

    HttpResponse internalExecute(HttpClient httpClient) throws IOException {
        //这里的配置会覆盖Manager中的配置
        RequestConfig defaultRequestConfig = ((Configurable) httpClient).getConfig();
        RequestConfig.Builder builder = RequestConfig.copy(defaultRequestConfig);
        if (this.useExpectContinue != null) {
            builder.setExpectContinueEnabled(this.useExpectContinue);
        }
        if (this.socketTimeout != null) {
            builder.setSocketTimeout(this.socketTimeout);
        }
        if (this.connectTimeout != null) {
            builder.setConnectTimeout(this.connectTimeout);
        }
        if (this.connectionRequestTimeout != null) {
            builder.setConnectTimeout(this.connectionRequestTimeout);
        }
        this.request.setConfig(builder.build());
        //headers
        if (headers != null && headers.length > 0) {
            this.request.setHeaders(headers);
        }

        //TODO httpContext 管理
        return httpClient.execute(this.request, (HttpContext) null);
    }

    public static URI buildUri(String url, Map<String, String> params) throws URISyntaxException {
        return new URIBuilder(url)
                .addParameters(map2NameValueList(params))
                .build();
    }

    /**
     * URI.class可以从完整的URL路径解析出scheme、host、path..., 也可以通过下面方式组装
     * http_URL = "http:" "//" host [ ":" port ] [ abs_path [ "?" query ]]
     * @param scheme    请求模式：http/https
     * @param host      主机名: 域名或ip
     * @param port      端口号
     * @param path      路由路径
     * @param params    请求参数
     * @return URI
     * @throws URISyntaxException if uri syntax error
     */
    public static URI buildUri(String scheme, String host, int port, String path, Map<String, String> params) throws URISyntaxException {
        return new URIBuilder()
                .setScheme(scheme)
                .setHost(host)
                .setPort(port)
                .setPath(path)
                .setParameters(map2NameValueList(params))
                .build();
    }


    public HttpClientRequest setUseExpectContinue(Boolean useExpectContinue) {
        this.useExpectContinue = useExpectContinue;
        return this;
    }

    public HttpClientRequest setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public HttpClientRequest setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public HttpClientRequest setHeaders(Map<String, String> headers) {
        this.headers = (Header[]) headers.entrySet().stream()
                .map(headerEntry -> new BasicHeader(headerEntry.getKey(), headerEntry.getValue()))
                .toArray();
        return this;
    }

    public HttpClientRequest setHeaders(Header[] headers) {
        this.headers = headers;
        return this;
    }

    private static List<NameValuePair> map2NameValueList(Map<String, String> map) {
        return map.entrySet().stream()
                .map(paramEntry -> (NameValuePair) new BasicNameValuePair(paramEntry.getKey(), paramEntry.getValue()))
                .collect(Collectors.toList());
    }
}
