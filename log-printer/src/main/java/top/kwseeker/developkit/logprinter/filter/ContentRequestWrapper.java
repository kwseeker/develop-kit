package top.kwseeker.developkit.logprinter.filter;

import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class ContentRequestWrapper extends ContentCachingRequestWrapper {
    private final String body;

    public ContentRequestWrapper(HttpServletRequest request) {
        super(request);
        body = getRequestPayload(request);
    }

    /**
     * 获取request：body
     * @param request
     * @return
     */
    private String getRequestPayload(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf1 = wrapper.getContentAsByteArray();
            if (buf1.length > 0) {
                try {
                    return new String(buf1, 0, buf1.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException var10) {
                    //NOOP
                }
            }
        }
        return "{}";
    }

    public String getBody() {
        return this.body;
    }
}
