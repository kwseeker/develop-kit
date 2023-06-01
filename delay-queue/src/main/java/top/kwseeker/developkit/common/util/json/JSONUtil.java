package top.kwseeker.developkit.common.util.json;

import cn.hutool.core.text.CharSequenceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

public class JSONUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModules(new JavaTimeModule()); // 解决 LocalDateTime 的序列化
    }

    @SneakyThrows
    public static String toJSONString(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows
    public static <T> T parseObject(String text, Class<T> clazz) {
        if (CharSequenceUtil.isEmpty(text)) {
            return null;
        }
        return objectMapper.readValue(text, clazz);
    }
}
