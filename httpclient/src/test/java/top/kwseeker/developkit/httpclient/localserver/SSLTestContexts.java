package top.kwseeker.developkit.httpclient.localserver;

import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;

public class SSLTestContexts {

    public static SSLContext createServerSSLContext() throws Exception {
        return SSLContexts.custom()
                .loadTrustMaterial(SSLTestContexts.class.getResource("/test.keystore"),
                        "nopassword".toCharArray())
                .loadKeyMaterial(SSLTestContexts.class.getResource("/test.keystore"),
                        "nopassword".toCharArray(), "nopassword".toCharArray())
                .build();
    }

    public static SSLContext createClientSSLContext() throws Exception {
        return SSLContexts.custom()
                .loadTrustMaterial(SSLTestContexts.class.getResource("/test.keystore"),
                        "nopassword".toCharArray())
                .build();
    }

}
