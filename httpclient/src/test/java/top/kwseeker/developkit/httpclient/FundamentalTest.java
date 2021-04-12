package top.kwseeker.developkit.httpclient;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import top.kwseeker.developkit.httpclient.localserver.LocalServerTestBase;

import java.io.IOException;

public class FundamentalTest extends LocalServerTestBase {

    /**
     * 启动内部HttpServer
     * @throws Exception
     */
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

    @Test
    public void testGet() throws Exception {
        final HttpHost target = start();
        final String baseURL = "http://localhost:" + target.getPort();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(baseURL + "/");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        System.out.println(response.getStatusLine());
        HttpEntity entity = response.getEntity();
        EntityUtils.consume(entity);
    }

    @After
    @Override
    public void shutDown() throws Exception {
        //Executor.closeIdleConnections();
        super.shutDown();
    }
}
