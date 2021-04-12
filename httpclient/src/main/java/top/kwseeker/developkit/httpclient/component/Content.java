package top.kwseeker.developkit.httpclient.component;

import org.apache.http.entity.ContentType;

public class Content {
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
