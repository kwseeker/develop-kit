package top.kwseeker.developkit.httpclient.localserver;

import org.apache.http.HttpHost;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.SSLServerSetupHandler;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.junit.After;
import org.junit.Before;
import top.kwseeker.developkit.httpclient.localserver.pathhandler.EchoHandler;
import top.kwseeker.developkit.httpclient.localserver.pathhandler.RandomHandler;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLServerSocket;
import java.util.concurrent.TimeUnit;

/**
 * 模拟服务端
 * 可以参考学习下不借助框架怎么实现web服务
 */
public class LocalServerTestBase {

    public enum ProtocolScheme { http, https }

    public static final String ORIGIN = "TEST/1.1";

    protected final ProtocolScheme scheme;

    protected ServerBootstrap serverBootstrap;
    protected HttpServer server;

    public LocalServerTestBase(final ProtocolScheme scheme) {
        this.scheme = scheme;
    }

    public LocalServerTestBase() {
        this(ProtocolScheme.http);
    }

    public String getSchemeName() {
        return this.scheme.name();
    }

    @Before
    public void setUp() throws Exception {
        //Socket配置
        // 配置包括超时时间、复用地址、soLinger、keepalive、tcpNoDelay、收发缓冲大小、backlogSize
        final SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(15000)
                .build();
        //
        this.serverBootstrap = ServerBootstrap.bootstrap()
                .setListenerPort(8443)
                .setSocketConfig(socketConfig)
                .setServerInfo(ORIGIN)
                .setSslSetupHandler(new SSLServerSetupHandler() {
                    @Override
                    public void initialize(final SSLServerSocket socket) throws SSLException {
                        socket.setEnabledProtocols(new String[] {"SSLv3", "SSLv2"});
                    }
                })
                .registerHandler("/echo/*", new EchoHandler())
                .registerHandler("/random/*", new RandomHandler());
        if (this.scheme.equals(ProtocolScheme.https)) {
            this.serverBootstrap.setSslContext(SSLTestContexts.createServerSSLContext());
        }
    }

    @After
    public void shutDown() throws Exception {
        if (this.server != null) {
            this.server.shutdown(10, TimeUnit.SECONDS);
        }
    }

    /**
     * 启动模拟服务端
     */
    public HttpHost start() throws Exception {
        //
        this.server = this.serverBootstrap.create();
        this.server.start();

        return new HttpHost("localhost", this.server.getLocalPort(), this.scheme.name());
    }
}
