package top.kwseeker.developkit.logprinter.filter;

import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

public class ContentResponseWrapper extends ContentCachingResponseWrapper {
    private final String body;

    public ContentResponseWrapper(HttpServletResponse response) {
        super(response);
        body = getResponsePayload(response);
    }

    /**
     * 获取响应response：body
     * @param response
     * @return
     */
    private String getResponsePayload(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            wrapper.setCharacterEncoding("UTF-8");
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return "{}";
    }

    public String getBody() {
        return this.body;
    }
}
