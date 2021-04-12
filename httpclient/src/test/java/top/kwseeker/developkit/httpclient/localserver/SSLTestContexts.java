package top.kwseeker.developkit.httpclient.localserver;

import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SSLTestContexts {

    public static SSLContext createServerSSLContext() throws Exception {
        //return SSLContexts.custom()
        //        .loadTrustMaterial(SSLTestContexts.class.getResource("/etc/ssl/certs/java/mystore"),
        //                "123456".toCharArray())
        //        .loadKeyMaterial(SSLTestContexts.class.getResource("/etc/ssl/certs/java/mystore"),
        //                "123456".toCharArray(), "123456".toCharArray())
        //        .build();
        return new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        }).build();
    }

    public static SSLContext createClientSSLContext() throws Exception {
        return SSLContexts.custom()
                .loadTrustMaterial(SSLTestContexts.class.getResource("/etc/ssl/certs/java/mystore"),
                        "123456".toCharArray())
                .build();
    }

}
