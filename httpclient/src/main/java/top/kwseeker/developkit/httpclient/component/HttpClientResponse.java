package top.kwseeker.developkit.httpclient.component;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.util.EntityUtils;
import top.kwseeker.developkit.httpclient.exception.IllegalTypeException;

import java.io.IOException;
import java.io.InputStream;

public class HttpClientResponse {

    private static final ResponseHandler<Content> responseHandler;

    private final HttpResponse response;
    private boolean consumed;

    static {
        //内部先判断statusLine，正常则调用handleEntity读数据到Content,否则抛出异常
        responseHandler = new AbstractResponseHandler<Content>() {
            @Override
            public Content handleEntity(final HttpEntity entity) throws IOException {
                return entity != null ?
                        new Content(EntityUtils.toByteArray(entity), ContentType.getOrDefault(entity)) :
                        Content.NO_CONTENT;
            }
        };
    }

    HttpClientResponse(final HttpResponse response) {
        this.response = response;
    }

    private void assertNotConsumed() {
        if (this.consumed) {
            throw new IllegalStateException("Response content has been already consumed");
        }
    }

    /* ==================== 解析响应数据 ======================*/
    /**
     * 有三种数据读取方式
     */

    /**
     * 将内容解析为对应的Class实例,现在基本都是返回JSON然后转成对象实例
     */
    public <T> T parseContent(Class<T> clazz) throws IOException {
        Content content = returnContent();
        if (!ContentType.APPLICATION_JSON.equals(content.getType())) {
            throw new IllegalTypeException("Response contentType is not json, can not parse");
        }
        return JSONObject.parseObject(content.getRaw(), clazz);
    }

    /**
     * 从返回值中读取原生数据(byte[])以及数据类型
     */
    public Content returnContent() throws IOException {
        assertNotConsumed();
        try {
            return responseHandler.handleResponse(this.response);
        } finally {
            dispose();
        }
    }

    /**
     * 设置状态丢弃数据并关闭流
     */
    private void dispose() {
        if (this.consumed) {
            return;
        }
        try {
            final HttpEntity entity = this.response.getEntity();
            final InputStream content = entity.getContent();
            if (content != null) {
                content.close();
            }
        } catch (final Exception ignore) {
        } finally {
            this.consumed = true;
        }
    }

    private static class Content {
        public static final Content NO_CONTENT = new Content(new byte[]{}, ContentType.DEFAULT_BINARY);

        private final byte[] raw;
        private final ContentType type;

        public Content(final byte[] raw, final ContentType type) {
            super();
            this.raw = raw;
            this.type = type;
        }

        public byte[] getRaw() {
            return raw;
        }

        public ContentType getType() {
            return type;
        }
    }
}
