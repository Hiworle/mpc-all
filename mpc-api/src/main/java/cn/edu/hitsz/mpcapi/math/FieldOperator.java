package cn.edu.hitsz.mpcapi.math;

import java.math.BigInteger;

/**
 * @author hope
 * @date 2023/9/9 - 9:50
 */
public final class FieldOperator {

    /*有限域的大小*/
    private final BigInteger n;

    /*本源根*/
    private final BigInteger a;

    public FieldOperator(BigInteger n, BigInteger a) {
        this.a = a;
        this.n = n;
    }

    /**
     * 有限域内的加法
     *
     * @throws ArithmeticException 抛出的运算错误，例如传入数据不在有限域内
     */
    public BigInteger add(BigInteger a, BigInteger b) throws ArithmeticException {
        // todo
        return null;
    }

    /**
     * 有限域内的减法
     *
     * @throws ArithmeticException 抛出的运算错误
     */
    public BigInteger sub(BigInteger a, BigInteger b) throws ArithmeticException {
        // todo
        return null;
    }

    /**
     * 有限域内的乘法
     *
     * @throws ArithmeticException 抛出的运算错误
     */
    public BigInteger mul(BigInteger a, BigInteger b) throws ArithmeticException {
        // todo
        return null;
    }

    /**
     * 有限域内的除法
     *
     * @throws ArithmeticException 抛出的运算错误
     */
    public BigInteger div(BigInteger a, BigInteger b) throws ArithmeticException {
        // todo
        return null;
    }


    /**
     * 求逆元
     *
     * @throws ArithmeticException 抛出的运算错误
     */
    public BigInteger inv(BigInteger a) throws ArithmeticException {
        // todo
        return null;
    }

    /**
     * 是否在有限域内
     */
    public boolean check(BigInteger a) {
        // todo
        return false;
    }

    /**
     * 是否都在有限域内
     */
    public boolean check(BigInteger[][] matrix) {
        // todo
        return false;
    }
}
