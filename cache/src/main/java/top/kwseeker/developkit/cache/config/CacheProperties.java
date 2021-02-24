package top.kwseeker.developkit.cache.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "demo.caches")
public class CacheProperties {

    private String cacheNamePrefix;
    private final Map<String, Duration> cacheConfigMap = new HashMap<>();

    public String getCacheNamePrefix() {
        return cacheNamePrefix;
    }

    public void setCacheNamePrefix(String cacheNamePrefix) {
        this.cacheNamePrefix = cacheNamePrefix;
    }

    public Map<String, Duration> getCacheConfigMap() {
        return cacheConfigMap;
    }
}

