package top.kwseeker.developkit.httpclient;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import top.kwseeker.developkit.httpclient.component.Content;
import top.kwseeker.developkit.httpclient.component.HttpClientRequest;
import top.kwseeker.developkit.httpclient.localserver.LocalServerTestBase;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;

public class HttpClientManagerTest extends LocalServerTestBase {

    @Before
    @Override
    public void setUp() throws Exception {
        //配置
        super.setUp();

        this.serverBootstrap.registerHandler("/", new HttpRequestHandler() {
            @Override
            public void handle(
                    final HttpRequest request,
                    final HttpResponse response,
                    final HttpContext context) throws HttpException, IOException {
                response.setEntity(new StringEntity("All is well", ContentType.TEXT_PLAIN));
            }
        });
        this.serverBootstrap.registerHandler("/echo", new HttpRequestHandler() {
            @Override
            public void handle(
                    final HttpRequest request,
                    final HttpResponse response,
                    final HttpContext context) throws HttpException, IOException {
                HttpEntity responseEntity = null;
                if (request instanceof HttpEntityEnclosingRequest) {
                    final HttpEntity requestEntity = ((HttpEntityEnclosingRequest) request).getEntity();
                    if (requestEntity != null) {
                        final ContentType contentType = ContentType.getOrDefault(requestEntity);
                        if (ContentType.TEXT_PLAIN.getMimeType().equals(contentType.getMimeType())) {
                            responseEntity = new StringEntity(
                                    EntityUtils.toString(requestEntity), ContentType.TEXT_PLAIN);
                        }
                    }
                }
                if (responseEntity == null) {
                    responseEntity = new StringEntity("echo", ContentType.TEXT_PLAIN);
                }
                response.setEntity(responseEntity);
            }
        });
    }

    //基本请求
    @Test
    public void testGet() throws Exception {
        final HttpHost target = start();
        final String baseURL = "http://localhost:" + target.getPort();

        Content content = HttpClientRequest.Get(baseURL+"/")
                .execute().returnContent();

        Assert.assertEquals("All is well", new String(content.getRaw()));
        Assert.assertEquals("text/plain", content.getType().getMimeType());
    }

    @Test
    public void testPost() throws Exception {
        final HttpHost target = start();
        final String baseURL = "http://localhost:" + target.getPort();

        Content content = HttpClientRequest.Post(baseURL+"/")
                .execute().returnContent();

        Assert.assertEquals("All is well", new String(content.getRaw()));
        Assert.assertEquals("text/plain", content.getType().getMimeType());
    }

    //HTTPS请求
    @Test
    public void testHttpsPost() throws Exception {
        //Content content = HttpClientRequest.Post("https://www.baidu.com")
        //        .execute().returnContent();
        final HttpHost target = start();
        System.out.println(target.getPort());
        Content content = HttpClientRequest.Post("https://localhost:"+ target.getPort() + "/")
        //Content content = HttpClientRequest.Post("https://kwseeker.top:" + target.getPort() + "/")
                .execute().returnContent();
    }

    @Test
    public void testsss() throws Exception {
        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(new File("my.keystore"), "nopassword".toCharArray(),
                        new TrustSelfSignedStrategy())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {

            HttpGet httpget = new HttpGet("https://httpbin.org/");

            System.out.println("Executing request " + httpget.getRequestLine());

            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    @After
    @Override
    public void shutDown() throws Exception {
        //Executor.closeIdleConnections();
        super.shutDown();
    }
}