package top.kwseeker.developkit.commonutil.price;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 浮点数精度问题 与 价格转换
 *
 * 浮点数精度问题：
 *  首先浮点数存储格式：
 *   float    32bit 最高位是符号位，接着8位是指数位（exponent，或称阶码），最后23位是尾数位（mantissa）
 *   double   64bit 最高位是符号位，接着11位是指数位，最后52位是尾数位
 *  浮点数的表示：mantissa * 2 ^ exponent （尾数和指数都是二进制数）
 *  比如：102.25F:
 *      二进制数表示为：1100110.01B
 *      将该二进制数规格化（即表示成 1.xx...x * 2 ^ yy...y, xx...x存储到尾数位，yy...y经过移码转换后存储到指数位）：1100110.01B=1.10011001 * 2 ^ 110
 *      将阶码(指数位)转换为移码表示()：110+1111111=10000101
 *      即最终：尾数 mantissa=10011001 阶码 exponent=10000101
 *      最终内存中存储数据： 0 10000101 10011001000000000000000
 *  浮点数的表示存在精度问题的原因：
 *  浮点数这种规格化的表示：1.xx...x * 2 ^ yy...y 还有另一种更易理解的（对应到内存bit位）的数学表达形式 F = 1*2^(0) + m1*2^(-1) + m2*2^(-2) + ... + mn*2^(-n) + ...
 *  注意这里 mn中n是m的脚标，有些浮点数经过很多次二分后还是无法除尽或超出尾数位长度就会丢失精度。
 */
public class PriceConverterUtil {

    public static int cnYuan2Cent(float yuan) {
        String yuanStr = String.valueOf(yuan);
        return cnYuan2Cent(yuanStr);
    }

    //public static int cnCent2Yuan(float yuan) {
    //    String yuanStr = String.valueOf(yuan);
    //    return cnYuan2Cent(yuanStr);
    //}

    public static int cnYuan2Cent(String yuan) {
        //BigDecimal bigDecimal = new BigDecimal(yuan).setScale(2, RoundingMode.HALF_UP);
        //return bigDecimal.multiply(new BigDecimal(100)).intValue();

        return new BigDecimal(yuan).movePointRight(2).intValue();
    }

    public static String cnCent2Yuan(int cent) {
        //return BigDecimal.valueOf(Long.valueOf(cent)).divide(new BigDecimal(100)).toString();

        return new BigDecimal(cent).movePointRight(2).toString();
    }
}
