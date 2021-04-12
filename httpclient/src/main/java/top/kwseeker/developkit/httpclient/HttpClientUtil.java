package top.kwseeker.developkit.httpclient;

import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import top.kwseeker.developkit.httpclient.component.Content;
import top.kwseeker.developkit.httpclient.component.HttpClientRequest;
import top.kwseeker.developkit.httpclient.component.HttpClientResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 发送请求与读取响应
 * 请求数据（可能定制的数据）
 *  请求行: 请求类型 + URL + 协议及版本
 *  请求头: 键值对格式，详细参考: https://www.rfc-editor.org/rfc/rfc2616.html P14 码友翻译：http://www.blogjava.net/Files/sunchaojin/http1.3.pdf
 *  请求体:
 * 响应数据（主要关注状态和响应体）
 *  响应行
 *  响应头
 *  响应体
 */
public class HttpClientUtil {



    public static <T> T doGet(String url, Class<T> clazz) throws IOException {
        return HttpClientRequest.Get(url)
                .execute()
                .parseContent(clazz);
    }

    public static <T> T doGet(String url, Map<String, String> headers, Map<String, String> params, Class<T> clazz) throws URISyntaxException, IOException {
        URI uri = HttpClientRequest.buildUri(url, params);
        return HttpClientRequest.Get(uri)
                .setHeaders(headers)
                .execute()
                .parseContent(clazz);
    }

    public static Content doGet(String url, Map<String, String> headers, Map<String, String> params) throws URISyntaxException, IOException {
        URI uri = HttpClientRequest.buildUri(url, params);
        return HttpClientRequest.Get(uri)
                .setHeaders(headers)
                .execute()
                .returnContent();
    }

    public static <T> T doPost(String url, Class<T> clazz) throws IOException {
        return HttpClientRequest.Post(url)
                .execute()
                .parseContent(clazz);
    }
}
