package top.kwseeker.log.calltrack.utils;

import java.util.UUID;

public class UUIDUtil {

    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
