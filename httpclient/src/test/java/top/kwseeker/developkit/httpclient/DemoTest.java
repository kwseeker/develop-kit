package top.kwseeker.developkit.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import sun.misc.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DemoTest {

    @Test
    public void testGet() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        System.out.println(response.getStatusLine());
        HttpEntity entity = response.getEntity();
        EntityUtils.consume(entity);
    }

    @Test
    public void testFluentGet() {
        Request.
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        System.out.println(response.getStatusLine());
        HttpEntity entity = response.getEntity();
        EntityUtils.consume(entity);
    }

    @Test
    public void testPost() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("");
        List<NameValuePair> nvps = new ArrayList <>();
        nvps.add(new BasicNameValuePair("username", "Arvin"));
        nvps.add(new BasicNameValuePair("password", "Lee"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity respContent = response.getEntity();
        EntityUtils.consume(respContent);
    }
}