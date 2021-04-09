package top.kwseeker.developkit.httpclient.localserver;

import org.apache.http.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Locale;

public class EchoHandler
        implements HttpRequestHandler {

    // public default constructor

    /**
     * Handles a request by echoing the incoming request entity.
     * If there is no request entity, an empty document is returned.
     *
     * @param request   the request
     * @param response  the response
     * @param context   the context
     *
     * @throws HttpException    in case of a problem
     * @throws IOException      in case of an IO problem
     */
    @Override
    public void handle(final HttpRequest request,
                       final HttpResponse response,
                       final HttpContext context)
            throws HttpException, IOException {

        final String method = request.getRequestLine().getMethod().toUpperCase(Locale.ROOT);
        if (!"GET".equals(method) &&
                !"POST".equals(method) &&
                !"PUT".equals(method)
        ) {
            throw new MethodNotSupportedException
                    (method + " not supported by " + getClass().getName());
        }

        HttpEntity entity = null;
        if (request instanceof HttpEntityEnclosingRequest) {
            entity = ((HttpEntityEnclosingRequest)request).getEntity();
        }

        // For some reason, just putting the incoming entity into
        // the response will not work. We have to buffer the message.
        final byte[] data;
        if (entity == null) {
            data = new byte [0];
        } else {
            data = EntityUtils.toByteArray(entity);
        }

        final ByteArrayEntity bae = new ByteArrayEntity(data);
        if (entity != null) {
            bae.setContentType(entity.getContentType());
        }
        entity = bae;

        response.setStatusCode(HttpStatus.SC_OK);
        response.setEntity(entity);

    } // handle


} // class EchoHandler

