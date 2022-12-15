package top.kwseeker.developkit.commonutil.price;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceConverterUtilTest {

    @Test
    public void testPriceConverter() {
        float yuan1 = 1.23F;     //实际存储的是 1.2300000190734863
        float yuan2 = 1.43F;     //实际存储的是 1.4299999475479126

        Assert.assertEquals(1.2300000190734863F, yuan1,0.000000119);  //1/(2^23)
        Assert.assertEquals(1.4299999475479126F, yuan2,0.000000119);  //1/(2^23)
        Assert.assertEquals(1.4299999475479126F, yuan2,0.000000001);  //1/(2^23)

        System.out.println((int)(1.4299999475479126F * 100));   //143
        System.out.println((int)(1.4299999475479126F * 10000000));  //14299999

        System.out.println(Float.valueOf(yuan2 * 100).toString());  //143.0
        System.out.println(Float.valueOf(yuan2 * 100.107F).toString()); //143.15302 实际存储的是 143.15301513671875
        System.out.println(Float.valueOf(yuan2 * 10000000).toString()); //1.4299999E7   //这个结果可以看到浮点数直接运算还是不靠谱

        float a = 1.6F;
        float b = 0.3F;
        float c = a + b;    //调试时看到 c=1.9000001, 实际后面应该还有一长串数据，只不过被什么给截断了
        Assert.assertNotEquals(1.9F, a + b, 0.0); //1.9F != 1.6F + 0.3F
        BigDecimal sum = new BigDecimal("1.6").add(new BigDecimal("0.3"));
        Assert.assertEquals(new BigDecimal("1.9").floatValue(), sum.floatValue(), 0.0);

        float yuan3 = 2.66F;
        Assert.assertEquals(266, PriceConverterUtil.cnYuan2Cent(yuan3));
    }

    @Test
    public void testBigDecimal() {
        float fa = 123.45678F;
        float fb = 1.6F;
        BigDecimal a = new BigDecimal("123.45678");
        BigDecimal b = new BigDecimal("1.623783473");

        BigDecimal addRet = a.add(b);
        BigDecimal subtractRet = a.subtract(b);
        BigDecimal multiplyRet = a.multiply(b);
        BigDecimal divideRet = a.divide(b, RoundingMode.HALF_UP);   //过半进1,就是四舍五入

        System.out.println(addRet.toString() + " --- " + (fa + fb));
        System.out.println(subtractRet.toString() + " --- " + (fa - fb));
        System.out.println(multiplyRet.toString() + " --- " + (fa * fb));
        System.out.println(divideRet.toString() + " --- " + (fa / fb));
        //125.05678 --- 125.05678  准确结果  125.05678      看似浮点数直接加减没有问题，实则有精度问题， (fa + fb)转字符串后估计有做四舍五入等截断操作
        //121.85678 --- 121.85678           121.85678
        //197.530848 --- 197.53085          197.530848
        //77.16049 --- 77.160484            77.1604875      BigDecimal也发生了截断小数点后第5位开始后面四舍五入了, 看源码是和被除数的scale保持一致

        //BigDecimal 除法算法原理：a.divide(b, RoundingMode.HALF_UP)
        //  12345678 / 16  被除数小数点右移了5位，除数小数点右移了1位
        //  123456780 / 16 因为除数小数点右移了1位，被除数补1个0
        //  求得整数位结果 7716048  和余数 12， 因为测试中选择的 RoundingMode.HALF_UP， 12 > (16/2), 所以进1，舍去余数；
        //  最终整数位结果 7716049，然后将小数点左移5位和被除数的小数位数scale一致。
        //  最终结果 77.16049
    }
}