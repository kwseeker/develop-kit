package top.kwseeker.developkit.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

public class HttpResponseParser {

    public static void getContent(HttpResponse httpResponse) {
        HttpEntity httpEntity = httpResponse.getEntity();

    }
}
