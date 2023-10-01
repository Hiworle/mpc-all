package cn.edu.hitsz.mpcapi.math;

import java.math.BigInteger;

/**
 * @param k     有限域的大小
 * @param alpha 本源根
 * @author hope
 * @date 2023/9/9 - 9:50
 */
public record FieldOperator(BigInteger k, BigInteger alpha) {

    /**
     * 有限域内的加法
     *
     * @throws ArithmeticException 抛出的运算错误，例如传入数据不在有限域内
     */
    public BigInteger add(BigInteger a, BigInteger b) throws ArithmeticException {
        return a.add(b).mod(k);
    }

    /**
     * 有限域内的减法
     *
     * @throws ArithmeticException 抛出的运算错误
     */
    public BigInteger sub(BigInteger a, BigInteger b) throws ArithmeticException {
        return a.subtract(b).mod(k);
    }

    /**
     * 有限域内的向量减法，例如 (a1,b1) - (a2,b2) -> (a1-b1, a2-b2)
     *
     * @throws ArithmeticException 抛出的运算错误
     */
    public BigInteger[] sub(BigInteger[] a, BigInteger[] b) throws ArithmeticException {
        if (a.length != b.length) throw new ArithmeticException("向量无法减！");
        BigInteger[] ans = new BigInteger[b.length];
        for (int i = 0; i < b.length; i++) {
            ans[i] = sub(a[i], b[i]);
        }
        return ans;
    }
    /**
     * 有限域内的向量减法，例如 a - (a2,b2) -> (a-a2, a-b2)
     *
     * @throws ArithmeticException 抛出的运算错误
     */
    public BigInteger[] sub(BigInteger a, BigInteger[] b) throws ArithmeticException {
        BigInteger[] ans = new BigInteger[b.length];
        for (int i = 0; i < b.length; i++) {
            ans[i] = sub(a, b[i]);
        }
        return ans;
    }

    /**
     * 有限域内的乘法
     *
     * @throws ArithmeticException 抛出的运算错误
     */
    public BigInteger mul(BigInteger a, BigInteger b) throws ArithmeticException {
        return a.multiply(b).mod(k);
    }

    /**
     * 有限域内的向量乘法，例如 (a1,a2) * (b1,b2) -> (a1*b1,a2*b2)
     *
     * @throws ArithmeticException 抛出的运算错误
     */
    public BigInteger[] mul(BigInteger[] a, BigInteger[] b) throws ArithmeticException {
        if (a.length != b.length) throw new ArithmeticException("向量无法乘！");
        BigInteger[] ans = new BigInteger[a.length];
        for (int i = 0; i < a.length; i++) {
            ans[i] = mul(a[i], b[i]);
        }
        return ans;
    }

    /**
     * 有限域内的向量乘法，例如 a * (b1,b2) -> (a*b1,a*b2)
     *
     * @throws ArithmeticException 抛出的运算错误
     */
    public BigInteger[] mul(BigInteger a, BigInteger[] b) throws ArithmeticException {
        BigInteger[] ans = new BigInteger[b.length];
        for (int i = 0; i < b.length; i++) {
            ans[i] = mul(a, b[i]);
        }
        return ans;
    }

    /**
     * 有限域内的除法
     *
     * @throws ArithmeticException 抛出的运算错误
     */
    public BigInteger div(BigInteger a, BigInteger b) throws ArithmeticException {
        return mul(a, inv(b));
    }

    /**
     * 有限域内的向量除法，例如 (a1, a2) / b -> (a1/b, a2/b)
     *
     * @throws ArithmeticException 抛出的运算错误
     */
    public BigInteger[] div(BigInteger[] a, BigInteger b) throws ArithmeticException {
        BigInteger[] ans = new BigInteger[a.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = div(a[i], b);
        }
        return ans;
    }
    /**
     * 有限域内的向量除法，例如 (a1, a2) / (b1, b2) -> (a1/b1, a2/b2)
     *
     * @throws ArithmeticException 抛出的运算错误
     */
    public BigInteger[] div(BigInteger[] a, BigInteger[] b) throws ArithmeticException {
        if (a.length != b.length) throw new ArithmeticException("向量无法除！");
        BigInteger[] ans = new BigInteger[a.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = div(a[i], b[i]);
        }
        return ans;
    }

    /**
     * 求逆元
     *
     * @throws ArithmeticException 抛出的运算错误
     */
    public BigInteger inv(BigInteger a) throws ArithmeticException {
        return a.modInverse(k);
    }

    /**
     * 求幂次
     *
     * @throws ArithmeticException 抛出的运算错误
     */
    public BigInteger exp(BigInteger a, BigInteger b) throws ArithmeticException {
        return a.modPow(b, k);
    }

}
