package top.kwseeker.kit.pubsub.redisson;

import org.junit.Test;
import org.redisson.Version;

import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * 读取依赖包 META-INF/MANIFEST.MF 内部定义的属性
 */
public class VersionTest {

    @Test
    public void testLogVersion() throws Exception {
        Enumeration<URL> resources = Version.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
        while (resources.hasMoreElements()) {
            Manifest manifest = new Manifest(resources.nextElement().openStream());
            Attributes attrs = manifest.getMainAttributes();
            if (attrs == null) {
                continue;
            }
            String name = attrs.getValue("Bundle-Name");
            if ("Redisson".equals(name)) {
                System.out.println("Redisson " + attrs.getValue("Bundle-Version"));
                break;
            }
        }
    }
}
