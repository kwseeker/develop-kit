package top.kwseeker.developkit.commonutil;

import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtilTest {

    @Test
    public void testRandomMultiCase() throws InterruptedException {
        Random random1 = new Random(123456);
        Thread.sleep(1000);
        Random random2 = new Random(123456);
        Thread.sleep(1000);
        Random random3 = new Random();

        for (int i = 0; i < 5; i++) {
            System.out.println(random1.nextInt(100) + " " + random2.nextInt(100) + " " + random3.nextInt(100));
        }
    }

    @Test
    public void testThreadLocalRandom() {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        ThreadLocalRandom threadLocalRandom1 = ThreadLocalRandom.current();
        for (int i = 0; i < 5; i++) {
            System.out.println(threadLocalRandom.nextInt(100) + " " + threadLocalRandom1.nextInt(100));
        }
    }

    @Test
    public void testSecureRandom() throws NoSuchAlgorithmException {
        SecureRandom random1 = SecureRandom.getInstance("SHA1PRNG");
        SecureRandom random2 = SecureRandom.getInstance("SHA1PRNG");

        for (int i = 0; i < 5; i++) {
            System.out.println(random1.nextInt(100) + " " + random2.nextInt(100));
        }
    }
}