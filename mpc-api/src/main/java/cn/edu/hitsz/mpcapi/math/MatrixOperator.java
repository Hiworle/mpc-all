package cn.edu.hitsz.mpcapi.math;

import java.math.BigInteger;

/**
 * @author hope
 * @date 2023/9/9 - 10:12
 */
public final class MatrixOperator {

    /**
     * 计算有限域内的矩阵乘法
     *
     * @throws ArithmeticException 计算错误，例如输入矩阵无法乘
     */
    public BigInteger[][] mul(BigInteger[][] a, BigInteger[][] b, FieldOperator operator) throws ArithmeticException {
        if (a[0].length != b.length) {
            throw new ArithmeticException("输入矩阵无法乘");
        }
        int x = a.length;
        int y = b[0].length;
        BigInteger[][] c = new BigInteger[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                c[i][j] = BigInteger.ZERO;
                for (int k = 0; k < b.length; k++) {
                    c[i][j] = operator.add(c[i][j], operator.mul(a[i][k], b[k][j]));
                }
            }
        }
        return c;
    }
}
