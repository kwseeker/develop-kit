package top.kwseeker.developkit.logprinter.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class LogFilter extends OncePerRequestFilter {

    static final Pattern SYMBOL_PATTERN = Pattern.compile("\t|\r|\n");

    private List<String> ignorePaths;
    private List<String> ignoreBodyPaths;
    @Value("${request.logging.ignore}")
    private String configPath;

    @Value("${request.logging.ignoreBodyLog}")
    private String configLogBodyPath;

    @PostConstruct
    private void onLoaded() {
        if (this.ignorePaths == null) {
            this.ignorePaths = new ArrayList<>();
        }
        if (this.ignoreBodyPaths == null) {
            this.ignoreBodyPaths = new ArrayList<>();
        }

        if (!StringUtils.isEmpty(configPath)) {
            this.ignorePaths.addAll(Arrays.asList(this.configPath.split(";")));
        }
        if (!StringUtils.isEmpty(configLogBodyPath)) {
            this.ignoreBodyPaths.addAll(Arrays.asList(this.configLogBodyPath.split(";")));
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String ctxPath = request.getContextPath();
        String uri = request.getRequestURI();
        String reqUri = truncateUri(uri, ctxPath);
        if ("/".equals(uri) || !ignoredLogging(reqUri)){
            filterChain.doFilter(request, response);
            return;
        }
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        try {
            long startTime = System.currentTimeMillis();
            filterChain.doFilter(request, response);
            long endTime = System.currentTimeMillis();
            String executeTime = String.valueOf(endTime - startTime).concat(" ms");
            request.setAttribute("executeTime", executeTime);
        } finally {
            logPayload(request, response);
            copyBodyToResponse(response);
        }
    }

    private boolean ignoredLogging(String reqUrl) {
        if (StringUtils.isEmpty(StringUtils.trimAllWhitespace(reqUrl))) {
            return false;
        } else {
            for (String ignore : ignorePaths){
                if (reqUrl.contains(ignore)){
                    return false;
                }
            }
            return true;
        }
    }
    private boolean ignoredBodyLogging(String reqUrl) {
        if (StringUtils.isEmpty(StringUtils.trimAllWhitespace(reqUrl))) {
            return false;
        } else {
            for (String ignore : ignoreBodyPaths){
                if (reqUrl.contains(ignore)){
                    return true;
                }
            }
            return false;
        }
    }

    private void logPayload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String reqBody = replaceSymbol((new ContentRequestWrapper(request)).getBody());
        String rspBody = replaceSymbol((new ContentResponseWrapper(response)).getBody());
        String reqUrl = request.getRequestURI();
        String v = null;
        if(ignoredBodyLogging(reqUrl)) {
            v = "URL " + reqUrl + " REQ " + reqBody + " RESP { . . .} " + " MS " + request.getAttribute("executeTime");
        }else {
            v = "URL " + reqUrl + " REQ " + reqBody + " RESP " + rspBody + " MS " + request.getAttribute("executeTime");
        }
        log.info(v);

    }

    private void copyBodyToResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        responseWrapper.copyBodyToResponse();
    }

    private String replaceSymbol(String str) {
        String dest = "";
        if (str!=null) {
            Matcher m = SYMBOL_PATTERN.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    private String truncateUri(String uri, String ctxPath) {
        if (StringUtils.isEmpty(uri)) {
            return "";
        }
        String result = uri.substring(ctxPath.length());
        if(result.indexOf("/") != 0) {
            return "/" + result;
        }
        return result;
    }
}

