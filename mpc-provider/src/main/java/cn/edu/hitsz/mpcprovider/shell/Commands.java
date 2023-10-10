package cn.edu.hitsz.mpcprovider.shell;

import cn.edu.hitsz.mpcapi.math.FieldOperator;
import cn.edu.hitsz.mpcapi.utils.HttpUtils;
import cn.edu.hitsz.mpcprovider.upload.Uploads;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import static cn.edu.hitsz.mpcprovider.ProviderContext.*;

/**
 * @author yifu
 * @date 2023/9/12 - 14:15
 */
@ShellComponent
public class Commands {

    private BigInteger[][] matrixA;
    private BigInteger[][] matrixB;
    private final FieldOperator op = new FieldOperator(k, alpha);


    @ShellMethod(value = "Echo input.", key = {"echo", "ec"})
    public void echo(String input) {
        System.out.println(input);
    }

    @ShellMethod(value = "Provide a matrix.", key = "matrix")
    public void matrix(int m, int n, @ShellOption(arity = 100) BigInteger[] numbers) {
        if (numbers.length != m * n) throw new RuntimeException("输入有误！");
        BigInteger[][] matrix = new BigInteger[m][n];
        int idx = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = numbers[idx++];
            }
        }
        if (matrixA == null) {
            matrixA = matrix;
        } else {
            matrixB = matrix;
        }
    }

    @ShellMethod(value = "Combine two matrix.", key = "mcomb")
    public void mcomb(int i, int j) {
        BigInteger[] randomA = getRandomArray(matrixA.length);
        BigInteger[] randomB = getRandomArray(matrixB[0].length);
        boolean flag = matrixA[0].length % 2 == 1;
        if (matrixA[0].length != matrixB.length) {
            System.out.println("矩阵无法相乘！");
            return;
        } else if (flag) {
            BigInteger[][] matrix = new BigInteger[matrixA.length][matrixA[0].length + 1];
            for (int m = 0; m < matrix.length; m++) {
                if (matrix[m].length - 1 >= 0)
                    System.arraycopy(matrixA[m], 0, matrix[m], 0, matrix[m].length - 1);
                matrix[m][matrix[0].length - 1] = randomA[m];
            }
            matrixA = matrix;
            matrix = new BigInteger[matrixB.length + 1][matrixB[0].length];
            for (int m = 0; m < matrix.length - 1; m++) {
                System.arraycopy(matrixB[m], 0, matrix[m], 0, matrix[m].length);
            }
            matrix[matrix.length - 1] = randomB;
            matrixB = matrix;
        }

        matrixB = transpose(matrixB);
        BigInteger[][] result = new BigInteger[matrixA.length][matrixB.length];
        for (int m = 0; m < matrixA.length; m++) {
            for (int n = 0; n < matrixB.length; n++) {
                Uploads.uploads(matrixA[m]);
                Uploads.uploads(matrixB[n]);

                BigInteger sum = combine(i, j);
                if (flag) {
                    sum = op.sub(sum, op.mul(randomA[m], randomB[n]));
                }
                result[m][n] = sum;
            }
        }

        System.out.println("=================== MATRIX ===================");
        for (BigInteger[] line : result) {
            for (BigInteger bigInteger : line) {
                System.out.printf("%10s", bigInteger);
            }
            System.out.println();
        }

        matrixA = null;
        matrixB = null;
    }

    @ShellMethod(value = "Provide a vector.", key = {"provide"})
    public void provide(@ShellOption(arity = 100) BigInteger[] numbers) {
        // 通过 shell 输入最大为 100 的向量
        // todo 暂时要求输入偶数向量
        System.out.printf("Read input success! num=%d%n", numbers.length);
        if (Uploads.uploads(numbers))
            System.out.println("Upload succeed!");
        else {
            System.out.println("Upload failed!");
        }
    }

    @Deprecated
    @ShellMethod(value = "[Deprecated] Combine the numbers.", key = "comb")
    public void comp(BigInteger si, BigInteger i, BigInteger sj, BigInteger j) {
        if (i.equals(j)) {
            throw new RuntimeException("Error");
        }
        if (i.compareTo(j) < 0) {
            BigInteger t = i;
            i = j;
            j = t;
            t = si;
            si = sj;
            sj = t;
        }
        FieldOperator op = new FieldOperator(k, alpha);
        BigInteger a1b1 = op.div(
                op.sub(si, op.mul(op.exp(alpha, op.sub(i, j)), sj)),
                op.sub(BigInteger.ONE, op.exp(alpha, op.sub(i, j)))
        );
        BigInteger a2b2 = op.div(
                op.sub(si, sj),
                op.sub(op.exp(alpha, op.sub(op.add(BigInteger.ONE, j), BigInteger.ONE)),
                        op.exp(alpha, op.sub(op.add(BigInteger.ONE, i), BigInteger.ONE)))
        );
        System.out.println("\na1b1 = " + a1b1);
        System.out.println("a2b2 = " + a2b2);
    }

    @ShellMethod(value = "Combine the numbers.", key = "combine")
    public BigInteger combine(int i, int j) {
        System.out.println("++++++++++++++++++++++++++++++++++++ COMBINE LOG ++++++++++++++++++++++++++++++++++++");
        if (i == j) {
            throw new RuntimeException("i, j 不应相同！");
        }
        if (i < j) {
            int t = i;
            i = j;
            j = t;
        }
        String ipI = getIp(i);
        String ipJ = getIp(j);

        String resultI;
        while ("NULL".equals(resultI = HttpUtils.httpPostRequest("http://" + ipI + "/result"))) ;
        String resultJ;
        while ("NULL".equals(resultJ = HttpUtils.httpPostRequest("http://" + ipJ + "/result"))) ;

        BigInteger[] si = parseResult(resultI);
        BigInteger[] sj = parseResult(resultJ);

        System.out.println("\n===================== S =====================");
        System.out.println("s[" + j + "] = " + Arrays.toString(sj));
        System.out.println("s[" + i + "] = " + Arrays.toString(si));

        BigInteger[] a1b1 = op.div(
                op.sub(si, op.mul(op.exp(alpha, op.sub(BigInteger.valueOf(i), BigInteger.valueOf(j))), sj)),
                op.sub(BigInteger.ONE, op.exp(alpha, op.sub(BigInteger.valueOf(i), BigInteger.valueOf(j))))
        );
        BigInteger[] a2b2 = op.div(
                op.sub(si, sj),
                op.sub(op.exp(alpha, op.sub(op.add(BigInteger.ONE, BigInteger.valueOf(j)), BigInteger.ONE)),
                        op.exp(alpha, op.sub(op.add(BigInteger.ONE, BigInteger.valueOf(i)), BigInteger.ONE)))
        );
        System.out.println("=================== RESULT ===================");
        BigInteger[] ans = new BigInteger[si.length + sj.length];
        for (int k = 0; k < a1b1.length; k++) {
            ans[2 * k] = a1b1[k];
            ans[2 * k + 1] = a2b2[k];
        }
        System.out.println(Arrays.toString(ans));

        BigInteger sum = BigInteger.ZERO;
        for (BigInteger an : ans) {
            sum = op.add(sum, an);
        }
        System.out.println("=================== SUM ===================");
        System.out.println(sum);
        return sum;
    }

    /**
     * 根据（最初的）下标获取 Pi 的 ip
     *
     * @param i （最初的）下标，1~n
     * @return Pi 的 ip 地址
     */
    private String getIp(int i) {
        switch (i) {
            case 1 -> {
                return calculators.get(0);
            }
            case 2 -> {
                return calculators.get(total - 1);
            }
            default -> {
                return calculators.get(i - 2);
            }
        }
    }

    /**
     * 将 result 转为 BigInteger 数组
     *
     * @param result 从计算节点获取的 result
     * @return result 对应的数组
     */
    private BigInteger[] parseResult(String result) {
        String[] split = result.substring(1, result.length() - 1).split(",");
        BigInteger[] ans = new BigInteger[split.length];
        for (int i = 0; i < split.length; i++) {
            ans[i] = new BigInteger(split[i].trim());
        }
        return ans;
    }


    private BigInteger[] getRandomArray(int n) {
        Random r = new Random();
        BigInteger[] arr = new BigInteger[n];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = BigInteger.valueOf(r.nextInt()).mod(k);
        }
        return arr;
    }

    private BigInteger[][] transpose(BigInteger[][] matrix) {
        BigInteger[][] m2 = new BigInteger[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                m2[j][i] = matrix[i][j];
            }
        }
        return m2;
    }
}
